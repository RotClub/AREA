<script lang="ts">
	import { apiRequest } from "$lib";

	export let link_state: boolean = false;
	export let link_href: string = "#";
	export let unlink_href: string = "#";
</script>

<div class="card h-[5rem] w-full md:w-[30rem] flex flex-row gap-2 shrink-0 p-4 items-center">
	<div class="w-8">
		<slot name="icon" />
	</div>
	<span class="font-semibold text-2xl">
		<slot name="title" />
	</span>
	<div class="flex-grow flex flex-row justify-end">
		{#if link_state}
			<button
				on:click={async () => {
					const res = await apiRequest("GET", unlink_href);
					try {
						const data = await res.json();
						if (res.status === 200) {
							window.location.href = data.url;
						} else {
							console.log(res.status, data.error);
						}
					} catch (e) {
						console.log(e);
					}
				}}
				class="btn variant-ghost-primary uppercase tracking-wider">Linked</button>
		{:else}
			<button
				on:click={async () => {
					const res = await apiRequest("GET", link_href);
					try {
						const data = await res.json();
						if (res.status === 200) {
							window.location.href = data.url;
						} else {
							console.log(res.status, data.error);
						}
					} catch (e) {
						console.log(e);
					}
				}}
				class="btn variant-filled-primary uppercase tracking-wider">Unlinked</button>
		{/if}
	</div>
</div>
