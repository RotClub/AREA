import { adaptUrl } from "$lib/api";

export enum PlatformType {
	Android = "Android",
	Web = "Web"
}

export function getPlatformType(request: Request): PlatformType {
	const userAgent = request.headers.get("user-agent") || "";

	if (/Android/i.test(userAgent) && /wv/.test(userAgent)) {
		return PlatformType.Android;
	} else {
		return PlatformType.Web;
	}
}

export function getRedirectionURL(type: PlatformType): string {
	switch (type) {
		case PlatformType.Android:
			return "https://area-app.vercel.app/app/redirect/";
		case PlatformType.Web:
			return `${adaptUrl()}/dashboard`;
		default:
			return "";
	}
}
