<script>
    import { onMount } from 'svelte';

    let isSteamConnected = false;

    onMount(() => {
        const urlParams = new URLSearchParams(window.location.search);
        isSteamConnected = urlParams.get('connected') === 'true';
    });

    function loginWithSteam() {
        window.location.href = `https://steamcommunity.com/openid/login?openid.ns=http://specs.openid.net/auth/2.0&openid.mode=checkid_setup&openid.return_to=http://localhost:8081/auth/steam/callback&openid.realm=http://localhost:8081&openid.identity=http://specs.openid.net/auth/2.0/identifier_select&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select`;
    }
</script>

<h1>Connexion Steam</h1>

{#if isSteamConnected}
    <p><input type="checkbox" checked disabled/> Connexion réussie </p> 
{:else}
    <p>Pas encore connecté à Steam.</p>
{/if}
<button on:click={loginWithSteam}>| Connectez-vous à Steam |</button>