import { useEffect, useState, useMemo } from 'react';
import { useSearchParams } from 'react-router-dom';
import { getAllArticles } from '../api/articles';
import { getCategories } from '../api/categories';
import ArticleCard from '../components/flowers/ArticleCard';
import Loader from '../components/common/Loader';
import Toast from '../components/common/Toast';
import { useToast } from '../hooks/useToast';

export default function CataloguePage() {
  const [articles, setArticles] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [searchParams, setSearchParams] = useSearchParams();
  const { toasts, addToast, removeToast } = useToast();

  const selectedCat = searchParams.get('categorie') || 'Tout';

  useEffect(() => {
    Promise.all([getAllArticles(), getCategories()])
      .then(([artRes, catRes]) => {
        setArticles(artRes.data);
        setCategories(catRes.data);
      })
      .finally(() => setLoading(false));
  }, []);

  const filtered = useMemo(() => {
    let list = articles;
    if (selectedCat !== 'Tout') list = list.filter(a => a.categorieNom === selectedCat);
    if (search.trim()) list = list.filter(a => a.nom.toLowerCase().includes(search.toLowerCase()));
    return list;
  }, [articles, selectedCat, search]);

  const setCat = (cat) => setSearchParams(cat === 'Tout' ? {} : { categorie: cat });

  return (
    <>
      <Toast toasts={toasts} onRemove={removeToast} />

      <div className="page-header">
        <div className="container">
          <p className="section-eyebrow">Catalogue</p>
          <h1 className="page-title">Nos fleurs</h1>
          <p className="page-subtitle">Choisissez parmi {articles.length} variétés d'exception</p>
        </div>
      </div>

      <div className="container catalogue-layout">
        {/* Search */}
        <div className="catalogue-search-wrap">
          <input
            type="search"
            className="search-input"
            placeholder="Rechercher une fleur…"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        {/* Category filter */}
        <div className="cat-filter-wrap">
          {['Tout', ...categories.map(c => c.nom)].map((cat) => (
            <button
              key={cat}
              className={`cat-filter-btn ${selectedCat === cat ? 'active' : ''}`}
              onClick={() => setCat(cat)}
            >
              {cat}
            </button>
          ))}
        </div>

        {/* Results */}
        {loading ? (
          <div className="flex-center" style={{ minHeight: 300 }}><Loader size={48} /></div>
        ) : filtered.length === 0 ? (
          <div className="empty-state">
            <p className="empty-state-text">Aucune fleur trouvée pour cette recherche.</p>
          </div>
        ) : (
          <>
            <p className="catalogue-count">{filtered.length} résultat{filtered.length > 1 ? 's' : ''}</p>
            <div className="articles-grid">
              {filtered.map((a) => (
                <ArticleCard key={a.id} article={a} onToast={addToast} />
              ))}
            </div>
          </>
        )}
      </div>
    </>
  );
}
