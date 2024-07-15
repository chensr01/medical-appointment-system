import axios from 'axios';

const axiosInstance = axios.create({
  // baseURL: 'http://localhost:8080',
  baseURL: 'http://128.2.204.151:8080',
});

export default axiosInstance;