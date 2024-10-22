<script lang="ts">
	import Node from "$lib/components/Workspace/Node.svelte";
	import WorkspaceProgramCard from "$lib/components/Workspace/ProgramCard.svelte";
	import SubNode from "$lib/components/Workspace/SubNode.svelte";
	import { SlideToggle, getModalStore } from "@skeletonlabs/skeleton";
	import { X, Info, Plus } from "lucide-svelte";
	import type { ModalSettings, ModalStore } from "@skeletonlabs/skeleton";
	import { onMount } from "svelte";
	import { parse as cookieParser } from "cookie";
	import { Actions } from "$lib/services";

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
		new Promise<boolean>((resolve) => {
			const modal: ModalSettings = {
				type: "component",
				component: "addActionModalComponent",
				response: (r: boolean) => {
					resolve(r);
				}
			};

			modalStore.trigger(modal);
		}).then(async (r) => {
			if (r) {
				loaded = false;
				await window.fetch(`/api/programs/${inspecting_node}/node`, {
					method: "PUT",
					headers: {
						"Content-Type": "application/json",
						Authorization: `Bearer ${cookieParser(document.cookie)["token"]}`
					},
					body: JSON.stringify({
						isAction: true,
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

	async function createProgram() {
		loaded = false;
		const response = await window.fetch("/api/programs", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				name: "New program"
			})
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
		const response = await window.fetch(`/api/programs/${inspecting_node}`, {
			method: "DELETE"
		});

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
		programs = await (await window.fetch("/api/programs")).json();
		console.log(programs);
		loaded = true;
	});
</script>

<div class="w-full h-full flex flex-row">
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
			<div class="flex flex-row gap-2 text-secondary-300 items-center">
				<Info />
				<span class="text-secondary-300"
					>Nodes: 0<!--{inspecting_node != -1 ? programs[inspecting_node].nodeAmount : 0}--></span>
			</div>
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
						<Node action={action.actionId} meta={action.metadata} actionId={action.id} bind:edit={editing} programId={inspecting_node} bind:programs={programs} bind:loaded={loaded}>
							{#each action.reactions as reaction}
								<SubNode reaction={reaction.reactionId} meta={reaction.metadata} />
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
