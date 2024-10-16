import { adaptUrl } from "$lib/api";
import { PrismaClient } from "@prisma/client";
import { error, redirect } from "@sveltejs/kit";
import { config } from "dotenv";
import queryString from "query-string";

config();

const prisma = new PrismaClient();

export async function GET({ url }) {
    const code = url.searchParams.get("code");

    if (!code) {
        throw error(400, "Code not found in the callback URL");
    }

    const tokenResponse = await fetch("https://eu.battle.net/oauth/token", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            Authorization: `Basic ${Buffer.from(
                `${process.env.BATTLENET_CLIENT_ID}:${process.env.BATTLENET_CLIENT_SECRET}`
            ).toString("base64")}`
        },
        body: queryString.stringify({
            grant_type: "authorization_code",
            code: code,
            redirect_uri: `${adaptUrl()}/api/link/battlenet`
        })
    });

    if (!tokenResponse.ok) {
        const errorText = await tokenResponse.text();
        throw error(403, `Battle.net error: ${tokenResponse.status} ${errorText}`);
    }

    const tokenData = await tokenResponse.json();
    const accessToken = tokenData.access_token;

    const userResponse = await fetch("https://eu.battle.net/oauth/userinfo", {
        headers: {
            Authorization: `Bearer ${accessToken}`
        }
    });

    if (!userResponse.ok) {
        const errorText = await userResponse.text();
        throw error(403, `Battle.net error: ${userResponse.status} ${errorText}`);
    }

    const userData = await userResponse.json();

    // await prisma.user.upsert({
    //     where: { battlenetId: userData.id },
    //     update: { battlenetData: userData },
    //     create: { battlenetId: userData.id, battlenetData: userData }
    // });

    return redirect(302, `${adaptUrl()}/dashboard/services`); //?provider=battlenet&success=true
}
