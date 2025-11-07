import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080",
  headers: { "Content-Type": "application/json" },
});

api.interceptors.response.use(
  (r) => r,
  (e) => {
    const status = e?.response?.status;
    const data = e?.response?.data;
    const message = data?.message || data?.error || e?.message || "Request failed";
    return Promise.reject({ status, data, message });
  }
);

export default api;
