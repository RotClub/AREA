<script lang="ts">
	import { Cog, Play, Plus, Trash2 } from "lucide-svelte";

	export let action: string;
	export let meta: { [key: string]: string } = {};
	export let edit: boolean;
</script>

<div class="card w-full flex-shrink-0 flex flex-col overflow-hidden">
	<div class="flex flex-col justify-between h-full p-2">
		<div class="flex flex-row items-center px-2">
			<span class="text-xl font-semibold text-secondary-300">On: </span>
			<span class="ml-2">{action}</span>
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
				<div class="flex flex-col items-center justify-center px-4 py-2 shrink-0">
					<span class="text-secondary-300 text-xl shrink-0 text-nowrap">{key}</span>
					<span class="shrink-0 text-nowrap">{value}</span>
				</div>
			{/each}
		</div>
		<div class="w-full flex flex-col alternate">
			<slot />
		</div>
	</div>
	{#if edit}
		<button
			class="bg-primary-500 w-full h-6 flex flex-row justify-center items-center hover:bg-primary-700 transition-colors">
			<span>
				<Plus />
			</span>
		</button>
	{/if}
</div>

<style>
	.alternate:nth-child(even) {
		background-color: rgb(var(--color-surface-500));
		border-left: 1px solid rgb(var(--color-surface-600));
		border-right: 1px solid rgb(var(--color-surface-600));
	}
</style>
