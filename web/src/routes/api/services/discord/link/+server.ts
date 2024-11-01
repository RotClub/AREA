import { adaptUrl } from "$lib/api";
import { getPlatformType } from "$lib/cross";
import queryString from "query-string";

export const GET = async (event) => {
	const token = event.request.headers.get("Authorization")?.replace("Bearer ", "");
	const platform = getPlatformType(event.request);
	const state = JSON.stringify({
		jwt: token,
		user_agent: platform
	});

	if (!process.env.DISCORD_CLIENT_ID) {
		return new Response(JSON.stringify({ error: "No Discord client ID provided" }), {
			headers: {
				"Content-Type": "application/json"
			},
			status: 400
		});
	}
	const authorizationUrl =
		"https://discord.com/oauth2/authorize?" +
		queryString.stringify({
			client_id: process.env.DISCORD_CLIENT_ID,
			response_type: "code",
			redirect_uri: `${adaptUrl()}/api/services/discord/callback`,
			integration_type: 1,
			scope: "identify email openid messages.read connections guilds",
			state: state
		});
	return new Response(JSON.stringify({ url: authorizationUrl }), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
