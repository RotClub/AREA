import { adaptUrl } from "$lib/api";
import { error, redirect } from "@sveltejs/kit";
import { addProvider } from "$lib/provider";
import { getRedirectionURL, PlatformType } from "$lib/cross";

export const GET = async (event) => {
	const code = event.url.searchParams.get("code");
	const err_msg = event.url.searchParams.get("error_description");
	const err = event.url.searchParams.get("error");
	const state = JSON.parse(event.url.searchParams.get("state"));
	const user_agent = state.user_agent;
	const token = state.jwt;

	if (!token) {
		error(400, "No token provided");
	}
	if (err) {
		error(400, err + ": " + err_msg);
	}
	const client_id = process.env.REDDIT_CLIENT_ID;
	const client_secret = process.env.REDDIT_CLIENT_SECRET;
	const res_reddit = await fetch("https://www.reddit.com/api/v1/access_token", {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded",
			Authorization: `Basic ${Buffer.from(`${client_id}:${client_secret}`).toString("base64")}`
		},
		body: new URLSearchParams({
			grant_type: "authorization_code",
			code: code || "",
			redirect_uri: `${adaptUrl()}/api/services/reddit/callback`
		})
	});
	const data = await res_reddit.text();
	if (!res_reddit.ok) {
		error(400, data);
	}
	const res = await addProvider("reddit", JSON.parse(data), token);
	if (!res.ok) {
		const ans = await res.json();
		error(400, ans.error);
	}
	return redirect(301, getRedirectionURL(PlatformType[user_agent as keyof typeof PlatformType]));
};
