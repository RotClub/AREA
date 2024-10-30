import { adaptUrl } from "$lib/api";
import { error } from "@sveltejs/kit";
import { removeProvider } from "$lib/provider";
import { PrismaClient, Provider } from "@prisma/client";

export const GET = async (event) => {
	const token = event.request.headers.get("Authorization")?.replace("Bearer ", "");
	const client_id = process.env.DISCORD_CLIENT_ID;
	const client_secret = process.env.DISCORD_CLIENT_SECRET;

	if (!token) {
		error(400, "No token provided");
	}
	if (!client_id || !client_secret) {
		error(500, "Discord client ID or client secret not found");
	}
	const client = new PrismaClient()
	const user_service = await client.user.findUnique({
		where: {
			token: token
		},
		select: {
			Service: {
				where: {
					providerType: Provider.DISCORD
				}
			}
		}
	});
	if (!user_service) {
		client.$disconnect();
		error(404, "Discord service not found in User data");
	}
	const service_data = JSON.parse(JSON.stringify(user_service.Service.at(0)?.metadata));
	client.$disconnect();
	if (!service_data) {
		error(404, "User Data not found for Discord service");
	}
	const revoke = await fetch("https://discord.com/api/oauth2/token/revoke", {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded",
			"Authorization": `Basic ${Buffer.from(`${client_id}:${client_secret}`).toString("base64")}`
		},
		body: new URLSearchParams(
			{
				token: service_data["access_token"],
				token_type_hint: 'access_token'
			}
		)
	});
	if (!revoke.ok) {
		error(revoke.status, "Failed to revoke Discord access token");
	}
	const res = await removeProvider("discord", event.request);
	if (!res.ok) {
		const ans = await res.json();
		error(res.status, ans.error);
	}
	return new Response(JSON.stringify({ url: `${adaptUrl()}/dashboard` }), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
