import { createJWTToken } from "$lib/auth";
import { PrismaClient } from "@prisma/client";

export const GET = async (event) => {
	const token = event.request.headers.get("Authorization")?.replace("Bearer ", "");

	if (!token) {
		return new Response(JSON.stringify({ error: "Token is required" }), {
			status: 400,
			headers: { "Content-Type": "application/json" }
		});
	}
	const client = new PrismaClient();
	const user = await client.user.findUnique({
		where: {
			token: token
		}
	});
	if (!user) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "User not found" }), {
			status: 404,
			headers: { "Content-Type": "application/json" }
		});
	}
	const refresh = createJWTToken({
		email: user.email,
		password: user.hashedPassword,
		username: user.username,
		role: user.role,
		old_token: token
	});

	if (!refresh) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "Failed to create token" }), {
			status: 500,
			headers: { "Content-Type": "application/json" }
		});
	}
	const update = await client.user.update({
		where: {
			token: token
		},
		data: {
			token: refresh as string
		}
	});
	client.$disconnect();
	if (!update) {
		return new Response(JSON.stringify({ error: "Failed to update token" }), {
			status: 500,
			headers: { "Content-Type": "application/json" }
		});
	}
	client.$disconnect();
	return new Response(JSON.stringify({ success: "Logged out" }), {
		status: 200,
		headers: { "Content-Type": "application/json" }
	});
};