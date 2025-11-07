// src/stores/user.ts
import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import { useAuthStore } from './auth'

export interface Role {
  role_id: number
  role_name: string
}

export interface User {
  id: number
  username: string
  email: string
  roles: Role[]
  status: 'ACTIVE' | 'INACTIVE'
}

export const useUserStore = defineStore('user', () => {
  const users = ref<User[]>([])
  const totalUsersCount = ref(0)
  const loading = ref(false)
  const error = ref<string | null>(null)
  const processing = ref(false)
  const roleList = ref<Role[]>([])
  const isLoadingRoles = ref(false)

  const authStore = useAuthStore()
  const API_URL = 'http://localhost:8080/api'

  const getAuthHeaders = () => {
    if (!authStore.token) throw new Error('No token found')
    return { Authorization: `Bearer ${authStore.token}` }
  }

  const getAllUsers = async () => {
    if (!authStore.token) {
      error.value = 'No token found. กรุณาเข้าสู่ระบบ'
      return
    }

    loading.value = true
    error.value = null
    try {
      const res = await axios.get(`${API_URL}/users`, { headers: getAuthHeaders() })
      users.value = res.data
      totalUsersCount.value = res.data.length
    } catch (err: any) {
      console.error('Fetch users failed:', err)
      if (err.response?.status === 403) {
        error.value = 'Forbidden: คุณไม่มีสิทธิ์เข้าถึงข้อมูลนี้'
      } else {
        error.value = err.message || 'Fetch users failed'
      }
    } finally {
      loading.value = false
    }
  }

  const deleteUser = async (userId: number) => {
    processing.value = true
    try {
      await axios.delete(`${API_URL}/users/${userId}`, { headers: getAuthHeaders() })
      users.value = users.value.filter((u) => u.id !== userId)
    } catch (err: any) {
      console.error('Delete user failed:', err)
      throw err
    } finally {
      processing.value = false
    }
  }

  const fetchRoles = async () => {
    isLoadingRoles.value = true
    try {
      const res = await axios.get(`${API_URL}/roles`, { headers: getAuthHeaders() })
      roleList.value = res.data
    } catch (err) {
      console.error('Fetch roles failed:', err)
    } finally {
      isLoadingRoles.value = false
    }
  }

  return {
    users,
    totalUsersCount,
    loading,
    error,
    processing,
    roleList,
    isLoadingRoles,
    getAllUsers,
    deleteUser,
    fetchRoles,
  }
})
