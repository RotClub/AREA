<script lang="ts">
	import ServiceCard from "$lib/components/Services/ServiceCard.svelte";
	import { Earth } from "lucide-svelte";
</script>

<div class="w-full h-full flex flex-col items-center">
	<div class="container w-full h-full flex flex-col items-center py-6 overflow-y-scroll gap-6">
		<ServiceCard linked>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 1</svelte:fragment>
		</ServiceCard>
		<ServiceCard>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 2</svelte:fragment>
		</ServiceCard>
		<ServiceCard>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 2</svelte:fragment>
		</ServiceCard>
		<ServiceCard>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 2</svelte:fragment>
		</ServiceCard>
		<ServiceCard>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 2</svelte:fragment>
		</ServiceCard>
		<ServiceCard>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 2</svelte:fragment>
		</ServiceCard>
		<ServiceCard>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 2</svelte:fragment>
		</ServiceCard>
		<ServiceCard>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 2</svelte:fragment>
		</ServiceCard>
		<ServiceCard>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 2</svelte:fragment>
		</ServiceCard>
		<ServiceCard>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 2</svelte:fragment>
		</ServiceCard>
		<ServiceCard>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 2</svelte:fragment>
		</ServiceCard>
		<ServiceCard>
			<svelte:fragment slot="icon"><Earth class="text-primary-500" /></svelte:fragment>
			<svelte:fragment slot="title">Service 2</svelte:fragment>
		</ServiceCard>
	</div>
</div>
