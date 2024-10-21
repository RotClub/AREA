import { PrismaClient, Provider } from "@prisma/client";

export const Actions: [
	{
		service: Provider;
		actions: [{ id: string; meta: object }?];
	}?
] = [
	{
		service: Provider.SPOTIFY,
		actions: [
			{
				id: "listening-track",
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

export async function linkUserService(token: string, provider: Provider, data: object) {
	const client = new PrismaClient();
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
		return false;
	}
	const delete_service = await client.service.deleteMany({
		where: {
			userId: user_id.id,
			providerType: provider
		}
	});
	if (!delete_service) {
		client.$disconnect();
		return false;
	}
	const update_service = await client.user.update({
		where: {
			token: token
		},
		data: {
			Service: {
				create: {
					providerType: Provider.SPOTIFY,
					metadata: data
				},
			}
		}
	});
	client.$disconnect();
	return update_service
}

export async function unlinkUserService(token: string, provider: Provider) {
	const client = new PrismaClient();
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
		return false;
	}
	const delete_service = await client.service.deleteMany({
		where: {
			userId: user_id.id,
			providerType: provider
		}
	});
	client.$disconnect();
	return delete_service;
}