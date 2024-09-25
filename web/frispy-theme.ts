import type { CustomThemeConfig } from "@skeletonlabs/tw-plugin";

export const FrispyTheme: CustomThemeConfig = {
	name: "frispy-theme",
	properties: {
		// =~= Theme Properties =~=
		"--theme-font-family-base":
			"Nunito, Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji'",
		"--theme-font-family-heading":
			"Nunito, Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji'",
		"--theme-font-color-base": "0 0 0",
		"--theme-font-color-dark": "255 255 255",
		"--theme-rounded-base": "4px",
		"--theme-rounded-container": "8px",
		"--theme-border-base": "1px",
		// =~= Theme On-X Colors =~=
		"--on-primary": "255 255 255",
		"--on-secondary": "255 255 255",
		"--on-tertiary": "255 255 255",
		"--on-success": "0 0 0",
		"--on-warning": "0 0 0",
		"--on-error": "255 255 255",
		"--on-surface": "255 255 255",
		// =~= Theme Colors  =~=
		// primary | #B03AEB
		"--color-primary-50": "243 225 252", // #f3e1fc
		"--color-primary-100": "239 216 251", // #efd8fb
		"--color-primary-200": "235 206 250", // #ebcefa
		"--color-primary-300": "223 176 247", // #dfb0f7
		"--color-primary-400": "200 117 241", // #c875f1
		"--color-primary-500": "176 58 235", // #B03AEB
		"--color-primary-600": "158 52 212", // #9e34d4
		"--color-primary-700": "132 44 176", // #842cb0
		"--color-primary-800": "106 35 141", // #6a238d
		"--color-primary-900": "86 28 115", // #561c73
		// secondary | #7b3b9b
		"--color-secondary-50": "235 226 240", // #ebe2f0
		"--color-secondary-100": "229 216 235", // #e5d8eb
		"--color-secondary-200": "222 206 230", // #decee6
		"--color-secondary-300": "202 177 215", // #cab1d7
		"--color-secondary-400": "163 118 185", // #a376b9
		"--color-secondary-500": "123 59 155", // #7b3b9b
		"--color-secondary-600": "111 53 140", // #6f358c
		"--color-secondary-700": "92 44 116", // #5c2c74
		"--color-secondary-800": "74 35 93", // #4a235d
		"--color-secondary-900": "60 29 76", // #3c1d4c
		// tertiary | #4e2960
		"--color-tertiary-50": "228 223 231", // #e4dfe7
		"--color-tertiary-100": "220 212 223", // #dcd4df
		"--color-tertiary-200": "211 202 215", // #d3cad7
		"--color-tertiary-300": "184 169 191", // #b8a9bf
		"--color-tertiary-400": "131 105 144", // #836990
		"--color-tertiary-500": "78 41 96", // #4e2960
		"--color-tertiary-600": "70 37 86", // #462556
		"--color-tertiary-700": "59 31 72", // #3b1f48
		"--color-tertiary-800": "47 25 58", // #2f193a
		"--color-tertiary-900": "38 20 47", // #26142f
		// success | #3de184
		"--color-success-50": "226 251 237", // #e2fbed
		"--color-success-100": "216 249 230", // #d8f9e6
		"--color-success-200": "207 248 224", // #cff8e0
		"--color-success-300": "177 243 206", // #b1f3ce
		"--color-success-400": "119 234 169", // #77eaa9
		"--color-success-500": "61 225 132", // #3de184
		"--color-success-600": "55 203 119", // #37cb77
		"--color-success-700": "46 169 99", // #2ea963
		"--color-success-800": "37 135 79", // #25874f
		"--color-success-900": "30 110 65", // #1e6e41
		// warning | #EAB308
		"--color-warning-50": "252 244 218", // #fcf4da
		"--color-warning-100": "251 240 206", // #fbf0ce
		"--color-warning-200": "250 236 193", // #faecc1
		"--color-warning-300": "247 225 156", // #f7e19c
		"--color-warning-400": "240 202 82", // #f0ca52
		"--color-warning-500": "234 179 8", // #EAB308
		"--color-warning-600": "211 161 7", // #d3a107
		"--color-warning-700": "176 134 6", // #b08606
		"--color-warning-800": "140 107 5", // #8c6b05
		"--color-warning-900": "115 88 4", // #735804
		// error | #d63d45
		"--color-error-50": "249 226 227", // #f9e2e3
		"--color-error-100": "247 216 218", // #f7d8da
		"--color-error-200": "245 207 209", // #f5cfd1
		"--color-error-300": "239 177 181", // #efb1b5
		"--color-error-400": "226 119 125", // #e2777d
		"--color-error-500": "214 61 69", // #d63d45
		"--color-error-600": "193 55 62", // #c1373e
		"--color-error-700": "161 46 52", // #a12e34
		"--color-error-800": "128 37 41", // #802529
		"--color-error-900": "105 30 34", // #691e22
		// surface | #27262c
		"--color-surface-50": "223 222 223", // #dfdedf
		"--color-surface-100": "212 212 213", // #d4d4d5
		"--color-surface-200": "201 201 202", // #c9c9ca
		"--color-surface-300": "169 168 171", // #a9a8ab
		"--color-surface-400": "104 103 107", // #68676b
		"--color-surface-500": "39 38 44", // #27262c
		"--color-surface-600": "35 34 40", // #232228
		"--color-surface-700": "29 29 33", // #1d1d21
		"--color-surface-800": "23 23 26", // #17171a
		"--color-surface-900": "19 19 22" // #131316
	}
};
