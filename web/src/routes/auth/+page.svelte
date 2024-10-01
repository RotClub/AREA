<script lang="ts">
	import { EvervaultCard } from "$lib/components/Aceternity/EvervaultCard";
	import { onMount } from "svelte";

	let loaded: boolean = false;

	onMount(() => {
		loaded = true;
	});
</script>

<div class="w-full h-3/4 flex flex-col justify-center items-center">
	<div class="relative container mx-auto max-w-md h-[30rem] space-y-4">
		<div class="flex flex-row justify-center items-center gap-6">
			<img src="/icon.svg" alt="icon" class="w-24 h-24" />
			<span class="font-semibold text-5xl">Frispy</span>
		</div>
		<div class="card h-[30rem] max-w-md flex flex-col">
			<span class="w-full flex flex-row items-center justify-center text-xl mt-4"
				>Authentication</span>
			<div class="w-full h-2 flex flex-row items-center justify-center">
				<span class="w-[0%] border-b-2 splitter" class:loaded></span>
			</div>
		</div>
	</div>
</div>

<style>
	.splitter.loaded {
		width: 80% !important;
		transition: width 0.75s;
	}
</style>
