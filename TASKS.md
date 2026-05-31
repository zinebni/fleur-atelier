# ✅ TASKS — Fleur & Atelier
> Suivi des tâches par phase. Coche chaque item au fur et à mesure avec Copilot.

---

## LÉGENDE
- `[ ]` À faire
- `[x]` Terminé
- `[~]` En cours
- `[!]` Bloqué / problème

---

## PHASE 0 — SETUP (Faire manuellement AVANT Copilot)

### Backend
- [ ] Créer le projet Spring Boot sur [start.spring.io](https://start.spring.io)
  - Group: `com.fleuratelier`
  - Artifact: `fleur-atelier-api`
  - Dépendances: Spring Web, Spring Data JPA, Spring Security, PostgreSQL Driver, Lombok, Validation
- [ ] Créer la base de données PostgreSQL : `fleur_atelier_db`
- [ ] Remplir `application.properties` depuis `.env.example`
- [ ] Vérifier que le projet compile : `./mvnw spring-boot:run`

### Frontend
- [ ] Créer le projet React avec Vite : `npm create vite@latest fleur-atelier-ui -- --template react`
- [ ] Installer les dépendances : `npm install axios react-router-dom`
- [ ] Installer les fonts Google : Playfair Display + Inter (dans index.html)
- [ ] Créer la structure de dossiers src/ (voir PROJECT_CONTEXT.md section 8)
- [ ] Créer `src/styles/variables.css` avec la palette complète
- [ ] Vérifier que Vite démarre : `npm run dev`

---

## PHASE 1 — MODÈLE DE DONNÉES BACKEND

### 1.1 Enums
- [ ] `StatutCommande.java` : PANIER, EN_ATTENTE, PAYEE, EXPEDIEE, ANNULEE
- [ ] `RoleUtilisateur.java` : CLIENT, ADMIN

### 1.2 Entités JPA
- [ ] `Categorie.java` — id, nom, description, emoji
- [ ] `Article.java` — id, nom, description, prix, stock, imageUrl, disponible, categorie (ManyToOne)
- [ ] `Utilisateur.java` — id, email, motDePasse, prenom, nom, role, panier (OneToOne)
- [ ] `Panier.java` — id, utilisateur (OneToOne), lignesPanier (OneToMany), dateCreation
- [ ] `LignePanier.java` — id, panier (ManyToOne), article (ManyToOne), quantite
- [ ] `Commande.java` — id, utilisateur, lignesCommande, montantTotal, statut, modePaiement, messagePersonnalise, dateCommande, datePaiement
- [ ] `LigneCommande.java` — id, commande, article, quantite, prixUnitaire (snapshot du prix)

### 1.3 Repositories
- [ ] `CategorieRepository.java`
- [ ] `ArticleRepository.java` — ajouter findByCategorieId(), findByNomContainingIgnoreCase(), findByDisponibleTrue()
- [ ] `UtilisateurRepository.java` — ajouter findByEmail()
- [ ] `PanierRepository.java` — ajouter findByUtilisateurId()
- [ ] `LignePanierRepository.java`
- [ ] `CommandeRepository.java` — ajouter findByUtilisateurId()

### 1.4 Validation
- [ ] Tester que Hibernate crée les tables correctement au démarrage
- [ ] Vérifier les relations en base (clés étrangères)

---

## PHASE 2 — SÉCURITÉ JWT

### 2.1 Dépendances
- [ ] Ajouter `jjwt-api`, `jjwt-impl`, `jjwt-jackson` dans pom.xml

### 2.2 Implémentation
- [ ] `JwtService.java` — generateToken(), validateToken(), extractEmail()
- [ ] `UserDetailsServiceImpl.java` — implémenter UserDetailsService avec UtilisateurRepository
- [ ] `JwtAuthFilter.java` — OncePerRequestFilter, lire header Authorization
- [ ] `SecurityConfig.java` — configurer les routes publiques vs protégées, désactiver CSRF, ajouter le filtre JWT
- [ ] `CorsConfig.java` — autoriser http://localhost:5173

### 2.3 Auth Controller
- [ ] DTOs : `RegisterRequest`, `LoginRequest`, `AuthResponse` (token + user info)
- [ ] `AuthService.java` — register(), login()
- [ ] `AuthController.java` — POST /api/auth/register, POST /api/auth/login

### 2.4 Validation
- [ ] Tester register avec Postman/REST client
- [ ] Tester login → recevoir un token JWT
- [ ] Tester un endpoint protégé sans token → 401
- [ ] Tester un endpoint protégé avec token → 200

---

## PHASE 3 — CATALOGUE (Backend)

### 3.1 DTOs
- [ ] `ArticleResponse.java` — tous les champs + nomCategorie
- [ ] `CategorieResponse.java`

### 3.2 Services
- [ ] `ArticleService.java`
  - [ ] getAllArticles(Pageable) → Page\<ArticleResponse\>
  - [ ] getArticleById(Long) → ArticleResponse
  - [ ] searchArticles(String query) → List\<ArticleResponse\>
  - [ ] getArticlesByCategorie(Long categorieId) → List\<ArticleResponse\>
- [ ] `CategorieService.java`
  - [ ] getAllCategories() → List\<CategorieResponse\>

### 3.3 Controllers
- [ ] `ArticleController.java` — GET /api/articles, /api/articles/{id}, /api/articles/search, /api/articles/categorie/{id}
- [ ] `CategorieController.java` — GET /api/categories

### 3.4 Données de test
- [ ] `data.sql` ou `DataInitializer.java` (@Component + CommandLineRunner) avec les catégories et articles du PROJECT_CONTEXT.md section 9

### 3.5 Validation
- [ ] Tester GET /api/articles → liste paginée
- [ ] Tester GET /api/articles?q=rose → recherche
- [ ] Tester GET /api/articles/categorie/1 → filtre

---

## PHASE 4 — PANIER (Backend)

### 4.1 DTOs
- [ ] `LignePanierResponse.java` — id, article, quantite, sousTotal
- [ ] `PanierResponse.java` — id, lignes, total général, nombre d'articles
- [ ] `AjouterAuPanierRequest.java` — articleId, quantite

### 4.2 Service
- [ ] `PanierService.java`
  - [ ] getPanierByUser(Long userId) → PanierResponse
  - [ ] ajouterArticle(Long userId, AjouterAuPanierRequest) → PanierResponse
    - Vérifier stock disponible
    - Si l'article est déjà dans le panier → incrémenter la quantité
    - Si nouveau → créer une nouvelle LignePanier
  - [ ] modifierQuantite(Long lignePanierId, Integer nouvelleQuantite) → PanierResponse
  - [ ] supprimerLigne(Long lignePanierId)
  - [ ] viderPanier(Long userId)

### 4.3 Controller
- [ ] `PanierController.java` — tous les endpoints du panier (authentifiés)

### 4.4 Validation
- [ ] Ajouter un article → vérifier qu'il apparaît dans le panier
- [ ] Ajouter le même article → quantité incrémentée
- [ ] Modifier quantité → total mis à jour
- [ ] Tenter d'ajouter avec stock = 0 → erreur 400

---

## PHASE 5 — COMMANDE & PAIEMENT (Backend — Critique)

### 5.1 Strategy Pattern
- [ ] `PaymentStrategy.java` — interface avec `PaymentResult processPayment(Commande commande)`
- [ ] `PaymentResult.java` — record/classe avec succès (boolean), message, transactionId
- [ ] `CarteBancairePayment.java` — simule un paiement carte (succès 90% du temps pour les tests)
- [ ] `PaypalPayment.java` — simule un paiement PayPal
- [ ] `PaymentContext.java` — Map<String, PaymentStrategy> injectée par Spring

### 5.2 DTOs
- [ ] `CreerCommandeRequest.java` — messagePersonnalise (optionnel)
- [ ] `PaiementRequest.java` — commandeId, modePaiement ("CARTE" ou "PAYPAL")
- [ ] `CommandeResponse.java` — tous les détails de la commande
- [ ] `LigneCommandeResponse.java`

### 5.3 Service Commande (TRANSACTIONNEL)
- [ ] `CommandeService.java`
  - [ ] `@Transactional` creerCommande(Long userId, CreerCommandeRequest)
    - Vérifier que le panier n'est pas vide
    - Vérifier le stock de chaque article
    - Créer la Commande avec statut EN_ATTENTE
    - Créer les LigneCommande (avec snapshot du prix)
    - Calculer montantTotal
    - Vider le panier
  - [ ] `@Transactional` payerCommande(Long commandeId, String modePaiement)
    - Vérifier statut = EN_ATTENTE
    - Re-vérifier le stock (sécurité)
    - Appeler PaymentContext.pay()
    - Si succès : décrémenter stock, statut → PAYEE, datePaiement = now()
    - Si échec : lancer exception → rollback automatique
  - [ ] getCommandesByUser(Long userId)
  - [ ] getCommandeById(Long commandeId, Long userId)

### 5.4 Controllers
- [ ] `CommandeController.java`
- [ ] `PaiementController.java`

### 5.5 Gestion des exceptions
- [ ] `StockInsuffisantException.java`
- [ ] `PaiementEchecException.java`
- [ ] `ResourceNotFoundException.java`
- [ ] `GlobalExceptionHandler.java` (@ControllerAdvice) — retourner des erreurs JSON propres

### 5.6 Validation (très important)
- [ ] Flux complet : login → ajouter articles → créer commande → payer avec CARTE → vérifier stock décrémenté
- [ ] Tester rollback : simuler un échec de paiement → vérifier que le stock N'A PAS changé
- [ ] Tester double paiement → erreur 400

---

## PHASE 6 — ADMIN (Backend)

- [ ] `AdminArticleController.java` — CRUD articles (role ADMIN requis)
- [ ] `AdminCommandeController.java` — voir toutes commandes, changer statut
- [ ] Vérifier que les routes /api/admin/** refusent les CLIENT → 403

---

## PHASE 7 — FRONTEND REACT

### 7.1 Infrastructure
- [ ] `axiosInstance.js` — baseURL depuis .env, intercepteur requête (ajouter JWT), intercepteur réponse (401 → logout)
- [ ] `authApi.js` — login(), register()
- [ ] `articlesApi.js` — getAll(), getById(), search(), getByCategorie()
- [ ] `panierApi.js` — getPanier(), ajouter(), modifier(), supprimer(), vider()
- [ ] `commandesApi.js` — creer(), getAll(), getById(), payer()

### 7.2 Contexts
- [ ] `AuthContext.jsx` — user, token, isAuthenticated, login(), logout(), register()
- [ ] `PanierContext.jsx` — panier, nombreArticles, total, addItem(), removeItem(), updateQty(), clear()

### 7.3 Composants UI (thème Fleur & Atelier)
- [ ] `Button.jsx` — variants: primary (rose), secondary (blanc bordé), ghost, danger
- [ ] `Input.jsx` — label, placeholder, erreur en rouge
- [ ] `Card.jsx` — shadow, border-radius 12px
- [ ] `Badge.jsx` — statuts commande avec couleurs (EN_ATTENTE=amber, PAYEE=vert, EXPEDIEE=bleu)
- [ ] `Spinner.jsx` — loading state animé
- [ ] `Modal.jsx` — confirmation, overlay

### 7.4 Layout
- [ ] `Navbar.jsx` — logo "Fleur & Atelier", liens, icône panier avec compteur, login/logout
- [ ] `Footer.jsx` — simple, élégant
- [ ] `Layout.jsx` — wrapper Navbar + children + Footer

### 7.5 Pages
- [ ] `HomePage.jsx`
  - Hero section : titre poétique + CTA "Créer mon bouquet"
  - Section "Nos fleurs du moment" : 4 articles featured
  - Section "Comment ça marche" : 3 étapes (Choisir → Composer → Commander)

- [ ] `CataloguePage.jsx`
  - Sidebar filtre catégories (avec emojis)
  - Barre de recherche
  - Grille 3 colonnes d'ArticleCard
  - Pagination

- [ ] `ArticleDetailPage.jsx`
  - Grande image (placeholder si pas d'URL)
  - Nom, catégorie, prix par tige
  - Description
  - Sélecteur de quantité (1-20)
  - Bouton "Ajouter à ma composition"
  - Badge stock restant

- [ ] `PanierPage.jsx`
  - Titre : "Ma composition en cours 🌸"
  - Liste des LignePanier avec image, nom, prix unitaire, quantité modifiable, sous-total, bouton supprimer
  - Résumé : nombre de tiges, montant total
  - Input "Message personnalisé" (pour la carte cadeau)
  - Bouton "Commander ce bouquet"
  - Lien retour catalogue

- [ ] `CheckoutPage.jsx`
  - Récapitulatif de la commande (read-only)
  - Choix du mode de paiement : carte bancaire ou PayPal (radio buttons stylisés avec icônes)
  - Bouton "Confirmer et payer"
  - Loading state pendant le paiement
  - Redirect vers confirmation si succès

- [ ] `CommandesPage.jsx`
  - Liste des commandes avec date, montant, statut (badge coloré)
  - Détail dépliable de chaque commande (articles commandés)

- [ ] `LoginPage.jsx` — formulaire épuré, lien vers register
- [ ] `RegisterPage.jsx` — prénom, nom, email, mot de passe

### 7.6 Routes (App.jsx)
```
/                   → HomePage (public)
/catalogue          → CataloguePage (public)
/articles/:id       → ArticleDetailPage (public)
/panier             → PanierPage (protégé)
/checkout           → CheckoutPage (protégé)
/commandes          → CommandesPage (protégé)
/login              → LoginPage (redirect si déjà connecté)
/register           → RegisterPage (redirect si déjà connecté)
```

---

## PHASE 8 — INTÉGRATION & TESTS FINAUX

- [ ] Tester le flux complet end-to-end : register → browse → ajouter → panier → checkout → paiement → confirmation
- [ ] Vérifier CORS : pas d'erreur dans la console navigateur
- [ ] Vérifier que les tokens JWT expirent et que le logout fonctionne
- [ ] Tester sur mobile (responsive)
- [ ] Vérifier les états de chargement (spinners)
- [ ] Vérifier les messages d'erreur (stock insuffisant, paiement échoué)
- [ ] Nettoyer les console.log

---

## PHASE 9 — BONUS (si le temps le permet)

- [ ] Upload d'image pour les articles (admin)
- [ ] Filtres par fourchette de prix
- [ ] Suggestions de bouquets prédéfinis (compositions populaires)
- [ ] Page de confirmation de commande avec animation 🎉
- [ ] Mode admin complet dans React
- [ ] Tests unitaires Spring Boot (Mockito)
