export async function actionIsStreamingTrigger(
	userId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
	return false;
}

export async function actionViewcountReachesTrigger(
	userId: number,
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	service_meta: any,
	meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
    const url: string = `https://api.twitch.tv/helix/users`
    return false;
}
