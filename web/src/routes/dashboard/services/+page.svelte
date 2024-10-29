<script lang="ts">
	import ServiceCard from "$lib/components/Services/ServiceCard.svelte";
	import { onMount } from "svelte";
	import ServiceCardPlaceholder from "$lib/components/Services/ServiceCardPlaceholder.svelte";
	import { apiRequest } from "$lib";

	let data: Array<{
		service: string;
		link: boolean;
		title: string;
		link_href: string;
		unlink_href: string;
	}> = [];
	onMount(async () => {
		const res = await apiRequest("GET", "/api/services");
		data = await res.json();
	});
</script>

<div class="w-full h-full flex flex-col items-center">
	<div class="container w-full h-full flex flex-col items-center p-6 overflow-y-scroll gap-6">
		{#if data.length === 0}
			<ServiceCardPlaceholder />
			<ServiceCardPlaceholder />
			<ServiceCardPlaceholder />
			<ServiceCardPlaceholder />
			<ServiceCardPlaceholder />
			<ServiceCardPlaceholder />
			<ServiceCardPlaceholder />
		{:else}
			{#each data as service}
				<ServiceCard
					link_state={service.link}
					link_href={service.link_href}
					unlink_href={service.unlink_href}>
					<svelte:fragment slot="icon"
						><img
							src="/provider/{service.service.toLowerCase()}-icon.svg"
							alt={service.service} /></svelte:fragment>
					<svelte:fragment slot="title">{service.title}</svelte:fragment>
				</ServiceCard>
			{/each}
		{/if}
	</div>
</div>
