# Infra Manager --- Notes de cours

## Java, Maven et organisation du projet

*Date : 9 novembre 2026*

## 1. Le projet

Nous développons **Infra Manager**, une application backend de gestion
d'équipements informatiques et réseau.

L'objectif n'est pas seulement d'obtenir une application qui fonctionne
: le projet doit aussi montrer que nous savons écrire du code
compréhensible, maintenable, testable et expliquer nos choix techniques.

Nous avançons progressivement : - comprendre les notions avant de les
utiliser ; - écrire le code nous-mêmes, avec des explications et des
indices ; - ajouter les outils et les couches uniquement quand ils
deviennent utiles ; - éviter le sur-engineering.

Le projet est un projet **Maven** utilisant **Java 21**.

## 2. Structure Maven actuelle

À la racine du dépôt, nous avons notamment :

``` text
infra-manager/
├── docs/
│   └── ROADMAP-JAVA.md
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── inframanager/
    └── test/
        └── java/
            └── com/
                └── inframanager/
```

-   `pom.xml` : décrit le projet Maven et sa configuration.
-   `src/main/java` : contient le code de l'application.
-   `src/test/java` : contient le code des tests automatisés.
-   `target/` : dossier généré par Maven lors de la compilation ou
    d'autres tâches. Il ne faut généralement pas le versionner.

Les tests servent notamment à vérifier le comportement du code et à
détecter les régressions avant de livrer une modification.

## 3. Qu'est-ce qu'un package Java ?

Un **package** est un espace de noms qui permet de regrouper et
d'organiser les classes Java.

Par exemple :

``` java
package com.inframanager.equipment;
```

Ce nom indique que la classe appartient au package
`com.inframanager.equipment`.

Dans l'organisation habituelle d'un projet Java, les éléments du nom
correspondent aux dossiers :

``` text
src/main/java/
└── com/
    └── inframanager/
        └── equipment/
```

Le dossier `inframanager` n'a pas été renommé ni supprimé : `equipment`
est un sous-dossier de `inframanager`.

### Pourquoi IntelliJ affichait-il `com.inframanager.equipment` sur une seule ligne ?

IntelliJ peut regrouper visuellement les packages imbriqués avec
l'option **Compact Middle Packages**.

Cela ne modifie pas les dossiers sur le disque. C'est uniquement une
option d'affichage de l'explorateur de projet.

### À quoi servent les packages ?

-   Organiser les classes par sujet ou responsabilité.
-   Retrouver plus facilement le code.
-   Éviter certaines collisions de noms entre classes.
-   Participer aux règles de visibilité en Java.

On peut importer une classe d'un autre package, par exemple :

``` java
import com.inframanager.equipment.Equipment;
```

Un package n'est toutefois pas exactement l'équivalent d'un module
JavaScript ou Node.js : les règles et le fonctionnement sont différents.

## 4. Organiser le code : deux approches

Nous avons découvert deux façons courantes de structurer une
application.

### A. Organisation par couches

Les classes sont regroupées selon leur rôle technique :

``` text
com.inframanager
├── controller/
├── service/
├── repository/
└── model/
```

Rôles habituels :

  -----------------------------------------------------------------------
  Couche                              Responsabilité
  ----------------------------------- -----------------------------------
  `controller`                        Reçoit les requêtes HTTP et
                                      construit les réponses

  `service`                           Porte la logique métier et
                                      orchestre les opérations

  `repository`                        Gère l'accès aux données

  `model`                             Contient les objets représentant le
                                      domaine ou les données, selon la
                                      conception choisie
  -----------------------------------------------------------------------

Exemple d'ajout d'un équipement dans une API : 1. Le controller reçoit
la requête. 2. Le service applique les règles métier. 3. Le repository
effectue l'enregistrement. 4. Le controller renvoie la réponse HTTP.

Cette organisation est appelée **architecture en couches** (*Layered
Architecture*).

### B. Organisation par domaine ou fonctionnalité

Les classes sont regroupées selon la fonctionnalité à laquelle elles
appartiennent. Chaque domaine peut ensuite contenir ses propres couches
:

``` text
com.inframanager
├── equipment/
│   ├── controller/
│   ├── service/
│   └── repository/
└── network/
    ├── controller/
    ├── service/
    └── repository/
```

Par exemple : - `equipment` regroupe les fonctionnalités liées à la
gestion des équipements ; - `network` regroupe les fonctionnalités liées
au réseau.

Cette organisation est souvent appelée **package-by-feature** ou
**organisation par fonctionnalité**.

Elle permet de garder ensemble le code qui évolue pour une même
fonctionnalité, au lieu de disperser toutes ses classes dans de grands
dossiers techniques communs.

## 5. L'approche retenue pour Infra Manager

Nous allons partir sur une **organisation par domaine fonctionnel**,
avec une séparation des responsabilités à l'intérieur de chaque domaine.

Pourquoi ? - Le projet pourra accueillir plusieurs fonctionnalités. - Le
code lié à une fonctionnalité restera regroupé. - Nous pourrons
distinguer les responsabilités sans créer toutes les couches à
l'avance. - Cette organisation pourra évoluer si le projet grandit.

Important : une arborescence de packages ne suffit pas à elle seule à
garantir une bonne architecture ou une Clean Architecture. Il faudra
aussi respecter les responsabilités de chaque composant et maîtriser le
sens des dépendances entre eux.

Nous ne devons pas créer tous les dossiers et toutes les classes
immédiatement. Nous les ajouterons au moment où le besoin sera concret.

Le package `equipment` créé pendant la séance est vide. Nous déciderons
de le conserver et de le compléter lorsque nous commencerons cette
fonctionnalité ; il n'y a rien à coder dedans pour le moment.

## 6. Ce que nous n'allons pas faire tout de suite

Nous n'ajoutons pas encore Spring Boot, une base de données ou toutes
les couches d'une API.

Nous allons d'abord comprendre les bases Java et la manière dont les
composants collaborent. Nous intégrerons les outils backend au bon
moment, en expliquant leur utilité.


------------------------------------------------------------------------

## Vocabulaire à retenir

  -----------------------------------------------------------------------
  Terme                               Définition courte
  ----------------------------------- -----------------------------------
  Package                             Espace de noms qui organise les
                                      classes Java

  Couche (*layer*)                    Regroupement de composants ayant un
                                      rôle technique

  Domaine / fonctionnalité            Partie de l'application consacrée à
  (*feature*)                         un besoin métier

  Controller                          Point d'entrée HTTP d'une API

  Service                             Composant qui porte ou orchestre la
                                      logique métier

  Repository                          Composant chargé de l'accès aux
                                      données

  Maven                               Outil de gestion de projet, de
                                      dépendances et de compilation Java

  Régression                          Problème introduit par une
                                      modification qui casse un
                                      comportement existant
  -----------------------------------------------------------------------
