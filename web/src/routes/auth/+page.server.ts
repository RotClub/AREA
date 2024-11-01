import { type Actions, fail, redirect } from "@sveltejs/kit";
import { encryptPWD } from "$lib/auth";
import { PrismaClient } from "@prisma/client";
import { getToken } from "$lib/web";

export const actions = {
	default: async (event) => {
		const data = await event.request.formData();
		const email = data.get("email");
		const login: boolean = data.get("login") === "on";
		const password = encryptPWD(String(data.get("password")));
		const client = new PrismaClient();

		if (!login) {
			const confirmPassword = encryptPWD(String(data.get("confirmPassword")));
			if (password !== confirmPassword) {
				return fail(400, { error: "Passwords do not match" });
			}
			const res = await event.fetch("/api/auth/register", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({
					email: email,
					password: password,
					role: "USER",
					username: data.get("username")
				})
			})
				.then((res) => res.json())
				.catch((error) => fail(500, { error: "Failed to create user: " + error }));
			console.log(res);
			return redirect(301, "/auth?login=true");
		}

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
			event.cookies.set(getToken(), user.token || "", { path: "/", httpOnly: false });
		} catch (error) {
			client.$disconnect();
			return fail(500, { error: "Failed to log in" });
		}
		return redirect(301, "/dashboard");
	}
} satisfies Actions;
