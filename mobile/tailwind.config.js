// tailwind.config.js
const plugin = require('tailwindcss/plugin');

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './app/**/*.{css,xml,html,vue,svelte,ts,tsx}'
  ],
  // use the .ns-dark class to control dark mode (applied by NativeScript) - since 'media' (default) is not supported.
  darkMode: ['class', '.ns-dark'],
  theme: {
    extend: {
      fontFamily: {
        base: ["Nunito", "Inter", "ui-sans-serif", "system-ui", "-apple-system", "BlinkMacSystemFont", "'Segoe UI'", "Roboto", "'Helvetica Neue'", "Arial", "'Noto Sans'", "sans-serif", "'Apple Color Emoji'", "'Segoe UI Emoji'", "'Segoe UI Symbol'", "'Noto Color Emoji'"],
        heading: ["Nunito", "Inter", "ui-sans-serif", "system-ui", "-apple-system", "BlinkMacSystemFont", "'Segoe UI'", "Roboto", "'Helvetica Neue'", "Arial", "'Noto Sans'", "sans-serif", "'Apple Color Emoji'", "'Segoe UI Emoji'", "'Segoe UI Symbol'", "'Noto Color Emoji'"]
      },
      colors: {
        primary: {
          50: "#f3e1fc", // 243 225 252
          100: "#efd8fb", // 239 216 251
          200: "#ebcefa", // 235 206 250
          300: "#dfb0f7", // 223 176 247
          400: "#c875f1", // 200 117 241
          500: "#b03aeb", // 176 58 235
          600: "#9e34d4", // 158 52 212
          700: "#842cb0", // 132 44 176
          800: "#6a238d", // 106 35 141
          900: "#561c73", // 86 28 115
        },
        secondary: {
          50: "#ebe2f0", // 235 226 240
          100: "#e5d8eb", // 229 216 235
          200: "#decee6", // 222 206 230
          300: "#cab1d7", // 202 177 215
          400: "#a376b9", // 163 118 185
          500: "#7b3b9b", // 123 59 155
          600: "#6f358c", // 111 53 140
          700: "#5c2c74", // 92 44 116
          800: "#4a235d", // 74 35 93
          900: "#3c1d4c", // 60 29 76
        },
        tertiary: {
          50: "#e4dfe7", // 228 223 231
          100: "#dcd4df", // 220 212 223
          200: "#d3cad7", // 211 202 215
          300: "#b8a9bf", // 184 169 191
          400: "#836990", // 131 105 144
          500: "#4e2960", // 78 41 96
          600: "#462556", // 70 37 86
          700: "#3b1f48", // 59 31 72
          800: "#2f193a", // 47 25 58
          900: "#26142f", // 38 20 47
        },
        success: {
          50: "#e2fbed", // 226 251 237
          100: "#d8f9e6", // 216 249 230
          200: "#cff8e0", // 207 248 224
          300: "#b1f3ce", // 177 243 206
          400: "#77eaa9", // 119 234 169
          500: "#3de184", // 61 225 132
          600: "#37cb77", // 55 203 119
          700: "#2ea963", // 46 169 99
          800: "#25874f", // 37 135 79
          900: "#1e6e41", // 30 110 65
        },
        warning: {
          50: "#fcf4da", // 252 244 218
          100: "#fbf0ce", // 251 240 206
          200: "#faecc1", // 250 236 193
          300: "#f7e19c", // 247 225 156
          400: "#f0ca52", // 240 202 82
          500: "#eab308", // 234 179 8
          600: "#d3a107", // 211 161 7
          700: "#b08606", // 176 134 6
          800: "#8c6b05", // 140 107 5
          900: "#735804", // 115 88 4
        },
        error: {
          50: "#f9e2e3", // 249 226 227
          100: "#f7d8da", // 247 216 218
          200: "#f5cfd1", // 245 207 209
          300: "#efb1b5", // 239 177 181
          400: "#e2777d", // 226 119 125
          500: "#d63d45", // 214 61 69
          600: "#c1373e", // 193 55 62
          700: "#a12e34", // 161 46 52
          800: "#802529", // 128 37 41
          900: "#691e22", // 105 30 34
        },
        surface: {
          50: "#dfdedf", // 223 222 223
          100: "#d4d4d5", // 212 212 213
          200: "#c9c9ca", // 201 201 202
          300: "#a9a8ab", // 169 168 171
          400: "#68676b", // 104 103 107
          500: "#27262c", // 39 38 44
          600: "#232228", // 35 34 40
          700: "#1d1d21", // 29 29 33
          800: "#17171a", // 23 23 26
          900: "#131316", // 19 19 22
        },
      },
      borderRadius: {
        base: "4px",
        container: "8px",
      },
      borderWidth: {
        base: "1px",
      },
      textColor: {
        base: "rgb(0, 0, 0)",
        dark: "rgb(255, 255, 255)",
      },
    },
  },
  plugins: [
    /**
     * A simple inline plugin that adds the ios: and android: variants
     * 
     * Example usage: 
     *
     *   <Label class="android:text-red-500 ios:text-blue-500" />
     *
     */
    plugin(function ({ addVariant }) {
      addVariant('android', '.ns-android &');
      addVariant('ios', '.ns-ios &');
    }),
  ],
  corePlugins: {
    preflight: false // disables browser-specific resets
  }
}