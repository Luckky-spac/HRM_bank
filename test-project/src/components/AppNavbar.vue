<script lang="ts" setup>
import { computed, ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const auth = useAuthStore()
const router = useRouter()


const handleLogout = () => {
  auth.logout()
  router.push({ name: 'homeview' })
}

const isLoggedIn = computed(() => !!auth.token && !!auth.user)

const menus = ref([
  {
    title: 'Dashboard',
    path: '/dashboard',
  },
  {
    title: 'Profile',
    path: '/profile',
  },
  {
    title: 'Account',
    path: '/account',
  },
  {
    title: 'About',
    path: '/about',
  },
])
</script>

<template>

  <nav class="navbar bg-base-100 shadow-sm">
    <div class="navbar-start">
      <router-link :to="{ name: 'homeview' }" class="btn btn-ghost text-xl">HRM</router-link>
      <div class="dropdown lg:hidden">
        <button tabindex="0" class="btn btn-ghost">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h8m-8 6h16" />
          </svg>
        </button>
        <ul tabindex="0" class="menu menu-sm dropdown-content bg-base-100 rounded-box z-10 mt-3 w-52 p-2 shadow">
          <li v-for="menu in menus" :key="menu.path">
            <router-link :to="menu.path">{{ menu.title }}</router-link>
          </li>
        </ul>
      </div>
    </div>
    <div class="navbar-center hidden lg:flex">
      <ul class="menu menu-horizontal px-1">
        <li v-for="menu in menus" :key="menu.path">
          <router-link :to="menu.path">{{ menu.title }}</router-link>
        </li>
      </ul>
    </div>

    <div class="navbar-end">

      <RouterLink v-if="!isLoggedIn" :to="{ name: 'login' }" class="w-10 rounded-full">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
          <path
            d="M320 312C386.3 312 440 258.3 440 192C440 125.7 386.3 72 320 72C253.7 72 200 125.7 200 192C200 258.3 253.7 312 320 312zM290.3 368C191.8 368 112 447.8 112 546.3C112 562.7 125.3 576 141.7 576L498.3 576C514.7 576 528 562.7 528 546.3C528 447.8 448.2 368 349.7 368L290.3 368z" />
        </svg>
      </RouterLink>
      <div v-else class="flex items-center gap-3">
        <div class="flex items-center gap-2">
          <div class="w-8 h-8 rounded-full flex items-center justify-center bg-gray-200">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" class="w-5 h-5 fill-current text-gray-600">
              <path
                d="M224 256A128 128 0 1 0 224 0a128 128 0 1 0 0 256zm-45.7 48C79.8 304 0 383.8 0 482.3C0 498.7 13.3 512 29.7 512H418.3c16.4 0 29.7-13.3 29.7-29.7C448 383.8 368.2 304 269.7 304H178.3z" />
            </svg>
          </div>
          <span v-if="auth.user" class="text-sm font-medium text-gray-700">{{ auth.user?.username || 'User' }}</span>
        </div>
        <button @click="handleLogout" class="btn btn-sm btn-ghost">Logout</button>
      </div>

    </div>
  </nav>

</template>

<style scoped></style>
