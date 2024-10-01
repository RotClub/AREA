<script lang="ts">
	import Node from "$lib/components/Workspace/Node.svelte";
	import WorkspaceProgramCard from "$lib/components/Workspace/ProgramCard.svelte";
	import { X, Info } from "lucide-svelte";
</script>

<div class="w-full h-full flex flex-row">
	<div
		class="h-full w-2/3 bg-surface-700 overflow-y-scroll p-4 space-y-4 border-r-2 border-surface-500">
		<WorkspaceProgramCard title="test" nodes={3} />
	</div>
	<div class="flex flex-col h-full w-full">
		<div class="flex flex-row items-center justify-between h-12 w-full bg-surface-700 px-2 border-b-2 border-surface-500">
			<div class="flex flex-row gap-2 text-secondary-300">
				<Info />
				<span class="text-secondary-300">Nodes: 3</span>
			</div>
			<button class="btn text-error-500 font-semibold !p-2">
				<span>
					Delete program
				</span>
				<span>
					<X />
				</span>
			</button>
		</div>
		<div class="flex flex-col overflow-y-scroll p-8 px-16 gap-8">
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
			<Node action="Lorem Ipsum A Dolor Sit Amet" />
		</div>
	</div>
	<div class="h-full w-2/3 bg-surface-700 border-l-2 border-surface-500"></div>
</div>
