import { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useBouquet } from '../context/BouquetContext';
import { createCommande } from '../api/commandes';
import BouquetItem from '../components/flowers/BouquetItem';
import Loader from '../components/common/Loader';
import Toast from '../components/common/Toast';
import { useToast } from '../hooks/useToast';
import { formatPrice } from '../utils/format';

export default function BouquetPage() {
  const { bouquet, loading, fetchBouquet, clearBouquet } = useBouquet();
  const { toasts, addToast, removeToast } = useToast();
  const navigate = useNavigate();
  const [ordering, setOrdering] = useState(false);

  useEffect(() => { fetchBouquet(); }, [fetchBouquet]);

  const handleOrder = async () => {
    setOrdering(true);
    try {
      const { data } = await createCommande();
      clearBouquet();
      addToast('Commande créée avec succès !', 'success');
      setTimeout(() => navigate(`/commandes/${data.id}`), 800);
    } catch (err) {
      addToast(err.response?.data?.message || 'Erreur lors de la création de la commande.', 'error');
    } finally { setOrdering(false); }
  };

  const lignes = bouquet?.lignes ?? [];
  const total = bouquet?.prixTotal ?? 0;
  const isEmpty = lignes.length === 0;

  return (
    <>
      <Toast toasts={toasts} onRemove={removeToast} />

      <div className="page-header">
        <div className="container">
          <p className="section-eyebrow">Composition</p>
          <h1 className="page-title">Mon Bouquet</h1>
        </div>
      </div>

      <div className="container bouquet-layout">
        {loading ? (
          <div className="flex-center" style={{ minHeight: 300 }}><Loader size={48} /></div>
        ) : isEmpty ? (
          <div className="empty-state">
            <div className="empty-state-icon">🌸</div>
            <h2 className="empty-state-title">Votre bouquet est vide</h2>
            <p className="empty-state-text">Parcourez notre catalogue et ajoutez vos fleurs préférées.</p>
            <Link to="/catalogue" className="btn btn--gold">Découvrir les fleurs</Link>
          </div>
        ) : (
          <>
            {/* Items */}
            <div className="bouquet-items">
              <h2 className="bouquet-section-title">Vos fleurs ({lignes.length} variété{lignes.length > 1 ? 's' : ''})</h2>
              {lignes.map((l) => (
                <BouquetItem key={l.id} ligne={l} onToast={addToast} />
              ))}
            </div>

            {/* Summary */}
            <aside className="bouquet-summary">
              <h2 className="bouquet-section-title">Récapitulatif</h2>
              <div className="summary-lines">
                {lignes.map((l) => (
                  <div key={l.id} className="summary-line">
                    <span>{l.articleNom} × {l.quantite}</span>
                    <span>{formatPrice(l.articlePrix * l.quantite)}</span>
                  </div>
                ))}
              </div>
              <div className="summary-total">
                <span>Total</span>
                <span className="summary-total-price">{formatPrice(total)}</span>
              </div>
              <button className="btn btn--gold btn--full" onClick={handleOrder} disabled={ordering}>
                {ordering ? 'Création…' : 'Passer ma commande'}
              </button>
              <Link to="/catalogue" className="btn btn--ghost btn--full" style={{ marginTop: '0.75rem' }}>
                Continuer mes achats
              </Link>
            </aside>
          </>
        )}
      </div>
    </>
  );
}
