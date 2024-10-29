import { error, redirect } from "@sveltejs/kit";
import queryString from "query-string";
import { addProvider } from "$lib/provider";
import { adaptUrl } from "$lib/api";
import { getToken } from "$lib/web";

export const GET = async (event) => {
	const openIdParams = queryString.parse(event.url.search);
	const token = event.url.searchParams.get(getToken());

	if (!openIdParams["openid.claimed_id"]) {
		throw error(400, "Invalid Steam OpenID response");
	}
	if (!token) {
		throw error(400, "No token provided");
	}
	//@ts-expect-error Linter doesn't like this, but it's fine
	const steam_id = openIdParams["openid.claimed_id"].split("/").pop();
	const data = {
		steam_id: steam_id
	};
	const res = await addProvider("steam", data, token);
	if (!res.ok) {
		const ans = await res.json();
		error(400, ans.error);
	}
	return redirect(301, `${adaptUrl()}/dashboard`);
};
