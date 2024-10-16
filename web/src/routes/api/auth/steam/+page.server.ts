import { redirect } from '@sveltejs/kit';

export const load = async () => {
  const steamOpenIdUrl = 'https://steamcommunity.com/openid/login';
  const returnUrl = 'http://localhost:8081/api/link/steam';
  const realm = 'http://localhost:8081';

  const authUrl = `${steamOpenIdUrl}?openid.ns=http://specs.openid.net/auth/2.0&openid.mode=checkid_setup&openid.return_to=${encodeURIComponent(returnUrl)}&openid.realm=${encodeURIComponent(realm)}&openid.identity=http://specs.openid.net/auth/2.0/identifier_select&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select`;

  return redirect(302, authUrl);
};
