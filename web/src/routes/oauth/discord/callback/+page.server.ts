import { adaptUrl } from "$lib/api";
import { error, redirect } from "@sveltejs/kit";
import queryString from "query-string";

export const load = async (event) => {
	const code = event.url.searchParams.get("code");
	const err_msg = event.url.searchParams.get("error_description");
	const err = event.url.searchParams.get("error");

	if (err) {
		error(400, err + ": " + err_msg);
	}
	const client_secret = process.env.DISCORD_CLIENT_SECRET;
	const client_id = process.env.DISCORD_CLIENT_ID;
	const res_discord = await fetch(
		"https://discord.com/api/oauth2/token",
		{
			method: "POST",
			headers: {
				"Content-Type": "application/x-www-form-urlencoded",
			},
			body: new URLSearchParams({
				client_id: client_id || "",
				client_secret: client_secret || "",
				grant_type: "client_credentials",
				code: code || "",
				redirect_uri: `${adaptUrl()}/oauth/discord/callback`,
				scope: "identify email openid messages.read connections"
			})
		}
	);
	const data = await res_discord.json();
	if (!res_discord.ok) {
		error(400, data);
	}
	const token = event.cookies.get("token");
	if (!token) {
		error(400, "No token provided");
	}
	const res = await event.fetch(`/api/services/discord/link`, {
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
	return redirect(301, `${adaptUrl()}/dashboard/services`);
};
