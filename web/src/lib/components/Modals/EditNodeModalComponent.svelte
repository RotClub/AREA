<script lang="ts">
	import { onMount, type SvelteComponent } from "svelte";

	import { getModalStore } from "@skeletonlabs/skeleton";
	import { getInputTypeFromMeta, type ActionMetaDataType } from "$lib/services";

	export let parent: SvelteComponent;
	parent = parent || null;

	let settings: Record<string, string> = {};
	let mounted: boolean = false;

	const modalStore = getModalStore();

	async function onFormSubmit(): Promise<void> {
		if ($modalStore[0].response) $modalStore[0].response(settings);
		modalStore.close();
	}

	onMount(async () => {
		console.log($modalStore[0]);
		if ($modalStore[0].meta["EDITING"]) {
			for (const [key, data] of Object.entries($modalStore[0].meta["data"]) as any) {
				settings[key] = data;
			}
		}
		mounted = true;
	});
</script>

{#if $modalStore[0]}
	<div class="card p-4 w-modal shadow-xl space-y-4">
		<header class="text-2xl font-bold">Configuration</header>
		<article>Edit here the configuration of the node.</article>
		<div
			class="relative border border-surface-500 bg-surface-900 p-4 space-y-4 rounded-container-token overflow-y-scroll gap-2 h-[32rem]">
			{#if mounted}
				{#if $modalStore[0].meta["EDITING"]}
					{#each Object.entries($modalStore[0].meta["schema"]) as [key, fieldSettings]}
						<div class="flex flex-col">
							<label for={fieldSettings.id}>{fieldSettings.displayName}</label>
							{#if getInputTypeFromMeta(fieldSettings) === "text"}
								<input
									type="text"
									class="input"
									name={fieldSettings.id}
									bind:value={settings[key]} />
							{:else if getInputTypeFromMeta(fieldSettings) === "number"}
								<input
									type="number"
									class="input"
									name={fieldSettings.id}
									bind:value={settings[key]} />
							{:else if getInputTypeFromMeta(fieldSettings) === "checkbox"}
								<input
									type="checkbox"
									class="input"
									name={fieldSettings.id}
									bind:checked={settings[key]} />
							{:else if getInputTypeFromMeta(fieldSettings) === "date"}
								<input
									type="date"
									class="input"
									name={fieldSettings.id}
									bind:value={settings[key]} />
							{/if}
						</div>
					{/each}
				{:else}
					{#each Object.entries($modalStore[0].meta) as [key, fieldSettings]}
						<div class="flex flex-col">
							<label for={fieldSettings.id}>{fieldSettings.displayName}</label>
							{#if getInputTypeFromMeta(fieldSettings) === "text"}
								<input
									type="text"
									class="input"
									name={fieldSettings.id}
									bind:value={settings[key]} />
							{:else if getInputTypeFromMeta(fieldSettings) === "number"}
								<input
									type="number"
									class="input"
									name={fieldSettings.id}
									bind:value={settings[key]} />
							{:else if getInputTypeFromMeta(fieldSettings) === "checkbox"}
								<input
									type="checkbox"
									class="input"
									name={fieldSettings.id}
									bind:checked={settings[key]} />
							{:else if getInputTypeFromMeta(fieldSettings) === "date"}
								<input
									type="date"
									class="input"
									name={fieldSettings.id}
									bind:value={settings[key]} />
							{/if}
						</div>
					{/each}
				{/if}
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
