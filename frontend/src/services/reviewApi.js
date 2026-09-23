import axios from 'axios';

const reviewApi = axios.create({
  baseURL: 'http://localhost:8081/api'
});

export const reviewAPI = {
  getByBook: (bookId) => reviewApi.get(`/reviews?bookId=${bookId}`),
  create: (data) => reviewApi.post('/reviews', data),
  delete: (id) => reviewApi.delete(`/reviews/${id}`)
};

export default reviewApi;
