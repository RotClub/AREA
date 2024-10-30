import { PrismaClient, type Action } from "@prisma/client";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export async function refreshRedditToken(service_meta: any): Promise<Record<string, any>> {
	const basic: string = Buffer.from(`${process.env.REDDIT_CLIENT_ID}:${process.env.REDDIT_CLIENT_SECRET}`).toString("base64");
	const url: string = "https://www.reddit.com/api/v1/access_token";
	const res: Response = await fetch(url, {
		method: "POST",
		headers: {
			Authorization: `Basic ${basic}`,
			"Content-Type": "application/x-www-form-urlencoded"
		},
		body: new URLSearchParams({
			grant_type: "refresh_token",
			refresh_token: service_meta.refresh_token
		})
	});
	if (!res.ok) {
		return {};
	}
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const data: Record<string, any> = await res.json();
	data["expires_at"] = new Date((data["expires_in"] as number * 1000) + Date.now()).getTime();
	return data;
}

export async function actionUnreadMessageTrigger(
    nodeId: number,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    service_meta: any,
    meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
    const url: string = "https://oauth.reddit.com/message/unread";
    const res: Response = await fetch(url, {
        headers: {
            Authorization: `Bearer ${service_meta.access_token}`
        }
    });
    if (!res.ok) {
        return false;
    }
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const data: Record<string, any> = await res.json();
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
                inbox_count: data["data"]["dist"]
            }
        }
    });
    if (!action.metadata) {
        return false;
    }
    return data["data"]["dist"] > (action.metadata as Record<string, string | number | boolean | Date>)["inbox_count"];
}

export async function actionGainedKarmaTrigger(
    nodeId: number,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    service_meta: any,
    meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
    const url: string = `https://oauth.reddit.com/user/${meta.username}/about`;
    const res: Response = await fetch(url, {
        headers: {
            Authorization: `Bearer ${service_meta.access_token}`
        }
    });
    if (!res.ok) {
        return false;
    }
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const data: Record<string, any> = await res.json();
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
                karma: data["data"]["total_karma"]
            }
        }
    });
    if (!action.metadata) {
        return false;
    }
    return data["data"]["total_karma"] > (action.metadata as Record<string, string | number | boolean | Date>)["karma"];
}

export async function actionUsernameBecameAvailableTrigger(
    nodeId: number,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    service_meta: any,
    meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
    const url: string = `https://oauth.reddit.com/api/username_available?user=${meta.username}`;
    const res: Response = await fetch(url, {
        headers: {
            Authorization: `Bearer ${service_meta.access_token}`
        }
    });
    const result: boolean = Boolean(await res.text());
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
                available: result
            }
        }
    });
    if (!action.metadata) {
        return false;
    }
    return result && !(action.metadata as Record<string, string | number | boolean | Date>)["available"];
}

export async function reactionAddFriendTrigger(
    nodeId: number,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    service_meta: any,
    meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
    const url: string = `https://oauth.reddit.com/api/v1/me/friends/${meta.username}`;
    const res: Response = await fetch(url, {
        method: "PUT",
        headers: {
            Authorization: `Bearer ${service_meta.access_token}`
        },
        body: JSON.stringify({
            name: meta.username,
            note: meta.note
        })
    });
    return res.ok;
}

export async function reactionDeleteFriendTrigger(
    nodeId: number,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    service_meta: any,
    meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
    const url: string = `https://oauth.reddit.com/api/v1/me/friends/${meta.username}`;
    const res: Response = await fetch(url, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${service_meta.access_token}`
        },
        body: JSON.stringify({
            name: meta.username
        })
    });
    return res.ok;
}
