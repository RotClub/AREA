import { error, redirect } from "@sveltejs/kit";
import queryString from "query-string";
import { verifySteamOpenID } from '$lib/steam';

export const load = async ({ url }) => {
  const openIdParams = queryString.parse(url.search);

  if (!openIdParams['openid.claimed_id']) {
    throw error(400, "Invalid Steam OpenID response");
  }

  const steamId = openIdParams['openid.claimed_id'].split('/').pop();
  console.log("Steam ID:", steamId);

  const isValid = await verifySteamOpenID(openIdParams);
  if (!isValid) {
    throw error(400, "Steam OpenID verification failed");
  }
  
  return redirect(302, `/dashboard/services?steamId=${steamId}`);
};
