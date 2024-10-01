<script lang="ts">
	import { MessageSquareMore, Globe, CircleUserRound, Menu, CircleHelp } from "lucide-svelte";
	import LinkButton from "$lib/components/LinkButton.svelte";
	import {
		getDrawerStore,
		getModalStore,
		type DrawerSettings,
		type ModalSettings,
		Avatar
	} from "@skeletonlabs/skeleton";
	import { onMount } from "svelte";
	import type { PageData } from "./$types";
	import { page } from "$app/stores";

	let userPictureBlobUrl: string | undefined = undefined;

	const drawerStore = getDrawerStore();
	const navSettings: DrawerSettings = {
		id: "navmenu",
		rounded: "rounded-xl",
		padding: "p-4",
		width: "w-[280px] md:w-[480px]",
		position: "right"
	};
</script>

<div class="h-screen">
	<header
		class="bg-surface-600 w-full sticky top-0 h-16 flex flex-row items-center justify-between px-4 z-10">
		<a
			href="/"
			class="text-xl font-bold text-white flex flex-row space-x-2 justify-center items-center">
			<img src="/icon.svg" alt="icon" class="w-8 h-8" />
			<span> Frispy </span>
		</a>
		<div class="hidden text-secondary-400 font-semibold h-full space-x-8 md:flex flex-row">
			<LinkButton href="/dashboard">Home</LinkButton>
			<LinkButton href="/dashboard/workspace">Workspace</LinkButton>
			<LinkButton href="/dashboard/explore">Explore</LinkButton>
		</div>
		<div class="hidden md:flex flex-row items-center h-full space-x-8 aspect-1">
			<a href="/dashboard/profile" class="flex justify-center items-center w-full h-full p-2">
				<Avatar
					round={true}
					userFullName="yo"
					src=""
					width="w-full h-full"
					border="border-2 border-surface-400">
					<CircleUserRound class="text-surface-400" />
				</Avatar>
			</a>
		</div>
		<div class="flex absolute right-0 mr-4 md:hidden flex-row items-center h-full space-x-8">
			<button
				class="btn-icon variant-filled-primary"
				on:click={() => {
					drawerStore.open(navSettings);
				}}><Menu /></button>
		</div>
	</header>
	<main class="overflow-auto h-[calc(100%-4rem)]"><slot /></main>
</div>
