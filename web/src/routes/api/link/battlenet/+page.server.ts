import { adaptUrl } from "$lib/api";
import { error, redirect } from "@sveltejs/kit";
import queryString from "query-string";
import { config } from "dotenv"

export const load = async ({ cookies, url }) => {
	const code = url.searchParams.get("code");
	const err = url.searchParams.get("error");
	let state = false;

	if (err) {
		console.error("Error in URL:", err);
		error(400, "Could not get Battle.net authorization: " + err);
	}
	config();
	try {
		const res = await fetch(
			"https://battle.net/oauth/token?" +
				queryString.stringify({
					grant_type: "authorization_code",
					code: code,
					redirect_uri: `${adaptUrl()}/api/link/battlenet`,
					client_id: process.env.BATTLENET_CLIENT_ID,
					client_secret: process.env.BATTLENET_CLIENT_SECRET
				}),
			{
				method: "POST",
				headers: {
					"Content-Type": "application/x-www-form-urlencoded",
					Authorization:
						"Basic " +
						Buffer.from(process.env.BATTLENET_CLIENT_ID + ":" + process.env.BATTLENET_CLIENT_SECRET).toString(
							"base64"
						)
				}
			}
		);

		console.log("Battle.net response status:", res.status);

		const data = await res.json();
		console.log("Battle.net response data:", data);

		const token = cookies.get("token");
		console.log("Received token from cookies:", token);

		if (!token) {
			console.error("No token provided");
			error(400, "No token provided");
		}

		state = true;
	} catch (e) {
		console.error("Error linking user to Battle.net service:", e);
		error(500, "Could not link user to Battle.net service: " + e);
	}

	console.log("Redirecting to dashboard with state:", state);
	return redirect(
		301,
		`${adaptUrl()}/dashboard/services?` +
			queryString.stringify({
				provider: "battlenet",
				success: state
			})
	);
};
