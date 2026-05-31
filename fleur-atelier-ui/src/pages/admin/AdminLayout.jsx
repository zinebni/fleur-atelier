import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export default function AdminLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => { await logout(); navigate('/'); };

  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        <div className="admin-sidebar-brand">
          <span className="admin-logo">Bouquet</span>
          <span className="admin-role-badge">Admin</span>
        </div>
        <nav className="admin-nav">
          <NavLink to="/admin" end className={({ isActive }) => `admin-nav-link ${isActive ? 'active' : ''}`}>
            <span>📊</span> Tableau de bord
          </NavLink>
          <NavLink to="/admin/articles" className={({ isActive }) => `admin-nav-link ${isActive ? 'active' : ''}`}>
            <span>🌸</span> Articles
          </NavLink>
          <NavLink to="/admin/commandes" className={({ isActive }) => `admin-nav-link ${isActive ? 'active' : ''}`}>
            <span>📋</span> Commandes
          </NavLink>
          <NavLink to="/admin/livraisons" className={({ isActive }) => `admin-nav-link ${isActive ? 'active' : ''}`}>
            <span>🚚</span> Livraisons
          </NavLink>
        </nav>
        <div className="admin-sidebar-footer">
          <p className="admin-user-name">{user?.prenom} {user?.nom}</p>
          <button className="btn btn--ghost btn--sm" onClick={handleLogout}>Déconnexion</button>
        </div>
      </aside>
      <main className="admin-main"><Outlet /></main>
    </div>
  );
}
