<script lang="ts" setup>
import { ref, watch, onMounted } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import MainDashboard from '@/components/MainDashboard.vue'

const auth = useAuthStore()
const router = useRouter()

onMounted(async () => {
  if (!auth.user && auth.token) {
    await auth.fetchProfile()
  }
})

watch(() => auth.user?.photo_url, () => {
  cacheBuster.value = Date.now()
})

function logout() {
  auth.logout()
  router.push({ name: 'login' })
}
</script>

<template>

  <div class="flex h-screen">
    <!-- Sidebar -->
    <aside class="w-64 bg-gray-600 text-white p-4">

      <div class="text-center">
        <router-link to="/dashboard" class="text-xl font-bold mb-6 ">Dashboard</router-link>
        <div class="mt-5">
          <div>
            <div class="avatar">
              <div class="ring-primary ring-offset-base-100 w-24 rounded-full ring-2 ring-offset-2">
                <img :src="auth.user?.photo_url
                  ? `http://localhost:8080${auth.user.photo_url}?t=${cacheBuster}`
                  : '/default-avatar.jpg'" alt="User photo" class="object-cover w-full h-full" />
              </div>
            </div>
          </div>
          <div class="mt-5">
            <h1 class="text-xl font-bold mb-1 ">Your Email</h1>
            <p>{{ auth.user?.email }}</p>
          </div>
        </div>
        <div class="divider divider-info"></div>
      </div>

      <nav>
        <ul>
          <li class="mb-5">
            <router-link to="/dashboard" class="hover:underline">Dashboard</router-link>
          </li>
          <li class="mb-5">
            <router-link to="/account" class="hover:underline">Account</router-link>
          </li>
          <li class="mb-5">
            <router-link to="/employee" class="hover:underline">Employee</router-link>
          </li>
          <li class="mb-5">
            <router-link to="/transaction" class="hover:underline">transaction</router-link>
          </li>
          <li class="mb-5">
            <router-link to="/profile" class="hover:underline">Profile</router-link>
          </li>
          <li class="mb-5">
            <button @click="logout" class="w-full text-left hover:underline">Logout</button>
          </li>
        </ul>
      </nav>
    </aside>

    <!-- Main content -->
    <main class="flex-1 bg-gray-100 p-6 overflow-auto">
      <MainDashboard />
    </main>

  </div>
</template>
