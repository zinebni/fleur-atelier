import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getAllArticles } from '../../api/articles';
import { getCategories } from '../../api/categories';
import Loader from '../../components/common/Loader';
import { formatPrice } from '../../utils/format';

export default function AdminDashboard() {
  const [articles, setArticles] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([getAllArticles(), getCategories()])
      .then(([a, c]) => { setArticles(a.data); setCategories(c.data); })
      .finally(() => setLoading(false));
  }, []);

  const available = articles.filter(a => a.disponible).length;
  const outOfStock = articles.filter(a => !a.disponible).length;
  const totalValue = articles.reduce((s, a) => s + (a.prix * a.stock), 0);

  const stats = [
    { label: 'Articles', value: articles.length, icon: '🌸' },
    { label: 'Disponibles', value: available, icon: '✅' },
    { label: 'Rupture de stock', value: outOfStock, icon: '⚠️' },
    { label: 'Valeur du stock', value: formatPrice(totalValue), icon: '💰' },
    { label: 'Catégories', value: categories.length, icon: '🏷️' },
  ];

  return (
    <div className="admin-page">
      <div className="admin-page-header">
        <h1 className="admin-page-title">Tableau de bord</h1>
        <Link to="/admin/articles" className="btn btn--gold btn--sm">Gérer les articles</Link>
      </div>

      {loading ? <div className="flex-center" style={{ minHeight: 200 }}><Loader size={40} /></div> : (
        <>
          <div className="admin-stats-grid">
            {stats.map((s) => (
              <div key={s.label} className="admin-stat-card">
                <span className="admin-stat-icon">{s.icon}</span>
                <span className="admin-stat-value">{s.value}</span>
                <span className="admin-stat-label">{s.label}</span>
              </div>
            ))}
          </div>

          <div className="admin-section">
            <h2 className="admin-section-title">Articles récents</h2>
            <div className="admin-table-wrap">
              <table className="admin-table">
                <thead>
                  <tr><th>Nom</th><th>Catégorie</th><th>Prix</th><th>Stock</th><th>Statut</th></tr>
                </thead>
                <tbody>
                  {articles.slice(0, 8).map((a) => (
                    <tr key={a.id}>
                      <td>{a.nom}</td>
                      <td>{a.categorie?.nom}</td>
                      <td>{formatPrice(a.prix)}</td>
                      <td>{a.stock}</td>
                      <td><span className={`badge ${a.disponible ? 'badge--success' : 'badge--error'}`}>{a.disponible ? 'Disponible' : 'Indisponible'}</span></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
