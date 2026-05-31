import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { BouquetProvider } from './context/BouquetContext';
import { ProtectedRoute, AdminRoute } from './components/layout/RouteGuards';
import Navbar from './components/layout/Navbar';
import Footer from './components/layout/Footer';
import HomePage from './pages/HomePage';
import CataloguePage from './pages/CataloguePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import BouquetPage from './pages/BouquetPage';
import CommandesPage from './pages/CommandesPage';
import CommandeDetailPage from './pages/CommandeDetailPage';
import AdminLayout from './pages/admin/AdminLayout';
import AdminDashboard from './pages/admin/AdminDashboard';
import AdminArticlesPage from './pages/admin/AdminArticlesPage';
import AdminLivraisonsPage from './pages/admin/AdminLivraisonsPage';
import AdminCommandesPage from './pages/admin/AdminCommandesPage';
import AdminCommandeDetailPage from './pages/admin/AdminCommandeDetailPage';

function PublicLayout({ children }) {
  return (
    <>
      <Navbar />
      <main className="main-content">{children}</main>
      <Footer />
    </>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <BouquetProvider>
          <Routes>
            {/* Public routes */}
            <Route path="/" element={<PublicLayout><HomePage /></PublicLayout>} />
            <Route path="/catalogue" element={<PublicLayout><CataloguePage /></PublicLayout>} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            {/* Protected user routes */}
            <Route element={<ProtectedRoute />}>
              <Route path="/bouquet" element={<PublicLayout><BouquetPage /></PublicLayout>} />
              <Route path="/commandes" element={<PublicLayout><CommandesPage /></PublicLayout>} />
              <Route path="/commandes/:id" element={<PublicLayout><CommandeDetailPage /></PublicLayout>} />
            </Route>

            {/* Admin routes */}
            <Route element={<AdminRoute />}>
              <Route path="admin" element={<AdminLayout />}>
                <Route index element={<AdminDashboard />} />
                <Route path="articles" element={<AdminArticlesPage />} />
                <Route path="livraisons" element={<AdminLivraisonsPage />} />
                <Route path="commandes" element={<AdminCommandesPage />} />
                <Route path="commandes/:id" element={<AdminCommandeDetailPage />} />
              </Route>
            </Route>

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </BouquetProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}
