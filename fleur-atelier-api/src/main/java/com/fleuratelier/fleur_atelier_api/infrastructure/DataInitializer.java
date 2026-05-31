// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/DataInitializer.java
package com.fleuratelier.fleur_atelier_api.infrastructure;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fleuratelier.fleur_atelier_api.domain.enums.RoleUtilisateur;
import com.fleuratelier.fleur_atelier_api.domain.model.Article;
import com.fleuratelier.fleur_atelier_api.domain.model.Categorie;
import com.fleuratelier.fleur_atelier_api.domain.model.Utilisateur;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.ArticleRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.CategorieRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.UtilisateurRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Seeds the database with realistic categories and flower articles on startup.
 * Runs only if no categories are present (idempotent).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

        private final CategorieRepository categorieRepository;
        private final ArticleRepository articleRepository;
        private final UtilisateurRepository utilisateurRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        @Transactional
        public void run(String... args) {
                seedUsers();

                if (categorieRepository.count() > 0) {
                        log.info("DataInitializer: catalogue data already present, skipping seed.");
                        return;
                }

                log.info("DataInitializer: seeding categories and articles...");

                // ── Categories ─────────────────────────────────────────────────────
                Categorie roses = save(Categorie.builder()
                                .nom("Roses")
                                .description("Symboles d'amour et d'élégance, nos roses sont cultivées avec soin.")
                                .emoji("🌹")
                                .build());

                Categorie tulipes = save(Categorie.builder()
                                .nom("Tulipes")
                                .description("Fraîches et colorées, les tulipes apportent gaieté à tout bouquet.")
                                .emoji("🌷")
                                .build());

                Categorie orchidees = save(Categorie.builder()
                                .nom("Orchidées")
                                .description("Fleurs exotiques d'une beauté sophistiquée et durable.")
                                .emoji("🌸")
                                .build());

                Categorie pivoine = save(Categorie.builder()
                                .nom("Pivoines")
                                .description("Luxueuses et romantiques, parfaites pour les grandes occasions.")
                                .emoji("🌺")
                                .build());

                Categorie tournesols = save(Categorie.builder()
                                .nom("Tournesols")
                                .description("Fleurs exotiques d'une beauté sophistiquée et durable.")
                                .emoji("🌻")
                                .build());

                Categorie hortensias = save(Categorie.builder()
                                .nom("Hortensias")
                                .description("l'hortensia symbolise principalement la gratitude, la compréhension profonde et l'appréciation sincère. Avec leurs têtes généreuses, ils représentent également l'abondance, la grâce et la beauté.")
                                .emoji("🪷")
                                .build());

                Categorie lys = save(Categorie.builder()
                                .nom("Lys")
                                .description("Le lys en bouquet symbolise principalement la pureté, l'amour chaste, la majesté et la féminité. C'est une fleur incontournable pour les mariages, synonyme de félicité et de sincérité.")
                                .emoji("🍁")
                                .build());

                Categorie marguerites = save(Categorie.builder()
                                .nom("Marguerites")
                                .description("Fleurs exotiques d'une beauté sophistiquée et durable.")
                                .emoji("🌼")
                                .build());

                Categorie jonquilles = save(Categorie.builder()
                                .nom("Jonquilles")
                                .description("Fleurs exotiques d'une beauté sophistiquée et durable.")
                                .emoji("🏵️")
                                .build());

                Categorie freesias = save(Categorie.builder()
                                .nom("Freesias")
                                .description("Fleurs exotiques d'une beauté sophistiquée et durable.")
                                .emoji("🌺")
                                .build());

                Categorie delphiniums = save(Categorie.builder()
                                .nom("Delphiniums")
                                .description("Fleurs exotiques d'une beauté sophistiquée et durable.")
                                .emoji("🪻")
                                .build());

                Categorie gypsophile = save(Categorie.builder()
                                .nom("Gypsophile")
                                .description("Nuage de petites fleurs blanches, accompagnement idéal pour toutes fleurs.")
                                .emoji("☁️")
                                .build());

                Categorie eucalyptus = save(Categorie.builder()
                                .nom("Eucalyptus")
                                .description("Tige d'eucalyptus odorant, ajoute texture et parfum subtil.")
                                .emoji("🌿")
                                .build());

                Categorie muguet = save(Categorie.builder()
                                .nom("Muguet")
                                .description("Fleurs de mai, symbole de chance et de renouveau.")
                                .emoji("🍀")
                                .build());
                Categorie bouquet = save(Categorie.builder()
                                .nom("Bouquet")
                                .description("Bouquet de fleurs, prêt à offrir, composé par finesse et avec amour.")
                                .emoji("💐")
                                .build());
                Categorie accessoires = save(Categorie.builder()
                                .nom("Accessoires")
                                .description("Rubans, papiers cadeau et verdure pour sublimer votre bouquet.")
                                .emoji("🎀")
                                .build());

                // ── Articles ───────────────────────────────────────────────────────
                List<Article> articles = List.of(
                                // Roses
                                Article.builder().nom("Rose Rouge Éternelle").description(
                                                "Une rose rouge veloutée, symbole de passion. Hauteur 60 cm.")
                                                .prix(new BigDecimal("30.50")).stock(150).disponible(true)
                                                .imageUrl("http://localhost:8080/images/rose-rouge.jpg")
                                                .categorie(roses)
                                                .build(),
                                Article.builder().nom("Rose Blanche Innocence").description(
                                                "Délicate rose blanche, idéale pour mariages et cérémonies.")
                                                .prix(new BigDecimal("45.20")).stock(120).disponible(true)
                                                .imageUrl("http://localhost:8080/images/rose-blanche.jpg")
                                                .categorie(roses)
                                                .build(),
                                Article.builder().nom("Rose hot pink")
                                                .description("Rose rose pâle au parfum subtil, très appréciée.")
                                                .prix(new BigDecimal("55.00")).stock(100).disponible(true)
                                                .imageUrl("http://localhost:8080/images/rose_hote_pink.jpg")
                                                .categorie(roses)
                                                .build(),
                                Article.builder().nom("Rose Jaune Amitié")
                                                .description("Rose jaune soleil, message d'amitié sincère.")
                                                .prix(new BigDecimal("27.90")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/rose-jaune.jpg")
                                                .categorie(roses)
                                                .build(),
                                Article.builder().nom("Rose Bordeaux Velours")
                                                .description("Rose bordeaux profonde, classe et sophistication.")
                                                .prix(new BigDecimal("30.80")).stock(60).disponible(true)
                                                .imageUrl("http://localhost:8080/images/rose-bordeaux.jpg")
                                                .categorie(roses)
                                                .build(),
                                Article.builder().nom("Rose rose").description(
                                                "Rose rose feminin et delicate parfait pour bouquet romantique.")
                                                .prix(new BigDecimal("30.80")).stock(120).disponible(true)
                                                .imageUrl("http://localhost:8080/images/rose-rose.jpg")
                                                .categorie(roses)
                                                .build(),

                                // Tulipes
                                Article.builder().nom("Tulipe Rouge Vif")
                                                .description("Tulipe rouge éclatante, signe de déclaration d'amour.")
                                                .prix(new BigDecimal("52.20")).stock(200).disponible(true)
                                                .imageUrl("http://localhost:8080/images/tulipe-rouge.jpg")
                                                .categorie(tulipes)
                                                .build(),
                                Article.builder().nom("Tulipe Mauve Rêverie").description(
                                                "Tulipe mauve douce, idéale pour composer un bouquet printanier.")
                                                .prix(new BigDecimal("52.10")).stock(180).disponible(true)
                                                .imageUrl("http://localhost:8080/images/tulipe-mauve.jpg")
                                                .categorie(tulipes)
                                                .build(),
                                Article.builder().nom("Tulipe Blanche Pureté")
                                                .description("Tulipe blanche élégante, intemporelle et raffinée.")
                                                .prix(new BigDecimal("52.00")).stock(150).disponible(true)
                                                .imageUrl("http://localhost:8080/images/tulipe-blanche.jpg")
                                                .categorie(tulipes)
                                                .build(),
                                Article.builder().nom("Tulipe Perroquet Exotique")
                                                .description("Tulipe frangée aux couleurs mêlées de rose et de vert.")
                                                .prix(new BigDecimal("2.80")).stock(70).disponible(true)
                                                .imageUrl("http://localhost:8080/images/tulipe-perroquer.jpg")
                                                .categorie(tulipes).build(),
                                Article.builder().nom("Tulipe rose").description("Tulipe ").prix(new BigDecimal("52.80"))
                                                .stock(70).disponible(true)
                                                .imageUrl("http://localhost:8080/images/tulip-rose.jpg")
                                                .categorie(tulipes)
                                                .build(),
                                // Orchidées
                                Article.builder().nom("Orchidée Phalaenopsis Blanche").description(
                                                "L'orchidée papillon par excellence. Fleurit pendant 3 mois.")
                                                .prix(new BigDecimal("66.50")).stock(50).disponible(true)
                                                .imageUrl("http://localhost:8080/images/orchide-blanche.jpg")
                                                .categorie(orchidees).build(),
                                Article.builder().nom("Orchidée Dendrobium Rose").description(
                                                "Petites fleurs roses délicates, parfait pour bouquets exotiques.")
                                                .prix(new BigDecimal("99.80")).stock(40).disponible(true)
                                                .imageUrl("http://localhost:8080/images/orchide-rose.jpg")
                                                .categorie(orchidees).build(),
                                Article.builder().nom("Orchidée Cymbidium Vert")
                                                .description("Orchidée rare aux teintes chartreuse, très tendance.")
                                                .prix(new BigDecimal("79.20")).stock(30).disponible(true)
                                                .imageUrl("http://localhost:8080/images/orchide-vert.jpg")
                                                .categorie(orchidees).build(),

                                // Pivoines
                                Article.builder().nom("Pivoine Rose Coral Charm").description(
                                                "Pivoine aux grands pompons corail, reine des bouquets romantiques.")
                                                .prix(new BigDecimal("55.80")).stock(60).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Pivoine-Rose.jpg")
                                                .categorie(pivoine).build(),
                                Article.builder().nom("Pivoine Blanche Sarah Bernhardt").description(
                                                "Pivoine blanche double avec reflets crème, délicatement parfumée.")
                                                .prix(new BigDecimal("55.80")).stock(50).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Pivoine-blanche.jpg")
                                                .categorie(pivoine).build(),
                                Article.builder().nom("Pivoine Rouge Rubis")
                                                .description("Pivoine rouge profond, spectaculaire et lumineuse.")
                                                .prix(new BigDecimal("55.80")).stock(40).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Pivoine-rouge.jpg")
                                                .categorie(pivoine).build(),

                                // hortensias
                                Article.builder().nom("Hortensia Blanc")
                                                .description("Hortensia blanc, symbole de pureté et de gratitude.")
                                                .prix(new BigDecimal("77.80")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Hortensia-Blanc.jpg")
                                                .categorie(hortensias).build(),
                                Article.builder().nom("Hortensia Bleu")
                                                .description("Hortensia bleu, symbole d'amour et de tendresse.")
                                                .prix(new BigDecimal("77.80")).stock(70).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Hortensia-Bleu.jpg")
                                                .categorie(hortensias).build(),
                                Article.builder().nom("Hortensia Rose")
                                                .description("Hortensia rose, symbole de gratitude et d'amour sincère.")
                                                .prix(new BigDecimal("77.80")).stock(60).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Hortensia-Rose.jpg")
                                                .categorie(hortensias).build(),

                                // lys
                                Article.builder().nom("Lys Blanc").description(
                                                "Le lys blanc incarne la pureté et l'innocence. Souvent utilisé lors des cérémonies de mariage pour symboliser l'amour pur et éternel.")
                                                .prix(new BigDecimal("68.80")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Lys-Blanc.jpg")
                                                .categorie(lys).build(),
                                Article.builder().nom("Lys Jaune").description(
                                                "Le lys jaune évoque la joie, l'optimisme et l'amitié. Parfaits pour transmettre des vœux de bonheur et de succès à vos proches.")
                                                .prix(new BigDecimal("68.80")).stock(70).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Lys-jaune.jpg")
                                                .categorie(lys).build(),
                                Article.builder().nom("Lys Rose").description(
                                                "Le lys rose est synonyme de douceur et de tendresse. Souvent offerts pour exprimer des sentiments romantiques.")
                                                .prix(new BigDecimal("68.80")).stock(60).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Lys-Rose.jpg")
                                                .categorie(lys).build(),
                                Article.builder().nom("Lys Orange").description(
                                                "Le lys orange symbolise la passion, l'énergie et le désir.")
                                                .prix(new BigDecimal("68.80")).stock(60).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Lys-Orange.jpg")
                                                .categorie(lys).build(),
                                Article.builder().nom("Lys Rouge").description(
                                                "Le lys rouge est un symbole puissant d'amour, de passion et de désir ardent.")
                                                .prix(new BigDecimal("68.80")).stock(60).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Lys-rouge.jpg")
                                                .categorie(lys).build(),
                                Article.builder().nom("Lys Violet").description(
                                                "Le lys violet est un symbole de royauté, de noblesse et de dignité.")
                                                .prix(new BigDecimal("68.80")).stock(60).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Lys-violet.jpg")
                                                .categorie(lys).build(),

                                // marguerites
                                Article.builder().nom("Marguerite Blanche")
                                                .description("Marguerite blanche, symbole de pureté et de gratitude.")
                                                .prix(new BigDecimal("99.50")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Marguerite-blanche.jpg")
                                                .categorie(marguerites).build(),
                                Article.builder().nom("Marguerite Jaune")
                                                .description("Marguerite jaune, symbole d'amour et de tendresse.")
                                                .prix(new BigDecimal("99.20")).stock(70).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Marguerite-Jaune.jpg")
                                                .categorie(marguerites).build(),
                                Article.builder().nom("Marguerite Rose").description(
                                                "Marguerite rose, symbole de gratitude et d'amour sincère.")
                                                .prix(new BigDecimal("99.00")).stock(60).disponible(true)
                                                .imageUrl("http://localhost:8080/images/marguerite-rose.jpg")
                                                .categorie(marguerites).build(),

                                // tournesols
                                Article.builder().nom("Tournesol Nain")
                                                .description("Tournesol nain, symbole de joie et d'optimisme.")
                                                .prix(new BigDecimal("49.00")).stock(60).disponible(true)
                                                .imageUrl("http://localhost:8080/images/tournesol.jpg")
                                                .categorie(tournesols).build(),

                                // jonquilles
                                Article.builder().nom("Jonquille")
                                                .description("Jonquille, symbole de joie et d'optimisme.")
                                                .prix(new BigDecimal("55.00")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/jonquille.jpg")
                                                .categorie(jonquilles).build(),

                                // freesias
                                Article.builder().nom("Freesia").description("Freesia, symbole de joie et d'optimisme.")
                                                .prix(new BigDecimal("65.00")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Freesia.jpg")
                                                .categorie(freesias).build(),

                                // delphiniums
                                Article.builder().nom("Delphinium")
                                                .description("Delphinium, symbole de joie et d'optimisme.")
                                                .prix(new BigDecimal("89.00")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Delphinium.jpg")
                                                .categorie(delphiniums).build(),

                                // Accessoires
                                Article.builder().nom("Ruban Satin Ivoire (1m)").description(
                                                "Ruban satin 25mm, toucher doux, idéal pour nouer votre bouquet.")
                                                .prix(new BigDecimal("15.00")).stock(500).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Ruban-Satin-Ivoire.jpg")
                                                .categorie(accessoires).build(),
                                Article.builder().nom("Papier Kraft Naturel").description(
                                                "Feuille de papier kraft 50x50cm, emballage authentique et élégant.")
                                                .prix(new BigDecimal("25.00")).stock(300).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Papier-Kraft-Naturel.jpg")
                                                .categorie(accessoires).build(),
                                Article.builder().nom("Gypsophile (tige)").description(
                                                "Nuage de petites fleurs blanches, accompagnement idéal pour toutes fleurs.")
                                                .prix(new BigDecimal("90.00")).stock(200).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Gypsophile.jpg")
                                                .categorie(gypsophile).build(),
                                Article.builder().nom("Gypsophile (tige)").description(
                                                "Nuage de petites fleurs blanches, accompagnement idéal pour toutes fleurs.")
                                                .prix(new BigDecimal("90.00")).stock(200).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Gypsophile.jpg")
                                                .categorie(accessoires).build(),
                                Article.builder().nom("Feuillage Eucalyptus (tige)").description(
                                                "Tige d'eucalyptus odorant, ajoute texture et parfum subtil.")
                                                .prix(new BigDecimal("50.00")).stock(180).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Eucalyptus.jpg")
                                                .categorie(eucalyptus).build(),
                                Article.builder().nom("Feuillage Eucalyptus (tige)").description(
                                                "Tige d'eucalyptus odorant, ajoute texture et parfum subtil.")
                                                .prix(new BigDecimal("50.00")).stock(180).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Eucalyptus.jpg")
                                                .categorie(accessoires).build(),
                                Article.builder().nom("Carte Message Personnalisée").description(
                                                "Carte cartonnée épaisse avec enveloppe, pour votre message personnel.")
                                                .prix(new BigDecimal("5.00")).stock(400).disponible(true)
                                                .imageUrl("http://localhost:8080/images/carte-a-fleur.jpg")
                                                .categorie(accessoires).build(),

                                // muguet
                                Article.builder().nom("Muguet (tige)")
                                                .description("Muguet, symbole de porte-bonheur et de renouveau.")
                                                .prix(new BigDecimal("45.00")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/muguet.jpg")
                                                .categorie(muguet).build(),
                                Article.builder().nom("Muguet (tige)")
                                                .description("Muguet, symbole de porte-bonheur et de renouveau.")
                                                .prix(new BigDecimal("45.00")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/muguet.jpg")
                                                .categorie(accessoires).build(),

                                // ruban
                                Article.builder().nom("Ruban organza rose").description(
                                                "Ruban satin 25mm, toucher doux, idéal pour nouer votre bouquet.")
                                                .prix(new BigDecimal("15")).stock(500).disponible(true)
                                                .imageUrl("http://localhost:8080/images/rubon-organza-rose.jpeg")
                                                .categorie(accessoires).build(),
                                Article.builder().nom("Ruban organza rouge").description(
                                                "Ruban satin 25mm, toucher doux, idéal pour nouer votre bouquet.")
                                                .prix(new BigDecimal("15")).stock(500).disponible(true)
                                                .imageUrl("http://localhost:8080/images/rubon-rouge.jpg")
                                                .categorie(accessoires).build(),
                                Article.builder().nom("Ruban Cinamon Rose (38mm)").description(
                                                "Ruban satin 25mm, toucher doux, idéal pour nouer votre bouquet.")
                                                .prix(new BigDecimal("15")).stock(500).disponible(true)
                                                .imageUrl("http://localhost:8080/images/rubon-Cinamon-Rose-38mm.jpeg")
                                                .categorie(accessoires).build(),
                                Article.builder().nom("Ruban satin rouge").description(
                                                "Ruban satin 25mm, toucher doux, idéal pour nouer votre bouquet.")
                                                .prix(new BigDecimal("15")).stock(500).disponible(true)
                                                .imageUrl("http://localhost:8080/images/rubon-rouge.jpg")
                                                .categorie(accessoires).build(),

                                // des emballages
                                Article.builder().nom("Papier kraft rose poudré").description(
                                                "Papier kraft rose poudré, emballage authentique et élégant.")
                                                .prix(new BigDecimal("20")).stock(300).disponible(true)
                                                .imageUrl("http://localhost:8080/images/papier-rose.jpg")
                                                .categorie(accessoires).build(),

                                Article.builder().nom("Papier fleuriste noire").description(
                                                "Papier fleuriste noire, emballage authentique et élégant.")
                                                .prix(new BigDecimal("20")).stock(300).disponible(true)
                                                .imageUrl("http://localhost:8080/images/papier-noire.jpg")
                                                .categorie(accessoires).build(),

                                // bouquet
                                Article.builder()
                                                .nom("Bouquet hot pink ")
                                                .description("Bouquet composé de roses fuchsia, de marguerites et de feuillages verts.")
                                                .prix(new BigDecimal("2999")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/bouquet-hot-pink.jpg")
                                                .categorie(bouquet)
                                                .build(),
                                Article.builder()
                                                .nom("Bouquet orchide pour mariee")
                                                .description("Bouquet d'orchidée pour mariée, composé d'orchidée blanche et de verdure.")
                                                .prix(new BigDecimal("1299")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/bouquet-orchide-pur-mariee.jpg")
                                                .categorie(bouquet)
                                                .build(),
                                Article.builder()
                                                .nom("Bouquet OTulipes et roses ")
                                                .description("Bouquet de tulipes et roses.")
                                                .prix(new BigDecimal("2799")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/Bouquet-Otulipes-et-Roses.jpg")
                                                .categorie(bouquet)
                                                .build(),
                                Article.builder()
                                                .nom("Bouquet Red XL ")
                                                .description("Bouquet de mille roses rouges.")
                                                .prix(new BigDecimal("5000")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/bouquet-red-xl.jpeg")
                                                .categorie(bouquet)
                                                .build(),
                                Article.builder()
                                                .nom("Bouquet pirouette ")
                                                .description("Bouquet de pirouette.")
                                                .prix(new BigDecimal("4000")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/bouquet-pirou.png")
                                                .categorie(bouquet)
                                                .build(),
                                Article.builder()
                                                .nom("Mother’s Day Golden Garden ")
                                                .description("Bouquet de Mother’s Day Golden Garden.")
                                                .prix(new BigDecimal("3000")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/bouquet-mama.png")
                                                .categorie(bouquet)
                                                .build(),
                                Article.builder()
                                                .nom("Bouquet lady-pink ")
                                                .description("Bouquet de lady-pink.")
                                                .prix(new BigDecimal("1400")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/bouquet-girly.jpg")
                                                .categorie(bouquet)
                                                .build(),
                                Article.builder()
                                                .nom("Bouquet lady ")
                                                .description("Bouquet for you lady.")
                                                .prix(new BigDecimal("1400")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/bouquet-lady.png")
                                                .categorie(bouquet)
                                                .build(),
                                Article.builder()
                                                .nom("lavander touch ")
                                                .description("Bouquet de lavander touch.")
                                                .prix(new BigDecimal("1099")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/bouquet-perpel.jpg")
                                                .categorie(bouquet)
                                                .build(),
                                Article.builder()
                                                .nom("Bouquet of honey ")
                                                .description("Bouquet de of honey.")
                                                .prix(new BigDecimal("1500")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/bouquetofhoney.jpg")
                                                .categorie(bouquet)
                                                .build(),
                                Article.builder()
                                                .nom("Butterfly garden ")
                                                .description("Bouquet de butterfly garden.")
                                                .prix(new BigDecimal("2500")).stock(80).disponible(true)
                                                .imageUrl("http://localhost:8080/images/bouquet-butterfly.png")
                                                .categorie(bouquet)
                                                .build()

                );

                articleRepository.saveAll(articles);

                log.info("DataInitializer: seeded {} categories and {} articles.", 5, articles.size());
        }

        // ── Users ─────────────────────────────────────────────────────────────
        private void seedUsers() {
                if (utilisateurRepository.count() > 0) {
                        log.info("DataInitializer: users already present, skipping user seed.");
                        return;
                }

                log.info("DataInitializer: seeding users...");

                utilisateurRepository.saveAll(List.of(

                                // ── Admin account ────────────────────────────────────────────────
                                Utilisateur.builder()
                                                .email("admin@fleur-atelier.fr")
                                                .password(passwordEncoder.encode("Admin@1234"))
                                                .role(RoleUtilisateur.ADMIN)
                                                .prenom("Sophie")
                                                .nom("Dubois")
                                                .build(),

                                // ── Regular users ────────────────────────────────────────────────
                                Utilisateur.builder()
                                                .email("marie@example.com")
                                                .password(passwordEncoder.encode("User@1234"))
                                                .role(RoleUtilisateur.USER)
                                                .prenom("Marie")
                                                .nom("Laurent")
                                                .build(),

                                Utilisateur.builder()
                                                .email("julien@example.com")
                                                .password(passwordEncoder.encode("User@1234"))
                                                .role(RoleUtilisateur.USER)
                                                .prenom("Julien")
                                                .nom("Martin")
                                                .build(),

                                Utilisateur.builder()
                                                .email("camille@example.com")
                                                .password(passwordEncoder.encode("User@1234"))
                                                .role(RoleUtilisateur.USER)
                                                .prenom("Camille")
                                                .nom("Bernard")
                                                .build()));

                log.info("DataInitializer: seeded users.");
                log.info("  ADMIN  → admin@fleur-atelier.fr  / Admin@1234");
                log.info("  USER   → marie@example.com       / User@1234");
                log.info("  USER   → julien@example.com      / User@1234");
                log.info("  USER   → camille@example.com     / User@1234");
        }

        private Categorie save(Categorie categorie) {
                return categorieRepository.save(categorie);
        }
}
