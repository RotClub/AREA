import { adaptUrl } from "$lib/api";
import { redirect } from "@sveltejs/kit";
import queryString from "query-string";

function generateRandomString(length: number) {
	let text = "";
	const possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	for (let i = 0; i < length; i++) {
		text += possible.charAt(Math.floor(Math.random() * possible.length));
	}
	return text;
}

export const load = async () => {
	const state = generateRandomString(16);

	return redirect(
		300,
		"https://www.reddit.com/api/v1/authorize?" +
			queryString.stringify({
				client_id: process.env.REDDIT_CLIENT_ID,
				response_type: "code",
				state: state,
				redirect_uri: `${adaptUrl()}/oauth/reddit/callback`,
				duration: "permanent",
				scope: "identity edit history modposts modwiki privatemessages read submit subscribe vote wikiedit wikiread"
			})
	);
};
