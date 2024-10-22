import { adaptUrl } from "$lib/api";
import { redirect } from "@sveltejs/kit";
import queryString from "query-string";

export const load = async () => {
	return redirect(300,
		"https://id.twitch.tv/oauth2/authorize?" + queryString.stringify({
			client_id: process.env.TWITCH_CLIENT_ID,
			response_type: "code",
			redirect_uri: `${adaptUrl()}/oauth/twitch/callback`,
			scope: "channel:manage:schedule channel:read:subscriptions channel:bot analytics:read:games analytics:read:extensions",
			force_verify: true
		})
	)
};
