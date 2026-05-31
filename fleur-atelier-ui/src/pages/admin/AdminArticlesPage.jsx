import { useEffect, useRef, useState } from 'react';
import { getAllArticles, createArticle, updateArticle, deleteArticle } from '../../api/articles';
import { getCategories } from '../../api/categories';
import client from '../../api/client';
import Modal from '../../components/common/Modal';
import Loader from '../../components/common/Loader';
import Toast from '../../components/common/Toast';
import { useToast } from '../../hooks/useToast';
import { formatPrice } from '../../utils/format';

const EMPTY = { nom: '', description: '', prix: '', stock: '', disponible: true, imageUrl: '', categorieId: '' };
const FALLBACK = 'https://images.unsplash.com/photo-1490750967868-88df5691cc5a?w=80';

export default function AdminArticlesPage() {
  const [articles, setArticles] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [saving, setSaving] = useState(false);
  const [deleteId, setDeleteId] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [preview, setPreview] = useState('');
  const fileInputRef = useRef(null);
  const { toasts, addToast, removeToast } = useToast();

  const load = () => {
    setLoading(true);
    Promise.all([getAllArticles(), getCategories()])
      .then(([a, c]) => { setArticles(a.data); setCategories(c.data); })
      .finally(() => setLoading(false));
  };

  useEffect(() => { load(); }, []);

  const openCreate = () => {
    setEditing(null); setForm(EMPTY); setPreview(''); setModalOpen(true);
  };
  const openEdit = (a) => {
    setEditing(a);
    setForm({
      nom: a.nom, description: a.description || '', prix: a.prix,
      stock: a.stock, disponible: a.disponible,
      imageUrl: a.imageUrl || '', categorieId: a.categorieId || ''
    });
    setPreview(a.imageUrl || '');
    setModalOpen(true);
  };

  const onField = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((f) => ({ ...f, [name]: type === 'checkbox' ? checked : value }));
    if (name === 'imageUrl') setPreview(value);
  };

  // Handle file selection → upload immediately → update form.imageUrl
  const onFileChange = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    // Show local preview while uploading
    const localUrl = URL.createObjectURL(file);
    setPreview(localUrl);

    const data = new FormData();
    data.append('file', file);
    setUploading(true);
    try {
      const res = await client.post('/upload/image', data, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      const serverUrl = res.data.url;
      setForm((f) => ({ ...f, imageUrl: serverUrl }));
      setPreview(serverUrl);
      URL.revokeObjectURL(localUrl);
      addToast('Image téléchargée avec succès.', 'success');
    } catch (err) {
      setPreview(form.imageUrl); // revert to old preview
      addToast(err.response?.data?.message || 'Erreur lors de l\'upload.', 'error');
    } finally {
      setUploading(false);
      // reset so same file can be re-selected
      if (fileInputRef.current) fileInputRef.current.value = '';
    }
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    const payload = {
      ...form,
      prix: parseFloat(form.prix),
      stock: parseInt(form.stock),
      categorieId: parseInt(form.categorieId),
    };
    try {
      if (editing) { await updateArticle(editing.id, payload); addToast('Article mis à jour.', 'success'); }
      else { await createArticle(payload); addToast('Article créé.', 'success'); }
      setModalOpen(false);
      load();
    } catch (err) {
      addToast(err.response?.data?.message || 'Erreur lors de la sauvegarde.', 'error');
    } finally { setSaving(false); }
  };

  const onDelete = async () => {
    try {
      await deleteArticle(deleteId);
      addToast('Article supprimé.', 'success');
      setDeleteId(null);
      load();
    } catch { addToast('Erreur lors de la suppression.', 'error'); }
  };

  const filtered = articles.filter(a => a.nom.toLowerCase().includes(search.toLowerCase()));

  return (
    <div className="admin-page">
      <Toast toasts={toasts} onRemove={removeToast} />

      <div className="admin-page-header">
        <h1 className="admin-page-title">Articles ({articles.length})</h1>
        <button className="btn btn--gold btn--sm" onClick={openCreate}>+ Nouvel article</button>
      </div>

      <input className="search-input" placeholder="Rechercher…" value={search}
        onChange={e => setSearch(e.target.value)} style={{ marginBottom: '1.5rem', maxWidth: 360 }} />

      {loading ? <div className="flex-center" style={{ minHeight: 200 }}><Loader size={40} /></div> : (
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead><tr><th>Image</th><th>Nom</th><th>Catégorie</th><th>Prix</th><th>Stock</th><th>Statut</th><th>Actions</th></tr></thead>
            <tbody>
              {filtered.map((a) => (
                <tr key={a.id}>
                  <td><img src={a.imageUrl || FALLBACK} alt={a.nom} className="admin-article-thumb" onError={(e) => { e.target.src = FALLBACK; }} /></td>
                  <td className="admin-article-name">{a.nom}</td>
                  <td>{a.categorieNom}</td>
                  <td>{formatPrice(a.prix)}</td>
                  <td>{a.stock}</td>
                  <td><span className={`badge ${a.disponible ? 'badge--success' : 'badge--error'}`}>{a.disponible ? 'Dispo' : 'Indispo'}</span></td>
                  <td className="admin-actions">
                    <button className="btn btn--ghost btn--xs" onClick={() => openEdit(a)}>Éditer</button>
                    <button className="btn btn--danger btn--xs" onClick={() => setDeleteId(a.id)}>Suppr.</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Create / Edit Modal */}
      <Modal open={modalOpen} onClose={() => setModalOpen(false)} title={editing ? 'Modifier l\'article' : 'Nouvel article'}>
        <form className="admin-form" onSubmit={onSubmit}>
          <div className="form-group">
            <label className="form-label">Nom *</label>
            <input name="nom" className="form-input" required value={form.nom} onChange={onField} />
          </div>
          <div className="form-group">
            <label className="form-label">Description</label>
            <textarea name="description" className="form-input form-textarea" value={form.description} onChange={onField} />
          </div>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Prix (MAD) *</label>
              <input name="prix" type="number" step="0.01" min="0.01" className="form-input" required value={form.prix} onChange={onField} />
            </div>
            <div className="form-group">
              <label className="form-label">Stock *</label>
              <input name="stock" type="number" min="0" className="form-input" required value={form.stock} onChange={onField} />
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Catégorie *</label>
            <select name="categorieId" className="form-input" required value={form.categorieId} onChange={onField}>
              <option value="">— Choisir —</option>
              {categories.map(c => <option key={c.id} value={c.id}>{c.nom}</option>)}
            </select>
          </div>

          {/* ── Image section ────────────────────────────────── */}
          <div className="form-group">
            <label className="form-label">Image du produit</label>

            {/* Preview */}
            <div className="image-upload-zone" onClick={() => fileInputRef.current?.click()}>
              {preview ? (
                <img src={preview} alt="Aperçu" className="image-upload-preview" onError={() => setPreview('')} />
              ) : (
                <div className="image-upload-placeholder">
                  <span className="image-upload-icon">🌸</span>
                  <span>Cliquer pour choisir une image</span>
                  <span className="image-upload-hint">JPEG, PNG ou WebP · max 5 Mo</span>
                </div>
              )}
              {uploading && <div className="image-upload-overlay"><Loader size={32} /></div>}
            </div>

            <input
              ref={fileInputRef}
              type="file"
              accept="image/jpeg,image/png,image/webp,image/gif"
              style={{ display: 'none' }}
              onChange={onFileChange}
            />

            <button
              type="button"
              className="btn btn--ghost btn--sm"
              style={{ marginTop: '0.5rem', width: '100%' }}
              onClick={() => fileInputRef.current?.click()}
              disabled={uploading}
            >
              {uploading ? 'Téléchargement…' : (preview ? '🔄 Changer l\'image' : '📁 Choisir un fichier')}
            </button>

            {/* Fallback URL field */}
            <details style={{ marginTop: '0.75rem' }}>
              <summary className="image-upload-hint" style={{ cursor: 'pointer' }}>Ou saisir une URL manuellement</summary>
              <input
                name="imageUrl"
                className="form-input"
                style={{ marginTop: '0.5rem' }}
                value={form.imageUrl}
                onChange={onField}
                placeholder="https://…"
              />
            </details>
          </div>

          <label className="form-check">
            <input type="checkbox" name="disponible" checked={form.disponible} onChange={onField} />
            <span>Disponible à la vente</span>
          </label>
          <div className="modal-actions">
            <button type="button" className="btn btn--ghost" onClick={() => setModalOpen(false)}>Annuler</button>
            <button type="submit" className="btn btn--gold" disabled={saving || uploading}>
              {saving ? 'Sauvegarde…' : (editing ? 'Mettre à jour' : 'Créer')}
            </button>
          </div>
        </form>
      </Modal>

      {/* Delete confirm */}
      <Modal open={!!deleteId} onClose={() => setDeleteId(null)} title="Confirmer la suppression">
        <p style={{ color: 'var(--text-2)', marginBottom: '1.5rem' }}>Cette action est irréversible. L'article sera définitivement supprimé.</p>
        <div className="modal-actions">
          <button className="btn btn--ghost" onClick={() => setDeleteId(null)}>Annuler</button>
          <button className="btn btn--danger" onClick={onDelete}>Supprimer</button>
        </div>
      </Modal>
    </div>
  );
}
