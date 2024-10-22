import { PrismaClient, Provider } from "@prisma/client";

export const Actions: [
	{
		service: Provider;
		displayName: string;
		iconPath: string;
		actions: [{ id: string; displayName: string; meta: object }?];
	}?
] = [
	{
		service: Provider.SPOTIFY,
		displayName: "Spotify",
		iconPath: "/provider/spotify-icon.svg",
		actions: [
			{
				id: "listening-track",
				displayName: "Listening to track",
				meta: {
					track_id: "string"
				}
			}
		]
	}
];

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

	if (!user || Actions.length === 0) {
		return [];
	}

	const accessibleActions = [];
	for (const service of user.Service) {
		// @ts-expect-error 'action' is possibly 'undefined'. ts(18048)
		const actions = Actions.find((action) => action.service === service.providerType);
		if (actions) accessibleActions.push(actions);
	}
	return accessibleActions;
}

export function getProviderTitle(provider: Provider) {
	switch (provider) {
		case Provider.SPOTIFY:
			return "Spotify";
		case Provider.DISCORD:
			return "Discord";
		case Provider.X:
			return "X";
		case Provider.RIOT:
			return "Riot Games";
		case Provider.EPICGAMES:
			return "Epic Games";
		case Provider.BATTLENET:
			return "Battle.net";
		case Provider.STEAM:
			return "Steam";
		default:
			return "Unknown";
	}
}
