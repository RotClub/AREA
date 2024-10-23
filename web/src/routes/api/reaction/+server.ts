import { GetAccessibleReactions } from "$lib/services";

export const GET = async ({ cookies }) => {
	return new Response(JSON.stringify(await GetAccessibleReactions(cookies.get("token"))), {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
};