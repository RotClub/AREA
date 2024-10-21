<script lang="ts">
	import { FilePenLine, Code } from "lucide-svelte";

	export let title: string;
	export let nodes: number;

	let editing: boolean = false;
	let input: HTMLInputElement;

	function init(node: HTMLInputElement) {
		input = node;
		node.focus();
	}

	function toggleEditing(toggle: boolean) {
		if (!toggle && title.length === 0) {
			title = "Untitled";
		}
		editing = toggle;
	}
</script>

<div class="card border-2 border-secondary-400 w-full h-[6rem] flex flex-row overflow-hidden">
	<div class="w-4/5 p-4 flex flex-col space-y-3">
		<div class="flex flex-row h-min items-center space-x-2">
			{#if !editing}
				<span class="text-2xl font-semibold text-secondary-300 overflow-x-scroll"
					>{title}</span>
				<button
					class="btn p-0"
					on:click={() => {
						toggleEditing(true);
					}}>
					<FilePenLine />
				</button>
			{:else}
				<input
					type="text"
					class="input"
					bind:value={title}
					on:focusout={() => {
						toggleEditing(false);
						editing ? input.focus() : 0;
					}}
					on:keypress={(e) => {
						e.key === "Enter" ? toggleEditing(false) : 0;
					}}
					on:submit={() => {
						toggleEditing(false);
					}}
					use:init />
			{/if}
		</div>
		<span class="font-regular text-sm text-surface-200"
			>Nodes: <span class="text-white">{nodes}</span></span>
	</div>
	<div class="flex-grow border-l-2 border-secondary-400 flex flex-col">
		<div class="h-full w-full bg-surface-500 hover:brightness-125 transition-all">
			<button class="btn h-full w-full"><Code /></button>
		</div>
	</div>
</div>
