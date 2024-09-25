import { join } from "path";
import type { Config } from "tailwindcss";
import type { PluginAPI } from "tailwindcss/types/config";
import forms from "@tailwindcss/forms";
import typography from "@tailwindcss/typography";
import { skeleton } from "@skeletonlabs/tw-plugin";
import { FrispyTheme } from "./frispy-theme";
import flattenColorPalette from "tailwindcss/lib/util/flattenColorPalette";
import aspectRatio from "@tailwindcss/aspect-ratio";
import viewTransitions from "tailwindcss-view-transitions";

export default {
	darkMode: "class",
	content: [
		"./src/**/*.{html,js,svelte,ts}",
		join(require.resolve("@skeletonlabs/skeleton"), "../**/*.{html,js,svelte,ts}")
	],
	theme: {
		extend: {}
	},
	plugins: [
		forms,
		typography,
		skeleton({
			themes: {
				custom: [FrispyTheme]
			}
		}),
		aspectRatio,
		addVariablesForColors,
		viewTransitions
	]
} satisfies Config;

function addVariablesForColors({ addBase, theme }: PluginAPI) {
	const allColors = flattenColorPalette(theme("colors"));
	const newVars: Record<string, string> = Object.fromEntries(
		Object.entries(allColors).map(([key, val]) => [`--${key}`, val as string])
	);

	addBase({
		":root": newVars
	});
}
