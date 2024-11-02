<script lang="ts">
	import Node from "$lib/components/Workspace/Node.svelte";
	import WorkspaceProgramCard from "$lib/components/Workspace/ProgramCard.svelte";
	import SubNode from "$lib/components/Workspace/SubNode.svelte";
	import { SlideToggle, getModalStore } from "@skeletonlabs/skeleton";
	import { X, Plus, ArrowLeft, Trash2 } from "lucide-svelte";
	import type { ModalSettings, ModalStore } from "@skeletonlabs/skeleton";
	import { onMount } from "svelte";
	import { getRequiredMetadataFromId } from "$lib/services";
	import { parse as cookieParser } from "cookie";
	import { apiRequest } from "$lib";

	let modalStore: ModalStore = getModalStore();

	let editing: boolean = false;
	let loaded: boolean = false;

	let inspecting_node: number = -1;

	let programs: Array<{
		name: string;
		id: number;
		nodeAmount: number;
	}> = [];

	async function openAddActionModal() {
		const newActionId: string = await new Promise<string>((resolve) => {
			const modal: ModalSettings = {
				type: "component",
				component: "addActionModalComponent",
				response: (r: string) => {
					resolve(r);
				}
			};

			modalStore.trigger(modal);
		});
		if (!newActionId) return;
		if (Object.keys(getRequiredMetadataFromId(newActionId)).length === 0) {
			loaded = false;
			await apiRequest("PUT", `/api/programs/${inspecting_node}/node`, {
				isAction: true,
				id: newActionId,
				metadata: {}
			});
			programs = await (await apiRequest("GET", "/api/programs")).json();
			loaded = true;
			return;
		}
		const newActionMeta: Record<string, string> = await new Promise<Record<string, string>>(
			(resolve) => {
				const modal: ModalSettings = {
					type: "component",
					component: "editNodeModalComponent",
					meta: getRequiredMetadataFromId(newActionId),
					response: (r: Record<string, string>) => {
						resolve(r);
					}
				};

				modalStore.trigger(modal);
			}
		);
		if (!newActionMeta) return;
		loaded = false;
		await apiRequest("PUT", `/api/programs/${inspecting_node}/node`, {
			isAction: true,
			id: newActionId,
			metadata: newActionMeta
		});
		programs = await (await apiRequest("GET", "/api/programs")).json();
		loaded = true;
	}

	async function createProgram() {
		loaded = false;
		const response = await apiRequest("POST", "/api/programs", {
			name: "New program"
		});

		if (response.ok) {
			programs = [...programs, await response.json()];
		}
		loaded = true;
	}

	async function deleteProgram() {
		if (inspecting_node === -1) return;
		editing = false;
		loaded = false;
		const response = await apiRequest("DELETE", `/api/programs/${inspecting_node}`);

		if (response.ok) {
			programs = programs.filter((program) => program.id !== inspecting_node);
		}
		inspecting_node = -1;
		loaded = true;
	}

	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	function getProgramContent(id: number): any {
		return programs.find((program) => program.id === id);
	}

	onMount(async () => {
		programs = await (await apiRequest("GET", "/api/programs")).json();
		loaded = true;
	});
</script>

