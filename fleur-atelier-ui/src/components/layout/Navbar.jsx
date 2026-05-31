import { useState, useEffect } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useBouquet } from '../../context/BouquetContext';

export default function Navbar() {
  const { user, isAuthenticated, isAdmin, logout } = useAuth();
  const { itemCount } = useBouquet();
  const navigate = useNavigate();
  const [scrolled, setScrolled] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener('scroll', onScroll);
    return () => window.removeEventListener('scroll', onScroll);
  }, []);

  const handleLogout = async () => {
    await logout();
    setMenuOpen(false);
    navigate('/');
  };

  return (
    <header className={`navbar ${scrolled ? 'navbar--scrolled' : ''}`}>
      <div className="navbar-inner">
        {/* Logo */}
        <Link to="/" className="navbar-logo" onClick={() => setMenuOpen(false)}>
          <span className="navbar-logo-text">Bouquet</span>
          <span className="navbar-logo-dot">·</span>
        </Link>

        {/* Desktop Nav */}
        <nav className="navbar-links">
          <NavLink to="/" end className={({ isActive }) => `navbar-link ${isActive ? 'active' : ''}`}>
            Accueil
          </NavLink>
          <NavLink to="/catalogue" className={({ isActive }) => `navbar-link ${isActive ? 'active' : ''}`}>
            Catalogue
          </NavLink>
          <NavLink to="/commandes" className={({ isActive }) => `navbar-link ${isActive ? 'active' : ''}`}>
            Mes commandes
          </NavLink>
          {isAdmin && (
            <NavLink to="/admin" className={({ isActive }) => `navbar-link ${isActive ? 'active' : ''}`}>
              Admin
            </NavLink>
          )}
        </nav>

        {/* Right actions */}
        <div className="navbar-actions">
          {isAuthenticated && (
            <Link to="/bouquet" className="navbar-bouquet-btn" title="Mon bouquet">
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
                <path d="M12 2C6.5 2 2 6.5 2 12s4.5 10 10 10 10-4.5 10-10S17.5 2 12 2z"/>
                <path d="M12 8v8M8 12h8"/>
              </svg>
              {itemCount > 0 && <span className="navbar-badge">{itemCount}</span>}
            </Link>
          )}

          {isAuthenticated ? (
            <div className="navbar-user">
              <span className="navbar-user-name">{user.prenom}</span>
              <button className="btn btn--ghost btn--sm" onClick={handleLogout}>
                Déconnexion
              </button>
            </div>
          ) : (
            <div className="navbar-auth">
              <Link to="/login" className="btn btn--ghost btn--sm">Connexion</Link>
              <Link to="/register" className="btn btn--gold btn--sm">Créer un compte</Link>
            </div>
          )}

          {/* Hamburger */}
          <button className="navbar-hamburger" onClick={() => setMenuOpen(!menuOpen)} aria-label="Menu">
            <span className={`hamburger-line ${menuOpen ? 'open' : ''}`} />
            <span className={`hamburger-line ${menuOpen ? 'open' : ''}`} />
            <span className={`hamburger-line ${menuOpen ? 'open' : ''}`} />
          </button>
        </div>
      </div>

      {/* Mobile menu */}
      {menuOpen && (
        <div className="navbar-mobile">
          <NavLink to="/" end className="navbar-mobile-link" onClick={() => setMenuOpen(false)}>Accueil</NavLink>
          <NavLink to="/catalogue" className="navbar-mobile-link" onClick={() => setMenuOpen(false)}>Catalogue</NavLink>
          {isAuthenticated && (
            <NavLink to="/bouquet" className="navbar-mobile-link" onClick={() => setMenuOpen(false)}>
              Mon Bouquet {itemCount > 0 && `(${itemCount})`}
            </NavLink>
          )}
          {isAuthenticated && (
            <NavLink to="/commandes" className="navbar-mobile-link" onClick={() => setMenuOpen(false)}>Mes Commandes</NavLink>
          )}
          {isAdmin && (
            <NavLink to="/admin" className="navbar-mobile-link" onClick={() => setMenuOpen(false)}>Administration</NavLink>
          )}
          {isAuthenticated ? (
            <button className="navbar-mobile-link text-left" onClick={handleLogout}>Déconnexion</button>
          ) : (
            <>
              <Link to="/login" className="navbar-mobile-link" onClick={() => setMenuOpen(false)}>Connexion</Link>
              <Link to="/register" className="navbar-mobile-link" onClick={() => setMenuOpen(false)}>Créer un compte</Link>
            </>
          )}
        </div>
      )}
    </header>
  );
}
