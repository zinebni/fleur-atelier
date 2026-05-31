import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const BG = 'https://images.unsplash.com/photo-1508610048659-a06b669e3321?w=1200&q=80';

export default function LoginPage() {
  const { login, loading } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const user = await login(form);
      navigate(user.role === 'ADMIN' ? '/admin' : '/catalogue');
    } catch (err) {
      setError(err.response?.data?.message || 'Identifiants invalides.');
    }
  };

  return (
    <div className="auth-layout">
      <div className="auth-image" style={{ backgroundImage: `url(${BG})` }}>
        <div className="auth-image-overlay" />
        <div className="auth-image-text">
          <h2>Bienvenue</h2>
          <p>Composez des bouquets uniques avec nos fleurs d'exception.</p>
        </div>
      </div>

      <div className="auth-form-side">
        <div className="auth-form-wrap">
          <Link to="/" className="auth-logo">Bouquet</Link>
          <h1 className="auth-title">Connexion</h1>
          <p className="auth-sub">Pas encore de compte ? <Link to="/register" className="link-gold">Créer un compte</Link></p>

          {error && <div className="alert alert--error">{error}</div>}

          <form className="auth-form" onSubmit={onSubmit}>
            <div className="form-group">
              <label className="form-label">Adresse e-mail</label>
              <input
                type="email" name="email" className="form-input" required
                value={form.email} onChange={onChange}
                placeholder="vous@exemple.com"
              />
            </div>
            <div className="form-group">
              <label className="form-label">Mot de passe</label>
              <input
                type="password" name="password" className="form-input" required
                value={form.password} onChange={onChange}
                placeholder="••••••••"
              />
            </div>
            <button type="submit" className="btn btn--gold btn--full" disabled={loading}>
              {loading ? 'Connexion…' : 'Se connecter'}
            </button>
          </form>

        </div>
      </div>
    </div>
  );
}
