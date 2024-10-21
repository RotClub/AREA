<script lang="ts">
	import { goto } from "$app/navigation";

	export let link_state: boolean = false;
	export let link_href: string = "#";
	export let unlink_href: string = "#";
</script>

<div class="card h-[5rem] w-[30rem] flex flex-row gap-2 shrink-0 p-4 items-center">
	<div class="w-8">
		<slot name="icon" />
	</div>
	<span class="font-semibold text-2xl">
		<slot name="title" />
	</span>
	<div class="flex-grow flex flex-row justify-end">
		{#if link_state}
			<button
				on:click={() => {
					goto(unlink_href);
				}}
				class="btn variant-ghost-primary uppercase tracking-wider">Linked</button>
		{:else}
			<button
				on:click={() => {
					goto(link_href);
				}}
				class="btn variant-filled-primary uppercase tracking-wider">Unlinked</button>
		{/if}
	</div>
</div>
