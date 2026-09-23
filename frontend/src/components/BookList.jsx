import React, { useState, useEffect } from 'react';
import { bookAPI, authorAPI, publisherAPI, genreAPI } from '../services/api';
import ReviewList from './ReviewList';

function BookList() {
  const [books, setBooks] = useState([]);
  const [expandedReviewsId, setExpandedReviewsId] = useState(null);
  const [authors, setAuthors] = useState([]);
  const [publishers, setPublishers] = useState([]);
  const [genres, setGenres] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingBook, setEditingBook] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [formData, setFormData] = useState({
    title: '',
    isbn: '',
    publicationYear: '',
    pages: '',
    price: '',
    publisherId: '',
    authorIds: [],
    genreIds: []
  });

  useEffect(() => {
    fetchBooks();
    fetchAuthors();
    fetchPublishers();
    fetchGenres();
  }, []);

  const fetchBooks = async () => {
    try {
      const response = await bookAPI.getAll();
      setBooks(response.data);
    } catch (error) {
      console.error('Error fetching books:', error);
    }
  };

  const fetchAuthors = async () => {
    try {
      const response = await authorAPI.getAll();
      setAuthors(response.data);
    } catch (error) {
      console.error('Error fetching authors:', error);
    }
  };

  const fetchPublishers = async () => {
    try {
      const response = await publisherAPI.getAll();
      setPublishers(response.data);
    } catch (error) {
      console.error('Error fetching publishers:', error);
    }
  };

  const fetchGenres = async () => {
    try {
      const response = await genreAPI.getAll();
      setGenres(response.data);
    } catch (error) {
      console.error('Error fetching genres:', error);
    }
  };

  const handleSearch = async () => {
    if (searchTerm.trim() === '') {
      fetchBooks();
    } else {
      try {
        const response = await bookAPI.search(searchTerm);
        setBooks(response.data);
      } catch (error) {
        console.error('Error searching books:', error);
      }
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleMultiSelect = (e, field) => {
    const options = e.target.options;
    const selected = [];
    for (let i = 0; i < options.length; i++) {
      if (options[i].selected) {
        selected.push(Number(options[i].value));
      }
    }
    setFormData(prev => ({ ...prev, [field]: selected }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = {
        ...formData,
        publicationYear: formData.publicationYear ? Number(formData.publicationYear) : null,
        pages: formData.pages ? Number(formData.pages) : null,
        price: formData.price ? Number(formData.price) : null,
        publisherId: Number(formData.publisherId)
      };

      if (editingBook) {
        await bookAPI.update(editingBook.id, data);
      } else {
        await bookAPI.create(data);
      }

      resetForm();
      fetchBooks();
    } catch (error) {
      console.error('Error saving book:', error);
    }
  };

  const handleEdit = (book) => {
    setEditingBook(book);
    setFormData({
      title: book.title,
      isbn: book.isbn || '',
      publicationYear: book.publicationYear || '',
      pages: book.pages || '',
      price: book.price || '',
      publisherId: book.publisher?.id || '',
      authorIds: book.authors?.map(a => a.id) || [],
      genreIds: book.genres?.map(g => g.id) || []
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this book?')) {
      try {
        await bookAPI.delete(id);
        fetchBooks();
      } catch (error) {
        console.error('Error deleting book:', error);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      title: '',
      isbn: '',
      publicationYear: '',
      pages: '',
      price: '',
      publisherId: '',
      authorIds: [],
      genreIds: []
    });
    setEditingBook(null);
    setShowForm(false);
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h2>Livros</h2>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancelar' : 'Novo Livro'}
        </button>
      </div>

      <div style={{ marginBottom: '20px', display: 'flex', gap: '10px' }}>
        <input
          type="text"
          placeholder="Buscar por título..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          style={{ flex: 1, padding: '10px', border: '1px solid #ddd', borderRadius: '4px' }}
        />
        <button className="btn btn-primary" onClick={handleSearch}>Buscar</button>
      </div>

      {showForm && (
        <div className="modal-overlay" onClick={resetForm}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h2>{editingBook ? 'Editar Livro' : 'Novo Livro'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Título *</label>
                <input type="text" name="title" value={formData.title} onChange={handleInputChange} required />
              </div>
              <div className="form-group">
                <label>ISBN</label>
                <input type="text" name="isbn" value={formData.isbn} onChange={handleInputChange} />
              </div>
              <div className="form-group">
                <label>Ano de Publicação</label>
                <input type="number" name="publicationYear" value={formData.publicationYear} onChange={handleInputChange} />
              </div>
              <div className="form-group">
                <label>Páginas</label>
                <input type="number" name="pages" value={formData.pages} onChange={handleInputChange} />
              </div>
              <div className="form-group">
                <label>Preço</label>
                <input type="number" step="0.01" name="price" value={formData.price} onChange={handleInputChange} />
              </div>
              <div className="form-group">
                <label>Editora *</label>
                <select name="publisherId" value={formData.publisherId} onChange={handleInputChange} required>
                  <option value="">Selecione uma editora</option>
                  {publishers.map(p => (
                    <option key={p.id} value={p.id}>{p.name}</option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>Autores</label>
                <select multiple value={formData.authorIds} onChange={(e) => handleMultiSelect(e, 'authorIds')}>
                  {authors.map(a => (
                    <option key={a.id} value={a.id}>{a.name}</option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>Gêneros</label>
                <select multiple value={formData.genreIds} onChange={(e) => handleMultiSelect(e, 'genreIds')}>
                  {genres.map(g => (
                    <option key={g.id} value={g.id}>{g.name}</option>
                  ))}
                </select>
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-secondary" onClick={resetForm}>Cancelar</button>
                <button type="submit" className="btn btn-primary">{editingBook ? 'Atualizar' : 'Salvar'}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="grid">
        {books.map(book => (
          <div key={book.id} className="card">
            <h3>{book.title}</h3>
            {book.isbn && <p><strong>ISBN:</strong> {book.isbn}</p>}
            {book.publicationYear && <p><strong>Ano:</strong> {book.publicationYear}</p>}
            {book.pages && <p><strong>Páginas:</strong> {book.pages}</p>}
            {book.price && <p><strong>Preço:</strong> R$ {Number(book.price).toFixed(2)}</p>}
            {book.publisher && <p><strong>Editora:</strong> {book.publisher.name}</p>}
            {book.authors && book.authors.length > 0 && (
              <div>
                <strong>Autores:</strong>
                <div>
                  {book.authors.map(a => (
                    <span key={a.id} className="badge">{a.name}</span>
                  ))}
                </div>
              </div>
            )}
            {book.genres && book.genres.length > 0 && (
              <div>
                <strong>Gêneros:</strong>
                <div>
                  {book.genres.map(g => (
                    <span key={g.id} className="badge">{g.name}</span>
                  ))}
                </div>
              </div>
            )}
            <div className="actions">
              <button className="btn btn-primary" onClick={() => handleEdit(book)}>Editar</button>
              <button className="btn btn-danger" onClick={() => handleDelete(book.id)}>Excluir</button>
              <button
                className="btn btn-secondary"
                onClick={() => setExpandedReviewsId(expandedReviewsId === book.id ? null : book.id)}
              >
                {expandedReviewsId === book.id ? 'Ocultar Avaliações' : 'Avaliações'}
              </button>
            </div>
            {expandedReviewsId === book.id && <ReviewList bookId={book.id} />}
          </div>
        ))}
      </div>

      {books.length === 0 && (
        <p style={{ textAlign: 'center', color: '#666', marginTop: '20px' }}>Nenhum livro encontrado.</p>
      )}
    </div>
  );
}

export default BookList;
