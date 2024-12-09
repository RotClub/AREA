<script lang="ts">
	import "../app.postcss";

	// Highlight JS
	import hljs from "highlight.js/lib/core";
	import "highlight.js/styles/github-dark.css";
	import {
		initializeStores,
		storeHighlightJs,
		Modal,
		Drawer,
		getDrawerStore
	} from "@skeletonlabs/skeleton";
	import xml from "highlight.js/lib/languages/xml"; // for HTML
	import css from "highlight.js/lib/languages/css";
	import javascript from "highlight.js/lib/languages/javascript";
	import typescript from "highlight.js/lib/languages/typescript";
	import { onNavigate } from "$app/navigation";

	import type { ModalComponent } from "@skeletonlabs/skeleton";

	hljs.registerLanguage("xml", xml); // for HTML
	hljs.registerLanguage("css", css);
	hljs.registerLanguage("javascript", javascript);
	hljs.registerLanguage("typescript", typescript);
	storeHighlightJs.set(hljs);

	initializeStores();

	const drawerStore = getDrawerStore();

	// Floating UI for Popups
	import { computePosition, autoUpdate, flip, shift, offset, arrow } from "@floating-ui/dom";
	import { storePopup } from "@skeletonlabs/skeleton";
	import AddActionModalComponent from "$lib/components/Modals/AddActionModalComponent.svelte";
	import AddReactionModalComponent from "$lib/components/Modals/AddReactionModalComponent.svelte";
	import EditNodeModalComponent from "$lib/components/Modals/EditNodeModalComponent.svelte";
	storePopup.set({ computePosition, autoUpdate, flip, shift, offset, arrow });

	onNavigate((navigation) => {
		if (!document.startViewTransition) return;

		return new Promise<void>((resolve) => {
			document.startViewTransition(async () => {
				resolve();
				await navigation.complete;
			});
		});
	});

	const modalRegistry: Record<string, ModalComponent> = {
		addActionModalComponent: { ref: AddActionModalComponent },
		addReactionModalComponent: { ref: AddReactionModalComponent },
		editNodeModalComponent: { ref: EditNodeModalComponent }
	};
</script>

<Drawer>
	{#if $drawerStore.id === "navmenu"}
		<div class="w-full h-full flex flex-col justify-center items-center gap-8">
			<a href="/dashboard" class="text-3xl font-semibold">Home</a>
			<a href="/dashboard/workspace" class="text-3xl font-semibold">Workspace</a>
			<a href="/dashboard/services" class="text-3xl font-semibold">Services</a>
			<a href="/dashboard/profile" class="text-3xl font-semibold">Profile</a>
		</div>
	{/if}
</Drawer>
<Modal components={modalRegistry} />
<slot />
