import { adaptUrl } from "$lib/api";
import queryString from "query-string";
import { getPlatformType } from "$lib/cross";

export const GET = async (event) => {
	const token = event.request.headers.get("Authorization")?.replace("Bearer ", "");
	const platform = getPlatformType(event.request)
	const state = JSON.stringify({
		jwt: token,
		user_agent: platform
	})
	const scope =
		"user-read-private user-read-email user-read-currently-playing user-modify-playback-state";

	if (!process.env.SPOTIFY_CLIENT_ID) {
		return new Response(JSON.stringify({ error: "No Spotify client ID provided" }), {
			headers: {
				"Content-Type": "application/json"
			},
			status: 400
		});
	}
	const authorizationUrl =
		`https://accounts.spotify.com/authorize?` +
		queryString.stringify({
			response_type: "code",
			client_id: process.env.SPOTIFY_CLIENT_ID,
			scope: scope,
			redirect_uri: `${adaptUrl()}/api/services/spotify/callback`,
			state: state,
			show_dialog: true
		});
	return new Response(JSON.stringify({ url: authorizationUrl }), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
