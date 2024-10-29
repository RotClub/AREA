import { adaptUrl } from "$lib/api";
import queryString from "query-string";

export const GET = async (event) => {
	const token = event.request.headers.get("Authorization")?.replace("Bearer ", "");
	if (!process.env.REDDIT_CLIENT_ID) {
		return new Response(JSON.stringify({ error: "No Reddit client ID provided" }), {
			headers: {
				"Content-Type": "application/json"
			},
			status: 400
		});
	}
	const authorizationUrl =
		"https://www.reddit.com/api/v1/authorize?" +
		queryString.stringify({
			client_id: process.env.REDDIT_CLIENT_ID,
			response_type: "code",
			state: token,
			redirect_uri: `${adaptUrl()}/api/services/reddit/callback`,
			duration: "permanent",
			scope: "identity edit history modposts modwiki privatemessages read submit subscribe vote wikiedit wikiread"
		});
	return new Response(JSON.stringify({ url: authorizationUrl }), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};