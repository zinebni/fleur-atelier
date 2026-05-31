import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getMyCommandes } from '../api/commandes';
import Loader from '../components/common/Loader';
import { formatPrice, formatDate, STATUS_LABEL, STATUS_CLASS } from '../utils/format';

export default function CommandesPage() {
  const [commandes, setCommandes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getMyCommandes().then(({ data }) => setCommandes(data)).finally(() => setLoading(false));
  }, []);

  return (
    <>
      <div className="page-header">
        <div className="container">
          <p className="section-eyebrow">Historique</p>
          <h1 className="page-title">Mes commandes</h1>
        </div>
      </div>
      <div className="container" style={{ paddingTop: '2rem', paddingBottom: '4rem' }}>
        {loading ? (
          <div className="flex-center" style={{ minHeight: 300 }}><Loader size={48} /></div>
        ) : commandes.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon">📦</div>
            <h2 className="empty-state-title">Aucune commande</h2>
            <p className="empty-state-text">Composez votre premier bouquet pour passer une commande.</p>
            <Link to="/catalogue" className="btn btn--gold">Découvrir les fleurs</Link>
          </div>
        ) : (
          <div className="commandes-list">
            {commandes.map((c) => (
              <Link key={c.id} to={`/commandes/${c.id}`} className="commande-card">
                <div className="commande-card-left">
                  <span className="commande-card-id">Commande #{c.id}</span>
                  <span className="commande-card-date">{formatDate(c.dateCommande)}</span>
                  <span className="commande-card-items">{c.lignes?.length ?? 0} article{(c.lignes?.length ?? 0) > 1 ? 's' : ''}</span>
                </div>
                <div className="commande-card-right">
                  <span className={`badge ${STATUS_CLASS[c.statut] ?? ''}`}>{STATUS_LABEL[c.statut] ?? c.statut}</span>
                  <span className="commande-card-total">{formatPrice(c.montantTotal)}</span>
                  <span className="commande-card-arrow">→</span>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>
    </>
  );
}
