import { adaptUrl } from "$lib/api";
import { error } from "@sveltejs/kit";
import { removeProvider } from "$lib/provider";

export const GET = async (event) => {
	const res = await removeProvider("reddit", event.request)
	if (!res.ok) {
		const ans = await res.json();
		error(400, ans.error);
	}
	return new Response(JSON.stringify({ url: `${adaptUrl()}/dashboard` }), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
