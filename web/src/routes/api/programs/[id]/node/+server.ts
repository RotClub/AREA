import { PrismaClient } from "@prisma/client";
import { checkAccess } from "$lib/api";
import { UserRole } from "@prisma/client";

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

export const DELETE = async ({ request, params }) => {
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
					delete: {
						id: body.id
					}
				}
			}
		});
	}

	if (body.isReaction) {
		await client.reaction.delete({
			where: {
				id: body.id
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

// update action or reaction metadata
export const PATCH = async ({ request, params }) => {
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
		await client.action.update({
			where: {
				id: body.id
			},
			data: {
				metadata: body.metadata
			}
		});
	}

	if (body.isReaction) {
		await client.reaction.update({
			where: {
				id: body.id
			},
			data: {
				metadata: body.metadata
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
