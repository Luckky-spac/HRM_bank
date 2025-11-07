<!-- UserList.vue -->
<script setup lang="ts">
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useAuthStore } from '@/stores/auth';
import { Edit, Trash2 } from 'lucide-vue-next'
import { watchEffect } from 'vue'

const userStore = useUserStore()
const emit = defineEmits(['open-update'])
const authStore = useAuthStore();

onMounted(async () => {
  if (authStore.token) {
    await userStore.getAllUsers();
    await userStore.fetchRoles();
  } else {
    console.warn('Token not ready yet. ต้อง login ก่อน fetch');
  }
});

watchEffect(() => {
  if (authStore.token) {
    userStore.getAllUsers();
    userStore.fetchRoles();
  }
});


const handleDeleteUser = async (userId: number, username: string) => {
  if (!confirm(`แน่ใจหรือไม่ที่จะเปลี่ยนสถานะผู้ใช้ ${username} เป็น INACTIVE?`)) return
  try {
    await userStore.deleteUser(userId)
    alert(`เปลี่ยนสถานะ ${username} เป็น INACTIVE สำเร็จ!`)
  } catch (err: any) {
    alert(`เกิดข้อผิดพลาด: ${err.message || 'ไม่สามารถลบได้'}`)
  }
}

const openUpdateDrawer = (id: number) => {
  emit('open-update', id)
}
</script>

<template>
  <h2 class="text-2xl font-bold mb-4 text-gray-800">รายการผู้ใช้</h2>

  <div v-if="userStore.loading" class="alert alert-info shadow-lg">
    <span class="loading loading-spinner"></span> กำลังโหลดข้อมูลผู้ใช้...
  </div>

  <div v-else-if="userStore.error" class="alert alert-error shadow-lg">
    <span>{{ userStore.error }}</span>
    <button @click="userStore.getAllUsers()" class="btn btn-sm btn-error ml-2">ลองโหลดใหม่</button>
  </div>

  <div v-else-if="userStore.users.length > 0">
    <p class="text-sm italic text-gray-600 mb-4">
      จำนวนผู้ใช้ทั้งหมด: {{ userStore.totalUsersCount }} คน
    </p>
    <div class="overflow-x-auto">
      <table class="table table-zebra w-full shadow-md bg-white">
        <thead>
          <tr class="bg-gray-200">
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Role</th>
            <th>สถานะ</th>
            <th class="text-center">การดำเนินการ</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in userStore.users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.email }}</td>
            <td>
              <span v-if="user.roles?.length">{{ user.roles.map(r => r.role_name).join(', ') }}</span>
              <span v-else>ผู้ใช้ทั่วไป</span>
            </td>
            <td>
              <div :class="['badge', user.status === 'ACTIVE' ? 'badge-success' : 'badge-error', 'badge-outline']">
                {{ user.status }}
              </div>
            </td>
            <td class="flex justify-center space-x-2">
              <button @click="openUpdateDrawer(user.id)" class="btn btn-warning btn-sm">
                <Edit :size="16" /> แก้ไข
              </button>
              <button @click="handleDeleteUser(user.id, user.username)" class="btn btn-error btn-sm text-white">
                <Trash2 :size="16" /> ลบ
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <div v-else class="alert alert-warning shadow-lg">
    ยังไม่มีข้อมูลผู้ใช้ในระบบ
  </div>
</template>

<style scoped></style>
