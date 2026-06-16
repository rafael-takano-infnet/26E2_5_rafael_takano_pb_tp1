import React, { useState, useEffect } from 'react';
import { publisherAPI } from '../services/api';

function PublisherList() {
  const [publishers, setPublishers] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingPublisher, setEditingPublisher] = useState(null);
  const [formData, setFormData] = useState({ name: '', country: '' });

  useEffect(() => {
    fetchPublishers();
  }, []);

  const fetchPublishers = async () => {
    try {
      const response = await publisherAPI.getAll();
      setPublishers(response.data);
    } catch (error) {
      console.error('Error fetching publishers:', error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingPublisher) {
        await publisherAPI.update(editingPublisher.id, formData);
      } else {
        await publisherAPI.create(formData);
      }
      resetForm();
      fetchPublishers();
    } catch (error) {
      console.error('Error saving publisher:', error);
    }
  };

  const handleEdit = (publisher) => {
    setEditingPublisher(publisher);
    setFormData({ name: publisher.name, country: publisher.country || '' });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this publisher?')) {
      try {
        await publisherAPI.delete(id);
        fetchPublishers();
      } catch (error) {
        console.error('Error deleting publisher:', error);
      }
    }
  };

  const resetForm = () => {
    setFormData({ name: '', country: '' });
    setEditingPublisher(null);
    setShowForm(false);
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h2>Editoras</h2>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancelar' : 'Nova Editora'}
        </button>
      </div>

      {showForm && (
        <div className="modal-overlay" onClick={resetForm}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h2>{editingPublisher ? 'Editar Editora' : 'Nova Editora'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Nome *</label>
                <input type="text" name="name" value={formData.name} onChange={handleInputChange} required />
              </div>
              <div className="form-group">
                <label>País</label>
                <input type="text" name="country" value={formData.country} onChange={handleInputChange} />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-secondary" onClick={resetForm}>Cancelar</button>
                <button type="submit" className="btn btn-primary">{editingPublisher ? 'Atualizar' : 'Salvar'}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="grid">
        {publishers.map(publisher => (
          <div key={publisher.id} className="card">
            <h3>{publisher.name}</h3>
            {publisher.country && <p><strong>País:</strong> {publisher.country}</p>}
            <div className="actions">
              <button className="btn btn-primary" onClick={() => handleEdit(publisher)}>Editar</button>
              <button className="btn btn-danger" onClick={() => handleDelete(publisher.id)}>Excluir</button>
            </div>
          </div>
        ))}
      </div>

      {publishers.length === 0 && (
        <p style={{ textAlign: 'center', color: '#666', marginTop: '20px' }}>Nenhuma editora encontrada.</p>
      )}
    </div>
  );
}

export default PublisherList;
