<script lang="ts">
	import { Cog, Trash2, Play } from "lucide-svelte";

	export let reaction: string;
	export let meta: { [key: string]: string } = {};
</script>

<div class="w-full flex flex-col border-t-[1px] border-surface-600">
	<div class="flex flex-row items-center px-2 pt-0">
		<span class="text-xl font-semibold text-secondary-300">Do: </span>
		<span class="ml-2">{reaction}</span>
		<div class="flex flex-row flex-grow justify-end">
			<button class="btn-icon text-surface-200">
				<Cog />
			</button>
			<button class="btn-icon text-error-500">
				<Trash2 />
			</button>
			<button class="btn-icon text-success-500">
				<Play fill="#3de184" />
			</button>
		</div>
	</div>
	<div class="flex flex-row overflow-x-scroll">
		{#each Object.entries(meta) as [key, value]}
			<div class="flex flex-col items-center justify-center px-4 py-2">
				<span class="text-secondary-300 text-xl shrink-0 text-nowrap">{key}</span>
				<span class=" shrink-0 text-nowrap">{value}</span>
			</div>
		{/each}
	</div>
</div>
