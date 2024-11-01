import { checkAccess } from "$lib/api.js";
import { PrismaClient, UserRole } from "@prisma/client";

export const DELETE = async ({ request, params }) => {
	const client = new PrismaClient();

	const bearer = request.headers.get("Authorization");
	const token = bearer ? bearer.replace("Bearer ", "") : "";
	const user = await client.user.findUnique({
		where: {
			token: token || ""
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

	const actions = await client.action.findMany({
		where: {
			programId: Number(params.id)
		}
	});

	actions.forEach(async (action) => {
		await client.reaction.deleteMany({
			where: {
				actionId: action.id
			}
		});
	});

	await client.action.deleteMany({
		where: {
			programId: Number(params.id)
		}
	});

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

export const PATCH = async ({ request, params }) => {
	const client = new PrismaClient();
	const body = await request.json();

	const bearer = request.headers.get("Authorization");
	const token = bearer ? bearer.replace("Bearer ", "") : "";
	const user = await client.user.findUnique({
		where: {
			token: token || ""
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
