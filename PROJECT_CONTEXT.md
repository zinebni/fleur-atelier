# 🌸 PROJECT CONTEXT — Fleur & Atelier 

> Full-stack E-Commerce Platform with Custom Bouquet Builder
> Spring Boot + React + Clean Architecture + Scalable Payment System

---

# 🧭 1. PROJECT VISION

**Fleur & Atelier** is a modern e-commerce platform that allows users to create **custom flower bouquets** and purchase them through a secure and scalable system.

### 🌺 Core Feature — Bouquet Builder

Users can:

* Browse flowers and accessories
* Add items into a **Bouquet (domain object)**
* Customize quantities
* Preview composition
* Convert it into an order and pay

👉 The **Bouquet is NOT just a cart**, it is a **business entity**.

---

# 🏗️ 2. ARCHITECTURE OVERVIEW

This project follows a **Clean Architecture (layered)** approach:

```
com.fleuratelier/
├── domain/                     # Pure business logic (NO Spring)
│   ├── model/
│   ├── service/
│   └── enums/
│
├── application/                # Use cases (orchestrates domain)
│   ├── dto/
│   └── usecase/
│
├── infrastructure/             # Technical layer
│   ├── persistence/
│   ├── security/
│   └── payment/
│
├── web/                        # Controllers + REST API
│
└── FleurAtelierApplication.java
```

---

# 🧠 3. DOMAIN MODEL

## 🌺 Bouquet (NEW CORE ENTITY)

```java
class Bouquet {
    Long id;
    List<LigneBouquet> lignes;
    BigDecimal prixTotal;
}
```

```java
class LigneBouquet {
    Article article;
    int quantite;
}
```

---

## 📦 Article

* id
* nom
* description
* prix
* stock
* disponible
* categorie



---

## 🧾 Commande

* id
* utilisateur
* lignesCommande (SNAPSHOT)
* montantTotal
* statut
* modePaiement
* dateCommande
* datePaiement

---

## ❗ IMPORTANT — Snapshot Rule

```java
class LigneCommande {
    String nomArticle;
    BigDecimal prixUnitaire;
    int quantite;
}
```

👉 Orders must NOT depend on Article after creation.

---

# 💳 4. PAYMENT SYSTEM (STRATEGY PATTERN)

## Interface

```java
public interface PaymentStrategy {
    PaymentResult process(Commande commande);
}
```

## Result Object

```java
public class PaymentResult {
    boolean success;
    String transactionId;
    String message;
}
```

## Context

```java
@Service
public class PaymentContext {

    private final Map<String, PaymentStrategy> strategies;

    public PaymentResult pay(String mode, Commande commande) {
        PaymentStrategy strategy = strategies.get(mode.toLowerCase());

        if (strategy == null) {
            throw new IllegalArgumentException("Invalid payment mode");
        }

        return strategy.process(commande);
    }
}
```

👉 Add new payment = new class ONLY (Open/Closed Principle)

---

# 🔐 5. SECURITY

* JWT Authentication
* Refresh Token (recommended)
* Role-based access

```java
@PreAuthorize("hasRole('ADMIN')")
```

---

# ⚙️ 6. TRANSACTION MANAGEMENT

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

👉 Everything MUST be atomic.

---

# 📦 7. STOCK MANAGEMENT

## Optimistic Locking

```java
@Version
private Integer version;
```

👉 Prevents concurrent stock corruption.

---

# 📏 8. VALIDATION

Use Bean Validation:

```java
@NotNull
@Min(1)
private Integer quantite;
```

Controllers:

```java
public ResponseEntity<?> method(@Valid @RequestBody Request req)
```

---

# 🌐 9. REST API

## Public

```
GET /api/articles
GET /api/articles/{id}
GET /api/articles/search?q=
GET /api/categories
```

## Cart

```
GET    /api/panier
POST   /api/panier
PUT    /api/panier/{id}
DELETE /api/panier/{id}
```

## Orders

```
POST /api/commandes
GET  /api/commandes
```

## Payment

```
POST /api/paiements
```

---

# 🎨 10. FRONTEND (React)

* React 18 + Vite
* Context API (Auth + Cart)
* Axios with JWT interceptor
* Protected routes

---

# 🧪 11. TESTING

Minimum:

* Service unit tests
* Payment logic tests

---


# 📊 13. BUSINESS RULES

```
- Payment is REQUIRED to validate order
- Stock updated ONLY after successful payment
- Order becomes immutable after payment
- One active cart per user
- Bouquet is a business object
```



# 🎯 FINAL GOAL

Build a:
✔ Clean
✔ Scalable
✔ Realistic
✔ Production-ready architecture

---


