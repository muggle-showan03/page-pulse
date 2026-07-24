import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || (import.meta.env.PROD ? 'https://page-pulse-jbgc.onrender.com/api' : '/api'),
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
