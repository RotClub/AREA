import { adaptUrl } from "$lib/api";
import { PrismaClient } from "@prisma/client";
import { error, redirect } from "@sveltejs/kit";
import { BATTLENET_CLIENT_ID, BATTLENET_CLIENT_SECRET } from "$env/static/private";
import queryString from "query-string";

export const load = async ({ cookies, url }) => {
	const code = url.searchParams.get("code");
	const err = url.searchParams.get("error");
	let state = false;

	console.log("Battlenet Client ID:", BATTLENET_CLIENT_ID);
	console.log("Battlenet Client Secret", BATTLENET_CLIENT_SECRET);
	console.log("Received code:", code);
	console.log("Received error:", err);

	if (err) {
		console.error("Error in URL:", err);
		error(400, "Could not get Battle.net authorization: " + err);
	}
	try {
		const res = await fetch(
			"https://battle.net/oauth/token?" +
				queryString.stringify({
					grant_type: "authorization_code",
					code: code,
					redirect_uri: `${adaptUrl()}/api/link/battlenet`,
					client_id: BATTLENET_CLIENT_ID,
					client_secret: BATTLENET_CLIENT_SECRET
				}),
			{
				method: "POST",
				headers: {
					"Content-Type": "application/x-www-form-urlencoded",
					Authorization:
						"Basic " +
						Buffer.from(BATTLENET_CLIENT_ID + ":" + BATTLENET_CLIENT_SECRET).toString(
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

		const client = new PrismaClient();
		const user_services = await client.user.findUnique({
			where: {
				token: token
			},
			select: {
				services: true
			}
		});

		console.log("User services:", user_services);

		if (!user_services) {
			client.$disconnect();
			console.error("User not found");
			error(404, "User not found");
		}

		state = true;
		client.$disconnect();
	} catch (e) {
		console.error("Error linking user to Battle.net service:", e);
		error(500, "Could not link user to Battle.net service: " + e);
	}

	console.log("Redirecting to dashboard with state:", state);

	return redirect(
		301,
		`${adaptUrl()}/dashboard/services` +
			queryString.stringify({
				provider: "battlenet",
				success: state
			})
	);
};
