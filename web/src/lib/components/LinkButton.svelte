<script lang="ts">
	import { page } from "$app/stores";

	export let href: string = "";
</script>

{#if ($page.url.pathname === "/dashboard" && href === "/dashboard") || (href !== "/dashboard" && $page.url.pathname.includes(href))}
	<div class="h-full flex items-center">
		<a {href} class="text-primary-500 h-full flex flex-col items-center justify-center">
			<span>
				<slot />
			</span>
			<span class="h-1 w-full bottom-4 bg-primary-500 vt-name-[active] rounded-sm"></span>
		</a>
	</div>
{:else}
	<div class="h-full flex items-center">
		<a {href} class="h-full flex flex-col items-center justify-center">
			<span>
				<slot />
			</span>
		</a>
	</div>
{/if}
