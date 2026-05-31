import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getAllLivraisons, updateLivraisonStatut } from '../../api/livraisons';
import Toast from '../../components/common/Toast';
import { useToast } from '../../hooks/useToast';
import Loader from '../../components/common/Loader';
import { formatDate, formatPrice, DELIVERY_STATUS_LABEL, DELIVERY_STEPS } from '../../utils/format';

export default function AdminLivraisonsPage() {
  const [livraisons, setLivraisons] = useState([]);
  const [loading, setLoading] = useState(true);
  const { toasts, addToast, removeToast } = useToast();

  const load = () => {
    setLoading(true);
    getAllLivraisons().then(({ data }) => setLivraisons(data)).finally(() => setLoading(false));
  };

  useEffect(() => { load(); }, []);

  const handleStatut = async (id, statut) => {
    try {
      await updateLivraisonStatut(id, statut);
      addToast('Statut mis à jour.', 'success');
      load();
    } catch {
      addToast('Erreur lors de la mise à jour.', 'error');
    }
  };

  return (
    <div className="admin-page">
      <Toast toasts={toasts} onRemove={removeToast} />
      <div className="admin-page-header">
        <h1 className="admin-page-title">Livraisons ({livraisons.length})</h1>
      </div>

      {loading ? (
        <div className="flex-center" style={{ minHeight: 200 }}><Loader size={40} /></div>
      ) : livraisons.length === 0 ? (
        <p style={{ color: 'var(--text-2)' }}>Aucune livraison pour le moment.</p>
      ) : (
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Livraison</th>
                <th>Commande</th>
                <th>Adresse</th>
                <th>Ville</th>
                <th>Téléphone</th>
                <th>Frais</th>
                <th>Statut</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {livraisons.map((l) => (
                <tr key={l.id}>
                  <td style={{ color: 'var(--text-2)', fontSize: '0.85rem' }}>#{l.id}</td>
                  <td>
                    {l.commandeId ? (
                      <Link to={`/admin/commandes/${l.commandeId}`} className="admin-commande-id">
                        #{l.commandeId}
                      </Link>
                    ) : (
                      <span style={{ color: 'var(--text-3)' }}>—</span>
                    )}
                  </td>
                  <td className="admin-article-name">{l.adresse}</td>
                  <td>{l.ville}</td>
                  <td>{l.telephone}</td>
                  <td>{formatPrice(l.fraisLivraison)}</td>
                  <td>
                    <select
                      className="livraison-statut-select"
                      value={l.statut}
                      onChange={(e) => handleStatut(l.id, e.target.value)}
                    >
                      {DELIVERY_STEPS.map((s) => (
                        <option key={s} value={s}>{DELIVERY_STATUS_LABEL[s]}</option>
                      ))}
                    </select>
                  </td>
                  <td style={{ fontSize: '0.8rem', color: 'var(--text-2)' }}>{formatDate(l.dateCreation)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
