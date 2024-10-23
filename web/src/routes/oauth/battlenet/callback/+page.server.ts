import { adaptUrl } from "$lib/api";
import { error, redirect } from "@sveltejs/kit";
import queryString from "query-string";
import { config } from "dotenv";

export const load = async (event) => {
	const code = event.url.searchParams.get("code");
	const err = event.url.searchParams.get("error");

	if (err) {
		error(400, "Could not get Battle.net authorization: " + err);
	}
	
	config();
	const battle_res = await fetch(
		"https://eu.battle.net/oauth/token?" +
			queryString.stringify({
				grant_type: "authorization_code",
				code: code,
				redirect_uri: `${adaptUrl()}/oauth/battlenet/callback`,
				client_id: process.env.BATTLENET_CLIENT_ID,
				client_secret: process.env.BATTLENET_CLIENT_SECRET
			}),
		{
			method: "POST",
			headers: {
				"Content-Type": "application/x-www-form-urlencoded",
				Authorization:
					"Basic " +
					Buffer.from(
						process.env.BATTLENET_CLIENT_ID + ":" + process.env.BATTLENET_CLIENT_SECRET
					).toString("base64")
			}
		}
	);
	const data = await battle_res.json();
	if (!battle_res.ok) {
		error(400, data.error);
	}
	const token = event.cookies.get("token");
	if (!token) {
		error(400, "No token provided");
	}
	const res = await event.fetch(`/api/services/battlenet/link`, {
		method: "POST",
		headers: {
			Authorization: `Bearer ${token}`
		},
		body: JSON.stringify(data)
	});
	if (!res.ok) {
		const ans = await res.json();
		error(400, ans.error);
	}

	return redirect(301, `${adaptUrl()}/dashboard/services?`);
};
