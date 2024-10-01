<script lang="ts">
    export let linked: boolean = false;
</script>

<div class="card h-[5rem] w-[30rem] flex flex-row shrink-0 p-4 items-center">
    <div class="w-8">
        <slot name="icon" />
    </div>
    <span class="font-semibold text-2xl">
        <slot name="title" />
    </span>
    <div class="flex-grow flex flex-row justify-end">
        {#if linked}
            <button class="btn variant-ghost-primary uppercase tracking-wider">Linked</button>
        {:else}
            <button class="btn variant-filled-primary uppercase tracking-wider">Unlinked</button>
        {/if}
    </div>
</div>
