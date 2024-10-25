import { adaptUrl } from "$lib/api";
import { error, redirect } from "@sveltejs/kit";

export const load = async (event) => {
	const code = event.url.searchParams.get("code");
	const err_msg = event.url.searchParams.get("error_description");
	const err = event.url.searchParams.get("error");

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
			redirect_uri: `${adaptUrl()}/oauth/reddit/callback`
		})
	});
	const data = await res_reddit.text();
	if (!res_reddit.ok) {
		error(400, data);
	}
	const token = event.cookies.get("token");
	if (!token) {
		error(400, "No token provided");
	}
	const res = await event.fetch(`/api/services/reddit/link`, {
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
