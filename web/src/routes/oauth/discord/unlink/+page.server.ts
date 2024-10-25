import { adaptUrl } from "$lib/api";
import { error, redirect } from "@sveltejs/kit";

export const load = async (event) => {
	const token = event.url.searchParams.get("token");

	if (!token) {
		error(400, "No token provided");
	}
	const res = await event.fetch(`/api/services/discord/unlink`, {
		method: "GET",
		headers: {
			Authorization: `Bearer ${token}`
		}
	});
	if (!res.ok) {
		const ans = await res.json();
		error(400, ans.error);
	}
	return redirect(301, `${adaptUrl()}/redirect?new_url=/dashboard/services`);
};
