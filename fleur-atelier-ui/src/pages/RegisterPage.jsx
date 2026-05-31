import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const BG = 'https://images.unsplash.com/photo-1534637282532-3e7ad90ab783?w=1200&q=80';

export default function RegisterPage() {
  const { register, loading } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ prenom: '', nom: '', email: '', password: '' });
  const [error, setError] = useState('');

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await register(form);
      navigate('/catalogue');
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la création du compte.');
    }
  };

  return (
    <div className="auth-layout">
      <div className="auth-image" style={{ backgroundImage: `url(${BG})` }}>
        <div className="auth-image-overlay" />
        <div className="auth-image-text">
          <h2>Rejoignez-nous</h2>
          <p>Créez votre compte et composez des bouquets sur mesure.</p>
        </div>
      </div>

      <div className="auth-form-side">
        <div className="auth-form-wrap">
          <Link to="/" className="auth-logo">Bouquet</Link>
          <h1 className="auth-title">Créer un compte</h1>
          <p className="auth-sub">Déjà membre ? <Link to="/login" className="link-gold">Se connecter</Link></p>

          {error && <div className="alert alert--error">{error}</div>}

          <form className="auth-form" onSubmit={onSubmit}>
            <div className="form-row">
              <div className="form-group">
                <label className="form-label">Prénom</label>
                <input type="text" name="prenom" className="form-input" required
                  value={form.prenom} onChange={onChange} placeholder="Alice" />
              </div>
              <div className="form-group">
                <label className="form-label">Nom</label>
                <input type="text" name="nom" className="form-input" required
                  value={form.nom} onChange={onChange} placeholder="Dupont" />
              </div>
            </div>
            <div className="form-group">
              <label className="form-label">Adresse e-mail</label>
              <input type="email" name="email" className="form-input" required
                value={form.email} onChange={onChange} placeholder="vous@exemple.com" />
            </div>
            <div className="form-group">
              <label className="form-label">Mot de passe</label>
              <input type="password" name="password" className="form-input" required minLength={6}
                value={form.password} onChange={onChange} placeholder="••••••••" />
            </div>
            <button type="submit" className="btn btn--gold btn--full" disabled={loading}>
              {loading ? 'Création…' : 'Créer mon compte'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
