// src/stores/auth.ts
import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User } from '@/types/user'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const user = ref<User | null>(null)

  const setUser = (u: Partial<User> | null, t?: string) => {
    if (!u) {
      user.value = null
      localStorage.setItem('token', t)
    } else {
      user.value = {
        id: u.id ?? user.value?.id,
        name: u.name ?? user.value?.name ?? '',
        email: u.email ?? user.value?.email ?? '',
        role: u.role ?? user.value?.role ?? 'user',
        age: (u.age ?? user.value?.age ?? 0) as number,
        address: u.address ?? user.value?.address ?? '',
        photo_url: u.photo_url ?? user.value?.photo_url ?? '',
        userId: u.userId ?? (user.value as any)?.userId,
        username: u.username ?? (user.value as any)?.username,
        nameLa: (u as any).nameLa ?? (user.value as any)?.nameLa,
        nameEn: (u as any).nameEn ?? (user.value as any)?.nameEn,
        gender: (u as any).gender ?? (user.value as any)?.gender,
        customerEmail: (u as any).customerEmail ?? (user.value as any)?.customerEmail,
        employeeEmail: (u as any).employeeEmail ?? (user.value as any)?.employeeEmail,
      }
    }

    if (t !== undefined) {
      token.value = t ?? null
      if (t) localStorage.setItem('token', t)
      else localStorage.removeItem('token')
    }
  }

  const fetchProfile = async (): Promise<User | null> => {
    if (!token.value) return null
    try {
      const res = await fetch('http://localhost:8080/api/auth/user/profile', {
        headers: { Authorization: `Bearer ${token.value}` },
      })
      if (!res.ok) throw new Error('Failed')
      const data = await res.json()
      setUser({
        id: data.id?.toString(),
        name: data.name ?? data.username,
        email: data.email,
        role: (data.role as any) || 'user',
        age: (data.age as any) || 0,
        address: (data.address as any) || '',
        photo_url: data.photo_url || data.photoUrl || '',
        userId: data.userId,
        username: data.username,
        nameLa: data.nameLa,
        nameEn: data.nameEn,
        gender: data.gender,
        customerEmail: data.customerEmail,
        employeeEmail: data.employeeEmail,
      })
      return user.value
    } catch (e) {
      console.error('Fetch profile failed:', e)
      logout()
      return null
    }
  }

  const logout = () => {
    setUser(null)
    token.value = null
    localStorage.removeItem('token')
  }

  // โหลดตอนเริ่ม
  if (token.value && !user.value) {
    fetchProfile()
  }

  return { user, token, setUser, fetchProfile, logout }
})
