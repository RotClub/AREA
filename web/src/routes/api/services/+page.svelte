<script>
    import { goto } from '$app/navigation';
  
    const login = async () => {
      await goto('/auth/steam');
    };
  </script>
  
  <button on:click={login}>
    Connectez-vous avec Steam
  </button>
  