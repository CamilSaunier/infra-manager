# Infra Manager — Notes de cours

## Java, Maven et organisation du projet

**Date : 12 novembre 2026**

## 1. Le projet

Nous développons **Infra Manager**, une application backend de gestion d'équipements informatiques et réseau.

L'objectif n'est pas seulement d'obtenir une application qui fonctionne : le projet doit aussi montrer que nous savons
écrire du code compréhensible, maintenable, testable et expliquer nos choix techniques.

Nous avançons progressivement :

- comprendre les notions avant de les utiliser ;
- écrire le code nous-mêmes, avec des explications et des indices ;
- ajouter les outils et les couches uniquement quand ils deviennent utiles ;
- éviter le sur-engineering.

Le projet est un projet **Maven** utilisant **Java 21**.

## 2. Structure Maven actuelle

À la racine du dépôt, nous avons actuellement :

```text
infra-manager/
├── docs/
│   ├── Organisation-projet.md
│   └── ROADMAP-JAVA.md
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── inframanager/
    │               └── equipment/
    │                   └── domain/
    │                       └── Equipment.java
    └── test/
        └── java/
            └── com/
                └── inframanager/
                    └── equipment/
                        └── domain/
                            └── EquipmentTest.java
```

- `pom.xml` : décrit le projet Maven et sa configuration.
- `src/main/java` : contient le code de l'application.
- `src/test/java` : contient le code des tests automatisés.
- `target/` : dossier généré par Maven lors de la compilation ou d'autres tâches. Il ne faut généralement pas le
  versionner.

Les tests servent notamment à vérifier le comportement du code et à détecter les régressions avant de livrer une
modification.

À ce stade, notre modèle métier `Equipment` est placé dans le package `equipment.domain`. Ses règles de validation sont
couvertes par des tests JUnit.

## 3. Qu'est-ce qu'un package Java ?

Un **package** est un espace de noms qui permet de regrouper et d'organiser les classes Java.

Par exemple :

```java
package com.inframanager.equipment.domain;
```

Ce nom indique que la classe appartient au package `com.inframanager.equipment.domain`.

Dans l'organisation habituelle d'un projet Java, les éléments du nom correspondent aux dossiers :

```text
src/main/java/
└── com/
    └── inframanager/
        └── equipment/
            └── domain/
```

### Pourquoi IntelliJ affichait-il plusieurs packages sur une seule ligne ?

IntelliJ peut regrouper visuellement les packages imbriqués avec l'option **Compact Middle Packages**.

Cela ne modifie pas les dossiers sur le disque. C'est uniquement une option d'affichage de l'explorateur de projet.

### À quoi servent les packages ?

- Organiser les classes par sujet ou responsabilité.
- Retrouver plus facilement le code.
- Éviter certaines collisions de noms entre classes.
- Participer aux règles de visibilité en Java.

On peut importer une classe d'un autre package, par exemple :

```java
import com.inframanager.equipment.domain.Equipment;
```

Un package n'est toutefois pas exactement l'équivalent d'un module JavaScript ou Node.js : les règles et le
fonctionnement sont différents.

## 4. Organiser le code : deux approches

Nous avons découvert deux façons courantes de structurer une application.

### A. Organisation par couches

Les classes sont regroupées selon leur rôle technique :

```text
com.inframanager
├── controller/
├── service/
├── repository/
└── model/
```

Rôles habituels :

| Couche       | Responsabilité                                                                          |
|--------------|-----------------------------------------------------------------------------------------|
| `controller` | Reçoit les requêtes HTTP et construit les réponses                                      |
| `service`    | Porte ou orchestre les cas d'utilisation et la logique métier                           |
| `repository` | Gère l'accès aux données                                                                |
| `model`      | Contient les objets représentant le domaine ou les données, selon la conception choisie |

Cette organisation est appelée **architecture en couches** (*Layered Architecture*).

### B. Organisation par domaine ou fonctionnalité

Les classes sont regroupées selon la fonctionnalité à laquelle elles appartiennent. Chaque domaine peut ensuite contenir
ses propres responsabilités :

```text
com.inframanager
├── equipment/
│   ├── controller/
│   ├── domain/
│   ├── dto/
│   ├── repository/
│   └── service/
└── network/
    ├── controller/
    ├── domain/
    ├── dto/
    ├── repository/
    └── service/
```

Par exemple :

- `equipment` regroupe les fonctionnalités liées à la gestion des équipements ;
- `network` regroupe les fonctionnalités liées au réseau.

Cette organisation est souvent appelée **Package by Feature** ou **organisation par fonctionnalité**.

Elle permet de garder ensemble le code qui évolue pour une même fonctionnalité, au lieu de disperser toutes ses classes
dans de grands dossiers techniques communs.

## 5. L'architecture retenue pour Infra Manager

Nous avons retenu une **organisation par fonctionnalité (Package by Feature)**, avec une séparation des responsabilités
à l'intérieur de chaque fonctionnalité.

Le projet sera construit comme un **monolithe modulaire** : une seule application, organisée en fonctionnalités
distinctes et dont les responsabilités sont clairement séparées.

### Structure cible d'une fonctionnalité

Pour la fonctionnalité `equipment`, nous avons retenu cette organisation :

```text
equipment/
├── controller/
├── domain/
├── dto/
├── repository/
└── service/
```

Chaque package possède un rôle précis :

| Package      | Responsabilité                                                             |
|--------------|----------------------------------------------------------------------------|
| `domain`     | Modèles métier et règles propres au domaine                                |
| `dto`        | Objets de transfert de données utilisés pour les échanges avec l'extérieur |
| `service`    | Cas d'utilisation, orchestration et logique applicative                    |
| `repository` | Accès aux données et persistance                                           |
| `controller` | Réception des requêtes HTTP et construction des réponses                   |

### Quelques règles d'organisation

- Le domaine ne doit pas dépendre de Spring Boot, de PostgreSQL ou des détails techniques de persistance.
- Les DTO sont indépendants des modèles métier : ils représentent les données échangées, pas les règles métier.
- Les contrôleurs ne doivent pas porter la logique métier.
- Les services coordonnent les opérations et appliquent les règles relevant des cas d'utilisation.
- Les repositories isolent l'accès aux données.
- Les dépendances entre composants doivent rester cohérentes avec leurs responsabilités.

Les interfaces et les implémentations techniques des repositories pourront être distinguées lorsque nous aborderons la
persistance. Nous déciderons alors de leur organisation concrète.

### Pourquoi ce choix ?

- Le projet pourra accueillir plusieurs