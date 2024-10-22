<script lang="ts">
	import { onMount, type SvelteComponent } from "svelte";

	import { getModalStore, TreeView, TreeViewItem } from "@skeletonlabs/skeleton";
	import { goto } from "$app/navigation";
	import { getIconPathFromId, getDisplayNameFromId } from "$lib/services";

	export let parent: SvelteComponent;
	parent = parent || null;

	const modalStore = getModalStore();

	let loaded: boolean = false;
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	let services: any[] = [];
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	let selected: string | null = null;

	function onFormSubmit(): void {
		if ($modalStore[0].response) $modalStore[0].response(selected);
		modalStore.close();
	}

	function gotoServices(e: MouseEvent) {
		e.preventDefault();
		modalStore.close();
		goto("/dashboard/services");
	}

	function selectReaction(service: string, id: string) {
		selected = `${service}:${id}`;
	}

	onMount(async () => {
		services = await (await window.fetch("/api/reaction")).json();
		console.log(services);
		loaded = true;
	});
</script>

{#if $modalStore[0]}
	<div class="card p-4 w-modal shadow-xl space-y-4">
		<header class="text-2xl font-bold">Reaction</header>
		<article>
			Select here a reaction, they represent events to be triggered, can be configured later.
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
					{#each services as service}
						<TreeViewItem>
							<svelte:fragment slot="lead">
								<img
									src={service.iconPath}
									alt={service.service.toLowerCase()}
									width="32px" />
							</svelte:fragment>
							<span>{service.displayName}</span>
							<svelte:fragment slot="children">
								{#each service.reactions as reaction}
									<TreeViewItem
										on:click={() => {
											selectReaction(service.service, reaction.id);
										}}>{reaction.displayName}</TreeViewItem>
								{/each}
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
			{#if selected}
				<img src={getIconPathFromId(selected)} alt={selected} width="16px" />
				<span>{getDisplayNameFromId(selected)}</span>
			{:else}
				<span>None</span>
			{/if}
		</div>
		<footer class="flex flex-row items-center justify-end gap-2">
			<button
				class="btn variant-filled-surface"
				on:click={() => {
					modalStore.close();
				}}>Cancel</button>
			<button class="btn variant-filled-primary" on:click={onFormSubmit}>Confirm</button>
		</footer>
	</div>
{/if}
