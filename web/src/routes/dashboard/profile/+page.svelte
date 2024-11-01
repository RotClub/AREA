<script lang="ts">
	import { onMount } from "svelte";
	import { parse as cookieParser } from "cookie";
	import { apiRequest } from "$lib";
	import { goto } from "$app/navigation";

	let userData: {
		username: string;
		email: string;
	} = {
		username: "",
		email: ""
	};

	let password: string = "";
	let confirmPassword: string = "";
	let loading = true;

	onMount(async () => {
		userData = await (await apiRequest("GET", "/api/user")).json();
		loading = false;
	});

	async function updateProfile() {
		if (password !== confirmPassword) {
			alert("Password and Confirm Password must be the same");
			return;
		}

		loading = true;
		const response = await apiRequest("POST", "/api/user", {
			username: userData.username,
			email: userData.email,
			password: password
		});

		if (response.ok) {
			alert("Profile updated successfully");
		} else {
			alert("Failed to update profile");
		}

		loading = false;
	}

	async function signOut() {
		goto("/dashboard/profile/logout");
	}
</script>

<div class="w-full h-full flex flex-col justify-center items-center gap-2">
	<div class="card max-w-md w-full p-4 flex flex-col">
		<span class="text-3xl font-semibold">Profile</span>
		<hr class="mb-2" />
		<div class="flex flex-col gap-1">
			<div class="w-full">
				<label for="username">Username</label>
				<input
					type="text"
					name="username"
					class="input"
					bind:value={userData.username}
					disabled={loading} />
			</div>
			<div class="w-full">
				<label for="email">Email</label>
				<input
					type="email"
					name="email"
					class="input"
					bind:value={userData.email}
					disabled={loading} />
			</div>
			<div class="w-full">
				<label for="password">Password</label>
				<input
					type="password"
					name="password"
					class="input"
					bind:value={password}
					disabled={loading} />
			</div>
			<div class="w-full">
				<label for="confirmPassword">Confirm Password</label>
				<input
					type="password"
					name="confirmPassword"
					class="input"
					bind:value={confirmPassword}
					disabled={loading} />
			</div>
		</div>
		<div class="w-full flex justify-end">
			<button
				class="btn variant-filled-primary mt-2 w-24"
				on:click={updateProfile}
				disabled={loading}>
				Update
			</button>
		</div>
	</div>
	<div class="card max-w-md w-full p-4 flex flex-row justify-between items-center">
		<span class="text-lg font-semibold">Disconnect from your account</span>
		<button class="btn variant-filled-error w-24" on:click={signOut}>Sign out</button>
	</div>
	<div class="card max-w-md w-full p-4 flex flex-row justify-between items-center">
		<span class="text-lg font-semibold">Delete your account</span>
		<button class="btn variant-filled-error w-24" disabled>Delete</button>
	</div>
</div>
