import { error } from "@sveltejs/kit";
import { removeProvider } from "$lib/provider";
import { getPlatformType, getRedirectionURL } from "$lib/cross";

export const GET = async (event) => {
	const res = await removeProvider("twitch", event.request);
	if (!res.ok) {
		const ans = await res.json();
		error(400, ans.error);
	}
	return new Response(JSON.stringify({ url: getRedirectionURL(getPlatformType(event.request)) }), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
