import { adaptUrl } from "$lib/api";
import { Provider } from "@prisma/client";
import { error, redirect } from "@sveltejs/kit";
import { unlinkUserService } from "$lib/services";

export const load = async ({ url }) => {
	const token = url.searchParams.get("token");

	if (!token) {
		error(400, "No token provided");
	}
	const update_service = await unlinkUserService(token, Provider.SPOTIFY);
	if (!update_service) {
		error(500, "Could not unlink user to Spotify service");
	}
	return redirect(301, `${adaptUrl()}/redirect?new_url=/dashboard/services`);
};
