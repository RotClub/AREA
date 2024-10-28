import { adaptUrl } from "$lib/api";
import { error, redirect } from "@sveltejs/kit";
import queryString from "query-string";
import { addProvider } from "$lib/provider";

export const GET = async (event) => {
	const code = event.url.searchParams.get("code");
	const err = event.url.searchParams.get("error");
	const token = event.url.searchParams.get("state");

	if (!token) {
		error(400, "No token provided");
	}
	if (err) {
		error(400, "Could not get Battle.net authorization: " + err);
	}
	const battle_res = await fetch(
		"https://eu.battle.net/oauth/token?" +
			queryString.stringify({
				grant_type: "authorization_code",
				code: code,
				redirect_uri: `${adaptUrl()}/api/services/battlenet/callback`,
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
	const res = await addProvider("battlenet", data, token);
	if (!res.ok) {
		const ans = await res.json();
		error(400, ans.error);
	}
	return redirect(301, `${adaptUrl()}/dashboard`);
};
