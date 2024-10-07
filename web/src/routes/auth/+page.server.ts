import { type Actions, fail, redirect } from "@sveltejs/kit";
import { encryptPWD } from "$lib/auth";
import { PrismaClient } from "@prisma/client";

export const actions = {
	default: async (event) => {
		const data = await event.request.formData();
		const email = data.get("email");
		const password = encryptPWD(String(data.get("password")));
		const client = new PrismaClient();

		try {
			if (!email) {
				return fail(400, { error: "Email is required" });
			}
			const user = await client.user.findUnique({
				where: { email: String(email), hashedPassword: password }
			});
			client.$disconnect();
			if (!user) {
				return fail(401, { error: "Email or password invalid" });
			}
			event.cookies.set("token", user.token || "", { path: "/", httpOnly: false });
		} catch (error) {
			client.$disconnect();
			return fail(500, { error: "Failed to log in" });
		}
		return redirect(301, "/dashboard");
	}
} satisfies Actions;
