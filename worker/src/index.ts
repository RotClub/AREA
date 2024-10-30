import { Action, PrismaClient, Provider, Reaction, Service } from "@prisma/client";
import { Nodes } from "./lib/services";

async function userStillLinked(client: PrismaClient, provider: Provider, programID: number | null): Promise<boolean> {
    if (!programID) return false;
    const userId: number | null | undefined = await client.program.findFirst({
        where: {
            id: programID
        }
    }).then((program) => program?.userId);
    if (userId === null || userId === undefined) {
        return false;
    }
    return await client.service.findFirst({
        where: {
            providerType: provider,
            userId: userId
        }
    }) !== null;
}

async function refreshToken(client: PrismaClient, service: Service): Promise<void> {
    const now: Date = new Date();
    if (now < (service.metadata as any).expires_at) {
        return;
    }
    const refreshed_data: Record<string, any> = await Nodes.find((node) => node.service === service.providerType)?.refresh(service.metadata) as Record<string, any>;
    if (!refreshed_data || Object.keys(refreshed_data).length === 0) {
        return;
    }
    await client.service.update({
        where: {
            id: service.id
        },
        data: {
            metadata: {
                access_token: refreshed_data.access_token,
                expires_at: refreshed_data.expires_at,
            }
        }
    });
}

async function launchWorker(provider: Provider, actionID: string): Promise<void> {
    setInterval(async () => {
        const prisma: PrismaClient = new PrismaClient();
        const validActions: Array<Action> = await prisma.action.findMany({
            where: {
                actionId: `${provider}:${actionID}`,
                programId: {
                    not: null
                }
            }
        });
        validActions.filter(async (action) => await userStillLinked(prisma, provider, action.programId));
        validActions.forEach(async (action) => {
            if (action.programId === null) return;
            const metadata = action.metadata as Record<string, string | number | boolean | Date>;
            const userId: number | null | undefined = await prisma.program.findFirst({
                where: {
                    id: action.programId
                }
            }).then((program) => program?.userId);
            if (userId === null || userId === undefined) {
                return;
            }
            const service: Service | null = await prisma.service.findFirst({
                where: {
                    userId: userId,
                    providerType: provider
                }
            });
            if (service === null) {
                return;
            }
            const services: Array<Service> = await prisma.service.findMany({
                where: {
                    userId: userId,
                }
            });
            for (const s of services) {
                await refreshToken(prisma, s);
            }
            const triggered: boolean | undefined = await Nodes.find((node) => node.service === provider)?.actions.find((a) => a.id === actionID)?.trigger(action.id, service.metadata, metadata);
            if (triggered) {
                const reactions: Array<Reaction> = await prisma.reaction.findMany({
                    where: {
                        actionId: action.id
                    }
                });
                reactions.forEach(async (reaction) => {
                    const reactionMetadata = reaction.metadata as Record<string, string | number | boolean | Date>;
                    const reactionServiceMetadata: Record<string, string | number | boolean | Date> = await prisma.service.findFirst({
                        where: {
                            userId: userId,
                            providerType: reaction.reactionId.split(":")[0] as Provider
                        }
                    }).then((s) => s?.metadata as Record<string, string | number | boolean | Date>);
                    const triggeredReaction: boolean | undefined = await Nodes.find((node) => node.service === reaction.reactionId.split(":")[0])?.reactions.find((r) => r.id === reaction.reactionId.split(":")[1])?.trigger(reaction.id, reactionServiceMetadata, reactionMetadata);
                    if (!triggeredReaction)
                        console.log(`Failed to trigger reaction ${reaction.reactionId}`);
                });
            }
        });
        prisma.$disconnect();
    }, 7500);
}

async function main(): Promise<void> {
    for (const service of Nodes) {
        for (const action of service.actions) {
            launchWorker(service.service, action.id);
        }
    }
}

main();
