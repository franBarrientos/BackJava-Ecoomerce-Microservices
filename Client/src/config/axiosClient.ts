import axios from "axios";


declare global {
  interface Window {
    _env_: {
      [key: string]: any};
  }
}


const apiUrl: string = import.meta.env.VITE_BASE_URL ?? window._env_?.BASE_URL;
const apiClient = axios.create({
  baseURL: apiUrl,
  timeout: 25000,
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiClient;
