import { redirect } from "@sveltejs/kit";

export const GET = async ({ url }) => {
	const redurl = url.searchParams.get("new_url");
	if (!redurl) {
		return redirect(301, "/dashboard");
	}
	return redirect(301, redurl);
};
