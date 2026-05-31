import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useBouquet } from '../../context/BouquetContext';
import { formatPrice } from '../../utils/format';

export default function ArticleCard({ article, onToast }) {
  const { isAuthenticated } = useAuth();
  const { addItem } = useBouquet();
  const navigate = useNavigate();
  const [qty, setQty] = useState(1);
  const [adding, setAdding] = useState(false);

  const handleAdd = async () => {
    if (!isAuthenticated) { navigate('/login'); return; }
    setAdding(true);
    try {
      await addItem(article.id, qty);
      onToast?.(`${article.nom} ajouté au bouquet !`, 'success');
      setQty(1);
    } catch (err) {
      const msg = err.response?.data?.message || 'Stock insuffisant.';
      onToast?.(msg, 'error');
    } finally { setAdding(false); }
  };

  return (
    <article className={`article-card ${!article.disponible ? 'article-card--unavailable' : ''}`}>
      <div className="article-card-img-wrap">
        <img
          src={article.imageUrl || 'https://images.unsplash.com/photo-1490750967868-88df5691cc5a?w=600'}
          alt={article.nom}
          className="article-card-img"
          loading="lazy"
        />
        {!article.disponible && <span className="article-card-overlay-tag">Indisponible</span>}
      </div>

      <div className="article-card-body">
        <p className="article-card-category">{article.categorieNom}</p>
        <h3 className="article-card-name">{article.nom}</h3>
        <p className="article-card-desc">{article.description}</p>

        <div className="article-card-footer">
          <span className="article-card-price">{formatPrice(article.prix)}</span>

          {article.disponible && (
            <div className="article-card-actions">
              <div className="qty-control">
                <button className="qty-btn" onClick={() => setQty((q) => Math.max(1, q - 1))}>−</button>
                <span className="qty-value">{qty}</span>
                <button className="qty-btn" onClick={() => setQty((q) => Math.min(article.stock, q + 1))}>+</button>
              </div>
              <button className="btn btn--gold btn--sm" onClick={handleAdd} disabled={adding}>
                {adding ? '…' : 'Ajouter'}
              </button>
            </div>
          )}
        </div>
      </div>
    </article>
  );
}
