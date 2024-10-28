import { PrismaClient, Provider, UserRole } from "@prisma/client";
import { checkAccess } from "$lib/api";

export interface ActionMetaDataType {
	id: string;
	displayName: string;
	type: "string" | "number" | "boolean" | "date";
}

export type NodeType = Array<{
	service: Provider;
	displayName: string;
	iconPath: string;
	actions: Array<{ id: string; displayName: string; meta: Record<string, ActionMetaDataType> }>;
	reactions: Array<{ id: string; displayName: string; meta: Record<string, ActionMetaDataType> }>;
}>;

export const Nodes: NodeType = [
	{
		service: Provider.SPOTIFY,
		displayName: "Spotify",
		iconPath: "/provider/spotify-icon.svg",
		actions: [
			{
				id: "listening-track",
				displayName: "Listening to track",
				meta: {
					track_id: {
						id: "track_id",
						displayName: "Track",
						type: "string"
					}
				}
			}
		],
		reactions: [
			{
				id: "play-track",
				displayName: "Play track",
				meta: {
					track_id: {
						id: "track_id",
						displayName: "Track",
						type: "string"
					},
					position_ms: {
						id: "position_ms",
						displayName: "Position (ms)",
						type: "number"
					}
				}
			}
		]
	}
];

export function getInputTypeFromMeta(meta: ActionMetaDataType): string {
	switch (meta.type) {
		case "string":
			return "text";
		case "number":
			return "number";
		case "boolean":
			return "checkbox";
		case "date":
			return "date";
		default:
			return "text";
	}
}

export function getIconPathFromId(id: string): string {
	const [service] = id.split(":");
	const action = Nodes.find((s) => s.service === service);
	return action ? action.iconPath : "";
}

export function getDisplayNameFromId(id: string): string {
	const [service, action] = id.split(":");
	const foundService = Nodes.find((s) => s.service === service);
	if (!foundService) return "";
	let foundName = foundService.actions?.find((a) => a?.id === action);
	if (!foundName) foundName = foundService.reactions?.find((a) => a?.id === action);
	return foundName ? foundName.displayName : "";
}

export function getRequiredMetadataFromId(id: string): Record<string, ActionMetaDataType> {
	const [service, action] = id.split(":");
	const foundService = Nodes.find((s) => s.service === service);
	if (!foundService) return {};
	let foundMeta = foundService.actions?.find((a) => a?.id === action);
	if (!foundMeta) foundMeta = foundService.reactions?.find((a) => a?.id === action);
	return foundMeta?.meta || {};
}

export async function GetAccessibleActions(jwt: string | undefined) {
	if (!jwt) return [];
	const client = new PrismaClient();
	const user = await client.user.findUnique({
		where: {
			token: jwt
		},
		select: {
			Service: true
		}
	});
	client.$disconnect();

	if (!user || Nodes.length === 0) {
		return [];
	}

	const accessibleActions = [];
	for (const service of user.Service) {
		const actions = Nodes.find((action) => action.service === service.providerType);
		if (actions) accessibleActions.push(actions);
	}
	return accessibleActions;
}

export async function GetAccessibleReactions(jwt: string | undefined) {
	if (!jwt) return [];
	const client = new PrismaClient();
	const user = await client.user.findUnique({
		where: {
			token: jwt
		},
		select: {
			Service: true
		}
	});
	client.$disconnect();

	if (!user || Nodes.length === 0) {
		return [];
	}

	const accessibleReactions = [];
	for (const service of user.Service) {
		const actions = Nodes.find((action) => action.service === service.providerType);
		if (actions) accessibleReactions.push(actions);
	}
	return accessibleReactions;
}

export function getProviderTitle(provider: Provider) {
	switch (provider) {
		case Provider.SPOTIFY:
			return "Spotify";
		case Provider.DISCORD:
			return "Discord";
		case Provider.TWITCH:
			return "Twitch";
		case Provider.REDDIT:
			return "Reddit";
		case Provider.BATTLENET:
			return "Battle.net";
		case Provider.STEAM:
			return "Steam";
		default:
			return "Unknown";
	}
}

export async function addProvider(provider: string, data: Record<string, unknown>, request: Request): Promise<Response> {
	const providerList = Object.values(Provider).map((p) => String(p || "").toLowerCase());
	if (!providerList.includes(provider || "")) {
		return new Response(JSON.stringify({ error: "Invalid provider" }), {
			status: 400,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const correctProvider = Object.values(Provider).find(
		(p) => String(p).toLowerCase() === provider || ""
	);
	const client = new PrismaClient();
	const access = await checkAccess(client, request, UserRole.USER);
	if (!access.valid) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: access.err }), {
			status: 403,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const token = request.headers.get("Authorization")?.replace("Bearer ", "");
	const user_id = await client.user.findUnique({
		where: {
			token: token
		},
		select: {
			id: true
		}
	});
	if (!user_id) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "User not found" }), {
			status: 404,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const delete_service = await client.service.deleteMany({
		where: {
			userId: user_id.id,
			providerType: correctProvider
		}
	});
	if (!delete_service) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "Could not unlink user from service" }), {
			status: 500,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	if (correctProvider && data) {
		await client.user.update({
			where: {
				token: token
			},
			data: {
				Service: {
					create: {
						providerType: correctProvider,
						metadata: data as Record<string, string>
					}
				}
			}
		});
	}
	client.$disconnect();
	return new Response(`Provider ${provider} was successfully added to user`, {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
}

export async function removeProvider(provider: string, request: Request): Promise<Response> {
	const providerList = Object.values(Provider).map((p) => String(p).toLowerCase());
	if (!providerList.includes(provider || "")) {
		return new Response(JSON.stringify({ error: "Invalid provider" }), {
			status: 400,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const correctProvider = Object.values(Provider).find(
		(p) => String(p).toLowerCase() === provider || ""
	);
	const client = new PrismaClient();
	const access = await checkAccess(client, request, UserRole.USER);
	if (!access.valid) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: access.err }), {
			status: 403,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const token = request.headers.get("Authorization")?.replace("Bearer ", "");
	const user_id = await client.user.findUnique({
		where: {
			token: token
		},
		select: {
			id: true
		}
	});
	if (!user_id) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "User not found" }), {
			status: 404,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	await client.service.deleteMany({
		where: {
			userId: user_id.id,
			providerType: correctProvider
		}
	});
	client.$disconnect();
	return new Response(`Provider ${provider} was successfully remove from user`, {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
}