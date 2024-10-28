<script lang="ts">
	import ExampleCard from "$lib/components/Explore/ExampleCard.svelte";
</script>

<div class="w-full h-full overflow-hidden">
	<div class="container mx-auto mt-12 h-full overflow-x-hidden flex flex-col gap-4">
		<div class="w-full flex flex-row items-center gap-4">
			<span class="font-semibold text-3xl">Examples</span>
			<div class="w-full h-0.5 bg-white/30 rounded-token"></div>
		</div>
		<div class="w-full h-fit flex flex-row gap-2 flex-wrap content-start">
			<ExampleCard />
			<ExampleCard />
			<ExampleCard />
			<ExampleCard />
			<ExampleCard />
			<ExampleCard />
			<ExampleCard />
		</div>
	</div>
</div>
