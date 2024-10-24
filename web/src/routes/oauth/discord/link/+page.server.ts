import { adaptUrl } from "$lib/api";
import { redirect } from "@sveltejs/kit";
import queryString from "query-string";

export const load = async () => {
	return redirect(
		300,
		"https://discord.com/oauth2/authorize?" +
			queryString.stringify({
				client_id: process.env.DISCORD_CLIENT_ID,
				response_type: "code",
				redirect_uri: `${adaptUrl()}/oauth/discord/callback`,
				integration_type: 1,
				scope: "identify email openid messages.read connections"
			})
	);
};
