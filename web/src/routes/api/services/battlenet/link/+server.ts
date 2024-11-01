import { adaptUrl } from "$lib/api";
import { AuthorizationCode } from "simple-oauth2";
import { getPlatformType } from "$lib/cross";

export const GET = async (event) => {
	const token = event.request.headers.get("Authorization")?.replace("Bearer ", "");
	const platform = getPlatformType(event.request)
	const state = JSON.stringify({
		jwt: token,
		user_agent: platform
	})

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
	const authorizationUrl = client.authorizeURL({
		redirect_uri: `${adaptUrl()}/api/services/battlenet/callback`,
		scope: scope,
		state: state
	});
	return new Response(JSON.stringify({ url: authorizationUrl }), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
