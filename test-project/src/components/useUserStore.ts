import { ref } from 'vue'
import { apiFetch } from '@/services/api'
import type { User } from '@/types/user'

export function useUserStore() {
  const users = ref<User[]>([])

  // ดึง list ผู้ใช้
  async function fetchUsers() {
    const data = await apiFetch<{ users: User[] }>('/user')
    users.value = data.users
  }

  // update user info
  async function updateUser(id: string, data: Partial<User>) {
    const updated = await apiFetch<User>(`/user/${id}`, {
      method: 'PUT',
      body: data,
    })
    const index = users.value.findIndex((u) => u.id === id)
    if (index !== -1) users.value[index] = updated
    return updated
  }

  // update role
  async function updateUserRole(id: string, role: string) {
    const updated = await apiFetch<{ id: string; role: string }>(`/user/${id}/role`, {
      method: 'PUT',
      body: { role },
    })
    const index = users.value.findIndex((u) => u.id === id)
    if (index !== -1) users.value[index].role = role
    return updated
  }

  return { users, fetchUsers, updateUser, updateUserRole }
}
