# Initialisation de Spring Boot --- Infra Manager

> Documentation de référence pour l'intégration de Spring Boot dans le
> projet `infra-manager`.
>
> **État du projet au 12 novembre 2026** --- Java 21, Maven, Spring Boot
> 4.1.2.

## 1. Pourquoi intégrer Spring Boot ?

Avant Spring Boot, le projet était une application Java classique : nous
pouvions créer des objets, appliquer des règles métier et les tester,
mais nous n'avions pas encore de serveur HTTP capable d'exposer ces
fonctionnalités.

Spring Boot nous apporte notamment :

-   un serveur web embarqué (Tomcat dans notre configuration) ;
-   la gestion automatique de nombreux composants de l'application ;
-   une manière simple de créer une API REST ;
-   une configuration par défaut qui évite de tout paramétrer
    manuellement.

**Important :** Spring Boot ne remplace pas le code métier déjà écrit.
Nous conservons nos classes, nos tests et notre documentation. Il vient
ajouter l'infrastructure nécessaire pour exposer progressivement notre
application par HTTP.

## 2. Configuration choisie avec Spring Initializr

Le projet de référence a été généré sur [Spring
Initializr](https://start.spring.io/), avec les paramètres suivants :

  Paramètre        Valeur
  ---------------- --------------------
  Project          Maven
  Language         Java
  Spring Boot      4.1.2
  Group            `com.inframanager`
  Artifact         `infra-manager`
  Package name     `com.inframanager`
  Packaging        Jar
  Java             21
  Configuration    Properties
  Dépendance web   Spring Web

Spring Initializr a servi de modèle pour récupérer la configuration
nécessaire. **Le projet temporaire généré n'est pas le projet de
travail** : les modifications ont été intégrées dans le dépôt existant
`~/Bureau/Projects/infra-manager`.

## 3. Les éléments ajoutés au projet Maven

### 3.1 Le parent Spring Boot

Dans le `pom.xml`, le parent Spring Boot centralise la gestion des
versions de nombreuses dépendances et configure des valeurs par défaut
adaptées à Spring Boot.

``` xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.1.2</version>
    <relativePath/>
</parent>
```

Cela évite notamment d'avoir à préciser manuellement la version de
chaque dépendance Spring Boot compatible.

### 3.2 La version de Java

Le projet utilise Java 21. La propriété existante du projet est
conservée :

``` xml
<properties>
    <maven.compiler.release>21</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

    <jackson-bom.version>3.1.8</jackson-bom.version>
    <logback.version>1.6.6</logback.version>
</properties>
```

Les deux dernières propriétés correspondent aux versions de Jackson et
Logback retenues pour corriger les alertes de vulnérabilité rencontrées
pendant l'intégration. Elles ne sont pas des réglages obligatoires dans
tout projet Spring Boot : elles répondent à l'état des dépendances de ce
projet au moment de leur ajout.

### 3.3 Les dépendances

La configuration comprend actuellement :

``` xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.12.2</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
</dependencies>
```

-   **JUnit Jupiter** : permet de continuer à tester les règles métier
    et les services.
-   **Spring Boot Starter WebMVC** : apporte les éléments nécessaires à
    la création d'une application web / API HTTP avec Spring MVC.
-   **Spring Boot DevTools** : facilite le développement, notamment avec
    le redémarrage automatique lorsque les classes compilées changent.

`spring-boot-starter-webmvc` est le nom du starter utilisé par le projet
généré avec Spring Boot 4.1.2.

### 3.4 Le plugin Maven Spring Boot

Dans la section `<build><plugins>` :

``` xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

Ce plugin fournit notamment les objectifs Maven permettant de lancer
l'application et de produire un exécutable Spring Boot.

### 3.5 Vérifier l'intégration

Depuis la racine du projet :

``` bash
mvn clean test
```

Cette commande nettoie les fichiers compilés précédents puis compile et
exécute les tests.

Pour démarrer l'application :

``` bash
mvn spring-boot:run
```

Le serveur démarre sur le port 8080 par défaut. L'application peut alors
être appelée à l'adresse :

``` text
http://localhost:8080
```

Une réponse `404 Not Found` à la racine est normale tant qu'aucun
contrôleur ne définit de route `/`.

## 4. Le point d'entrée de l'application

Fichier : `src/main/java/com/inframanager/InfraManagerApplication.java`

``` java
package com.inframanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InfraManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfraManagerApplication.class, args);
    }
}
```

### `@SpringBootApplication`

C'est l'annotation principale d'une application Spring Boot. Elle
regroupe notamment trois mécanismes :

-   `@SpringBootConfiguration` : indique que cette classe participe à la
    configuration Spring ;
-   `@EnableAutoConfiguration` : demande à Spring Boot de configurer
    automatiquement certains éléments en fonction des dépendances et de
    la configuration présentes ;
-   `@ComponentScan` : demande à Spring de rechercher les composants à
    gérer.

Le scan commence par le package de la classe principale, ici
`com.inframanager`, et couvre ses sous-packages. C'est pourquoi notre
contrôleur placé dans `com.inframanager.equipment.controller` peut être
détecté.

### `SpringApplication.run(...)`

Cette instruction démarre l'application Spring : le contexte Spring est
créé, les composants sont détectés et le serveur web embarqué est
démarré.

## 5. Notre premier contrôleur REST

Fichier :
`src/main/java/com/inframanager/equipment/controller/EquipmentController.java`

``` java
package com.inframanager.equipment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @GetMapping
    public String getEquipment() {
        return "Liste des équipements";
    }
}
```

En ouvrant `http://localhost:8080/api/equipment`, le serveur renvoie
actuellement le texte :

