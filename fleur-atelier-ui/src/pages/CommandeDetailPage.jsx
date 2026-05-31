import { useEffect, useRef, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getCommandeById, payerCommande } from '../api/commandes';
import Loader from '../components/common/Loader';
import Toast from '../components/common/Toast';
import { useToast } from '../hooks/useToast';
import { FaCreditCard, FaPaypal } from 'react-icons/fa';
import {
  formatPrice, formatDate,
  STATUS_LABEL, STATUS_CLASS,
  DELIVERY_STATUS_LABEL, DELIVERY_STATUS_CLASS, DELIVERY_STEPS
} from '../utils/format';

/* ── Helpers ──────────────────────────────────────────────── */
const fmtCard  = (v) => v.replace(/\D/g, '').slice(0, 16).replace(/(.{4})/g, '$1 ').trim();
const fmtExpiry= (v) => { const d = v.replace(/\D/g, '').slice(0, 4); return d.length > 2 ? d.slice(0,2) + '/' + d.slice(2) : d; };
const fmtCvv   = (v) => v.replace(/\D/g, '').slice(0, 4);
const maskCard  = (n) => { const d = n.replace(/\s/g,''); return d.length < 4 ? '**** **** **** ****' : `**** **** **** ${d.slice(-4) || '****'}`; };

const CARD_EMPTY  = { numero: '', titulaire: '', expiry: '', cvv: '' };
const PP_EMPTY    = { email: '', motDePasse: '' };
const ADDR_EMPTY  = { adresse: '', ville: '', codePostal: '', telephone: '', pays: 'Maroc' };

/* ── Delivery tracker ─────────────────────────────────────── */
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

/* ── Animated card preview ────────────────────────────────── */
function CardPreview({ card, flipped }) {
  return (
    <div className={`payment-card-scene ${flipped ? 'flipped' : ''}`}>
      {/* Front */}
      <div className="payment-card payment-card--front">
        <div className="payment-card-chip">
          <div /><div /><div /><div />
        </div>
        <div className="payment-card-number">{maskCard(card.numero)}</div>
        <div className="payment-card-meta">
          <div>
            <p className="payment-card-meta-label">Titulaire</p>
            <p className="payment-card-meta-value">{card.titulaire || 'VOTRE NOM'}</p>
          </div>
          <div>
            <p className="payment-card-meta-label">Expiration</p>
            <p className="payment-card-meta-value">{card.expiry || 'MM/AA'}</p>
          </div>
        </div>
        <div className="payment-card-brand">💳</div>
      </div>
      {/* Back */}
      <div className="payment-card payment-card--back">
        <div className="payment-card-strip" />
        <div className="payment-card-cvv-area">
          <span className="payment-card-meta-label">CVV</span>
          <div className="payment-card-cvv-box">{card.cvv ? '•'.repeat(card.cvv.length) : '•••'}</div>
        </div>
      </div>
    </div>
  );
}

