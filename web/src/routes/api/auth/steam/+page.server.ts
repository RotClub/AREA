import { adaptUrl } from "$lib/api";
import { redirect } from "@sveltejs/kit";
import { AuthorizationCode } from "simple-oauth2";
import { config } from "dotenv";

export const load = async () => {
    const state = Math.random().toString(36).substring(2, 15);
    const scope = "openid";

    config();

    const client = new AuthorizationCode({
        client: {
            id: process.env.STEAM_CLIENT_ID || (() => { throw new Error("STEAM_CLIENT_ID is not defined"); })(),
            secret: process.env.STEAM_CLIENT_SECRET || (() => { throw new Error("STEAM_CLIENT_SECRET is not defined"); })()
        },
        auth: {
            tokenHost: "https://steamcommunity.com",
            authorizePath: "/openid/login",
            tokenPath: "/openid/token"
        }
    });
    const authorizationUri = client.authorizeURL({
        redirect_uri: `${adaptUrl()}/api/link/steam`,
        scope: scope,
        state: state
    });

    return redirect(302, authorizationUri);
};