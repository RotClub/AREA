import { verifyJWTAuth } from "$lib/auth";
import { UserRole, PrismaClient } from "@prisma/client";

export const checkAccess = async (
	client: PrismaClient,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	request: any,
	level: UserRole = UserRole.API_USER,
	apiMode = true
) => {
	let token: string;

	if (apiMode) {
		// Get the Bearer token from the request
		const bearer = request.headers.get("Authorization");
		token = bearer ? bearer.replace("Bearer ", "") : "";
	} else {
		// Get the token from the onLoad
		token = request.token;
	}
	// Verify the token
	const valid_token = await verifyJWTAuth(token);
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
			return user && accessLevel[user.role] >= accessLevel[level]
				? { valid: true, err: null }
				: {
						valid: false,
						err: "You don't have access to the API, reach out to an Administrator"
					};
		} catch (e) {
			return { valid: false, err: String(e) };
		}
	}
	return { valid: false, err: "Invalid token" };
};

export const adaptUrl = () => {
	if (process.env.VERCEL_URL) {
		return "https://area-app.vercel.app";
	} else {
		return "http://localhost:8081";
	}
};
