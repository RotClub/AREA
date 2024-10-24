import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

// eslint-disable-next-line @typescript-eslint/array-type
export function cn(...inputs: ClassValue[]) {
	return twMerge(clsx(inputs));
}
