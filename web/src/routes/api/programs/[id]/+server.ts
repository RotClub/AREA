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

export const PUT = async ({ request, params }) => {
	const client = new PrismaClient();
	const body = await request.json();
	const programId = Number(params.id);

	const { valid, err } = await checkAccess(client, request, UserRole.USER);
	if (!valid) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: err }), {
			status: 403,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}

	const user = await client.user.findUnique({
		where: {
			token: request.headers.get("Authorization")?.split(" ")[1]
		},
		select: {
			id: true
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

	const program = await client.program.findUnique({
		where: {
			id: programId,
			userId: user.id
		}
	});

	if (!program) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "Program not found" }), {
			status: 404,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}

	if (!body.isAction && !body.isReaction) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "Action or Reaction is required" }), {
			status: 400,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}

	if (body.isAction) {
		await client.program.update({
			where: {
				id: programId
			},
			data: {
				actions: {
					create: {
						actionId: body.id,
						metadata: body.metadata
					}
				}
			}
		});
	}

	if (body.isReaction) {
		const action = await client.action.findUnique({
			where: {
				id: body.id
			}
		});
		if (!action) {
			client.$disconnect();
			return new Response(JSON.stringify({ error: "Action not found" }), {
				status: 404,
				headers: {
					"Content-Type": "application/json"
				}
			});
		}
		await client.action.update({
			where: {
				id: body.id
			},
			data: {
				reactions: {
					create: {
						reactionId: body.reactionId,
						metadata: body.metadata
					}
				}
			}
		});
	}

	client.$disconnect();

	return new Response(JSON.stringify({ success: true }), {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
};
