import { adaptUrl } from "$lib/api";
import queryString from "query-string";

export const GET = async (event) => {
	const steamOpenIdUrl = "https://steamcommunity.com/openid/login";
	const returnUrl = `${adaptUrl()}/api/services/steam/callback?` +
		queryString.stringify({
			token: event.request.headers.get("Authorization")?.replace("Bearer ", "")
		});
	const realm = adaptUrl();

	const authorizationUrl = `${steamOpenIdUrl}?openid.ns=http://specs.openid.net/auth/2.0&openid.mode=checkid_setup&openid.return_to=${encodeURIComponent(returnUrl)}&openid.realm=${encodeURIComponent(realm)}&openid.identity=http://specs.openid.net/auth/2.0/identifier_select&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select`;

	return new Response(JSON.stringify({ url: authorizationUrl }), {
		headers: {
			"Content-Type": "application/json"
		},
		status: 200
	});
};
