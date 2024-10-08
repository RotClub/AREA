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
	const scope = "user-read-private user-read-email";

	return redirect(
		300,
		`https://accounts.spotify.com/authorize?` +
			queryString.stringify({
				response_type: "code",
				client_id: process.env.SPOTIFY_CLIENT_ID,
				scope: scope,
				redirect_uri: `${adaptUrl()}/api/link/spotify`,
				state: state,
				show_dialog: true
			})
	);
};
