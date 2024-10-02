import { PrismaClient } from "@prisma/client";
import { encryptPWD } from "$lib/auth";

export const POST = async ({ request }) => {
	const client = new PrismaClient();

	// Validate the token + user role
	try {
		const body = await request.json();

		if (!body.email || !body.password) {
			return new Response(JSON.stringify({ error: "Email and password are required" }), {
				status: 400,
				headers: { "Content-Type": "application/json" },
			});
		}
		const password = encryptPWD(body.password);
		const user = await client.user.findUnique({
			where: {
				email: body.email
			},
			select: {
				token: true,
				role: true,
				hashedPassword: true
			}
		});
		client.$disconnect();
		if (!user) {
			return new Response(JSON.stringify({ error: "User not found" }), {
				status: 404,
				headers: { "Content-Type": "application/json" },
			});
		}
		if (user.hashedPassword != password) {
			return new Response(JSON.stringify({ error: "Password is invalid" }), {
				status: 404,
				headers: { "Content-Type": "application/json" },
			});
		}
		return new Response(JSON.stringify({
			token: user.token,
			role: user.role
		}), {
			status: 200,
			headers: { "Content-Type": "application/json" },
		});
	} catch (err) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "Failed to find user: " + err }), {
			status: 500,
			headers: { "Content-Type": "application/json" },
		});
	}
}