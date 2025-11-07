<script lang="ts" setup>
import { onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';

const auth = useAuthStore()

onMounted(async () => {
  if (auth.token) {
    await auth.fetchProfile()
  }
})
</script>

<template>

  <div class="hero min-h-screen"
    style="background-image: url(https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhahBCETE0kiZ2MIZOQMQi3KWREP2YPpmqCbM2yo5hyphenhyphensrd5I4Y31O5I11UYUgdL_XKvsxGgeOx1DNEqxfDV5y3_z_pYFVKi4eDAYMj8jgOn82KzPQswE6uXPqVEQdcqjH8IzaufFpVfL2CU/s1600/BCEL.jpg);">
    <div class="hero-overlay"></div>
    <div class="hero-content text-neutral-content text-center">
      <div class="max-w-md">
        <h1 class="mb-5 text-5xl font-bold">HRM branch</h1>
        <p class="mb-5 indent-8">
          HRM branch
        </p>
      </div>
    </div>
  </div>

</template>
