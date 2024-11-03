import type { LayoutServerLoad } from "./$types";
import { redirect } from "@sveltejs/kit";
import { checkAccess } from "$lib/api";
import { PrismaClient, UserRole } from "@prisma/client";
import { getToken } from "$lib/web";
import { apiRequest } from "$lib";

export const load: LayoutServerLoad = async (event) => {
	await event.parent();

	// Get the token from the cookies
	const token = event.cookies.get(getToken());
	const client = new PrismaClient();

	try {
		await checkAccess(client, { token: token ? token : "" }, UserRole.USER, false);
	} catch (error) {
		console.error("Error during token validation:", error);
		throw redirect(302, "/auth");
	}
	const res = await event.fetch("/api/user", {
		headers: {
			Authorization: `Bearer ${token}`
		}
	});
	if (!res.ok) {
		console.error("Error during user information fetching");
		throw redirect(302, "/auth");
	}
	const user = await res.json();
	const avatar_seed = Object.values(user).join("");
	return {
		props: {
			...user,
			avatar_seed: avatar_seed
		}
	};
};
