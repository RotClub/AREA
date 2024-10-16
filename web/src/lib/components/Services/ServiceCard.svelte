<script lang="ts">
	import { Provider } from "@prisma/client";
	import queryString from "query-string";
	import { onMount } from "svelte";
	import { parse as cookie_parser } from "cookie";
	import { goto } from "$app/navigation";

	export let linked: boolean = false;
	export let provider: Provider = Provider.STEAM;
	export let href: string = "#";

	onMount(() => {
		const cookies = cookie_parser(document.cookie);

		href = redirectToService(cookies);
	});

	function redirectToService(cookies): string {
		if (provider === Provider.SPOTIFY) {
			if (!linked) {
				return `/api/auth/spotify`;
			} else {
				return "/api/unlink/spotify?" + queryString.stringify({ token: cookies.token });
			}
		} else if (provider === Provider.BATTLENET) {
			if (!linked) {
				return `/api/auth/battlenet`;
			} else {
				return "/api/unlink/battlenet?" + queryString.stringify({ token: cookies.token });
			}
		}
		return "#";
	}
</script>

<div class="card h-[5rem] w-[30rem] flex flex-row gap-2 shrink-0 p-4 items-center">
	<div class="w-8">
		<slot name="icon" />
	</div>
	<span class="font-semibold text-2xl">
		<slot name="title" />
	</span>
	<div class="flex-grow flex flex-row justify-end">
		{#if linked}
			<button
				on:click={() => {
					goto(href);
				}}
				class="btn variant-ghost-primary uppercase tracking-wider">Linked</button>
		{:else}
			<button
				on:click={() => {
					goto(href);
				}}
				class="btn variant-filled-primary uppercase tracking-wider">Unlinked</button>
		{/if}
	</div>
</div>
