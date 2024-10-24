import { PrismaClient, type Program } from "@prisma/client";

function getProgramNodeAmount(program: Program): number {
	//@ts-expect-error - This is a hack to get around the fact that linter doesn't know that Actions is a property of Program
	return program.Actions ? program.Actions.length : 0;
}

export const GET = async ({ request }) => {
	const client: PrismaClient = new PrismaClient();

	const bearer = request.headers.get("Authorization");
	const token = bearer ? bearer.replace("Bearer ", "") : "";
	const user = await client.user.findUnique({
		where: {
			token: token || ""
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

	const programs = (
		await client.program.findMany({
			where: {
				userId: user.id
			},
			select: {
				id: true,
				name: true,
				actions: {
					select: {
						actionId: true,
						id: true,
						metadata: true,
						reactions: true
					}
				}
			}
		})
	).sort((a, b) => a.id - b.id);

	for (const program of programs) {
		//@ts-expect-error - This is a hack to get around the fact that we add a value to the object that isn't in the schema
		program.nodeAmount = getProgramNodeAmount(program);
	}

	client.$disconnect();

	return new Response(JSON.stringify(programs), {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
};

export const POST = async ({ cookies, request }) => {
	const client = new PrismaClient();
	const body = await request.json();

	if (!body.name) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "Name is required" }), {
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
		},
		select: {
			Program: true,
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

	const program = await client.program.create({
		data: {
			name: body.name,
			User: {
				connect: {
					id: user.id
				}
			}
		}
	});

	client.$disconnect();

	return new Response(JSON.stringify(program), {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
};
