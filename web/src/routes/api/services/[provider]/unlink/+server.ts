import { PrismaClient, Provider, UserRole } from "@prisma/client";
import { checkAccess } from "$lib/api";

export const GET = async ({ params, request }) => {
	const { provider }: {provider: string} = params;
	const providerList = Object.values(Provider).map((provider) => String(provider).toLowerCase());
	if (!providerList.includes(provider)) {
		return new Response(JSON.stringify({ error: "Invalid provider" }), {
			status: 400,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const correctProvider = Object.values(Provider).find((p) => String(p).toLowerCase() === provider);
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
