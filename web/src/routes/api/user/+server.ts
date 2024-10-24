import { encryptPWD } from "$lib/auth.js";
import { PrismaClient } from "@prisma/client";

export const GET = async ({ request }) => {
	const client: PrismaClient = new PrismaClient();

	const bearer = request.headers.get("Authorization");
	const token = bearer ? bearer.replace("Bearer ", "") : "";
	const user = await client.user.findUnique({
		where: {
			token: token || ""
		},
		select: {
			email: true,
			username: true,
			createdAt: true,
			role: true
		}
	});

	if (!user) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "User not found" }), {
			status: 404,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}

	client.$disconnect();

	return new Response(JSON.stringify(user), {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
};

export const POST = async ({ request, cookies }) => {
	const client: PrismaClient = new PrismaClient();

	const { email, username, password } = await request.json();

	if (!email || !username) {
		return new Response(JSON.stringify({ error: "Missing required fields" }), {
			status: 400,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}

	const bearer = request.headers.get("Authorization");
	const token = bearer ? bearer.replace("Bearer ", "") : "";
	const user = await client.user.findUnique({
		where: {
			token: token
		}
	});

	if (!user) {
		return new Response(JSON.stringify({ error: "User not found" }), {
			status: 404,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}

	if (!password) {
		const updatedUser = await client.user.update({
			where: {
				token: token
			},
			data: {
				email,
				username
			}
		});
	}

	if (password) {
		const updatedUser = await client.user.update({
			where: {
				token: token
			},
			data: {
				email,
				username,
				hashedPassword: encryptPWD(password)
			}
		});
	}

	client.$disconnect();

	return new Response(JSON.stringify({ success: "User updated" }), {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
};
