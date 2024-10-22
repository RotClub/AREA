<script lang="ts">
	import { getDisplayNameFromId, getIconPathFromId } from "$lib/services";
	import { Cog, Play, Plus, Trash2 } from "lucide-svelte";
	import { type ModalSettings, type ModalStore, getModalStore } from "@skeletonlabs/skeleton";
	import { parse as cookieParser } from "cookie";

	let modalStore: ModalStore = getModalStore();

	export let action: string;
	export let meta: { [key: string]: string } = {};
	export let edit: boolean;
	export let programId: number;
	export let actionId: number;
	export let programs: Array<{
		name: string;
		id: number;
		nodeAmount: number;
	}>;
	export let loaded: boolean;

	async function openAddReactionModal() {
		new Promise<boolean>((resolve) => {
			const modal: ModalSettings = {
				type: "component",
				component: "addReactionModalComponent",
				response: (r: boolean) => {
					resolve(r);
				}
			};

			modalStore.trigger(modal);
		}).then(async (r) => {
			if (r) {
				loaded = false;
				await window.fetch(`/api/programs/${programId}/node`, {
					method: "PUT",
					headers: {
						"Content-Type": "application/json",
						Authorization: `Bearer ${cookieParser(document.cookie)["token"]}`
					},
					body: JSON.stringify({
						isReaction: true,
						id: r,
						metadata: {}
					})
				});
				programs = await (await window.fetch("/api/programs")).json();
				console.log(programs);
				loaded = true;
			}
		});
	}

	async function deleteNode() {
		loaded = false;
		await window.fetch(`/api/programs/${programId}/node`, {
			method: "DELETE",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${cookieParser(document.cookie)["token"]}`
			},
			body: JSON.stringify({
				isAction: true,
				id: actionId
			})
		});
		programs = await (await window.fetch("/api/programs")).json();
		loaded = true;
	}
</script>

<div class="card w-full flex-shrink-0 flex flex-col overflow-hidden">
	<div class="flex flex-col justify-between h-full p-2">
		<div class="flex flex-row items-center px-2">
			<span class="text-xl font-semibold text-secondary-300 mr-3">On: </span>
			<img src={getIconPathFromId(action)} alt={action} width="24px" />
			<span class="ml-2">{getDisplayNameFromId(action)}</span>
			<div class="flex flex-row flex-grow justify-end">
				<button class="btn-icon text-surface-200">
					<Cog />
				</button>
				<button class="btn-icon text-error-500" on:click={deleteNode}>
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
			on:click={openAddReactionModal}
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