``` text
Liste des équipements
```

### Comprendre les trois annotations utilisées

#### `@RestController`

Indique à Spring que cette classe est un contrôleur REST.

Spring détecte la classe, la prend en charge et utilise ses méthodes
pour traiter les requêtes HTTP. Les valeurs renvoyées par les méthodes
sont écrites dans le corps de la réponse HTTP (avec conversion si
nécessaire).

À retenir : un `@RestController` renvoie des données dans la réponse,
plutôt que de demander par défaut le rendu d'une page HTML via un moteur
de templates.

#### `@RequestMapping("/api/equipment")`

Définit le chemin commun associé au contrôleur.

Les méthodes de cette classe peuvent ensuite définir leur propre partie
de chemin et leur méthode HTTP.

Ici, le chemin de base est `/api/equipment`.

#### `@GetMapping`

Associe une méthode Java à une requête HTTP `GET`.

Comme aucun chemin supplémentaire n'est indiqué dans notre
`@GetMapping`, la méthode répond directement sur :

``` text
GET /api/equipment
```

`GET` est généralement utilisé pour consulter une ressource, sans
demander sa création ou sa modification.

## 6. Les annotations Spring à connaître progressivement

En Java, le terme officiel est **annotation**. On entend parfois «
décorateur » par analogie avec d'autres langages, mais en Java on parle
bien d'annotations : elles commencent par `@`.

Il n'est pas nécessaire de toutes les maîtriser immédiatement. Cette
liste sert de repère pour comprendre celles que nous rencontrerons au
fil du projet.

### 6.1 Déclarer les composants

  -----------------------------------------------------------------------
  Annotation                          Rôle
  ----------------------------------- -----------------------------------
  `@Component`                        Déclare une classe comme composant
                                      géré par Spring.

  `@Service`                          Spécialise `@Component` pour
                                      représenter une classe de logique
                                      applicative ou métier.

  `@Repository`                       Spécialise `@Component` pour une
                                      classe d'accès aux données.

  `@Controller`                       Déclare un contrôleur Spring MVC,
                                      souvent utilisé pour servir des
                                      vues.

  `@RestController`                   Déclare un contrôleur REST dont les
                                      retours sont écrits dans le corps
                                      HTTP.

  `@Configuration`                    Déclare une classe de configuration
                                      Spring.

  `@Bean`                             Déclare une méthode dont le
                                      résultat sera enregistré comme bean
                                      Spring.
  -----------------------------------------------------------------------

Un **bean** est un objet dont la création et le cycle de vie sont pris
en charge par le conteneur Spring.

`@Service`, `@Repository` et `@Controller` sont des spécialisations de
`@Component`, avec une intention plus précise. Il vaut mieux choisir
l'annotation qui décrit le rôle réel de la classe.

### 6.2 Définir les routes HTTP

  -----------------------------------------------------------------------
  Annotation                          Rôle
  ----------------------------------- -----------------------------------
  `@RequestMapping`                   Définit un chemin et éventuellement
                                      des conditions HTTP communes.

  `@GetMapping`                       Associe une méthode à une requête
                                      GET.

  `@PostMapping`                      Associe une méthode à une requête
                                      POST, souvent utilisée pour créer
                                      une ressource.

  `@PutMapping`                       Associe une méthode à une requête
                                      PUT, généralement pour remplacer
                                      une ressource.

  `@PatchMapping`                     Associe une méthode à une requête
                                      PATCH, généralement pour modifier
                                      partiellement une ressource.

  `@DeleteMapping`                    Associe une méthode à une requête
                                      DELETE.
  -----------------------------------------------------------------------

Exemple de correspondance :

  Action API                  Méthode HTTP courante
  --------------------------- -----------------------
  Consulter des équipements   `GET`
  Créer un équipement         `POST`
  Remplacer un équipement     `PUT`
  Modifier certains champs    `PATCH`
  Supprimer un équipement     `DELETE`

Cette correspondance est une convention REST courante. Le comportement
exact dépend de ce que l'endpoint implémente.

### 6.3 Récupérer les données d'une requête

  -----------------------------------------------------------------------
  Annotation                          Rôle
  ----------------------------------- -----------------------------------
  `@PathVariable`                     Récupère une valeur présente dans
                                      le chemin de l'URL.

  `@RequestParam`                     Récupère un paramètre de requête,
                                      généralement après `?`.

  `@RequestBody`                      Récupère le corps de la requête,
                                      par exemple un JSON, et le
                                      convertit en objet Java.

  `@ResponseStatus`                   Permet de définir le code HTTP
                                      renvoyé par une méthode ou une
                                      exception.
  -----------------------------------------------------------------------

