import { adaptUrl } from "$lib/api";
import { error, redirect } from "@sveltejs/kit";
import { addProvider } from "$lib/provider";

export const GET = async (event) => {
	const code = event.url.searchParams.get("code");
	const token = event.url.searchParams.get("state");
	const err_msg = event.url.searchParams.get("error_description");
	const err = event.url.searchParams.get("error");

	if (!token) {
		error(400, "No token provided");
	}
	if (err) {
		error(400, err + ": " + err_msg);
	}
	const res_discord = await fetch("https://discord.com/api/oauth2/token", {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded"
		},
		body: new URLSearchParams({
			client_id: process.env.DISCORD_CLIENT_ID || "",
			client_secret: process.env.DISCORD_CLIENT_SECRET || "",
			grant_type: "authorization_code",
			code: code || "",
			redirect_uri: `${adaptUrl()}/api/services/discord/callback`,
			scope: "identify email openid messages.read connections guilds"
		})
	});
	const data = await res_discord.json();
	if (!res_discord.ok) {
		error(400, data);
	}
	const res = await addProvider("discord", data, token);
	if (!res.ok) {
		const ans = await res.json();
		error(400, ans.error);
	}
	return redirect(301, `${adaptUrl()}/dashboard`);
};
