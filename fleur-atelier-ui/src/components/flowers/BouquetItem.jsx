import { useState } from 'react';
import { useBouquet } from '../../context/BouquetContext';
import { formatPrice } from '../../utils/format';

export default function BouquetItem({ ligne, onToast }) {
  const { updateItem, removeItem } = useBouquet();
  const [loading, setLoading] = useState(false);

  const change = async (delta) => {
    const newQty = ligne.quantite + delta;
    if (newQty < 1) return;
    setLoading(true);
    try {
      await updateItem(ligne.id, newQty);
    } catch (err) {
      onToast?.(err.response?.data?.message || 'Erreur de mise à jour.', 'error');
    } finally { setLoading(false); }
  };

  const remove = async () => {
    setLoading(true);
    try {
      await removeItem(ligne.id);
      onToast?.(`${ligne.articleNom} retiré.`, 'success');
    } catch { onToast?.('Erreur lors de la suppression.', 'error'); }
    finally { setLoading(false); }
  };

  return (
    <div className={`bouquet-item ${loading ? 'bouquet-item--loading' : ''}`}>
      <img
        src={ligne.articleImageUrl || 'https://images.unsplash.com/photo-1490750967868-88df5691cc5a?w=200'}
        alt={ligne.articleNom}
        className="bouquet-item-img"
      />
      <div className="bouquet-item-info">
        <h4 className="bouquet-item-name">{ligne.articleNom}</h4>
        <p className="bouquet-item-price">{formatPrice(ligne.articlePrix)} / tige</p>
      </div>
      <div className="bouquet-item-controls">
        <div className="qty-control">
          <button className="qty-btn" onClick={() => change(-1)} disabled={loading}>−</button>
          <span className="qty-value">{ligne.quantite}</span>
          <button className="qty-btn" onClick={() => change(1)} disabled={loading}>+</button>
        </div>
        <span className="bouquet-item-subtotal">
          {formatPrice(ligne.articlePrix * ligne.quantite)}
        </span>
        <button className="bouquet-item-remove" onClick={remove} disabled={loading} aria-label="Retirer">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14H6L5 6"/><path d="M10 11v6M14 11v6"/>
            <path d="M9 6V4h6v2"/>
          </svg>
        </button>
      </div>
    </div>
  );
}
