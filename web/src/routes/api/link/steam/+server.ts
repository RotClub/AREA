import { adaptUrl } from "$lib/api";
import { redirect } from "@sveltejs/kit";
import { AuthorizationCode } from "simple-oauth2";
import { config } from "dotenv";
import type { RequestHandler } from "@sveltejs/kit";

config();

export const POST: RequestHandler = async ({ request }) => {
	const { code, state } = await request.json();

	try {
		const tokenParams = {
			code,
			redirect_uri: `${adaptUrl()}/api/link/steam/callback`,
			scope: "openid"
		};

		const accessToken = await client.getToken(tokenParams);

		// Use the access token to get user information from Steam
		const steamId = accessToken.token.steamid;
		const token = accessToken.token.access_token;

		// Confirm the link API with the obtained token and steamId
		// Add your logic here to handle the token and steamId

		return {
			status: 200,
			body: {
				message: "Steam account linked successfully",
				steamId,
				token
			}
		};
	} catch (error) {
		return {
			status: 400,
			body: {
				message: "Failed to link Steam account",
				error: error.message
			}
		};
	}
};