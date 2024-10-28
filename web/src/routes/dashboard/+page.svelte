<script lang="ts">
	import { BackgroundBeams } from "$lib/components/Aceternity/BackgroundBeams";
	import { ProgressRadial } from "@skeletonlabs/skeleton";
	import { Check, X } from "lucide-svelte";
	import { onMount } from "svelte";
	import { BoringAvatar } from "$lib/components/BoringAvatar";
	import { parse as cookieParser } from "cookie";
	import { apiRequest } from "$lib";

	export let data;

	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	let connectedServices: Array<any> = [];
	let connectAmount: number | undefined = undefined;
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	let userData: any = {};

	onMount(async () => {
		connectedServices = await (await apiRequest("GET", "/api/services")).json();
		connectAmount =
			connectedServices.length -
			// eslint-disable-next-line @typescript-eslint/no-explicit-any
			connectedServices.filter((service: any) => !service.link).length;

		userData = await (await apiRequest("GET", "/api/user")).json();
	});

	function getProgress(amount: number | undefined) {
		if (!amount) return undefined;
		return (amount / connectedServices.length) * 100;
	}
</script>

<div class="w-full h-full items-center justify-center">
	<div
		class="relative flex h-full flex-col items-center justify-center container mx-auto bg-surface-900 antialiased">
		<div class="flex flex-col w-full h-full space-y-8 p-8 z-10">
			<span class="text-3xl font-semibold"
				>Hello <span class="text-primary-500">{userData.username || "..."}</span>, welcome
				to your dashboard.</span>
			<div class="flex flex-row space-x-8 h-[30rem] !mt-4">
				<div class="card w-[60%] p-4 flex flex-col">
					<span class="text-2xl font-semibold">Linked services:</span>
					<div class="w-full h-full flex flex-row">
						<div
							class="w-1/2 flex flex-col justify-center items-center text-lg font-medium overflow-y-auto">
							{#each connectedServices as service}
								<div class="flex flex-row items-center justify-between w-4/5">
									<img
										src="/provider/{service.service.toLowerCase()}-icon.svg"
										alt={service.title}
										class="w-4 h-4" />
									<span
										class:text-success-500={service.link}
										class:text-error-500={!service.link}>{service.title}</span>
									{#if service.link}
										<Check class="w-8 h-8 text-success-500" />
									{:else}
										<X class="w-8 h-8 text-error-500" />
									{/if}
								</div>
							{/each}
						</div>
						<div class="w-1/2 flex justify-center items-center">
							<ProgressRadial
								value={getProgress(connectAmount)}
								width="w-[70%]"
								meter="stroke-primary-500"
								>{connectAmount}/{connectedServices.length}</ProgressRadial>
						</div>
					</div>
				</div>
				<div class="card w-[40%] p-4 flex flex-col">
					<span class="text-2xl font-semibold mb-6">Programs statistics:</span>
					<div class="flex flex-row justify-evenly">
						<div class="flex flex-col justify-center items-center">
							<span class="font-semibold text-xl">Programs</span>
							<span class="text-primary-500 text-lg">2</span>
						</div>
						<div class="flex flex-col justify-center items-center">
							<span class="font-semibold text-xl">Nodes avg.</span>
							<span class="text-primary-500 text-lg">5.7</span>
						</div>
						<div class="flex flex-col justify-center items-center">
							<span class="font-semibold text-xl">Empty programs</span>
							<span class="text-primary-500 text-lg">0</span>
						</div>
					</div>
				</div>
			</div>
			<div class="flex flex-row space-x-8 h-[30rem]">
				<div class="card w-[40%] p-4 flex flex-row justify-between">
					<div class="flex flex-col gap-2">
						<span class="text-2xl font-semibold mb-2">Profile:</span>
						<div class="flex flex-row items-end gap-2">
							<span class="font-semibold text-xl">Username:</span>
							<span class="text-primary-500 text-lg">{userData.username || "Loading..."}</span>
						</div>
						<div class="flex flex-row items-end gap-2">
							<span class="font-semibold text-xl">Email:</span>
							<span class="text-primary-500 text-lg">
								<a href="mailto:{userData.email}">{userData.email || "Loading..."}</a>
							</span>
						</div>
						<div class="flex flex-row items-end gap-2">
							<span class="font-semibold text-xl">Role:</span>
							<span class="text-primary-500 text-lg">{userData.role || "Loading..."}</span>
						</div>
						<div class="flex flex-row items-end gap-2">
							<span class="font-semibold text-xl">Created at:</span>
							<span class="text-primary-500 text-lg">{userData.createdAt || "Loading..."}</span>
						</div>
					</div>
					<div class="w-20 h-20">
						<BoringAvatar name={data.props.avatar_seed} />
					</div>
				</div>
				<div class="card w-[60%] p-4"></div>
			</div>
		</div>
	</div>
	<BackgroundBeams />
</div>
