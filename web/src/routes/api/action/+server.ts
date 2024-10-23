import { GetAccessibleActions } from "$lib/services";

export const GET = async ({ request }) => {
	const bearer = request.headers.get("Authorization");
	const token = bearer ? bearer.replace("Bearer ", "") : "";
	return new Response(JSON.stringify(await GetAccessibleActions(token || "")), {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
};
