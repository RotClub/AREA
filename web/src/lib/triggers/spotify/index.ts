// eslint-disable-next-line @typescript-eslint/no-explicit-any
export async function refreshSpotifyToken(service_meta: any): Promise<Record<string, any>> {
	const basic: string = Buffer.from(
		`${process.env.SPOTIFY_CLIENT_ID}:${process.env.SPOTIFY_CLIENT_SECRET}`
	).toString("base64");
	const url: string = "https://accounts.spotify.com/api/token";
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
		console.error(res);
		console.error(await res.text());
		return {};
	}
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const data: Record<string, any> = await res.json();
	data["expires_at"] = new Date((data["expires_in"] as number) * 1000 + Date.now()).getTime();
	return data;
}

export async function actionListeningTrackTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
	const url: string = "https://api.spotify.com/v1/me/player/currently-playing";

	const response: Response = await fetch(url, {
		headers: {
			Authorization: `Bearer ${service_meta.access_token}`
		}
	});

	if (!response.ok) {
		return false;
	}
	if (response.status === 204) {
		return false;
	}
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const data: any = await response.json();
	return meta.track_id === data["item"]["id"];
}

export async function reactionPlayTrackTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
	const url: string = "https://api.spotify.com/v1/me/player/play";

	const response: Response = await fetch(url, {
		method: "PUT",
		headers: {
			Authorization: `Bearer ${service_meta.access_token}`,
			"Content-Type": "application/json"
		},
		body: JSON.stringify({
			uris: [`spotify:track:${meta.track_id}`],
			position_ms: meta.position_ms
		})
	});
	return response.ok;
}

export async function reactionSkipToNextTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	meta: any
): Promise<boolean> {
	const url: string = "https://api.spotify.com/v1/me/player/next";

	const response: Response = await fetch(url, {
		method: "POST",
		headers: {
			Authorization: `Bearer ${service_meta.access_token}`
		}
	});
	return response.ok;
}

export async function reactionSkipToPreviousTrigger(
	nodeId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	meta: any
): Promise<boolean> {
	const url: string = "https://api.spotify.com/v1/me/player/previous";

	const response: Response = await fetch(url, {
		method: "POST",
		headers: {
			Authorization: `Bearer ${service_meta.access_token}`
		}
	});
	return response.ok;
}
