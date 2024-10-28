import { adaptUrl } from "$lib/api";
import { redirect } from "@sveltejs/kit";
import { AuthorizationCode } from "simple-oauth2";
import { config } from "dotenv";

export const GET = async (event) => {
	const scope = "openid wow.profile d3.profile sc2.profile";
	if (!process.env.BATTLENET_CLIENT_ID) {
		return new Response(JSON.stringify({ error: "No BattleNet client ID provided" }), {
			headers: {
				"Content-Type": "application/json"
			},
			status: 400
		});
	}
	if (!process.env.BATTLENET_CLIENT_SECRET) {
		return new Response(JSON.stringify({ error: "No BattleNet client secret provided" }), {
			headers: {
				"Content-Type": "application/json"
			},
			status: 400
		});
	}
	const client = new AuthorizationCode({
		client: {
			id: process.env.BATTLENET_CLIENT_ID,
			secret: process.env.BATTLENET_CLIENT_SECRET
		},
		auth: {
			tokenHost: "https://oauth.battle.net",
			authorizePath: "/authorize",
			tokenPath: "/token"
		}
	});
	const token = event.request.headers.get("Authorization")?.replace("Bearer ", "");
	const authorizationUrl = client.authorizeURL({
		redirect_uri: `${adaptUrl()}/api/services/battlenet/callback`,
		scope: scope,
		state: token
	});

	return new Response(JSON.stringify({ url: authorizationUrl }), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
