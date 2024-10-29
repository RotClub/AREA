import { parse as cookieParser } from "cookie";
import { getToken } from "$lib/web";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export async function apiRequest(
	method: string,
	url: string,
	body: Record<string, unknown> = {}
): Promise<Response> {
	if (!window || !document) return new Response("Not in a browser environment", { status: 500 });
	if (method === "GET") {
		const response = await window.fetch(url, {
			method: method,
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${cookieParser(document.cookie)[getToken()]}`
			}
		});
		return response;
	} else {
		const response = await window.fetch(url, {
			method: method,
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${cookieParser(document.cookie)[getToken()]}`
			},
			body: JSON.stringify(body)
		});
		return response;
	}
}
