<script lang="ts">
	import { onMount } from "svelte";

    let userData: {
        username: string;
        email: string;
    } = {
        username: "",
        email: "",
    };

    let password: string = "";
    let confirmPassword: string = "";
    let loading = true;

    onMount(async () => {
        userData = await (await window.fetch("/api/user")).json()
        console.log(userData);
        loading = false;
    });

    async function updateProfile() {
        if (password !== confirmPassword) {
            alert("Password and Confirm Password must be the same");
            return;
        }

        loading = true;

        const response = await window.fetch("/api/user", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                "username": userData.username,
                "email": userData.email,
                "password": password,
            }),
        });

        if (response.ok) {
            alert("Profile updated successfully");
        } else {
            alert("Failed to update profile");
        }

        loading = false;
    }
</script>

<div class="w-full h-full flex justify-center items-center">
    <div class="card max-w-md w-full p-4 flex flex-col">
        <span class="text-3xl font-semibold">Profile</span>
        <hr class="mb-2" />
        <div class="flex flex-col gap-1">
            <div class="w-full">
                <label for="username">Username</label>
                <input type="text" name="username" class="input" bind:value={userData.username} disabled={loading}>
            </div>
            <div class="w-full">
                <label for="email">Email</label>
                <input type="email" name="email" class="input" bind:value={userData.email} disabled={loading}>
            </div>
            <div class="w-full">
                <label for="password">Password</label>
                <input type="password" name="password" class="input" bind:value={password} disabled={loading}>
            </div>
            <div class="w-full">
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" name="confirmPassword" class="input" bind:value={confirmPassword} disabled={loading}>
            </div>
        </div>
        <div class="w-full flex justify-end">
            <button class="btn variant-filled-primary w-fit mt-2" on:click={updateProfile} disabled={loading}>
                Update
            </button>
        </div>
    </div>
</div>
