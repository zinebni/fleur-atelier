# 🤖 COPILOT RULES — Fleur & Atelier
> Instructions pour GitHub Copilot. À lire avant chaque session de travail.
> Colle ce fichier dans tes prompts Copilot Chat quand tu commences une nouvelle session.

---

## 🎯 IDENTITÉ DU PROJET

Tu travailles sur **Fleur & Atelier**, une boutique e-commerce florale avec composition personnalisée de bouquets.

- **Backend** : Spring Boot 3, PostgreSQL, Hibernate/JPA, Spring Security + JWT
- **Frontend** : React 18, Vite, Axios, React Router v6, Context API
- **Architecture** : REST API + SPA découplés
- **Pattern clé** : Strategy Pattern pour le système de paiement

Lis `PROJECT_CONTEXT.md` pour l'architecture complète et `TASKS.md` pour les tâches restantes.

---

## ⚙️ RÈGLES TECHNIQUES GÉNÉRALES

### Java / Spring Boot
- Utilise **Java 17+** (records, sealed classes si approprié)
- **Lombok obligatoire** : @Data, @Builder, @RequiredArgsConstructor, @Slf4j
- **Toutes les injections de dépendances via constructeur** (pas @Autowired sur les champs)
- Utilise **@RestControllerAdvice** pour la gestion globale des exceptions
- Toutes les réponses API retournent du JSON avec une structure cohérente
- Les controllers ne contiennent **aucune logique métier** — tout va dans les Services
- Les repositories ne contiennent **aucune logique** — juste des requêtes JPA

### Nommage Java
```
Classes     → PascalCase          (ArticleService, PanierController)
Méthodes    → camelCase           (getArticleById, ajouterAuPanier)
Variables   → camelCase           (montantTotal, dateCommande)
Constants   → UPPER_SNAKE_CASE    (JWT_SECRET, TOKEN_EXPIRATION)
Packages    → lowercase           (com.fleuratelier.service)
Tables SQL  → snake_case          (ligne_panier, date_commande)
```

### DTOs — Règle obligatoire
- **Ne jamais exposer les entités JPA directement** dans les réponses API
- Toujours créer un DTO Response correspondant
- Les entités JPA restent dans la couche service/repository — jamais dans les controllers

### Transactions
- **@Transactional uniquement dans la couche Service**, jamais dans les controllers
- Les opérations de paiement et de création de commande sont TOUJOURS @Transactional
- En cas d'exception dans une méthode @Transactional → rollback automatique — ne jamais gérer manuellement

### Sécurité
- Toutes les routes sauf `/api/auth/**` nécessitent un JWT valide
- Les routes `/api/admin/**` nécessitent le rôle ADMIN
- Ne jamais logger les mots de passe ou les tokens JWT
- Mots de passe encodés avec BCryptPasswordEncoder

---

## 🌿 RÈGLES MÉTIER — À RESPECTER ABSOLUMENT

```
1. STOCK
   → Vérifier le stock avant d'ajouter au panier
   → Ne décrémenter le stock QU'APRÈS un paiement confirmé
   → Si stock = 0, l'article est automatiquement marqué disponible=false

2. TRANSACTIONS
   → creerCommande() et payerCommande() doivent être @Transactional
   → Si le paiement échoue → exception → rollback → stock intact
   → Snapshot du prix dans LigneCommande (prixUnitaire = prix au moment de la commande)

3. PANIER
   → Un seul panier par utilisateur (OneToOne)
   → Vider le panier après création de commande réussie
   → Ajouter un article déjà présent → incrémenter la quantité existante

4. COMMANDE - STATUTS
   → Seuls les statuts valides : EN_ATTENTE → PAYEE → EXPEDIEE (ou ANNULEE)
   → Ne jamais sauter un statut
   → Seul un ADMIN peut passer de PAYEE à EXPEDIEE
5. log :
   → Logger toutes les opérations critiques (paiement, commande)
   → Utiliser @Slf4j
```

---

## 💻 RÈGLES FRONTEND REACT

