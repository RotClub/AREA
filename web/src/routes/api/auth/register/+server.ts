import { PrismaClient, UserRole } from "@prisma/client";
import { createJWTToken, encryptPWD } from "$lib/auth";

export const POST = async ({ request }) => {
	const client = new PrismaClient();

	try {
		const body = await request.json();

		if (!body.email || !body.password || !body.role || !body.username) {
			return new Response(
				JSON.stringify({ error: "Email, password, username and role are required" }),
				{
					status: 400,
					headers: { "Content-Type": "application/json" }
				}
			);
		}
		if (!Object.values(UserRole).includes(body.role)) {
			return new Response(JSON.stringify({ error: "Invalid role" }), {
				status: 400,
				headers: { "Content-Type": "application/json" }
			});
		}
		const password = encryptPWD(body.password);
		const token = createJWTToken({
			email: body.email,
			password: password,
			username: body.username,
			role: body.role
		});

		if (!token) {
			return new Response(JSON.stringify({ error: "Failed to create token" }), {
				status: 500,
				headers: { "Content-Type": "application/json" }
			});
		}

		const user = await client.user.create({
			data: {
				email: body.email,
				hashedPassword: password,
				role: body.role,
				username: body.username,
				token: token as string
			}
		});
		const res = new Response(JSON.stringify(user), {
			status: 200,
			headers: { "Content-Type": "application/json" }
		});
		client.$disconnect();
		return res;
	} catch (error) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "Failed to create user: " + error }), {
			status: 500,
			headers: { "Content-Type": "application/json" }
		});
	}
};
