<script lang="ts" setup>
import { ref } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AppNavbar from '@/components/AppNavbar.vue'
import AppFooter from '@/components/AppFooter.vue'

const router = useRouter()
const auth = useAuthStore()

const email = ref('')
const password = ref('')
const loading = ref(false)

const handleLogin = async () => {
  if (!email.value || !password.value) {
    alert('Email and password are required')
    return
  }
  loading.value = true
  try {
    const payload = {
      email: email.value,
      password: password.value,
    }
    console.log('Sending login payload: ', payload)

    const res = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })

    console.log('Response status:', res.status, res.statusText)

    const data = await res.json().catch(err => {
      console.error('Failed to parse JSON:', err)
      return null
    })

    console.log('Login response data:', data)

    if (!res.ok) {
      const errorMessage = data?.message || data?.error || `HTTP ${res.status}: ${res.statusText}` || 'Login failed'
      alert(`Error: ${errorMessage}`)
      return
    }

    if (data.token && data.user) {
      // login successful keep token
      auth.setUser(data.user, data.token)
      router.push({ name: 'homeview' })
    } else {
      alert('Invalid response from server')
    }
  } catch (err: any) {
    console.error('Login error:', err)
    alert(`Something went wrong: ${err.message || 'Please check if the backend server is running on port 8081'}`)
  } finally {
    loading.value = false
  }
}

const handleCancel = () => {
  email.value = ''
  password.value = ''
}
</script>
<template>
  <AppNavbar />

  <div class="flex justify-center items-center min-h-screen bg-gray-100">
    <div class="flex flex-col gap-2 bg-white p-3 rounded shadow-md w-150">
      <h1 class="font-bold flex justify-center items-center">Login</h1>
      <fieldset class="fieldset flex flex-col">
        <legend class="fieldset-legend">Your Email</legend>
        <input
          v-model="email"
          type="text"
          class="input input-bordered w-full"
          placeholder="Type Your Email"
        />
      </fieldset>

      <fieldset class="fieldset flex flex-col">
        <legend class="fieldset-legend">Your Pass</legend>
        <input
          v-model="password"
          type="password"
          class="input input-bordered w-full"
          placeholder="Type Your Password"
        />
      </fieldset>

      <div class="flex justify-between mt-4 m-5">
        <button @click="handleLogin" class="btn btn-info w-55 mr-2">Login</button>
        <button @click="handleCancel" class="btn btn-accent w-55">Cancel</button>
      </div>
      <RouterLink :to="{ name: 'register' }" class="text-blue-500 hover:text-blue-700">
        If you have no account, you can register here!</RouterLink
      >
    </div>
  </div>

  <AppFooter />
</template>
