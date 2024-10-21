<script lang="ts">
	import ServiceCard from "$lib/components/Services/ServiceCard.svelte";
	import { Earth } from "lucide-svelte";
	import { onMount } from "svelte";
	import type { Provider } from "@prisma/client";

	//@ts-expect-error - This is a hack to prevent the error
	let data: [{service: Provider, link: boolean, title: string}] = [];
	onMount(async () => {
		const res = await window.fetch("/api/services");
		data = await res.json();
	});
</script>

<div class="w-full h-full flex flex-col items-center">
	<div class="container w-full h-full flex flex-col items-center py-6 overflow-y-scroll gap-6">
		{#each data as service}
			<ServiceCard provider={service.service} linked={service.link}>
				<svelte:fragment slot="icon"
					><img src="/provider/{service.service.toLowerCase()}-icon.svg" alt="{service.service}" /></svelte:fragment>
				<svelte:fragment slot="title">{service.title}</svelte:fragment>
			</ServiceCard>
		{/each}
	</div>
</div>
