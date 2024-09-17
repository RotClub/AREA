<script lang="ts">
	import { page } from '$app/stores';

	export let href: string = '';
</script>

{#if ($page.url.pathname === '/dashboard' && href === '/dashboard') || (href !== '/dashboard' && $page.url.pathname.includes(href))}
	<div class="relative h-full flex items-center">
		<a {href} class="text-primary-500 h-full flex items-center justify-center">
			<slot />
		</a>
		<span class="absolute h-1 w-full bottom-0 left-0 bg-primary-500 active rounded-t-sm"></span>
	</div>
{:else}
	<div class="relative h-full flex items-center">
		<a {href} class="h-full flex items-center justify-center"><slot /></a>
	</div>
{/if}

<style>
	.active {
		view-transition-name: active-page;
	}
</style>
