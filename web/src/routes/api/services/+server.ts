import { PrismaClient, UserRole } from "@prisma/client";
import { Provider } from "@prisma/client";
import { checkAccess } from "$lib/api";
import { getProviderTitle } from "$lib/services";

export const GET = async ({ request, cookies }) => {
	let token: string | undefined;
	let access: { valid: boolean; err: string | null };

	const client = new PrismaClient();
	if (cookies.get("token")) {
		// Get the Bearer token from the cookies
		token = cookies.get("token");

		// Verify the token
		access = await checkAccess(client, { token: token ? token : "" }, UserRole.API_USER, false);
	} else {
		// Get the Bearer token from the request
		const bearer = request.headers.get("Authorization");
		token = bearer ? bearer.replace("Bearer ", "") : "";

		// Verify the token
		access = await checkAccess(client, request);
	}

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
		if (provider in userProviderList) {
			accessibleProviders.push({
				service: provider,
				link: true,
				title: getProviderTitle(provider)
			});
		} else {
			accessibleProviders.push({
				service: provider,
				link: false,
				title: getProviderTitle(provider)
			});
		}
	}
	return new Response(JSON.stringify(accessibleProviders), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
