import { adaptUrl } from "$lib/api";
import { redirect } from "@sveltejs/kit";
import { AuthorizationCode } from 'simple-oauth2';
import { BATTLENET_CLIENT_ID, BATTLENET_CLIENT_SECRET } from "$env/static/private";

const client = new AuthorizationCode({
    client: {
        id: BATTLENET_CLIENT_ID,
        secret: BATTLENET_CLIENT_SECRET,
    },
    auth: {
        tokenHost: 'https://oauth.battle.net',
        authorizePath: '/authorize',
        tokenPath: '/token',
    },
});

export const load = async () => {
    const state = Math.random().toString(36).substring(2, 15);
    const scope = "openid";

    const authorizationUri = client.authorizeURL({
        redirect_uri: `${adaptUrl()}/api/link/battlenet`,
        scope: scope,
        state: state,
    });

    return redirect(302, authorizationUri);
};