import React, { useState, useEffect } from 'react';
import { genreAPI } from '../services/api';

function GenreList() {
  const [genres, setGenres] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingGenre, setEditingGenre] = useState(null);
  const [formData, setFormData] = useState({ name: '', description: '' });

  useEffect(() => {
    fetchGenres();
  }, []);

  const fetchGenres = async () => {
    try {
      const response = await genreAPI.getAll();
      setGenres(response.data);
    } catch (error) {
      console.error('Error fetching genres:', error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingGenre) {
        await genreAPI.update(editingGenre.id, formData);
      } else {
        await genreAPI.create(formData);
      }
      resetForm();
      fetchGenres();
    } catch (error) {
      console.error('Error saving genre:', error);
    }
  };

  const handleEdit = (genre) => {
    setEditingGenre(genre);
    setFormData({ name: genre.name, description: genre.description || '' });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this genre?')) {
      try {
        await genreAPI.delete(id);
        fetchGenres();
      } catch (error) {
        console.error('Error deleting genre:', error);
      }
    }
  };

  const resetForm = () => {
    setFormData({ name: '', description: '' });
    setEditingGenre(null);
    setShowForm(false);
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h2>Gêneros</h2>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancelar' : 'Novo Gênero'}
        </button>
      </div>

      {showForm && (
        <div className="modal-overlay" onClick={resetForm}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h2>{editingGenre ? 'Editar Gênero' : 'Novo Gênero'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Nome *</label>
                <input type="text" name="name" value={formData.name} onChange={handleInputChange} required />
              </div>
              <div className="form-group">
                <label>Descrição</label>
                <textarea name="description" value={formData.description} onChange={handleInputChange} rows="4" />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-secondary" onClick={resetForm}>Cancelar</button>
                <button type="submit" className="btn btn-primary">{editingGenre ? 'Atualizar' : 'Salvar'}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="grid">
        {genres.map(genre => (
          <div key={genre.id} className="card">
            <h3>{genre.name}</h3>
            {genre.description && <p>{genre.description}</p>}
            <div className="actions">
              <button className="btn btn-primary" onClick={() => handleEdit(genre)}>Editar</button>
              <button className="btn btn-danger" onClick={() => handleDelete(genre.id)}>Excluir</button>
            </div>
          </div>
        ))}
      </div>

      {genres.length === 0 && (
        <p style={{ textAlign: 'center', color: '#666', marginTop: '20px' }}>Nenhum gênero encontrado.</p>
      )}
    </div>
  );
}

export default GenreList;
