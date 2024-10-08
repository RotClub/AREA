import { PrismaClient, UserRole } from "@prisma/client";
import { createJWTToken, encryptPWD } from "$lib/auth";
import { checkAccess } from "$lib/api";

export const POST = async ({ request }) => {
	const client = new PrismaClient();

	try {
		// Validate the token + user role
		const { valid, err } = await checkAccess(client, request);
		if (!valid) {
			client.$disconnect();
			return new Response(JSON.stringify({ error: err }), {
				status: 400,
				headers: { "Content-Type": "application/json" }
			});
		}
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
		const token = await createJWTToken({
			email: body.email,
			password: password,
			username: body.username,
			role: body.role
		}).then((token: string) => token);

		const user = await client.user.create({
			data: {
				email: body.email,
				hashedPassword: password,
				role: body.role,
				username: body.username,
				token: token
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
