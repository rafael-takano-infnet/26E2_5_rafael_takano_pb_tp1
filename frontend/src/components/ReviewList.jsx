import React, { useState, useEffect } from 'react';
import axios from 'axios';

const reviewApi = axios.create({
  baseURL: 'http://localhost:8081/api'
});

function ReviewList({ bookId }) {
  const [reviews, setReviews] = useState([]);
  const [formData, setFormData] = useState({ rating: '', comment: '' });

  useEffect(() => {
    fetchReviews();
  }, [bookId]);

  const fetchReviews = async () => {
    try {
      const response = await reviewApi.get('/reviews', { params: { bookId } });
      setReviews(response.data);
    } catch (error) {
      console.error('Error fetching reviews:', error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await reviewApi.post('/reviews', {
        bookId,
        rating: Number(formData.rating),
        comment: formData.comment
      });
      setFormData({ rating: '', comment: '' });
      fetchReviews();
    } catch (error) {
      console.error('Error saving review:', error);
    }
  };

  return (
    <div>
      <h4>Avaliações</h4>
      <form onSubmit={handleSubmit} style={{ display: 'flex', gap: '10px', marginBottom: '10px' }}>
        <input
          type="number"
          name="rating"
          placeholder="Nota (1-5)"
          min="1"
          max="5"
          value={formData.rating}
          onChange={handleInputChange}
          required
        />
        <input
          type="text"
          name="comment"
          placeholder="Comentário"
          value={formData.comment}
          onChange={handleInputChange}
        />
        <button type="submit" className="btn btn-primary">Adicionar</button>
      </form>

      {reviews.length === 0 && <p>Nenhuma avaliação encontrada.</p>}
      <ul>
        {reviews.map(review => (
          <li key={review.id}>
            <strong>{review.rating}/5</strong> {review.comment}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ReviewList;
