// src/api.ts

const BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080/api';

export async function apiFetch<T>(
  path: string,
  opts: RequestInit & { body?: Record<string, unknown> } = {}
): Promise<T> {
  const headers = new Headers(opts.headers || {});

  let body: BodyInit | undefined = undefined;

  if (opts.body instanceof FormData) {
    body = opts.body;
  } else if (opts.body !== undefined) {
    // default: send JSON
    body = JSON.stringify(opts.body);
    if (!headers.has('Content-Type')) {
      headers.set('Content-Type', 'application/json');
    }
  }

  // 💡 โค้ดนี้ยังดึง Token อยู่ แต่เราจะปิด Logic 401 ในด้านล่าง
  const token = localStorage.getItem('token')
  if (token && !headers.has('Authorization')) {
    headers.set('Authorization', `Bearer ${token}`);
  }

  // 🔴 บรรทัด 30 (ตาม Log ของคุณ)
  const res = await fetch(`${BASE}${path}`, { ...opts, headers, body});

  let data: T | null = null;
  try {
    data = await res.json();
  } catch(err) {
    console.error(err)
  }

  if (!res.ok) {
    if (res.status === 401) {
      // 🔴 BYPASS: ไม่ต้องเด้งไปหน้า Login ชั่วคราว
      console.warn(`[API BYPASS] Path ${path} returned 401, but redirect is temporarily disabled.`);

      // 💡 โยน Error กลับไป (เพื่อป้องกัน Unhandled Promise) แต่ไม่เด้งไปหน้า Login
      throw { status: res.status, message: 'Unauthorized (Auth Bypassed)' };

    } else {
        // โยน Error อื่นๆ (400, 500) ตามปกติ
        throw data || { status: res.status };
    }
  }

  return data as T;
}
