import { error, redirect } from "@sveltejs/kit";
import queryString from "query-string";

export const load = async (event) => {
	const openIdParams = queryString.parse(event.url.search);

	if (!openIdParams["openid.claimed_id"]) {
		throw error(400, "Invalid Steam OpenID response");
	}

	//@ts-expect-error Linter doesn't like this, but it's fine
	const steam_id = openIdParams["openid.claimed_id"].split("/").pop();
	const token = event.cookies.get("token");
	if (!token) {
		error(400, "No token provided");
	}
	const res = await event.fetch(`/api/services/steam/link`, {
		method: "POST",
		headers: {
			Authorization: `Bearer ${token}`
		},
		body: JSON.stringify({
			steam_id: steam_id
		})
	});
	if (!res.ok) {
		const ans = await res.json();
		error(400, ans.error);
	}
	return redirect(302, `/dashboard/services`);
};
