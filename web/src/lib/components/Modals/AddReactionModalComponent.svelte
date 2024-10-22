<script lang="ts">
	import { onMount, type SvelteComponent } from "svelte";

	import { getModalStore, TreeView, TreeViewItem } from "@skeletonlabs/skeleton";
	import { goto } from "$app/navigation";

	export let parent: SvelteComponent;
	parent = parent || null;

	const modalStore = getModalStore();

	let loaded: boolean = false;
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	let reactions: any[] = [];
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	let selected: any = null;

	function onFormSubmit(): void {
		// if ($modalStore[0].response) $modalStore[0].response(formData);
		modalStore.close();
	}

	function gotoServices(e: MouseEvent) {
		e.preventDefault();
		modalStore.close();
		goto("/dashboard/services");
	}

	onMount(async () => {
		reactions = await (await window.fetch("/api/reaction")).json();
		console.log(reactions);
		loaded = true;
	});
</script>

{#if $modalStore[0]}
	<div class="card p-4 w-modal shadow-xl space-y-4">
		<header class="text-2xl font-bold">Action</header>
		<article>
			Select here a reaction to be used to trigger events, can be configured later.
		</article>
		<article>
			If you don't see anything here, that means you have no <a
				href="/dashboard/services"
				class="text-primary-500"
				on:click={gotoServices}>linked services</a
			>.
		</article>
		<div
			class="relative border border-surface-500 bg-surface-900 p-4 space-y-4 rounded-container-token overflow-y-scroll h-[32rem]">
			{#if loaded}
				<TreeView>
					{#each reactions as reaction}
						<TreeViewItem>
							<svelte:fragment slot="lead">
								<img src="/provider/spotify-icon.svg" alt="spotify" width="32px" />
							</svelte:fragment>
							<span>Spotify</span>
							<svelte:fragment slot="children">
								<TreeViewItem>(Child of Child 1)</TreeViewItem>
								<TreeViewItem>(Child of Child 2)</TreeViewItem>
							</svelte:fragment>
						</TreeViewItem>
					{/each}
				</TreeView>
			{:else}
				<div class="absolute w-full h-full bg-surface-700 animate-pulse top-0 left-0"></div>
			{/if}
		</div>
		<div class="flex flex-row gap-2 items-center">
			<span class="text-lg font-semibold">Currently selected:</span>
			<span>{selected ? selected : "None"}</span>
		</div>
		<footer class="flex flex-row items-center justify-end gap-2">
			<button
				class="btn variant-filled-surface"
				on:click={() => {
					modalStore.close();
				}}>Cancel</button>
			<button class="btn variant-filled-primary">Confirm</button>
		</footer>
	</div>
{/if}
