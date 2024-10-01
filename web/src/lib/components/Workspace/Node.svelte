<script lang="ts">
	import { Cog, Play, Plus, Trash2 } from "lucide-svelte";

	export let action: string;
</script>

<div class="card w-full h-[10rem] flex-shrink-0 flex flex-col overflow-hidden">
	<div class="flex flex-col justify-between h-full">
		<div class="flex flex-row items-center p-4">
			<span class="text-xl font-semibold text-secondary-300">On: </span>
			<span class="ml-2">{action}</span>
			<div class="flex flex-row flex-grow justify-end">
				<button class="btn-icon text-surface-200">
					<Cog />
				</button>
				<button class="btn-icon text-error-500">
					<Trash2 />
				</button>
				<button class="btn-icon text-success-500">
					<Play fill="#3de184" />
				</button>
			</div>
		</div>
		<button class="bg-primary-500 w-full h-6 flex flex-row justify-center items-center hover:bg-primary-700 transition-colors">
			<span>
				<Plus />
			</span>
		</button>
	</div>
</div>