Exemples d'URL :

``` text
GET /api/equipment/42
GET /api/equipment?manufacturer=Cisco
```

Dans le premier cas, `42` peut être récupéré avec `@PathVariable`. Dans
le second, `manufacturer` peut être récupéré avec `@RequestParam`.

Pour un endpoint de création, un JSON envoyé dans le corps d'une requête
`POST` sera généralement récupéré avec `@RequestBody`.

### 6.4 Injection de dépendances

  -----------------------------------------------------------------------
  Annotation / mécanisme              Rôle
  ----------------------------------- -----------------------------------
  Injection par constructeur          Permet à Spring de fournir les
                                      dépendances nécessaires à une
                                      classe.

  `@Autowired`                        Peut indiquer explicitement un
                                      point d'injection.
  -----------------------------------------------------------------------

L'injection par constructeur est généralement recommandée pour les
dépendances obligatoires. Avec un seul constructeur, Spring sait
généralement l'utiliser sans qu'il soit nécessaire d'ajouter
`@Autowired`.

Exemple conceptuel :

``` java
@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }
}
```

Cet exemple illustre le principe ; il ne signifie pas que le service
actuel a déjà été connecté au conteneur Spring.

### 6.5 Validation des données

  -----------------------------------------------------------------------
  Annotation                          Rôle
  ----------------------------------- -----------------------------------
  `@Valid`                            Demande la validation d'un objet à
                                      partir de ses contraintes.

  `@NotNull`                          Interdit une valeur `null`.

  `@NotBlank`                         Interdit `null`, une chaîne vide ou
                                      composée uniquement d'espaces.

  `@Size`                             Définit une taille minimale et/ou
                                      maximale.

  `@Min` / `@Max`                     Définit une borne numérique
                                      minimale ou maximale.
  -----------------------------------------------------------------------

Ces annotations de validation nécessitent les dépendances de validation
appropriées. Elles ne sont pas encore intégrées à notre projet à ce
stade.

## 7. Comprendre une réponse HTTP

Une réponse HTTP comporte notamment trois éléments :

1.  **Le code de statut** : indique le résultat général du traitement.
2.  **Les headers (en-têtes)** : apportent des informations sur la
    réponse, par exemple son type de contenu.
3.  **Le body (corps)** : contient les données renvoyées, lorsqu'il y en
    a.

Quelques codes importants :

  -----------------------------------------------------------------------------
  Code                          Signification           Exemple d'utilisation
  ----------------------------- ----------------------- -----------------------
  `200 OK`                      La requête a réussi.    Un GET a renvoyé les
                                                        données demandées.

  `201 Created`                 Une ressource a été     Un POST a créé un
                                créée.                  équipement.

  `204 No Content`              La requête a réussi     Une suppression a
                                sans corps de réponse.  réussi sans contenu à
                                                        renvoyer.

  `400 Bad Request`             La requête est          Des données envoyées ne
                                invalide.               respectent pas les
                                                        règles attendues.

  `404 Not Found`               La ressource ou la      L'identifiant demandé
                                route n'a pas été       n'existe pas.
                                trouvée.                

  `500 Internal Server Error`   Une erreur serveur      Une exception non
                                inattendue s'est        traitée a interrompu le
                                produite.               traitement.
  -----------------------------------------------------------------------------

**Pour notre endpoint actuel :** `GET /api/equipment` renvoie `200 OK`
par défaut lorsque son traitement aboutit. `201 Created` serait adapté à
un endpoint qui crée effectivement un nouvel équipement, pas à notre GET
de consultation.

## 8. État actuel et prochaines étapes

À ce stade :

-   Spring Boot 4.1.2 est intégré au projet Maven existant ;
-   le projet utilise Java 21 ;
-   le point d'entrée `InfraManagerApplication` démarre l'application ;
-   le serveur web embarqué démarre sur le port 8080 ;
-   le contrôleur `EquipmentController` expose `GET /api/equipment` ;
-   les tests existants du projet passent après l'intégration ;
-   DevTools est configuré pour faciliter le redémarrage pendant le
    développement.

Nous n'avons pas encore construit toute l'API ni connecté le contrôleur
au service métier et à une base de données. Ces étapes viendront
progressivement, en conservant les tests et les règles métier déjà
écrits.

------------------------------------------------------------------------

## Références officielles

-   [Spring Boot ---
    Documentation](https://docs.spring.io/spring-boot/index.html)
-   [Spring Boot --- Maven
    Plugin](https://docs.spring.io/spring-boot/maven-plugin/)
-   [Spring Framework --- Web
    MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
-   [Spring Framework --- Core technologies et conteneur
    IoC](https://docs.spring.io/spring-framework/reference/core.html)
-   [Spring Framework ---
    Validation](https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html)
-   [Spring Initializr](https://start.spring.io/)
