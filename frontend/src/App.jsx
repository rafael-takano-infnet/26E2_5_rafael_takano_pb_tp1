import React, { useState, useEffect } from 'react';
import BookList from './components/BookList';
import AuthorList from './components/AuthorList';
import PublisherList from './components/PublisherList';
import GenreList from './components/GenreList';

function App() {
  const [activeTab, setActiveTab] = useState('books');

  const renderContent = () => {
    switch (activeTab) {
      case 'books':
        return <BookList />;
      case 'authors':
        return <AuthorList />;
      case 'publishers':
        return <PublisherList />;
      case 'genres':
        return <GenreList />;
      default:
        return <BookList />;
    }
  };

  return (
    <div>
      <header>
        <h1>Catálogo de Livros</h1>
        <nav>
          <button className={activeTab === 'books' ? 'active' : ''} onClick={() => setActiveTab('books')}>
            Livros
          </button>
          <button className={activeTab === 'authors' ? 'active' : ''} onClick={() => setActiveTab('authors')}>
            Autores
          </button>
          <button className={activeTab === 'publishers' ? 'active' : ''} onClick={() => setActiveTab('publishers')}>
            Editoras
          </button>
          <button className={activeTab === 'genres' ? 'active' : ''} onClick={() => setActiveTab('genres')}>
            Gêneros
          </button>
        </nav>
      </header>
      <div className="container">
        {renderContent()}
      </div>
    </div>
  );
}

export default App;
