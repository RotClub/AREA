<script lang="ts">
	export let name = "Clara Barton";
	export let colors = ["#92A1C6", "#2f037e", "#7f3df0", "#e619c3", "#C20D90"];
	export let title = false;
	export let square = false;
	export let size: string | null = null;

	const SIZE = 36;

	import { getBoolean, getContrast, getRandomColor, getUnit, hashCode } from "./utils";

	function generateData(name: string, colors: string[]) {
		const numFromName = hashCode(name);
		const range = colors && colors.length;
		const wrapperColor = getRandomColor(numFromName, colors, range);
		const preTranslateX = getUnit(numFromName, 10, 1);
		const wrapperTranslateX = preTranslateX < 5 ? preTranslateX + SIZE / 9 : preTranslateX;
		const preTranslateY = getUnit(numFromName, 10, 2);
		const wrapperTranslateY = preTranslateY < 5 ? preTranslateY + SIZE / 9 : preTranslateY;

		return {
			wrapperColor: wrapperColor,
			faceColor: getContrast(wrapperColor),
			backgroundColor: getRandomColor(numFromName + 13, colors, range),
			wrapperTranslateX: wrapperTranslateX,
			wrapperTranslateY: wrapperTranslateY,
			wrapperRotate: getUnit(numFromName, 360),
			wrapperScale: 1 + getUnit(numFromName, SIZE / 12) / 10,
			isMouthOpen: getBoolean(numFromName, 2),
			isCircle: getBoolean(numFromName, 1),
			eyeSpread: getUnit(numFromName, 5),
			mouthSpread: getUnit(numFromName, 3),
			faceRotate: getUnit(numFromName, 10, 3),
			faceTranslateX:
				wrapperTranslateX > SIZE / 6 ? wrapperTranslateX / 2 : getUnit(numFromName, 8, 1),
			faceTranslateY:
				wrapperTranslateY > SIZE / 6 ? wrapperTranslateY / 2 : getUnit(numFromName, 7, 2)
		};
	}

	const data = generateData(name, colors);
	let maskID = "mask-" + Math.random().toString(36).substr(2, 9); // Unique ID for mask
</script>

<svg
	viewBox="0 0 {SIZE} {SIZE}"
	fill="none"
	role="img"
	xmlns="http://www.w3.org/2000/svg"
	width={size}
	height={size}>
	{#if title}
		<title>{name}</title>
	{/if}
	<mask id={maskID} maskUnits="userSpaceOnUse" x="0" y="0" width={SIZE} height={SIZE}>
		<rect width={SIZE} height={SIZE} rx={square ? undefined : SIZE * 2} fill="#FFFFFF" />
	</mask>
	<g mask={`url(#${maskID})`}>
		<rect width={SIZE} height={SIZE} fill={data.backgroundColor} />
		<rect
			x="0"
			y="0"
			width={SIZE}
			height={SIZE}
			transform={`translate(${data.wrapperTranslateX} ${data.wrapperTranslateY}) rotate(${data.wrapperRotate} ${SIZE / 2} ${SIZE / 2}) scale(${data.wrapperScale})`}
			fill={data.wrapperColor}
			rx={data.isCircle ? SIZE : SIZE / 6} />
		<g
			transform={`translate(${data.faceTranslateX} ${data.faceTranslateY}) rotate(${data.faceRotate} ${SIZE / 2} ${SIZE / 2})`}>
			{#if data.isMouthOpen}
				<path
					d="M15 {19 + data.mouthSpread}c2 1 4 1 6 0"
					stroke={data.faceColor}
					fill="none"
					stroke-linecap="round" />
			{:else}
				<path d="M13,{19 + data.mouthSpread} a1,0.75 0 0,0 10,0" fill={data.faceColor} />
			{/if}
			<rect
				x={14 - data.eyeSpread}
				y="14"
				width="1.5"
				height="2"
				rx="1"
				fill={data.faceColor} />
			<rect
				x={20 + data.eyeSpread}
				y="14"
				width="1.5"
				height="2"
				rx="1"
				fill={data.faceColor} />
		</g>
	</g>
</svg>
