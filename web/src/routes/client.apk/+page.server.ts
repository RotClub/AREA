import { redirect } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async () => {
	if (process.env.VERCEL_URL) {
		redirect(307, "/client.apk");
	} else {
		redirect(307, "/mobile/client.apk");
	}
};
