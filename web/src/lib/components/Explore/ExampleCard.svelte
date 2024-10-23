<script lang="ts">
    import { File } from "lucide-svelte";
</script>

<div class="flex flex-col shrink-0 w-fit h-fit hover:bg-primary-800 p-2 rounded-token cursor-pointer">
    <div class="w-[calc(320px/1.5)] h-[9rem] card flex justify-center items-center">
        <File />
    </div>
    <div class="flex flex-row justify-between items-center w-full">
        <span class="font-medium text-lg">Example</span>
        <span class="text-secondary-400">3 Nodes</span>
    </div>
</div>
