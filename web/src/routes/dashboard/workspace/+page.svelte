<script lang="ts">
	import Node from "$lib/components/Workspace/Node.svelte";
	import WorkspaceProgramCard from "$lib/components/Workspace/ProgramCard.svelte";
	import SubNode from "$lib/components/Workspace/SubNode.svelte";
	import { SlideToggle } from "@skeletonlabs/skeleton";
	import { X, Info, Plus } from "lucide-svelte";
	import DotBackground from "$lib/components/Aceternity/DotBackground.svelte";

	let editing: boolean = false;
</script>

<div class="w-full h-full flex flex-row">
	<div
		class="h-full w-1/4 bg-surface-700 overflow-y-scroll p-4 space-y-4 border-r-2 border-surface-500">
		<WorkspaceProgramCard title="test" nodes={3} />
	</div>
	<div class="flex flex-col h-full w-3/4">
		<div
			class="flex flex-row items-center justify-between h-12 w-full bg-surface-700 px-2 border-b-2 border-surface-500">
			<div class="flex flex-row gap-2 text-secondary-300 items-center">
				<Info />
				<span class="text-secondary-300">Nodes: 3</span>
			</div>
			<div class="flex flex-row justify-center items-center gap-2">
				<span class="font-semibold text-xl">Editing</span>
				<SlideToggle
					name="editing"
					bind:checked={editing}
					active="bg-primary-500"
					background="bg-surface-800" />
			</div>
			<button class="btn text-error-500 font-semibold !p-2">
				<span> Delete program </span>
				<span>
					<X />
				</span>
			</button>
		</div>
		<div class="flex flex-col overflow-y-scroll p-8 px-16 gap-8 dark:bg-dot-white/[0.1]">
			<Node
				action="[Brawlhalla] Rank reached"
				meta={{ Player: "Kapsulon", Rank: "Diamond" }}
				edit={editing}>
				<SubNode
					reaction="[Spotify] Play track"
					meta={{ Device: "KapPhone", Track: "Celebration song", Volume: "200%" }} />
				<SubNode
					reaction="[Spotify] Play track"
					meta={{ Device: "KapPhone", Track: "Celebration song", Volume: "200%" }} />
				<SubNode
					reaction="[Spotify] Play track"
					meta={{ Device: "KapPhone", Track: "Celebration song", Volume: "200%" }} />
			</Node>
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			<Node action="Lorem Ipsum A Dolor Sit Amet" edit={editing} />
			{#if editing}
				<div class="w-full flex flex-row justify-center items-center">
					<button class="btn-icon variant-filled-primary"><Plus /></button>
				</div>
			{/if}
		</div>
	</div>
</div>
