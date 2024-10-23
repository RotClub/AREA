import { adaptUrl } from "$lib/api";
import { redirect } from "@sveltejs/kit";
import queryString from "query-string";

export const load = async () => {
	return redirect(
		300,
		"https://www.epicgames.com/id/authorize?" +
			queryString.stringify({
				client_id: process.env.EPICGAMES_CLIENT_ID,
				response_type: "code",
				redirect_uri: `${adaptUrl()}/oauth/epicgames/callback`,
				scope: "basic_profile friends_list presence"
			})
	);
};
