import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 60000,
});

export const analyzeUrl = async (url) => {
  const response = await api.post('/analyze', { url });
  return response.data;
};

export default api;
