<script lang="ts">
	import { BackgroundBeams } from '$lib/components/Aceternity/BackgroundBeams';
</script>

<div class="relative flex h-full w-full flex-col items-center justify-center rounded-md bg-surface-900 px-32 antialiased">
	<div class="flex flex-col w-full h-full space-y-8 p-8 z-10">
		<div class="flex flex-row space-x-8 h-[30rem]">
			<div class="card w-[60%]"></div>
			<div class="card w-[40%]"></div>
		</div>
		<div class="flex flex-row space-x-8 h-[30rem]">
			<div class="card w-[40%]"></div>
			<div class="card w-[60%]"></div>
		</div>
	</div>
	<BackgroundBeams />
</div>
