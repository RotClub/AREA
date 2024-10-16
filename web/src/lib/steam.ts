import queryString from "query-string";

export async function verifySteamOpenID(params: any): Promise<boolean> {
  const steamOpenIdUrl = 'https://steamcommunity.com/openid/login';

  const validationParams = {
    ...params,
    'openid.mode': 'check_authentication'
  };

  const response = await fetch(steamOpenIdUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: queryString.stringify(validationParams),
  });

  const responseText = await response.text();
  
  return responseText.includes('is_valid:true');
}
