# 🌸 Fleur & Atelier

> Plateforme E-Commerce Full Stack dédiée à la création et à la vente de bouquets personnalisés.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)
![React](https://img.shields.io/badge/React-18-blue)
![JWT](https://img.shields.io/badge/Security-JWT-red)
![Architecture](https://img.shields.io/badge/Architecture-Clean_Architecture-success)

---

# 📖 Présentation

**Fleur & Atelier** est une plateforme e-commerce moderne permettant aux utilisateurs de composer leurs propres bouquets floraux à partir d'un catalogue de fleurs et d'accessoires.

L'objectif du projet est de mettre en œuvre une architecture robuste, maintenable et évolutive en appliquant les bonnes pratiques du développement Full Stack moderne.

Contrairement à un simple panier d'achat, le **Bouquet** représente ici une véritable entité métier possédant ses propres règles de gestion.

---

# ✨ Fonctionnalités

## 🌺 Création de bouquets personnalisés

* Parcourir le catalogue de fleurs et accessoires
* Ajouter des articles à un bouquet
* Modifier les quantités
* Calcul automatique du prix total
* Prévisualiser la composition du bouquet

## 🛒 Gestion des commandes

* Conversion d'un bouquet en commande
* Historique des commandes
* Suivi du statut des commandes
* Conservation des données grâce au mécanisme de Snapshot

## 💳 Paiement sécurisé

* Plusieurs méthodes de paiement
* Architecture extensible basée sur le Pattern Strategy
* Gestion transactionnelle complète

## 🔐 Authentification et sécurité

* JWT Authentication
* Refresh Token
* Gestion des rôles
* Routes sécurisées

## 📦 Gestion du stock

* Vérification du stock avant paiement
* Mise à jour automatique après paiement
* Protection contre les accès concurrents

---

# 🏛️ Architecture

Le projet suit les principes de la **Clean Architecture** afin de garantir une séparation claire des responsabilités.

```text
com.fleuratelier/
│
├── domain/
│   ├── model/
│   ├── service/
│   └── enums/
│
├── application/
│   ├── dto/
│   └── usecase/
│
├── infrastructure/
│   ├── persistence/
│   ├── security/
│   └── payment/
│
├── web/
│
└── FleurAtelierApplication.java
```

## Description des couches

| Couche         | Responsabilité                      |
| -------------- | ----------------------------------- |
| Domain         | Logique métier pure                 |
| Application    | Cas d'utilisation et orchestration  |
| Infrastructure | Base de données, sécurité, paiement |
| Web            | API REST et contrôleurs             |

---

# 🧠 Modèle Métier

## Bouquet

```java
class Bouquet {
    Long id;
    List<LigneBouquet> lignes;
    BigDecimal prixTotal;
}
```

## LigneBouquet

```java
class LigneBouquet {
    Article article;
    int quantite;
}
```

## Article

```java
class Article {
    Long id;
    String nom;
    String description;
    BigDecimal prix;
    Integer stock;
    Boolean disponible;
    Categorie categorie;
}
```

## Commande

```java
class Commande {
    Long id;
    Utilisateur utilisateur;
    List<LigneCommande> lignesCommande;
    BigDecimal montantTotal;
    StatutCommande statut;
    ModePaiement modePaiement;
    LocalDateTime dateCommande;
    LocalDateTime datePaiement;
}
```

---

# 📸 Snapshot des commandes

Lorsqu'une commande est validée, les informations des articles sont copiées dans la commande.

```java
class LigneCommande {
    String nomArticle;
    BigDecimal prixUnitaire;
    Integer quantite;
}
```

Cette approche garantit que les commandes restent cohérentes même si les articles sont modifiés ou supprimés ultérieurement.

---

# 💳 Système de paiement

Le paiement repose sur le **Pattern Strategy**.

## Interface

```java
public interface PaymentStrategy {
    PaymentResult process(Commande commande);
}
```

## Résultat du paiement

```java
public class PaymentResult {
    boolean success;
    String transactionId;
    String message;
}
```

## Contexte de paiement

```java
@Service
public class PaymentContext {

    private final Map<String, PaymentStrategy> strategies;

    public PaymentResult pay(String mode, Commande commande) {

        PaymentStrategy strategy = strategies.get(mode.toLowerCase());

        if(strategy == null){
            throw new IllegalArgumentException("Mode de paiement invalide");
        }

        return strategy.process(commande);
    }
}
```

### Avantages

* Respect du principe Open/Closed
* Facilité d'ajout de nouvelles méthodes de paiement
* Couplage faible

---

# 🔐 Sécurité

Le projet utilise :

* Spring Security
* JWT Authentication
* Refresh Tokens
* Contrôle d'accès par rôle

Exemple :

```java
@PreAuthorize("hasRole('ADMIN')")
```

---

# ⚙️ Gestion des transactions

Toutes les opérations critiques sont atomiques.

```java
@Transactional
public void payerCommande(Long id, String mode) {

    Commande cmd = commandeRepository.findById(id);

    stockService.verify(cmd);

    PaymentResult result = paymentContext.pay(mode, cmd);

    if (!result.success) {
        throw new RuntimeException("Payment failed");
    }

    stockService.decrement(cmd);

    cmd.setStatut(PAYEE);
}
```

---

# 📦 Gestion du stock

Pour éviter les conflits liés aux commandes simultanées :

```java
@Version
private Integer version;
```

Le projet utilise l'Optimistic Locking fourni par JPA.

---

# ✅ Validation

Validation des données avec Bean Validation.

```java
@NotNull
@Min(1)
private Integer quantite;
```

Validation automatique dans les contrôleurs :

```java
@PostMapping
public ResponseEntity<?> create(
        @Valid @RequestBody Request request) {
}
```

---

# 🌐 API REST

## Catalogue

```http
GET /api/articles
GET /api/articles/{id}
GET /api/articles/search?q=
GET /api/categories
```

## Bouquet

```http
GET    /api/bouquets
POST   /api/bouquets
PUT    /api/bouquets/{id}
DELETE /api/bouquets/{id}
```

## Commandes

```http
POST /api/commandes
GET  /api/commandes
GET  /api/commandes/{id}
```

## Paiements

```http
POST /api/paiements
```

---

# 🎨 Frontend

Technologies utilisées :

* React 18
* Vite
* Context API
* React Router
* Axios
* JWT Interceptor

Fonctionnalités :

* Authentification
* Gestion du bouquet
* Gestion des commandes
* Routes protégées
* Interface responsive

---

# 🧪 Tests

Tests prévus :

### Tests Unitaires

* Services métier
* Cas d'utilisation
* Validation métier

### Tests de Paiement

* Paiement réussi
* Paiement refusé
* Gestion des erreurs

### Tests d'Intégration

* API REST
* Sécurité
* Persistance

---

# 📋 Règles Métier

* Le paiement est obligatoire pour valider une commande.
* Le stock est décrémenté uniquement après un paiement réussi.
* Une commande payée devient immuable.
* Un utilisateur possède un seul bouquet actif.
* Le bouquet est une entité métier et non un simple panier.
* Toutes les opérations de paiement sont transactionnelles.

---

# 🚀 Stack Technique

## Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* PostgreSQL
* JWT

## Frontend

* React 18
* Vite
* Axios
* React Router

## Outils

* Maven
* Git
* GitHub
* Docker
* Postman

---

# 🎯 Objectifs du projet

* Mettre en pratique la Clean Architecture
* Appliquer les principes SOLID
* Utiliser des Design Patterns
* Concevoir une API REST robuste
* Sécuriser une application avec JWT
* Gérer les transactions métier
* Développer une application Full Stack moderne

---

# 👨‍💻 Auteur

Projet réalisé dans le cadre d'un apprentissage avancé du développement Full Stack avec Spring Boot et React.

---

## 🌸 Fleur & Atelier

Créer des bouquets uniques grâce à une architecture élégante, robuste et évolutive.
