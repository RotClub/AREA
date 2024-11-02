import { type Action, PrismaClient } from "@prisma/client";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export async function refreshTwitchToken(service_meta: any): Promise<Record<string, any>> {
	return {};
}

export async function actionIsStreamingTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
	const client: PrismaClient = new PrismaClient();

	const url: string = `https://api.twitch.tv/helix/streams?user_id=${meta.user_id}`;
	const response: Response = await fetch(url, {
		method: "GET",
		headers: {
			Authorization: `Bearer ${service_meta.access_token}`,
			"Client-ID": process.env.TWITCH_CLIENT_ID || ""
		}
	});
	if (!response.ok) {
		return false;
	}
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const data: Record<string, any> = await response.json();
	if (data["data"].length === 0) {
		return false;
	}
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const stream: Record<string, any> = data["data"][0];
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
				stream_id: stream["id"]
			}
		}
	});
	if (!action.metadata) {
		return false;
	}
	return (
		(action.persistentData as Record<string, string | number | boolean | Date>)["stream_id"] !==
		stream["id"]
	);
}

export async function reactionStartCommercialTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
	const url: string = `https://api.twitch.tv/helix/channels/commercial?broadcaster_id=${meta.user_id}&length=${meta.length}`;
	const response: Response = await fetch(url, {
		method: "POST",
		headers: {
			Authorization: `Bearer ${service_meta.access_token}`,
			"Client-ID": process.env.TWITCH_CLIENT_ID || ""
		}
	});
	return response.ok;
}
