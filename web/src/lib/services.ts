import { PrismaClient, Provider } from "@prisma/client";
import {
	actionListeningTrackTrigger,
	reactionPlayTrackTrigger,
	reactionSkipToNextTrigger,
	reactionSkipToPreviousTrigger,
	refreshSpotifyToken
} from "./triggers/spotify";
import {
	actionIsStreamingTrigger,
	reactionStartCommercialTrigger,
	refreshTwitchToken
} from "./triggers/twitch";
import {
	actionPremiumExpiredTrigger,
	actionServerJoinedTrigger,
	actionUsernameChangedTrigger,
	refreshDiscordToken
} from "./triggers/discord";
import {
	actionGainedKarmaTrigger,
	actionUnreadMessageTrigger,
	actionUsernameBecameAvailableTrigger,
	reactionAddFriendTrigger,
	reactionDeleteFriendTrigger,
	refreshRedditToken
} from "./triggers/reddit";

export interface ActionMetaDataType {
	id: string;
	displayName: string;
	type: "string" | "number" | "boolean" | "date";
}

export type NodeType = Array<{
	service: Provider;
	displayName: string;
	iconPath: string;
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	refresh: (service_meta: any) => Promise<Record<string, any>>;
	actions: Array<{
		id: string;
		displayName: string;
		meta: Record<string, ActionMetaDataType>;
		trigger: (
			nodeId: number,
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
			nodeId: number,
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
		refresh: refreshSpotifyToken,
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
			},
			{
				id: "skip-next",
				displayName: "Skip to next",
				meta: {},
				trigger: reactionSkipToNextTrigger
			},
			{
				id: "skip-previous",
				displayName: "Skip to previous",
				meta: {},
				trigger: reactionSkipToPreviousTrigger
			}
		]
	},
	{
		service: Provider.TWITCH,
		displayName: "Twitch",
		iconPath: "/provider/twitch-icon.svg",
		refresh: refreshTwitchToken,
		actions: [
			{
				id: "is-streaming",
				displayName: "Is Streaming",
				meta: {
					channel_id: {
						id: "channel_id",
						displayName: "Channel ID",
						type: "string"
					}
				},
				trigger: actionIsStreamingTrigger
			}
		],
		reactions: [
			{
				id: "start-commercial",
				displayName: "Start Commercial",
				meta: {
					length: {
						id: "length",
						displayName: "Length",
						type: "number"
					},
					channel_id: {
						id: "channel_id",
						displayName: "Channel ID",
						type: "string"
					}
				},
				trigger: reactionStartCommercialTrigger
			}
		]
	},
	{
		service: Provider.DISCORD,
		displayName: "Discord",
		iconPath: "/provider/discord-icon.svg",
		refresh: refreshDiscordToken,
		actions: [
			{
				id: "username-changed",
				displayName: "Username Changed",
				meta: {},
				trigger: actionUsernameChangedTrigger
			},
			{
				id: "server-joined",
				displayName: "Server Joined",
				meta: {},
				trigger: actionServerJoinedTrigger
			},
			{
				id: "server-left",
				displayName: "Server Left",
				meta: {},
				trigger: actionServerJoinedTrigger
			},
			{
				id: "premium-expired",
				displayName: "Premium Expired",
				meta: {},
				trigger: actionPremiumExpiredTrigger
			}
		],
		reactions: []
	},
	{
		service: Provider.BATTLENET,
		displayName: "Battle.net",
		iconPath: "/provider/battlenet-icon.svg",
		refresh: async () => ({}),
		actions: [],
		reactions: []
	},
	{
		service: Provider.REDDIT,
		displayName: "Reddit",
		iconPath: "/provider/reddit-icon.svg",
		refresh: refreshRedditToken,
		actions: [
			{
				id: "unread-message",
				displayName: "Unread Message",
				meta: {},
				trigger: actionUnreadMessageTrigger
			},
			{
				id: "gained-karma",
				displayName: "Gained Karma",
				meta: {
					username: {
						id: "username",
						displayName: "Username",
						type: "string"
					}
				},
				trigger: actionGainedKarmaTrigger
			},
			{
				id: "username-available",
				displayName: "Username Available",
				meta: {
					username: {
						id: "username",
						displayName: "Username",
						type: "string"
					}
				},
				trigger: actionUsernameBecameAvailableTrigger
			}
		],
		reactions: [
			{
				id: "add-friend",
				displayName: "Add Friend",
				meta: {
					username: {
						id: "username",
						displayName: "Username",
						type: "string"
					},
					note: {
						id: "note",
						displayName: "Note",
						type: "string"
					}
				},
				trigger: reactionAddFriendTrigger
			},
			{
				id: "remove-friend",
				displayName: "Remove Friend",
				meta: {
					username: {
						id: "username",
						displayName: "Username",
						type: "string"
					}
				},
				trigger: reactionDeleteFriendTrigger
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
