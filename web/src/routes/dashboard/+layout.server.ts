import type { LayoutServerLoad } from "./$types";
import { redirect } from "@sveltejs/kit";
import { checkAccess } from "$lib/api";
import { PrismaClient, UserRole } from "@prisma/client";

export const load: LayoutServerLoad = async (event) => {
	await event.parent();

	// Get the token from the cookies
	const token = event.cookies.get("token");
	const client = new PrismaClient();

	try {
		await checkAccess(client, { token: token ? token : "" }, UserRole.USER, false);
	} catch (error) {
		console.error("Error during token validation:", error);
		throw redirect(302, "/auth");
	}
	const res = await event.fetch("/api/user");
	const user = await res.json();
	if (!user) {
		console.error("Error during user information fetching");
		throw redirect(302, "/auth");
	}
	const avatar_seed = Buffer.from(Object.values(user).join(""), "binary").toString("base64");
	return {
		props: {
			...user,
			avatar_seed: avatar_seed
		}
	};
};
