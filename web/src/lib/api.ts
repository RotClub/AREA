import { verifyJWTAuth } from "$lib/auth";
import { UserRole, PrismaClient } from "@prisma/client";

export const checkAPIAccess = (async (client: PrismaClient, request, url, level: UserRole) => {
	// Get the Bearer token from the request
	const bearer = request.headers.get("Authorization");
	const token = bearer ? bearer.replace("Bearer ", "") : "";

	// Verify the token
	const valid_token = await verifyJWTAuth(token, url.pathname);
	if (valid_token) {
		try {
			const user = await client.user.findUnique({
				where: {
					token: token
				},
				select: {
					role: true
				}
			});

			// Create a level dict to check the access level using the UserRole enum
			const accessLevel = Object.fromEntries(
				Object.entries(UserRole).map(([key], index) => [key, index])
			);
			return (user && accessLevel[user.role] >= accessLevel[level]) ?
				{valid: true, err: null} :
				{valid: false, err: "You don't have access to this route, you need to be a " + level};
		} catch (e) {
			return {valid: false, err: String(e)};
		}
	}
	return {valid: false, err: "Invalid token"};
})
