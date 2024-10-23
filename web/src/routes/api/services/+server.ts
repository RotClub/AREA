import { PrismaClient, UserRole } from "@prisma/client";
import { Provider } from "@prisma/client";
import { checkAccess } from "$lib/api";
import { getProviderTitle } from "$lib/services";

export const GET = async ({ request, cookies }) => {

	const client = new PrismaClient();
	// Get the Bearer token from the request
	const bearer = request.headers.get("Authorization");
	const token = bearer ? bearer.replace("Bearer ", "") : "";

	// Verify the token
	const access = await checkAccess(client, request);

	if (!access.valid) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: access.err }), {
			status: 403,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}

	const user = await client.user.findUnique({
		where: {
			token: token
		},
		select: {
			Service: true
		}
	});
	client.$disconnect();
	if (!user) {
		return new Response(JSON.stringify({ error: "User not found" }), {
			status: 404,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const accessibleProviders = [];
	const userProviderList = user.Service.map((service) => service.providerType);
	for (const provider of Object.values(Provider)) {
		accessibleProviders.push({
			service: provider,
			link: userProviderList.includes(provider),
			title: getProviderTitle(provider),
			link_href: `/oauth/${provider.toLowerCase()}/link`,
			unlink_href:
				`/oauth/${provider.toLowerCase()}/unlink` + (token ? `?token=${token}` : "")
		});
	}
	return new Response(JSON.stringify(accessibleProviders), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