<div class="w-full h-full lg:hidden bg-surface-900">
	{#if inspecting_node !== -1}
		<div class="flex flex-row items-center justify-between p-4">
			<button
				class="!w-fit !h-fit text-primary-500 flex flex-row"
				on:click={() => {
					inspecting_node = -1;
				}}>
				<ArrowLeft />
				<span class="font-medium">Go back</span>
			</button>
			<div class="flex flex-row gap-2">
				<span class="text-2xl font-semibold">Editing</span>
				<SlideToggle
					name="editing"
					bind:checked={editing}
					disabled={!loaded || inspecting_node === -1}
					active="bg-primary-500"
					background="bg-surface-800" />
			</div>
			<button
				class="btn variant-filled-error font-semibold !p-2"
				on:click={deleteProgram}
				disabled={!loaded || inspecting_node === -1}>
				<span>
					<Trash2 />
				</span>
			</button>
		</div>
		<div class="flex flex-col overflow-y-scroll p-8 gap-8 h-full">
			{#if programs && getProgramContent(inspecting_node).actions}
				{#each getProgramContent(inspecting_node).actions as action}
					<Node
						action={action.actionId}
						meta={action.metadata}
						actionId={action.id}
						bind:edit={editing}
						programId={inspecting_node}
						bind:programs
						bind:loaded>
						{#each action.reactions as reaction}
							<SubNode
								reaction={reaction.reactionId}
								meta={reaction.metadata}
								bind:edit={editing}
								bind:programs
								bind:loaded
								programId={inspecting_node}
								reactionId={reaction.id} />
						{/each}
					</Node>
				{/each}
			{/if}
			{#if editing}
				<div class="w-full flex flex-row justify-center items-center">
					<button class="btn variant-filled-primary" on:click={openAddActionModal}
						><Plus /> Add action</button>
				</div>
			{/if}
		</div>
	{:else}
		<div class="flex flex-row items-center justify-between p-4">
			<span class="text-2xl font-semibold">Workspace</span>
			<button class="btn variant-filled-primary" disabled={!loaded} on:click={createProgram}
				><Plus /> Create program</button>
		</div>
		<div class="flex flex-col gap-4 p-4">
			{#each programs as program}
				<WorkspaceProgramCard
					title={program.name}
					nodes={program.nodeAmount}
					bind:group={inspecting_node}
					id={program.id} />
			{/each}
		</div>
	{/if}
</div>
<div class="w-full h-full lg:flex flex-row hidden">
	<div
		class="h-full w-1/4 bg-surface-700 overflow-y-scroll p-4 space-y-4 border-r-2 border-surface-500 flex flex-col items-center">
		{#each programs as program}
			<WorkspaceProgramCard
				title={program.name}
				nodes={program.nodeAmount}
				bind:group={inspecting_node}
				id={program.id} />
		{/each}
		<button class="btn variant-filled-primary" disabled={!loaded} on:click={createProgram}
			><Plus /> Create program</button>
	</div>
	<div class="relative flex flex-col h-full w-3/4">
		{#if !loaded}
			<div class="absolute inset-0 bg-surface-800 opacity-50 w-full h-full"></div>
		{/if}
		<div
			class="flex flex-row items-center justify-between h-12 w-full bg-surface-700 px-2 border-b-2 border-surface-500">
			<div class="flex flex-row justify-center items-center gap-2">
				<span class="font-semibold text-xl">Editing</span>
				<SlideToggle
					name="editing"
					bind:checked={editing}
					disabled={!loaded || inspecting_node === -1}
					active="bg-primary-500"
					background="bg-surface-800" />
			</div>
			<button
				class="btn text-error-500 font-semibold !p-2"
				on:click={deleteProgram}
				disabled={!loaded || inspecting_node === -1}>
				<span> Delete program </span>
				<span>
					<X />
				</span>
			</button>
		</div>
		<div class="flex flex-col overflow-y-scroll p-8 px-16 gap-8 dark:bg-dot-white/[0.1] h-full">
			{#if inspecting_node !== -1}
				{#if programs && getProgramContent(inspecting_node).actions}
					{#each getProgramContent(inspecting_node).actions as action}
						<Node
							action={action.actionId}
							meta={action.metadata}
							actionId={action.id}
							bind:edit={editing}
							programId={inspecting_node}
							bind:programs
							bind:loaded>
							{#each action.reactions as reaction}
								<SubNode
									reaction={reaction.reactionId}
									meta={reaction.metadata}
									bind:edit={editing}
									bind:programs
									bind:loaded
									programId={inspecting_node}
									reactionId={reaction.id} />
							{/each}
						</Node>
					{/each}
				{/if}
				{#if editing}
					<div class="w-full flex flex-row justify-center items-center">
						<button class="btn variant-filled-primary" on:click={openAddActionModal}
							><Plus /> Add action</button>
					</div>
				{/if}
			{/if}
		</div>
	</div>
</div>
