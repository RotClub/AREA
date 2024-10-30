import { type Action, PrismaClient } from "@prisma/client";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export async function refreshDiscordToken(service_meta: any): Promise<Record<string, any>> {
	return {};
}

export async function actionUsernameChangedTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
	const url: string = "https://discord.com/api/users/@me";

	const response: Response = await fetch(url, {
		headers: {
			Authorization: `Bearer ${service_meta.access_token}`
		}
	});
	if (!response.ok) {
		return false;
	}
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const data: Record<string, any> = await response.json();
	const client: PrismaClient = new PrismaClient();
	const action: Action | null = await client.action.findFirst({
		where: {
			id: nodeId
		}
	});
	if (action === null) {
		return false;
	}
	await client.action.update({
		where: {
			id: nodeId
		},
		data: {
			persistentData: {
				username: data["global_name"]
			}
		}
	});
	if (!action.metadata) {
		return false;
	}
	return (
		(action.persistentData as Record<string, string | number | boolean | Date>)["username"] !==
		data["global_name"]
	);
}

export async function actionServerJoinedTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
	return false;
}
