<script lang="ts">
	import { Cog, Trash2, Play } from "lucide-svelte";
	import { parse as cookieParser } from "cookie";
	import { getModalStore, type ModalSettings, type ModalStore } from "@skeletonlabs/skeleton";
	import { getRequiredMetadataFromId, getDisplayNameFromId, getIconPathFromId } from "$lib/services";

	let modalStore: ModalStore = getModalStore();

	export let reaction: string;
	export let meta: { [key: string]: string } = {};
	export let edit: boolean;
	export let loaded: boolean;
	export let programId: number;
	export let reactionId: number;
	export let programs: Array<{
		name: string;
		id: number;
		nodeAmount: number;
	}>;

	async function deleteNode() {
		loaded = false;
		await window.fetch(`/api/programs/${programId}/node`, {
			method: "DELETE",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${cookieParser(document.cookie)["token"]}`
			},
			body: JSON.stringify({
				isReaction: true,
				id: reactionId
			})
		});
		programs = await (await window.fetch("/api/programs")).json();
		loaded = true;
	}

	async function editNodeMetaData() {
		new Promise<Record<string, string>>((resolve) => {
			const modal: ModalSettings = {
				type: "component",
				component: "editNodeModalComponent",
				meta: {
					EDITING: true,
					schema: getRequiredMetadataFromId(reaction),
					data: meta
				},
				response: (r: Record<string, string>) => {
					resolve(r);
				}
			};

			modalStore.trigger(modal);
		}).then(async (r) => {
			if (r) {
				loaded = false;
				const res = await window.fetch(`/api/programs/${programId}/node`, {
					method: "PATCH",
					headers: {
						"Content-Type": "application/json",
						Authorization: `Bearer ${cookieParser(document.cookie)["token"]}`
					},
					body: JSON.stringify({
						isReaction: true,
						id: reactionId,
						metadata: r
					})
				});
				if (res.ok) meta = r;
				loaded = true;
			}
		});
	}
</script>

<div class="w-full flex flex-col border-t-[1px] border-surface-600">
	<div class="flex flex-row items-center px-2 pt-0">
		<span class="text-xl font-semibold text-secondary-300 mr-3">Do: </span>
		<img src={getIconPathFromId(reaction)} alt={reaction} width="24px" />
		<span class="ml-2">{getDisplayNameFromId(reaction)}</span>
		<div class="flex flex-row flex-grow justify-end">
			{#if edit}
				<button class="btn-icon text-surface-200" on:click={editNodeMetaData}>
					<Cog />
				</button>
				<button class="btn-icon text-error-500" on:click={deleteNode}>
					<Trash2 />
				</button>
			{/if}
			<button class="btn-icon text-success-500">
				<Play fill="#3de184" />
			</button>
		</div>
	</div>
	<div class="flex flex-row overflow-x-scroll">
		{#each Object.entries(meta) as [key, value]}
			<div class="flex flex-col items-center justify-center px-4 py-2">
				<span class="text-secondary-300 text-xl shrink-0 text-nowrap">{getRequiredMetadataFromId(reaction)[key].displayName}</span>
				<span class=" shrink-0 text-nowrap">{value}</span>
			</div>
		{/each}
	</div>
</div>