/* ── Main page ────────────────────────────────────────────── */
export default function CommandeDetailPage() {
  const { id } = useParams();
  const [commande, setCommande]   = useState(null);
  const [loading, setLoading]     = useState(true);
  const [mode, setMode]           = useState('carte');
  const [addr, setAddr]           = useState(ADDR_EMPTY);
  const [card, setCard]           = useState(CARD_EMPTY);
  const [pp, setPp]               = useState(PP_EMPTY);
  const [cardFlipped, setFlipped] = useState(false);
  const [paying, setPaying]       = useState(false);
  const cvvRef                    = useRef(null);
  const { toasts, addToast, removeToast } = useToast();

  const load = () => {
    setLoading(true);
    getCommandeById(id).then(({ data }) => setCommande(data)).finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, [id]);

  /* Address */
  const onAddr = (e) => setAddr({ ...addr, [e.target.name]: e.target.value });

  /* Card formatting */
  const onCard = (e) => {
    const { name, value } = e.target;
    let v = value;
    if (name === 'numero')  v = fmtCard(value);
    if (name === 'expiry')  v = fmtExpiry(value);
    if (name === 'cvv')     v = fmtCvv(value);
    setCard((c) => ({ ...c, [name]: v }));
  };

  /* Validate */
  const validate = () => {
    if (!addr.adresse || !addr.ville || !addr.codePostal || !addr.telephone) {
      addToast('Veuillez remplir tous les champs de livraison.', 'error'); return false;
    }
    if (mode === 'carte') {
      const digits = card.numero.replace(/\s/g,'');
      if (digits.length < 16) { addToast('Numéro de carte invalide (16 chiffres requis).', 'error'); return false; }
      if (!card.titulaire.trim())  { addToast('Veuillez entrer le nom du titulaire.', 'error'); return false; }
      if (card.expiry.length < 5)  { addToast('Date d\'expiration invalide (MM/AA).', 'error'); return false; }
      if (card.cvv.length < 3)     { addToast('CVV invalide (3 ou 4 chiffres).', 'error'); return false; }
    }
    if (mode === 'paypal') {
      if (!pp.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) { addToast('Adresse email PayPal invalide.', 'error'); return false; }
      if (!pp.motDePasse) { addToast('Veuillez entrer votre mot de passe PayPal.', 'error'); return false; }
    }
    return true;
  };

  const handlePay = async () => {
    if (!validate()) return;
    setPaying(true);
    try {
      const { data } = await payerCommande({ commandeId: Number(id), modePaiement: mode, ...addr });
      setCommande(data);
      addToast('Paiement accepté ! Livraison en cours de préparation.', 'success');
    } catch (err) {
      addToast(err.response?.data?.message || 'Paiement refusé. Veuillez réessayer.', 'error');
    } finally { setPaying(false); }
  };

  if (loading) return <div className="flex-center" style={{ minHeight: '60vh' }}><Loader size={56} /></div>;
  if (!commande) return <div className="container"><p>Commande introuvable.</p></div>;

  const isPending = commande.statut === 'EN_ATTENTE';
  const livraison = commande.livraison;

  return (
    <>
      <Toast toasts={toasts} onRemove={removeToast} />
      <div className="page-header">
        <div className="container">
          <Link to="/commandes" className="back-link">← Mes commandes</Link>
          <h1 className="page-title">Commande #{commande.id}</h1>
          <span className={`badge ${STATUS_CLASS[commande.statut] ?? ''}`}>{STATUS_LABEL[commande.statut] ?? commande.statut}</span>
        </div>
      </div>

      <div className="container commande-detail-layout">

        {/* ── Left column ── */}
        <div className="commande-detail-main">

          {/* Articles */}
          <div className="card">
            <h2 className="card-title">Articles commandés</h2>
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
              <span>Sous-total articles</span>
              <span className="commande-grand-total-price">{formatPrice(commande.montantTotal)}</span>
            </div>
            {livraison && <>
              <div className="commande-grand-total" style={{ borderTop: 'none', paddingTop: 0 }}>
                <span>Frais de livraison</span><span>{formatPrice(livraison.fraisLivraison)}</span>
              </div>
              <div className="commande-grand-total" style={{ borderTop: '1px solid var(--border)', marginTop: '0.5rem' }}>
                <strong>Total TTC</strong>
                <span className="commande-grand-total-price">{formatPrice((commande.montantTotal ?? 0) + (livraison.fraisLivraison ?? 0))}</span>
              </div>
            </>}
          </div>

          {/* Delivery tracker */}
          {livraison && (
            <div className="card">
              <h2 className="card-title">Suivi de livraison</h2>
              <span className={`badge ${DELIVERY_STATUS_CLASS[livraison.statut] ?? ''}`} style={{ marginBottom: '1.5rem', display: 'inline-block' }}>
                {DELIVERY_STATUS_LABEL[livraison.statut] ?? livraison.statut}
              </span>
              <DeliveryTracker statut={livraison.statut} />
              <div className="delivery-address-display">
                <p className="info-label" style={{ marginTop: '1.5rem' }}>Adresse de livraison</p>
                <p className="info-value">{livraison.adresse}</p>
                <p className="info-value">{livraison.codePostal} {livraison.ville}, {livraison.pays}</p>
                <p className="info-value">📞 {livraison.telephone}</p>
              </div>
              {livraison.dateLivraison && (
                <p style={{ marginTop: '1rem', color: 'var(--success)', fontSize: '0.875rem' }}>
                  ✓ Livré le {formatDate(livraison.dateLivraison)}
                </p>
              )}
            </div>
          )}

          {/* Payment confirmed */}
          {commande.statut === 'PAYEE' && (
            <div className="card card--success">
              <h2 className="card-title">Paiement confirmé ✓</h2>
              <div className="payment-info-grid">
                <div><p className="info-label">Mode</p><p className="info-value">{commande.modePaiement}</p></div>
                <div><p className="info-label">Date</p><p className="info-value">{formatDate(commande.datePaiement)}</p></div>
              </div>
            </div>
          )}
        </div>

        {/* ── Right sidebar ── */}
        <aside className="commande-detail-aside">
          <div className="card">
            <h2 className="card-title">Informations</h2>
            <p className="info-label">Commande passée le</p>
            <p className="info-value">{formatDate(commande.dateCommande)}</p>
            <p className="info-label" style={{ marginTop: '1rem' }}>Statut</p>
            <span className={`badge ${STATUS_CLASS[commande.statut] ?? ''}`}>{STATUS_LABEL[commande.statut] ?? commande.statut}</span>
          </div>

          {isPending && (
            <div className="card">
              <h2 className="card-title">Payer & Livrer</h2>
              <p className="card-subtitle" style={{ marginBottom: '1.25rem' }}>
                Montant : <strong>{formatPrice(commande.montantTotal)}</strong> + livraison <strong>{formatPrice(25)}</strong>
              </p>

              {/* ── Delivery address ── */}
              <h3 className="delivery-form-title">📍 Adresse de livraison</h3>
              <div className="form-group">
                <label className="form-label">Adresse *</label>
                <input name="adresse" className="form-input" value={addr.adresse} onChange={onAddr} placeholder="12 Rue des Fleurs" />
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label className="form-label">Ville *</label>
                  <input name="ville" className="form-input" value={addr.ville} onChange={onAddr} placeholder="Casablanca" />
                </div>
                <div className="form-group">
                  <label className="form-label">Code postal *</label>
                  <input name="codePostal" className="form-input" value={addr.codePostal} onChange={onAddr} placeholder="20000" />
                </div>
              </div>
              <div className="form-group">
                <label className="form-label">Téléphone *</label>
                <input name="telephone" className="form-input" value={addr.telephone} onChange={onAddr} placeholder="+212 6XX XXX XXX" />
              </div>
              <div className="form-group">
                <label className="form-label">Pays</label>
                <input name="pays" className="form-input" value={addr.pays} onChange={onAddr} />
              </div>

              {/* ── Payment mode selector ── */}
              <h3 className="delivery-form-title">💳 Mode de paiement</h3>
              <div className="payment-modes">
                {[
                  { id: 'carte',  icon: <FaCreditCard />, label: 'Carte bancaire' },
                  { id: 'paypal', icon: <FaPaypal />, label: 'PayPal' },
                ].map((m) => (
                  <label key={m.id} className={`payment-mode-option ${mode === m.id ? 'selected' : ''}`}>
                    <input type="radio" name="mode" value={m.id} checked={mode === m.id} onChange={() => setMode(m.id)} />
                    <span className="payment-mode-icon">{m.icon}</span>
                    <span className="payment-mode-label">{m.label}</span>
                  </label>
                ))}
              </div>

              {/* ── Card fields ── */}
              {mode === 'carte' && (
                <div className="payment-card-form">
                  <CardPreview card={card} flipped={cardFlipped} />

                  <div className="form-group" style={{ marginTop: '1.25rem' }}>
                    <label className="form-label">Numéro de carte *</label>
                    <input
                      name="numero" className="form-input" value={card.numero}
                      onChange={onCard} placeholder="0000 0000 0000 0000"
                      inputMode="numeric" autoComplete="cc-number"
                    />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Titulaire *</label>
                    <input
                      name="titulaire" className="form-input" value={card.titulaire}
                      onChange={onCard} placeholder="Prénom NOM"
                      autoComplete="cc-name" style={{ textTransform: 'uppercase' }}
                    />
                  </div>
                  <div className="form-row">
                    <div className="form-group">
                      <label className="form-label">Expiration *</label>
                      <input
                        name="expiry" className="form-input" value={card.expiry}
                        onChange={onCard} placeholder="MM/AA"
                        inputMode="numeric" autoComplete="cc-exp"
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">CVV *</label>
                      <input
                        ref={cvvRef}
                        name="cvv" className="form-input" value={card.cvv}
                        onChange={onCard} placeholder="•••"
                        inputMode="numeric" autoComplete="cc-csc"
                        onFocus={() => setFlipped(true)}
                        onBlur={() => setFlipped(false)}
                      />
                    </div>
                  </div>
                </div>
              )}

              {/* ── PayPal fields ── */}
              {mode === 'paypal' && (
                <div className="payment-paypal-form">
                  <div className="paypal-logo">
                    <span>Pay</span><span>Pal</span>
                  </div>
                  <p className="paypal-tagline">Connectez-vous à votre compte PayPal</p>
                  <div className="form-group">
                    <label className="form-label">Email PayPal *</label>
                    <input
                      name="email" type="email" className="form-input"
                      value={pp.email} onChange={(e) => setPp({ ...pp, email: e.target.value })}
                      placeholder="votre@email.com" autoComplete="email"
                    />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Mot de passe *</label>
                    <input
                      name="motDePasse" type="password" className="form-input"
                      value={pp.motDePasse} onChange={(e) => setPp({ ...pp, motDePasse: e.target.value })}
                      placeholder="••••••••" autoComplete="current-password"
                    />
                  </div>
                </div>
              )}

              {/* ── Total + pay button ── */}
              <div className="delivery-total-preview" style={{ marginTop: '1.5rem' }}>
                <span>Total à payer</span>
                <span className="delivery-total-amount">{formatPrice((commande.montantTotal ?? 0) + 25)}</span>
              </div>
              <button className="btn btn--gold btn--full" onClick={handlePay} disabled={paying}>
                {paying ? 'Traitement en cours…' : '🔒 Confirmer & Payer'}
              </button>
              <p style={{ fontSize: '0.72rem', color: 'var(--text-3)', textAlign: 'center', marginTop: '0.75rem' }}>
                Paiement sécurisé · Données chiffrées SSL
              </p>
            </div>
          )}
        </aside>
      </div>
    </>
  );
}
