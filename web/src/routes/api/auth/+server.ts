import {createJWTToken, encryptPWD, verifyJWTAuth} from "$lib/auth";
import {UserRole} from "@prisma/client";
import {PrismaClient} from "@prisma/client";
import { checkAPIAccess } from "$lib/api";

export const POST = async ({ request, url }) => {
	const client = new PrismaClient();

	// Validate the token + user role
	const { valid, err } = await checkAPIAccess(client, request, url, UserRole.ADMIN);
	if (!valid) {
		return new Response(JSON.stringify({ error: err }), {
			status: 400,
			headers: { "Content-Type": "application/json" },
		});
	}

	try {
		const body = await request.json();

		if (!body.email || !body.password || !body.role) {
			return new Response(JSON.stringify({ error: "Email, password and role are required" }), {
				status: 400,
				headers: { "Content-Type": "application/json" },
			});
		}
		if (!Object.values(UserRole).includes(body.role)) {
			return new Response(JSON.stringify({ error: "Invalid role" }), {
				status: 400,
				headers: { "Content-Type": "application/json" }
			});
		}
		const password = encryptPWD(body.password);
		const token = await createJWTToken({ email: body.email, password: password, role: body.role })
			.then((token: string) => token);

		const user = await client.user.create(
			{
				data: {
					email: body.email,
					hashedPassword: password,
					role: body.role,
					token: token
				}
			});
		const res = new Response(JSON.stringify(user), {
			status: 200,
			headers: { "Content-Type": "application/json" },
		});
		console.log(res);
		client.$disconnect();
		return res;
	} catch (error) {
		console.error("Error creating user:", error);
		client.$disconnect();
		return new Response(JSON.stringify({ error: "Failed to create user" }), {
			status: 500,
			headers: { "Content-Type": "application/json" },
		});
	}
}