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
import svgToDataUri from "mini-svg-data-uri";

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
		viewTransitions,
		function ({ matchUtilities, theme }: any) {
			matchUtilities(
				{
					"bg-grid": (value: any) => ({
						backgroundImage: `url("${svgToDataUri(
							`<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32" width="32" height="32" fill="none" stroke="${value}"><path d="M0 .5H31.5V32"/></svg>`
						)}")`
					}),
					"bg-grid-small": (value: any) => ({
						backgroundImage: `url("${svgToDataUri(
							`<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32" width="8" height="8" fill="none" stroke="${value}"><path d="M0 .5H31.5V32"/></svg>`
						)}")`
					}),
					"bg-dot": (value: any) => ({
						backgroundImage: `url("${svgToDataUri(
							`<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32" width="16" height="16" fill="none"><circle fill="${value}" id="pattern-circle" cx="10" cy="10" r="1.6257413380501518"></circle></svg>`
						)}")`
					})
				},
				{ values: flattenColorPalette(theme("backgroundColor")), type: "color" }
			);
		}
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
