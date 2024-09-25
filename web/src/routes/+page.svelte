<script lang="ts">
	import { LogIn } from "lucide-svelte";
</script>

<div class="h-screen">
	<header
		class="bg-surface-600 w-full sticky top-0 h-16 flex flex-row justify-between items-center z-10 px-4">
		<a
			href="/"
			class="text-xl font-bold text-white flex flex-row space-x-2 justify-center items-center">
			<img src="/icon.svg" alt="icon" class="w-8 h-8" />
			<span> Frispy </span>
		</a>
		<a href="/auth" class="btn variant-filled-primary">
			<span><LogIn /></span>
			<span>Sign In</span>
		</a>
	</header>
</div>
