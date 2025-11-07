import axios from 'axios';
import type { User } from '@/types/user';
import { useAuthStore } from '@/stores/auth';

const API_URL = 'http://localhost:8080/api';

export default class UserService {
  static getAuthHeaders() {
    const authStore = useAuthStore();
    return { Authorization: `Bearer ${authStore.token}` };
  }

  static async getAllUsers(): Promise<User[]> {
    const res = await axios.get(`${API_URL}/users`, { headers: this.getAuthHeaders() });
    return res.data;
  }

  static async createUser(payload: any): Promise<User> {
    const res = await axios.post(`${API_URL}/users`, payload, { headers: this.getAuthHeaders() });
    return res.data;
  }

  static async deleteUser(userId: string) {
    const res = await axios.delete(`${API_URL}/users/${userId}`, { headers: this.getAuthHeaders() });
    return res.data;
  }

  static async getRoles() {
    const res = await axios.get(`${API_URL}/roles`, { headers: this.getAuthHeaders() });
    return res.data;
  }
}
