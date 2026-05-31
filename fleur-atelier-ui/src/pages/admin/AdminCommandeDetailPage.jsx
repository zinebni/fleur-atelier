import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import client from '../../api/client';
import { updateLivraisonStatut } from '../../api/livraisons';
import Loader from '../../components/common/Loader';
import Toast from '../../components/common/Toast';
import { useToast } from '../../hooks/useToast';
import {
  formatPrice, formatDate,
  STATUS_LABEL, STATUS_CLASS,
  DELIVERY_STATUS_LABEL, DELIVERY_STATUS_CLASS, DELIVERY_STEPS
} from '../../utils/format';

function DeliveryTracker({ statut }) {
  const currentIdx = DELIVERY_STEPS.indexOf(statut);
  return (
    <div className="delivery-tracker">
      {DELIVERY_STEPS.map((step, i) => (
        <div key={step} className={`tracker-step ${i <= currentIdx ? 'done' : ''} ${i === currentIdx ? 'active' : ''}`}>
          <div className="tracker-dot" />
          {i < DELIVERY_STEPS.length - 1 && <div className="tracker-line" />}
          <span className="tracker-label">{DELIVERY_STATUS_LABEL[step]}</span>
        </div>
      ))}
    </div>
  );
}

export default function AdminCommandeDetailPage() {
  const { id } = useParams();
  const [commande, setCommande] = useState(null);
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);
  const { toasts, addToast, removeToast } = useToast();

  const load = () => {
    setLoading(true);
    client.get(`/admin/commandes/${id}`).then(({ data }) => setCommande(data)).finally(() => setLoading(false));
  };

  useEffect(() => { load(); }, [id]);

  const handleStatut = async (statut) => {
    if (!commande?.livraison) return;
    setUpdating(true);
    try {
      await updateLivraisonStatut(commande.livraison.id, statut);
      addToast('Statut de livraison mis à jour.', 'success');
      load();
    } catch {
      addToast('Erreur lors de la mise à jour.', 'error');
    } finally { setUpdating(false); }
  };

  if (loading) return <div className="flex-center" style={{ minHeight: '60vh' }}><Loader size={48} /></div>;
  if (!commande) return <div className="admin-page"><p>Commande introuvable.</p></div>;

  const livraison = commande.livraison;

  return (
    <div className="admin-page">
      <Toast toasts={toasts} onRemove={removeToast} />

      <div className="admin-page-header">
        <div>
          <Link to="/admin/commandes" className="back-link">← Toutes les commandes</Link>
          <h1 className="admin-page-title">Commande #{commande.id}</h1>
        </div>
        <span className={`badge ${STATUS_CLASS[commande.statut] ?? ''}`}>
          {STATUS_LABEL[commande.statut] ?? commande.statut}
        </span>
      </div>

      <div className="commande-detail-layout">
        {/* Left */}
        <div className="commande-detail-main">

          {/* Client info */}
          <div className="card">
            <h2 className="card-title">👤 Client</h2>
            <div className="payment-info-grid">
              <div><p className="info-label">Nom</p><p className="info-value">{commande.utilisateurPrenom} {commande.utilisateurNom}</p></div>
              <div><p className="info-label">Email</p><p className="info-value">{commande.utilisateurEmail}</p></div>
              <div><p className="info-label">Commande le</p><p className="info-value">{formatDate(commande.dateCommande)}</p></div>
              {commande.datePaiement && <div><p className="info-label">Payée le</p><p className="info-value">{formatDate(commande.datePaiement)}</p></div>}
            </div>
          </div>

          {/* Articles */}
          <div className="card">
            <h2 className="card-title">🌸 Articles commandés</h2>
            <div className="commande-lignes">
              {commande.lignes?.map((l) => (
                <div key={l.id} className="commande-ligne">
                  <div className="commande-ligne-info">
                    <span className="commande-ligne-name">{l.nomArticle}</span>
                    <span className="commande-ligne-qty">× {l.quantite}</span>
                  </div>
                  <div className="commande-ligne-prices">
                    <span className="commande-ligne-unit">{formatPrice(l.prixUnitaire)} / tige</span>
                    <span className="commande-ligne-total">{formatPrice(l.sousTotal)}</span>
                  </div>
                </div>
              ))}
            </div>
            <div className="commande-grand-total">
              <span>Sous-total</span>
              <span className="commande-grand-total-price">{formatPrice(commande.montantTotal)}</span>
            </div>
            {livraison && (
              <div className="commande-grand-total" style={{ borderTop: 'none', paddingTop: 0 }}>
                <span>Frais livraison</span><span>{formatPrice(livraison.fraisLivraison)}</span>
              </div>
            )}
          </div>

          {/* Delivery tracking & management */}
          {livraison && (
            <div className="card">
              <h2 className="card-title">🚚 Livraison</h2>
              <div className="payment-info-grid" style={{ marginBottom: '1.5rem' }}>
                <div><p className="info-label">Adresse</p><p className="info-value">{livraison.adresse}</p></div>
                <div><p className="info-label">Ville / CP</p><p className="info-value">{livraison.ville} {livraison.codePostal}</p></div>
                <div><p className="info-label">Pays</p><p className="info-value">{livraison.pays}</p></div>
                <div><p className="info-label">Téléphone</p><p className="info-value">{livraison.telephone}</p></div>
              </div>

              <DeliveryTracker statut={livraison.statut} />

              <div style={{ marginTop: '1.5rem' }}>
                <p className="info-label" style={{ marginBottom: '0.75rem' }}>Mettre à jour le statut de livraison</p>
                <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                  {DELIVERY_STEPS.map((s) => (
                    <button
                      key={s}
                      className={`btn btn--sm ${livraison.statut === s ? 'btn--gold' : 'btn--ghost'}`}
                      onClick={() => handleStatut(s)}
                      disabled={updating || livraison.statut === s}
                    >
                      {DELIVERY_STATUS_LABEL[s]}
                    </button>
                  ))}
                </div>
              </div>

              {livraison.dateLivraison && (
                <p style={{ marginTop: '1rem', color: 'var(--success)', fontSize: '0.875rem' }}>
                  ✓ Livré le {formatDate(livraison.dateLivraison)}
                </p>
              )}
            </div>
          )}

          {!livraison && commande.statut === 'EN_ATTENTE' && (
            <div className="card">
              <p style={{ color: 'var(--text-2)' }}>⏳ En attente de paiement par le client. La livraison sera créée après le paiement.</p>
            </div>
          )}
        </div>

        {/* Right sidebar */}
        <aside className="commande-detail-aside">
          <div className="card">
            <h2 className="card-title">Résumé</h2>
            <p className="info-label">Statut commande</p>
            <span className={`badge ${STATUS_CLASS[commande.statut] ?? ''}`} style={{ marginBottom: '1rem', display: 'inline-block' }}>
              {STATUS_LABEL[commande.statut] ?? commande.statut}
            </span>
            {livraison && (
              <>
                <p className="info-label">Statut livraison</p>
                <span className={`badge ${DELIVERY_STATUS_CLASS[livraison.statut] ?? ''}`} style={{ marginBottom: '1rem', display: 'inline-block' }}>
                  {DELIVERY_STATUS_LABEL[livraison.statut] ?? livraison.statut}
                </span>
              </>
            )}
            <p className="info-label">Mode paiement</p>
            <p className="info-value">{commande.modePaiement ?? '—'}</p>
            <p className="info-label" style={{ marginTop: '1rem' }}>Total TTC</p>
            <p className="commande-grand-total-price">
              {formatPrice((commande.montantTotal ?? 0) + (livraison?.fraisLivraison ?? 0))}
            </p>
          </div>
        </aside>
      </div>
    </div>
  );
}
