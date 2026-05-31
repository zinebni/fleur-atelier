import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getCategories } from '../api/categories';
import { getAllArticles } from '../api/articles';
import { formatPrice } from '../utils/format';
import ArticleCard from '../components/flowers/ArticleCard';
import Loader from '../components/common/Loader';
import Toast from '../components/common/Toast';
import { useToast } from '../hooks/useToast';
import { useAuth } from '../context/AuthContext';

const HERO_BG = 'https://images.unsplash.com/photo-1487530811015-780f5b9a2394?w=1600&q=80';

export default function HomePage() {
  const [categories, setCategories] = useState([]);
  const [featured, setFeatured] = useState([]);
  const [loading, setLoading] = useState(true);
  const { toasts, addToast, removeToast } = useToast();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    Promise.all([getCategories(), getAllArticles()])
      .then(([catRes, artRes]) => {
        setCategories(catRes.data);
        setFeatured(artRes.data.filter(a => a.disponible).slice(0, 4));
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <Toast toasts={toasts} onRemove={removeToast} />

      {/* ── Hero ── */}
      <section className="hero" style={{ backgroundImage: `url(${HERO_BG})` }}>
        <div className="hero-overlay" />
        <div className="hero-content">
          <p className="hero-eyebrow">Art Floral de Luxe</p>
          <h1 className="hero-title">Composez votre<br /><em>bouquet idéal</em></h1>
          <p className="hero-subtitle">Choisissez parmi nos fleurs d'exception et créez une composition unique, livrée avec soin.</p>
          <div className="hero-actions">
            <Link to="/catalogue" className="btn btn--gold btn--lg">Découvrir le catalogue</Link>
            {!isAuthenticated && (
              <Link to="/register" className="btn btn--outline btn--lg">Créer un compte</Link>
            )}
          </div>
        </div>
        <div className="hero-scroll-hint">
          <span>Défiler</span>
          <div className="hero-scroll-line" />
        </div>
      </section>

      {/* ── Categories ── */}
      <section className="section">
        <div className="container">
          <div className="section-header">
            <p className="section-eyebrow">Collections</p>
            <h2 className="section-title">Nos familles florales</h2>
          </div>
          {loading ? (
            <div className="flex-center"><Loader /></div>
          ) : (
            <div className="categories-grid">
              {categories.map((cat) => (
                <Link key={cat.id} to={`/catalogue?categorie=${encodeURIComponent(cat.nom)}`} className="category-card">
                  <span className="category-emoji">{cat.emoji || '🌸'}</span>
                  <h3 className="category-name">{cat.nom}</h3>
                  <p className="category-desc">{cat.description}</p>
                  <span className="category-arrow">→</span>
                </Link>
              ))}
            </div>
          )}
        </div>
      </section>

      {/* ── Featured Articles ── */}
      <section className="section section--dark">
        <div className="container">
          <div className="section-header">
            <p className="section-eyebrow">Sélection</p>
            <h2 className="section-title">Fleurs du moment</h2>
          </div>
          {loading ? (
            <div className="flex-center"><Loader /></div>
          ) : (
            <div className="articles-grid">
              {featured.map((a) => (
                <ArticleCard key={a.id} article={a} onToast={addToast} />
              ))}
            </div>
          )}
          <div className="section-cta">
            <Link to="/catalogue" className="btn btn--outline">Voir toutes les fleurs</Link>
          </div>
        </div>
      </section>

      {/* ── How it works ── */}
      <section className="section">
        <div className="container">
          <div className="section-header">
            <p className="section-eyebrow">Processus</p>
            <h2 className="section-title">En trois étapes simples</h2>
          </div>
          <div className="steps-grid">
            {[
              { n: '01', title: 'Composez', desc: 'Parcourez notre catalogue et ajoutez vos fleurs préférées à votre bouquet personnalisé.' },
              { n: '02', title: 'Confirmez', desc: 'Validez votre bouquet pour créer une commande. Le prix est fixé au moment de la commande.' },
              { n: '03', title: 'Payez', desc: 'Réglez en toute sécurité par carte bancaire ou PayPal. Livraison rapide garantie.' },
            ].map((s) => (
              <div key={s.n} className="step-card">
                <span className="step-number">{s.n}</span>
                <h3 className="step-title">{s.title}</h3>
                <p className="step-desc">{s.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
    </>
  );
}
