export async function actionListeningTrackTrigger(
	userId: number,
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
	userId: number,
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
