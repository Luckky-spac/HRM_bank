<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AppNavbar from '@/components/AppNavbar.vue'
import AppFooter from '@/components/AppFooter.vue'

const auth = useAuthStore()
const router = useRouter()

const loading = ref(false)
const editMode = ref(false)
const selectedFile = ref<File | null>(null)
const previewUrl = ref<string | null>(null)

// ใช้ username แทน name
const username = ref('')

onMounted(async () => {
  if (!auth.token) {
    router.push({ name: 'login' })
    return
  }

  if (!auth.user) {
    await auth.fetchProfile()
  }

  if (auth.user) {
    username.value = auth.user.username || ''
  }
})

// บันทึกชื่อผู้ใช้
const saveProfile = async () => {
  if (!username.value.trim()) {
    alert('ກະລຸນາໃສ່ຊື່ຜູ້ໃຊ້')
    return
  }

  loading.value = true
  const formData = new FormData()
  formData.append('username', username.value.trim())

  try {
    const res = await fetch('http://localhost:8080/api/auth/user/profile/update', {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${auth.token}`
      },
      body: formData
    })

    if (res.ok) {
      const data = await res.json()
      auth.setUser(data.user)
      editMode.value = false
      alert('ປ່ຽນຊື່ຜູ້ໃຊ້ສຳເລັດ!')
    } else {
      alert('ບໍ່ສາມາດບັນທຶກໄດ້')
    }
  } catch (e) {
    console.error(e)
    alert('ເກີດຂໍ້ຜິດພາດ')
  } finally {
    loading.value = false
  }
}

// อัปโหลดรูปโปรไฟล์
const uploadPhoto = async () => {
  if (!selectedFile.value) return

  const formData = new FormData()
  formData.append('photo', selectedFile.value)

  try {
    const res = await fetch('http://localhost:8080/api/auth/user/profile/update', {
      method: 'POST',
      headers: { Authorization: `Bearer ${auth.token}` },
      body: formData
    })

    if (res.ok) {
      const data = await res.json()
      auth.setUser(data.user)
      previewUrl.value = null
      selectedFile.value = null
      alert('ອັບໂຫຼດຮູບສຳເລັດ!')
    }
  } catch (e) {
    alert('ອັບໂຫຼດຮູບລົ້ມເຫຼວ')
  }
}

const onFileChange = (e: Event) => {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (file) {
    selectedFile.value = file
    previewUrl.value = URL.createObjectURL(file)
  }
}
</script>

<template>
  <AppNavbar> </AppNavbar>
  <div class="min-h-screen bg-gradient-to-br from-purple-50 via-pink-50 to-indigo-50 p-6">
    <div class="max-w-3xl mx-auto">

      <!-- Card หลัก -->
      <div class="bg-white rounded-3xl shadow-2xl overflow-hidden">

        <!-- Header -->
        <div class="bg-gradient-to-r from-purple-600 to-pink-600 p-10 text-white text-center">
          <h1 class="text-5xl font-bold tracking-wider">Profile</h1>
          <p class="mt-2 text-purple-100">ຂໍ້ມູນສ່ວນຕົວຂອງທ່ານ</p>
        </div>

        <div class="p-10 space-y-10">

          <!-- รູບໂປຣໄຟລ໌ -->
          <div class="flex flex-col items-center">
            <div class="relative">
              <div class="w-40 h-40 rounded-full overflow-hidden ring-8 ring-purple-200 ring-offset-8 shadow-2xl">
                <img
                  :src="previewUrl || (auth.user?.photo_url
                    ? `http://localhost:8080${auth.user.photo_url}?t=${Date.now()}`
                    : '/default-avatar.jpg')"
                  alt="Profile"
                  class="w-full h-full object-cover"
                />
              </div>
              <label class="absolute bottom-2 right-2 cursor-pointer">
                <div class="bg-purple-600 p-3 rounded-full shadow-lg hover:bg-purple-700 transition">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 13a3 3 0 11-6 0 3 3 0 016 0z" />
                  </svg>
                </div>
                <input type="file" @change="onFileChange" class="hidden" accept="image/*" />
              </label>
            </div>
            <button
              @click="uploadPhoto"
              class="mt-4 btn bg-gradient-to-r from-purple-600 to-pink-600 text-white border-0 hover:shadow-xl px-8"
            >
              ອັບໂຫຼດຮູບໃໝ່
            </button>
          </div>

          <!-- ຂໍ້ມູນຜູ້ໃຊ້ -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-8">

            <!-- ຊື່ຜູ້ໃຊ້ -->
            <div class="bg-gradient-to-br from-indigo-50 to-purple-50 p-8 rounded-2xl shadow-xl border-l-8 border-indigo-600">
              <div class="flex items-center justify-between mb-4">
                <strong class="text-indigo-800 text-2xl">ຊື່ຜູ້ໃຊ້</strong>
                <button @click="editMode = true" class="text-indigo-600 hover:text-indigo-800">
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                </button>
              </div>
              <p class="text-4xl font-bold text-indigo-900 break-all">
                {{ auth.user?.username || 'ບໍ່ມີຊື່' }}
              </p>
            </div>

            <!-- ອີເມວ -->
            <div class="bg-gradient-to-br from-gray-50 to-gray-100 p-8 rounded-2xl shadow-xl border-l-8 border-gray-500">
              <strong class="text-gray-800 text-2xl block mb-4">ອີເມວ</strong>
              <p class="text-2xl font-medium text-gray-700 break-all">
                {{ auth.user?.email }}
              </p>
            </div>

            <!-- ຊື່ພາສາລາວ -->
            <div class="bg-gradient-to-br from-blue-50 to-cyan-50 p-8 rounded-2xl shadow-xl border-l-8 border-blue-600">
              <strong class="text-blue-800 text-2xl block mb-4">ຊື່ (ພາສາລາວ)</strong>
              <p class="text-4xl font-bold text-blue-900">
                {{ auth.user?.nameLa || 'ກຳລັງໂຫຼດ...' }}
              </p>
            </div>

            <!-- ຊື່ອັງກິດ -->
            <div class="bg-gradient-to-br from-green-50 to-emerald-50 p-8 rounded-2xl shadow-xl border-l-8 border-green-600">
              <strong class="text-green-800 text-2xl block mb-4">ຊື່ (English)</strong>
              <p class="text-4xl font-bold text-green-900">
                {{ auth.user?.nameEn || 'Loading...' }}
              </p>
            </div>
          </div>

          <!-- ຟອມແກ້ໄຂຊື່ -->
          <div v-if="editMode" class="bg-gradient-to-r from-purple-100 to-pink-100 p-8 rounded-3xl shadow-2xl border-4 border-purple-300">
            <h3 class="text-2xl font-bold text-purple-800 mb-6 text-center">ແກ້ໄຂຊື່ຜູ້ໃຊ້</h3>
            <input
              v-model="username"
              type="text"
              placeholder="ພິມຊື່ໃໝ່ທີ່ນີ້"
              class="input input-bordered w-full text-2xl py-6 text-center font-medium bg-white shadow-inner"
            />
            <div class="flex gap-4 mt-6">
              <button
                @click="saveProfile"
                :disabled="loading"
                class="flex-1 btn btn-success text-xl py-5 font-bold"
              >
                {{ loading ? 'ກຳລັງບັນທຶກ...' : 'ບັນທຶກ' }}
              </button>
              <button
                @click="editMode = false"
                class="flex-1 btn btn-error text-xl py-5 font-bold"
              >
                ຍົກເລີກ
              </button>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>

  <AppFooter></AppFooter>
</template>

<style scoped>
.btn:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 40px rgba(147, 51, 234, 0.4);
}
input:focus {
  outline: none;
  ring: 4px;
  ring-color: #a855f7;
}
</style>
