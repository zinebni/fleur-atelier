import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import client from '../../api/client';
import Loader from '../../components/common/Loader';
import { formatPrice, formatDate, STATUS_LABEL, STATUS_CLASS, DELIVERY_STATUS_LABEL, DELIVERY_STATUS_CLASS } from '../../utils/format';

export default function AdminCommandesPage() {
  const [commandes, setCommandes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    client.get('/admin/commandes')
      .then(({ data }) => setCommandes(data))
      .finally(() => setLoading(false));
  }, []);

  const filtered = filter === 'all' ? commandes : commandes.filter(c => c.statut === filter);

  return (
    <div className="admin-page">
      <div className="admin-page-header">
        <h1 className="admin-page-title">Commandes ({commandes.length})</h1>
        <div style={{ display: 'flex', gap: '0.5rem' }}>
          {['all', 'EN_ATTENTE', 'PAYEE', 'ANNULEE'].map(f => (
            <button key={f} className={`cat-filter-btn ${filter === f ? 'active' : ''}`} onClick={() => setFilter(f)}>
              {f === 'all' ? 'Toutes' : STATUS_LABEL[f] ?? f}
            </button>
          ))}
        </div>
      </div>

      {loading ? (
        <div className="flex-center" style={{ minHeight: 200 }}><Loader size={40} /></div>
      ) : filtered.length === 0 ? (
        <p style={{ color: 'var(--text-2)' }}>Aucune commande trouvée.</p>
      ) : (
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Commande</th>
                <th>Client</th>
                <th>Email</th>
                <th>Date</th>
                <th>Articles</th>
                <th>Total</th>
                <th>Paiement</th>
                <th>Livraison</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((c) => (
                <tr key={c.id}>
                  <td>
                    <Link to={`/admin/commandes/${c.id}`} className="admin-commande-id">
                      #{c.id}
                    </Link>
                  </td>
                  <td className="admin-article-name">
                    {c.utilisateurPrenom} {c.utilisateurNom}
                  </td>
                  <td style={{ color: 'var(--text-2)', fontSize: '0.8rem' }}>{c.utilisateurEmail}</td>
                  <td style={{ fontSize: '0.8rem' }}>{formatDate(c.dateCommande)}</td>
                  <td style={{ textAlign: 'center' }}>{c.lignes?.length ?? 0}</td>
                  <td>{formatPrice(c.montantTotal)}</td>
                  <td>
                    <span className={`badge ${STATUS_CLASS[c.statut] ?? ''}`}>
                      {STATUS_LABEL[c.statut] ?? c.statut}
                    </span>
                  </td>
                  <td>
                    {c.livraison ? (
                      <span className={`badge ${DELIVERY_STATUS_CLASS[c.livraison.statut] ?? ''}`}>
                        {DELIVERY_STATUS_LABEL[c.livraison.statut] ?? c.livraison.statut}
                      </span>
                    ) : <span style={{ color: 'var(--text-3)', fontSize: '0.8rem' }}>—</span>}
                  </td>
                  <td>
                    <Link to={`/admin/commandes/${c.id}`} className="btn btn--ghost btn--xs">
                      Détail →
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
