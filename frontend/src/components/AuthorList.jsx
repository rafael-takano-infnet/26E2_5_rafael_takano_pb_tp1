import React, { useState, useEffect } from 'react';
import { authorAPI } from '../services/api';

function AuthorList() {
  const [authors, setAuthors] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingAuthor, setEditingAuthor] = useState(null);
  const [formData, setFormData] = useState({ name: '', biography: '' });

  useEffect(() => {
    fetchAuthors();
  }, []);

  const fetchAuthors = async () => {
    try {
      const response = await authorAPI.getAll();
      setAuthors(response.data);
    } catch (error) {
      console.error('Error fetching authors:', error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingAuthor) {
        await authorAPI.update(editingAuthor.id, formData);
      } else {
        await authorAPI.create(formData);
      }
      resetForm();
      fetchAuthors();
    } catch (error) {
      console.error('Error saving author:', error);
    }
  };

  const handleEdit = (author) => {
    setEditingAuthor(author);
    setFormData({ name: author.name, biography: author.biography || '' });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this author?')) {
      try {
        await authorAPI.delete(id);
        fetchAuthors();
      } catch (error) {
        console.error('Error deleting author:', error);
      }
    }
  };

  const resetForm = () => {
    setFormData({ name: '', biography: '' });
    setEditingAuthor(null);
    setShowForm(false);
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h2>Autores</h2>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancelar' : 'Novo Autor'}
        </button>
      </div>

      {showForm && (
        <div className="modal-overlay" onClick={resetForm}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h2>{editingAuthor ? 'Editar Autor' : 'Novo Autor'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Nome *</label>
                <input type="text" name="name" value={formData.name} onChange={handleInputChange} required />
              </div>
              <div className="form-group">
                <label>Biografia</label>
                <textarea name="biography" value={formData.biography} onChange={handleInputChange} rows="4" />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-secondary" onClick={resetForm}>Cancelar</button>
                <button type="submit" className="btn btn-primary">{editingAuthor ? 'Atualizar' : 'Salvar'}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="grid">
        {authors.map(author => (
          <div key={author.id} className="card">
            <h3>{author.name}</h3>
            {author.biography && <p>{author.biography}</p>}
            <div className="actions">
              <button className="btn btn-primary" onClick={() => handleEdit(author)}>Editar</button>
              <button className="btn btn-danger" onClick={() => handleDelete(author.id)}>Excluir</button>
            </div>
          </div>
        ))}
      </div>

      {authors.length === 0 && (
        <p style={{ textAlign: 'center', color: '#666', marginTop: '20px' }}>Nenhum autor encontrado.</p>
      )}
    </div>
  );
}

export default AuthorList;
