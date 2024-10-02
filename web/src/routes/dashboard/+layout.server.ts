import type {LayoutServerLoad} from "./$types";
import { redirect } from "@sveltejs/kit";
import { checkAccess } from "$lib/api";
import { PrismaClient, UserRole } from "@prisma/client";

export const load: LayoutServerLoad = async (event) => {
	await event.parent();

	// Get the token from the cookies
	const token = event.cookies.get("token");
	const client = new PrismaClient();

	try {
		await checkAccess(client, {token: token ? token : ""}, UserRole.USER, false);
	} catch (error) {
		console.error("Error during token validation:", error);
		throw redirect(302, "/auth");
	}
};
