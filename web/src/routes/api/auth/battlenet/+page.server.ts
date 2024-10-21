import { adaptUrl } from "$lib/api";
import { redirect } from "@sveltejs/kit";
import { AuthorizationCode } from "simple-oauth2";
import { config } from "dotenv";

export const load = async () => {
	const state = Math.random().toString(36).substring(2, 15);
	const scope = "openid d3.profile sc2.profile";

	config();

	const client = new AuthorizationCode({
		client: {
			id: process.env.BATTLENET_CLIENT_ID ? process.env.BATTLENET_CLIENT_ID : "",
			secret: process.env.BATTLENET_CLIENT_SECRET ? process.env.BATTLENET_CLIENT_SECRET : ""
		},
		auth: {
			tokenHost: "https://oauth.battle.net",
			authorizePath: "/authorize",
			tokenPath: "/token"
		}
	});
	const authorizationUri = client.authorizeURL({
		redirect_uri: `${adaptUrl()}/api/link/battlenet`,
		scope: scope,
		state: state
	});

	return redirect(302, authorizationUri);
};
