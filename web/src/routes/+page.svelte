<script lang="ts">
	import { LogIn } from "lucide-svelte";
	import { parse as cookie_parser } from "cookie";
	import GradientAnimation from "$lib/components/Aceternity/GradientAnimation.svelte";
	import { goto } from "$app/navigation";

	const manageWelcome = async () => {
		const cookies = cookie_parser(document.cookie);
		if (cookies.token) {
			await goto("/dashboard");
		} else {
			await goto("/auth");
		}
	};
</script>

<div class="h-screen">
	<header
		class="bg-surface-600 w-full sticky top-0 h-16 flex flex-row justify-between items-center z-20 px-4">
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
	<GradientAnimation>
		<div
			class="absolute inset-0 z-10 flex-col content-center items-center justify-center px-8 text-center font-bold text-white">
			<p
				class="bg-gradient-to-bl from-purple-600 to-white/80 bg-clip-text text-transparent drop-shadow-2xl text-3xl md:text-4xl lg:text-7xl">
				Connect, Play, React
				<br />
				<br />
				Automate Your Digital Universe.
			</p>
			<br />
			<br />
			<br />
			<button
				on:click={manageWelcome}
				class="relative mx-auto mt-4 rounded-full border border-emerald-500/20 bg-emerald-300/10 px-4 py-2 text-center text-white backdrop-blur-sm text-2xl md:text-3xl lg:text-4xl">
				<span>Join now →</span>
				<div
					class="absolute inset-x-0 -bottom-px mx-auto h-px w-3/4 bg-gradient-to-r from-transparent via-emerald-500 to-transparent" />
			</button>
		</div>
	</GradientAnimation>
</div>
