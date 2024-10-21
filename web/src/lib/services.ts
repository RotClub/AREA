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
