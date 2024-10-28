import { adaptUrl } from "$lib/api";
import { redirect } from "@sveltejs/kit";
import queryString from "query-string";

export const GET = async (event) => {
	const token = event.request.headers.get("Authorization")?.replace("Bearer ", "");
	const scope = "user-read-private user-read-email";

	if (!process.env.SPOTIFY_CLIENT_ID) {
		return new Response(JSON.stringify({ error: "No Spotify client ID provided" }), {
			headers: {
				"Content-Type": "application/json"
			},
			status: 400
		});
	}
	const authorizationUrl = (
		`https://accounts.spotify.com/authorize?` +
			queryString.stringify({
				response_type: "code",
				client_id: process.env.SPOTIFY_CLIENT_ID,
				scope: scope,
				redirect_uri: `${adaptUrl()}/api/services/spotify/callback`,
				state: token,
				show_dialog: true
			})
	);
	return new Response(JSON.stringify({ url: authorizationUrl }), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
