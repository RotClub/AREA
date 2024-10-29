import { PrismaClient, Provider } from "@prisma/client";
import { actionListeningTrackTrigger, reactionPlayTrackTrigger } from "./triggers/spotify";

export interface ActionMetaDataType {
	id: string;
	displayName: string;
	type: "string" | "number" | "boolean" | "date";
}

export type NodeType = Array<{
	service: Provider;
	displayName: string;
	iconPath: string;
	actions: Array<{
		id: string;
		displayName: string;
		meta: Record<string, ActionMetaDataType>;
		trigger: (
			userId: number,
			// eslint-disable-next-line @typescript-eslint/no-explicit-any
			service_meta: any,
			meta: Record<string, string | number | boolean | Date>
		) => Promise<boolean>;
	}>;
	reactions: Array<{
		id: string;
		displayName: string;
		meta: Record<string, ActionMetaDataType>;
		trigger: (
			userId: number,
			// eslint-disable-next-line @typescript-eslint/no-explicit-any
			service_meta: any,
			meta: Record<string, string | number | boolean | Date>
		) => Promise<boolean>;
	}>;
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
				},
				trigger: actionListeningTrackTrigger
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
				},
				trigger: reactionPlayTrackTrigger
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
