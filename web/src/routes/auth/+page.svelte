<script lang="ts">
	import { SlideToggle } from "@skeletonlabs/skeleton";
	import { onMount } from "svelte";
	import { page } from "$app/stores";

	let loaded: boolean = false;

	let authToggle: boolean = $page.url.searchParams.get("login") === "true";

	let email: string = "";

	onMount(() => {
		loaded = true;
	});
</script>

<div class="w-full h-full flex flex-col justify-center items-center">
	<form class="card w-full max-w-md flex flex-col items-center" method="post">
		<span
			class="w-full flex flex-row items-center justify-center text-3xl font-semibold mt-4 mb-2"
			>{authToggle ? "Login" : "Register"}</span>
		<div class="w-full h-2 flex flex-row items-center justify-center">
			<span class="w-[0%] border-b-2 splitter" class:loaded></span>
		</div>
		<div class="w-full h-full flex flex-col items-center">
			<div class="w-4/5 h-full py-2 flex flex-col gap-2">
				{#if !authToggle}
					<!-- register -->
					<div>
						<label for="username">Username</label>
						<input
							type="text"
							id="username"
							name="username"
							class="input"
							placeholder="gougougaga" />
					</div>
					<div>
						<label for="email">Email</label>
						<input
							type="email"
							id="email"
							name="email"
							class="input"
							placeholder="gougougaga@example.com"
							bind:value={email} />
					</div>
					<div class="flex flex-row gap-2">
						<div class="w-1/2">
							<label for="password">Password</label>
							<input type="password" id="password" name="password" class="input" />
						</div>
						<div class="w-1/2">
							<label for="confirmPassword">Confirm Password</label>
							<input
								type="password"
								id="confirmPassword"
								name="confirmPassword"
								class="input" />
						</div>
					</div>
				{:else}
					<div>
						<label for="email">Email</label>
						<input
							type="email"
							id="email"
							name="email"
							class="input"
							placeholder="gougougaga@example.com"
							bind:value={email} />
					</div>
					<div>
						<label for="password">Password</label>
						<input type="password" id="password" name="password" class="input" />
					</div>
				{/if}
			</div>
		</div>
		<div class="w-full h-2 flex flex-row items-center justify-center">
			<span class="w-[0%] border-b-2 splitter" class:loaded></span>
		</div>
		<div class="w-4/5 flex flex-row justify-between items-center mb-4 mt-2">
			<div class="flex flex-row items-center gap-2">
				<span class="font-thin">Register</span>
				<SlideToggle
					name="login"
					bind:checked={authToggle}
					active="bg-primary-500"
					background="bg-primary-500" />
				<span class="font-thin">Login</span>
			</div>
			<button class="btn variant-filled-primary">
				<span> Confirm </span>
			</button>
		</div>
	</form>
</div>

<style>
	.splitter.loaded {
		width: 80% !important;
		transition: width 0.75s;
	}
</style>
