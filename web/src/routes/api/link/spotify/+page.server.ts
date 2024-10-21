import { adaptUrl } from "$lib/api";
import { Provider } from "@prisma/client";
import { error, redirect } from "@sveltejs/kit";
import queryString from "query-string";
import { linkUserService } from "$lib/services";

export const load = async ({ cookies, url }) => {
	const code = url.searchParams.get("code");
	const err = url.searchParams.get("error");

	if (err) {
		error(400, "Could get Spotify authorization: " + err);
	}
	try {
		const client_secret = process.env.SPOTIFY_CLIENT_SECRET;
		const client_id = process.env.SPOTIFY_CLIENT_ID;
		const res = await fetch(
			"https://accounts.spotify.com/api/token?" +
				queryString.stringify({
					grant_type: "authorization_code",
					code: code,
					redirect_uri: `${adaptUrl()}/api/link/spotify`,
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

		const data = await res.json();
		const token = cookies.get("token");
		if (!token) {
			error(400, "No token provided");
		}
		const update_service = await linkUserService(token, Provider.SPOTIFY, data);
		if (!update_service) {
			error(500, "Could not link user to Spotify service");
		}
	} catch (e) {
		error(500, "Could not link user to Spotify service: " + e);
	}
	return redirect(301, `${adaptUrl()}/dashboard/services`);
};
