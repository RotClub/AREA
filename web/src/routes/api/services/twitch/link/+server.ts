import { adaptUrl } from "$lib/api";
import { redirect } from "@sveltejs/kit";
import queryString from "query-string";

export const GET = async (event) => {
	const token = event.request.headers.get("Authorization")?.replace("Bearer ", "");

	if (!process.env.TWITCH_CLIENT_ID) {
		return new Response(JSON.stringify({ error: "No Twitch client ID provided" }), {
			headers: {
				"Content-Type": "application/json"
			},
			status: 400
		});
	}
	const authorizationUrl = (
		"https://id.twitch.tv/oauth2/authorize?" +
			queryString.stringify({
				client_id: process.env.TWITCH_CLIENT_ID,
				response_type: "code",
				redirect_uri: `${adaptUrl()}/api/services/twitch/callback`,
				scope: "channel:manage:schedule channel:read:subscriptions channel:bot analytics:read:games analytics:read:extensions",
				state: token,
				force_verify: true
			})
	);
	return new Response(JSON.stringify({ url: authorizationUrl }), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
