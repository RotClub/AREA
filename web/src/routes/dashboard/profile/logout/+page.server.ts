import { redirect } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async (event) => {
    const res: Response = await event.fetch("/api/auth/logout", {
        method: "GET",
        headers: {
            Authorization: `Bearer ${event.cookies.get("token")}`
        }
    });
    event.cookies.delete("token", { path: "/", httpOnly: false });
    return redirect(301, "/");
};
