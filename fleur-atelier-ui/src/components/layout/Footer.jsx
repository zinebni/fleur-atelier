import { Link } from 'react-router-dom';

export default function Footer() {
  return (
    <footer className="footer">
      <div className="footer-inner">
        <div className="footer-brand">
          <span className="footer-logo">Bouquet</span>
          <p className="footer-tagline">L'art floral réinventé, livré à votre porte.</p>
        </div>
        <div className="footer-links">
          <div className="footer-col">
            <h4 className="footer-col-title">Navigation</h4>
            <Link to="/" className="footer-link">Accueil</Link>
            <Link to="/catalogue" className="footer-link">Catalogue</Link>
            <Link to="/bouquet" className="footer-link">Mon Bouquet</Link>
          </div>
          <div className="footer-col">
            <h4 className="footer-col-title">Compte</h4>
            <Link to="/login" className="footer-link">Connexion</Link>
            <Link to="/register" className="footer-link">Inscription</Link>
            <Link to="/commandes" className="footer-link">Mes commandes</Link>
          </div>
        </div>
      </div>
      <div className="footer-bottom">
        <p>© {new Date().getFullYear()} Bouquet — Art Floral de Luxe. Tous droits réservés.</p>
      </div>
    </footer>
  );
}
