import { PrismaClient, Provider, UserRole } from "@prisma/client";
import { checkAccess } from "$lib/api";

export async function addProvider(
	provider: string,
	data: Record<string, unknown>,
	request: Request | string
): Promise<Response> {
	const providerList = Object.values(Provider).map((p) => String(p || "").toLowerCase());
	if (!providerList.includes(provider || "")) {
		return new Response(JSON.stringify({ error: "Invalid provider" }), {
			status: 400,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const correctProvider = Object.values(Provider).find(
		(p) => String(p).toLowerCase() === provider || ""
	);
	const client = new PrismaClient();
	let access: { valid: boolean; err: null | string };
	let token: string;
	if (typeof request === "string") {
		access = await checkAccess(client, { token: request }, UserRole.USER, false);
		token = request;
	} else {
		access = await checkAccess(client, request, UserRole.USER);
		token = request.headers.get("Authorization")?.replace("Bearer ", "") || "";
	}
	if (!access.valid) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: access.err }), {
			status: 403,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const user_id = await client.user.findUnique({
		where: {
			token: token
		},
		select: {
			id: true
		}
	});
	if (!user_id) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "User not found" }), {
			status: 404,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const delete_service = await client.service.deleteMany({
		where: {
			userId: user_id.id,
			providerType: correctProvider
		}
	});
	if (!delete_service) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "Could not unlink user from service" }), {
			status: 500,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	if (correctProvider && data) {
		await client.user.update({
			where: {
				token: token
			},
			data: {
				Service: {
					create: {
						providerType: correctProvider,
						metadata: data as Record<string, string>
					}
				}
			}
		});
	}
	client.$disconnect();
	return new Response(`Provider ${provider} was successfully added to user`, {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
}

export async function removeProvider(
	provider: string,
	request: Request | string
): Promise<Response> {
	const providerList = Object.values(Provider).map((p) => String(p).toLowerCase());
	if (!providerList.includes(provider || "")) {
		return new Response(JSON.stringify({ error: "Invalid provider" }), {
			status: 400,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const correctProvider = Object.values(Provider).find(
		(p) => String(p).toLowerCase() === provider || ""
	);
	const client = new PrismaClient();
	let access: { valid: boolean; err: null | string };
	let token: string;
	if (typeof request === "string") {
		access = await checkAccess(client, { token: request }, UserRole.USER, false);
		token = request;
	} else {
		access = await checkAccess(client, request, UserRole.USER);
		token = request.headers.get("Authorization")?.replace("Bearer ", "") || "";
	}
	if (!access.valid) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: access.err }), {
			status: 403,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	const user_id = await client.user.findUnique({
		where: {
			token: token
		},
		select: {
			id: true
		}
	});
	if (!user_id) {
		client.$disconnect();
		return new Response(JSON.stringify({ error: "User not found" }), {
			status: 404,
			headers: {
				"Content-Type": "application/json"
			}
		});
	}
	await client.service.deleteMany({
		where: {
			userId: user_id.id,
			providerType: correctProvider
		}
	});
	client.$disconnect();
	return new Response(`Provider ${provider} was successfully remove from user`, {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
}
