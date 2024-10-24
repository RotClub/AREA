import { GetAccessibleReactions } from "$lib/services";

export const GET = async ({ request }) => {
	const bearer = request.headers.get("Authorization");
	const token = bearer ? bearer.replace("Bearer ", "") : "";
	return new Response(JSON.stringify(await GetAccessibleReactions(token)), {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
};
