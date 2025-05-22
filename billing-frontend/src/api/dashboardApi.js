import axios from 'axios';

export const getDashboardStats = () =>
  axios.get("http://localhost:8081/api/dashboard/summary");