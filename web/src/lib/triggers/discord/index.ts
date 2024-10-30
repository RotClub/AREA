import { type Action, PrismaClient } from "@prisma/client";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export async function refreshDiscordToken(service_meta: any): Promise<Record<string, any>> {
	const basic: string = Buffer.from(
		`${process.env.DISCORD_CLIENT_ID}:${process.env.DISCORD_CLIENT_SECRET}`
	).toString("base64");
	const url: string = "https://discord.com/api/oauth2/token";

	const response: Response = await fetch(url, {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded",
			Authorization: `Basic ${basic}`
		},
		body: new URLSearchParams({
			grant_type: "refresh_token",
			refresh_token: service_meta.refresh_token
		})
	});
	if (!response.ok) {
		return {};
	}
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const data: Record<string, any> = await response.json();
	data["expires_at"] = new Date((data["expires_in"] as number) * 1000 + Date.now()).getTime();
	return data;
}

export async function actionUsernameChangedTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
	const url: string = "https://discord.com/api/users/@me";

	const response: Response = await fetch(url, {
		headers: {
			Authorization: `Bearer ${service_meta.access_token}`
		}
	});
	if (!response.ok) {
		return false;
	}
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const data: Record<string, any> = await response.json();
	const client: PrismaClient = new PrismaClient();
	const action: Action | null = await client.action.findFirst({
		where: {
			id: nodeId
		}
	});
	if (action === null) {
		return false;
	}
	await client.action.update({
		where: {
			id: nodeId
		},
		data: {
			persistentData: {
				username: data["global_name"]
			}
		}
	});
	if (!action.metadata) {
		return false;
	}
	return (
		(action.persistentData as Record<string, string | number | boolean | Date>)["username"] !==
		data["global_name"]
	);
}

export async function actionPremiumExpiredTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
	const url: string = "https://discord.com/api/users/@me";

	const response: Response = await fetch(url, {
		headers: {
			Authorization: `Bearer ${service_meta.access_token}`
		}
	});
	if (!response.ok) {
		return false;
	}
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const data: Record<string, any> = await response.json();
	const client: PrismaClient = new PrismaClient();
	const action: Action | null = await client.action.findFirst({
		where: {
			id: nodeId
		}
	});
	if (action === null) {
		return false;
	}
	await client.action.update({
		where: {
			id: nodeId
		},
		data: {
			persistentData: {
				premium: Number(data["premium_type"])
			}
		}
	});
	if (!action.metadata) {
		return false;
	}
	return (
		(action.persistentData as Record<string, number>["premium"]) > Number(data["premium_type"])
	);
}

export async function actionServerJoinedTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
	const client: PrismaClient = new PrismaClient();
	const url: string = `https://discord.com/api/users/@me/guilds`;

	const response: Response = await fetch(url, {
		headers: {
			Authorization: `Bearer ${service_meta.access_token}`
		}
	});
	if (!response.ok) {
		return false;
	}
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const data: Record<string, any> = await response.json();
	const action: Action | null = await client.action.findFirst({
		where: {
			id: nodeId
		}
	});
	if (action === null) {
		return false;
	}
	await client.action.update({
		where: {
			id: nodeId
		},
		data: {
			persistentData: {
				joined: data.length
			}
		}
	});
	if (!action.metadata) {
		return false;
	}
	return data.length > (action.persistentData as Record<string, number>)["joined"];
}

export async function actionServerLeftTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
	const client: PrismaClient = new PrismaClient();
	const url: string = `https://discord.com/api/users/@me/guilds`;

	const response: Response = await fetch(url, {
		headers: {
			Authorization: `Bearer ${service_meta.access_token}`
		}
	});
	if (!response.ok) {
		return false;
	}
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const data: Record<string, any> = await response.json();
	const action: Action | null = await client.action.findFirst({
		where: {
			id: nodeId
		}
	});
	if (action === null) {
		return false;
	}
	await client.action.update({
		where: {
			id: nodeId
		},
		data: {
			persistentData: {
				left: data.length
			}
		}
	});
	if (!action.metadata) {
		return false;
	}
	return data.length < (action.persistentData as Record<string, number>)["left"];
}