### Structure
- **Un fichier = un composant** nommé avec PascalCase
- Les appels API sont **exclusivement dans le dossier api/**, jamais dans les composants
- La logique de state est dans les **Context**, pas dans les pages
- Les pages sont des **assembleurs de composants**, pas des endroits pour la logique

### Style
- Utilise les **variables CSS** de `src/styles/variables.css` — jamais de couleurs hardcodées
- **CSS Modules** pour les styles de composants (ArticleCard.module.css)
- Thème : rose poudré (#D4829A), vert sauge (#7A9E7E), beige (#C4A882)
- Font heading : Playfair Display (titres poétiques)
- Font body : Inter (texte courant)

### Axios
- L'instance Axios dans `axiosInstance.js` gère automatiquement le JWT dans toutes les requêtes
- **Ne jamais** ajouter manuellement le header Authorization dans les appels API individuels
- Gérer les erreurs API avec try/catch dans les Context ou les fonctions de service

### UX obligatoire
- Tout appel API doit avoir un état de **loading** (Spinner)
- Tout appel API qui peut échouer doit afficher un **message d'erreur** lisible
- Les boutons doivent être **désactivés pendant le loading** pour éviter les doubles clics
- Les redirections après action (ajout panier, commande, paiement) sont immédiates

---

## 🚫 CE QUE TU NE DOIS JAMAIS FAIRE

```
❌ Mettre de la logique dans les controllers Spring Boot
❌ Exposer les entités JPA dans les réponses API (toujours utiliser des DTOs)
❌ Décrémenter le stock avant la confirmation du paiement
❌ Oublier @Transactional sur creerCommande() et payerCommande()
❌ Hardcoder des couleurs dans les composants React (utiliser les variables CSS)
❌ Mettre des appels axios directement dans les composants React (passer par api/)
❌ Créer des méthodes dans les repositories avec de la logique métier
❌ Utiliser @Autowired sur les champs (utiliser l'injection par constructeur)
❌ Retourner des erreurs HTTP 500 sans message explicatif
❌ Laisser des console.log() dans le code final
```

---

## ✅ FORMAT DE RÉPONSE ATTENDU DE COPILOT

Quand tu génères du code :

1. **Génère un fichier complet** — pas de `// ... reste du code`
2. **Inclus les imports** Java ou React nécessaires en haut
3. **Indique le chemin du fichier** en commentaire sur la première ligne
4. **Respecte la structure de packages** définie dans PROJECT_CONTEXT.md
5. **Si une dépendance manque** dans pom.xml ou package.json, indique-le clairement avant le code

---

## 📋 PROMPT TYPE POUR DÉMARRER UNE SESSION COPILOT

Copie-colle ce texte dans Copilot Chat au début de chaque session :

```
Je travaille sur Fleur & Atelier, une boutique florale e-commerce.
Stack : Spring Boot 3 + PostgreSQL + React 18 + Vite.
Architecture définie dans PROJECT_CONTEXT.md, tâches dans TASKS.md.

Règles importantes :
- Java : Lombok, injection par constructeur, DTOs, @Transactional dans les services
- React : Context API, CSS variables, appels API dans /api/ uniquement
- Jamais de logique dans les controllers
- Jamais de stock décrémenté avant paiement confirmé

Tâche actuelle : [DÉCRIS LA TÂCHE PRÉCISE ICI]
Génère le fichier complet avec le chemin en commentaire.
```

---

## 🐛 PROMPT TYPE POUR DÉBUGGER AVEC COPILOT

```
J'ai cette erreur dans Fleur & Atelier :
[COLLE L'ERREUR COMPLÈTE AVEC LE STACK TRACE]

Contexte :
- Fichier concerné : [nom du fichier]
- Ce que je faisais : [action]
- Code actuel : [colle le code]

Identifie la cause exacte et propose une correction uniquement pour ce fichier.
Ne modifie pas les autres fichiers et n'ajoute pas de nouvelles dépendances.
```

---

## 🔄 PROMPT TYPE POUR L'UI REACT AVEC COPILOT

```
Génère le composant React [NomDuComposant] pour Fleur & Atelier (boutique florale).

Design : thème rose poudré/vert sauge, élégant et féminin.
Variables CSS disponibles dans variables.css (--color-primary: #D4829A, etc.)
Utilise CSS Modules pour les styles.

Fonctionnalités requises :
- [liste les fonctionnalités]

Props reçues :
- [liste les props]

Le composant appelle l'API via : [fichier api/xxx.js]
```
