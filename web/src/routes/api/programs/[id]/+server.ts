import { checkAccess } from "$lib/api.js";
import { PrismaClient, UserRole } from "@prisma/client";

export const DELETE = async ({ cookies, params }) => {
	const client = new PrismaClient();

	const user = await client.user.findUnique({
		where: {
			token: cookies.get("token")
		},
		select: {
			Program: true
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

	if (!params.id) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "ID is required" }), {
			status: 400,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}

	await client.program.delete({
		where: {
			id: Number(params.id)
		}
	});

	client.$disconnect();

	return new Response(JSON.stringify({ success: true }), {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
};

export const PATCH = async ({ cookies, request, params }) => {
	const client = new PrismaClient();
	const body = await request.json();

	const user = await client.user.findUnique({
		where: {
			token: cookies.get("token")
		},
		select: {
			Program: true
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

	if (!body.name) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "Name is required" }), {
			status: 400,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}

	await client.program.update({
		where: {
			id: Number(params.id)
		},
		data: {
			name: body.name
		}
	});

	client.$disconnect();

	return new Response(JSON.stringify({ success: true }), {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
};
