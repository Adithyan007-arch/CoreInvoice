import axios from "axios";

const BASE_URL = "http://localhost:8081/api"; // Adjust if your port is different

const api = axios.create({
  baseURL: BASE_URL,
});

export default api;
