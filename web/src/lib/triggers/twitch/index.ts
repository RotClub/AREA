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
	return false;
}

export async function reactionStartCommercialTrigger(
    nodeId: number,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    service_meta: any,
    meta: Record<string, string | number | boolean | Date>
): Promise<boolean> {
    return false;
}
