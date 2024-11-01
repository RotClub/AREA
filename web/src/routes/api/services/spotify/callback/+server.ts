import { adaptUrl } from "$lib/api";
import { error, redirect } from "@sveltejs/kit";
import queryString from "query-string";
import { addProvider } from "$lib/provider";
import { getRedirectionURL, PlatformType } from "$lib/cross";

export const GET = async (event) => {
	const code = event.url.searchParams.get("code");
	const err = event.url.searchParams.get("error");
	const state = JSON.parse(event.url.searchParams.get("state"));
	const user_agent = state.user_agent;
	const token = state.jwt;

	if (!token) {
		error(400, "No token provided");
	}
	if (err) {
		error(400, "Could get Spotify authorization: " + err);
	}
	const client_secret = process.env.SPOTIFY_CLIENT_SECRET;
	const client_id = process.env.SPOTIFY_CLIENT_ID;
	const res_spotify = await fetch(
		"https://accounts.spotify.com/api/token?" +
			queryString.stringify({
				grant_type: "authorization_code",
				code: code,
				redirect_uri: `${adaptUrl()}/api/services/spotify/callback`,
				client_id: client_id,
				client_secret: client_secret
			}),
		{
			method: "POST",
			headers: {
				"Content-Type": "application/x-www-form-urlencoded",
				Authorization:
					"Basic " + Buffer.from(client_id + ":" + client_secret).toString("base64")
			}
		}
	);

	const data = await res_spotify.json();
	if (!res_spotify.ok) {
		error(400, data.error);
	}
	const res = await addProvider("spotify", data, token);
	if (!res.ok) {
		const ans = await res.json();
		error(400, ans.error);
	}
	return redirect(301, getRedirectionURL(PlatformType[user_agent as keyof typeof PlatformType]));
};
