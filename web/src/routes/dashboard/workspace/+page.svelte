<script lang="ts">
	import Node from "$lib/components/Workspace/Node.svelte";
	import WorkspaceProgramCard from "$lib/components/Workspace/ProgramCard.svelte";
</script>

<div class="w-full h-full flex flex-row">
	<div
		class="h-full w-2/3 bg-surface-700 overflow-y-scroll p-4 space-y-4 border-r-2 border-surface-500">
		<WorkspaceProgramCard title="test" nodes={3} />
	</div>
	<div class="h-full w-full overflow-y-scroll p-8 px-16">
		<Node action="action" />
	</div>
	<div class="h-full w-2/3 bg-surface-700 border-l-2 border-surface-500"></div>
</div>
