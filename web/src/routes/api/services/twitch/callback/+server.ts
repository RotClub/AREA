import { adaptUrl } from "$lib/api";
import { error, redirect } from "@sveltejs/kit";
import { addProvider } from "$lib/provider";

export const GET = async (event) => {
	const code = event.url.searchParams.get("code");
	const err_msg = event.url.searchParams.get("error_description");
	const err = event.url.searchParams.get("error");
	const token = event.url.searchParams.get("state");

	if (!token) {
		error(400, "No token provided");
	}
	if (err) {
		error(400, err + ": " + err_msg);
	}
	const client_id = process.env.TWITCH_CLIENT_ID;
	const client_secret = process.env.TWITCH_CLIENT_SECRET;
	const res_twitch = await fetch("https://id.twitch.tv/oauth2/token", {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded"
		},
		body: new URLSearchParams({
			client_id: client_id || "",
			client_secret: client_secret || "",
			grant_type: "authorization_code",
			code: code || "",
			redirect_uri: `${adaptUrl()}/api/services/twitch/callback`
		})
	});
	const data = await res_twitch.json();
	if (!res_twitch.ok) {
		error(400, data);
	}
	const res = await addProvider("twitch", data, token);
	if (!res.ok) {
		const ans = await res.json();
		error(400, ans.error);
	}
	return redirect(301, `${adaptUrl()}/dashboard`);
};
