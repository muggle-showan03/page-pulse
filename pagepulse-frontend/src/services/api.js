import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 30000,
});

export const analyzeUrl = async (url) => {
  const response = await api.post('/analyze', { url });
  return response.data;
};

export default api;
