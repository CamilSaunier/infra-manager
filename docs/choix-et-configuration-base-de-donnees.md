# Choix d'architecture et configuration de la base de données

Ce document décrit les choix retenus pour la gestion de la base de
données du projet **Infra Manager**. L'objectif est d'avoir une
configuration simple, cohérente et proche des pratiques utilisées dans
des projets Java / Spring Boot professionnels, sans ajouter de
complexité inutile.

## 1. Les outils et leurs responsabilités

Nous utilisons PostgreSQL, JPA/Hibernate et Flyway. Chaque outil a un
rôle distinct.

  -----------------------------------------------------------------------
Outil Responsabilité
  ----------------------------------- -----------------------------------
PostgreSQL Stocker les données et appliquer
les contraintes SQL

Spring Data JPA Fournir une interface pratique pour
accéder aux données

Hibernate Implémenter JPA et faire
correspondre les objets Java aux
tables SQL

Flyway Créer et faire évoluer le schéma de
la base avec des migrations SQL
versionnées
  -----------------------------------------------------------------------

**Règle importante : Flyway est le seul outil responsable de la création
et de l'évolution du schéma.** Hibernate ne doit pas créer ni modifier
les tables.

Cette séparation évite que deux outils essaient de gérer les mêmes
changements de structure.

## 2. Les classes Java et leur rôle

Nous séparons le modèle métier de sa représentation en base de données.

### `Equipment`

Emplacement :

``` text
src/main/java/com/inframanager/equipment/domain/Equipment.java
```

Cette classe représente le modèle métier. Elle contient les règles et
validations qui ont du sens pour un équipement, par exemple :

- le nom est obligatoire ;
- le fabricant est obligatoire ;
- le modèle est obligatoire ;
- une adresse IP, si elle est renseignée, doit respecter le format
  attendu.

Elle ne dépend pas de JPA ou d'Hibernate.

### `EquipmentEntity`

Emplacement :

``` text
src/main/java/com/inframanager/equipment/persistence/entity/EquipmentEntity.java
```

Cette classe représente la correspondance entre Java et la table
`equipment`.

Elle utilise les annotations JPA, notamment :

- `@Entity` pour déclarer une entité persistante ;
- `@Table` pour préciser le nom de la table ;
- `@Id` pour identifier la clé primaire ;
- `@GeneratedValue` pour configurer la génération de l'identifiant ;
- `@Column` pour préciser le nom ou certaines caractéristiques des
  colonnes.

Elle ne remplace pas `Equipment` : les deux classes ont des
responsabilités différentes.

## 3. PostgreSQL dans Docker

Le projet utilise PostgreSQL dans un conteneur Docker, afin de disposer
d'une base dédiée au projet sans dépendre de l'installation PostgreSQL
locale.

Le fichier `compose.yaml` à la racine du projet contient :

``` yaml
services:
  postgres:
    image: postgres:18
    container_name: infra-manager-db
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    ports:
      - "5433:5432"
    volumes:
      - postgres_data:/var/lib/postgresql

volumes:
  postgres_data:
```

Le port `5433` est celui utilisé depuis la machine hôte. Le conteneur
écoute, lui, sur le port `5432`.

Le volume `postgres_data` conserve les données lorsque le conteneur est
arrêté ou recréé.

Commandes utiles, à exécuter depuis la racine du projet :

``` bash
# Démarrer PostgreSQL
docker compose up -d

# Vérifier que PostgreSQL répond
docker compose exec postgres pg_isready -U infra_user -d infra_manager

# Ouvrir une session SQL
docker compose exec postgres psql -U infra_user -d infra_manager

# Arrêter le conteneur sans supprimer les données
docker compose down
```

Ne pas utiliser `docker compose down -v` sauf si l'on souhaite
réellement supprimer le volume et donc les données locales de la base.

## 4. Variables d'environnement et connexion

Les identifiants ne sont pas écrits directement dans le fichier de
configuration Spring.

À la racine du projet, le fichier `.env` contient les valeurs locales non précisé dans le `.env.example` :

``` env
POSTGRES_DB=
POSTGRES_USER=
POSTGRES_PASSWORD=
```

La valeur `` est un mot de passe provisoire pour
l'environnement local : il faut la remplacer par un mot de passe local
suffisamment robuste.

Le fichier `.env` doit rester ignoré par Git. Il ne doit pas être
commité.

Dans `src/main/resources/application.properties` :

``` properties
spring.config.import=optional:file:.env[.properties]
spring.datasource.url=jdbc:postgresql://localhost:5433/${POSTGRES_DB}
spring.datasource.username=${POSTGRES_USER}
spring.datasource.password=${POSTGRES_PASSWORD}
```

L'import `optional:file:.env[.properties]` permet à Spring Boot de lire
le fichier `.env` comme un fichier de propriétés. Le préfixe `optional:`
signifie que son absence ne bloque pas le chargement de la configuration
; les variables nécessaires devront toutefois être fournies par un autre
moyen pour que la connexion fonctionne.

Cette configuration suppose que l'application est lancée avec le
répertoire de travail positionné à la racine du projet, là où se trouve
`.env`.

## 5. Dépendances Maven

Le projet utilise les dépendances suivantes dans `pom.xml` :

### Spring Data JPA

``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

Elle apporte l'intégration Spring Data JPA et Hibernate.

### Pilote PostgreSQL

``` xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

Il permet à l'application Java de communiquer avec PostgreSQL via JDBC.

### Flyway

``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-flyway</artifactId>
</dependency>

<dependency>
<groupId>org.flywaydb</groupId>
<artifactId>flyway-database-postgresql</artifactId>
</dependency>
```

Le starter intègre Flyway à Spring Boot. Le module
`flyway-database-postgresql` apporte le support du moteur PostgreSQL.

Les versions sont gérées par le parent Spring Boot et son mécanisme de
gestion des dépendances. Il vaut mieux ne pas ajouter de versions
individuelles sans raison.

## 6. Flyway : gérer le schéma avec des migrations SQL

Les migrations sont des fichiers SQL versionnés dans Git.

Emplacement conventionnel :

``` text
src/main/resources/
└── db/
    └── migration/
        ├── V1__create_equipment_table.sql
        ├── V2__add_location_to_equipment.sql
        └── ...
```

Convention de nommage :

- `V` indique une migration versionnée ;
- le numéro indique l'ordre de la migration ;
- `__` sépare la version de sa description ;
- la description utilise des mots séparés par des underscores ;
- l'extension est `.sql`.

Exemples :

``` text
V1__create_equipment_table.sql
V2__add_location_to_equipment.sql
V3__add_equipment_status.sql
```

Au démarrage de l'application, Flyway recherche les migrations dans
`classpath:db/migration` (ce qui correspond au dossier
`src/main/resources/db/migration`).

Flyway conserve l'historique des migrations exécutées dans une table
appelée `flyway_schema_history`. Une migration déjà exécutée n'est
normalement pas rejouée.

**Règle d'équipe : une migration déjà appliquée ne doit pas être
modifiée pour changer le schéma.** Pour corriger ou faire évoluer la
base, on ajoute une nouvelle migration avec un nouveau numéro.

### Exemple d'évolution

- `V1` crée la table `equipment`.
- Plus tard, `V2` ajoute une colonne `location`.
- Ensuite, `V3` ajoute une contrainte ou une nouvelle table.

Chaque changement est explicite, historisé et partageable avec les
autres environnements.

## 7. Contraintes et intégrité des données

Les règles sont protégées à plusieurs niveaux, avec des responsabilités
complémentaires.

### Dans le modèle métier `Equipment`

Les validations Java permettent de refuser rapidement une donnée
invalide et de produire une erreur compréhensible par l'application.

Par exemple, `name`, `manufacturer` et `model` ne doivent pas être
`null`, vides ou composés uniquement d'espaces.

### Dans PostgreSQL, via Flyway

La base doit également protéger ses propres données. Pour les champs
obligatoires, la migration SQL utilisera :

- `NOT NULL` pour interdire `NULL` ;
- `CHECK` pour interdire les chaînes vides et les chaînes ne contenant
  que des espaces ;
- `VARCHAR(100)` pour limiter la longueur.

Exemple de contrainte SQL :

``` sql
name VARCHAR(100) NOT NULL
    CHECK (LENGTH(BTRIM(name)) > 0)
```

`NOT NULL` seul ne suffit pas : il autorise `''` et `'   '`. La
contrainte `CHECK` complète donc la règle.

Les mêmes règles seront appliquées à `manufacturer` et `model`.

Pour `ip_address`, la colonne reste facultative. Une longueur de 45
caractères permet de stocker une adresse IPv6 textuelle complète. Cette
limite ne vérifie toutefois pas, à elle seule, que la valeur est une
adresse IP valide : cette validation reste dans le modèle métier pour le
moment.

### Dans `EquipmentEntity`

Les annotations `@Column(nullable = false, length = 100)` décrivent la
correspondance attendue entre l'entité et les colonnes. Elles
documentent aussi les caractéristiques de mapping.

Mais **ce n'est pas `EquipmentEntity` qui pilote la création du schéma**
: les contraintes réelles sont définies dans les migrations Flyway.

Nous n'utilisons pas `@Check` pour ce projet. L'annotation Hibernate
`org.hibernate.annotations.Check` est dépréciée dans Hibernate 7 ; les
contraintes SQL sont donc gérées directement par Flyway.

## 8. Configuration Hibernate

Dans `application.properties`, nous ajouterons :

``` properties
spring.jpa.hibernate.ddl-auto=validate
```

`validate` demande à Hibernate de vérifier au démarrage que le schéma
existant est compatible avec les entités mappées.

Les modes à distinguer :

Valeur Comportement
  --------------- ------------------------------------------------------
`none`          Aucune gestion automatique du schéma par Hibernate
`validate`      Vérifie la compatibilité du mapping avec le schéma
`update`        Tente de modifier automatiquement le schéma
`create`        Crée le schéma au démarrage
`create-drop`   Crée le schéma au démarrage et le supprime à l'arrêt

Pour notre projet, nous retenons `validate`.

L'ordre logique au démarrage est alors :

1. Spring Boot initialise la connexion à PostgreSQL.
2. Flyway applique les migrations qui n'ont pas encore été exécutées.
3. Hibernate vérifie que les entités correspondent au schéma obtenu.
4. L'application démarre si tout est cohérent.

À noter : la validation Hibernate ne remplace pas les tests des
contraintes SQL. Elle vérifie principalement la compatibilité du mapping
et du schéma, pas toutes les règles métier ni chaque contrainte `CHECK`.

## 9. Structure attendue du projet

Les parties concernées par la base de données seront organisées ainsi :

``` text
infra-manager/
├── compose.yaml
├── .env
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/inframanager/equipment/
        │       ├── domain/
        │       │   └── Equipment.java
        │       └── persistence/
        │           └── entity/
        │               └── EquipmentEntity.java
        └── resources/
            ├── application.properties
            └── db/
                └── migration/
                    └── V1__create_equipment_table.sql
```

Cette structure est indicative : les autres packages du projet (`controller`, `dto`, `repository`, `service`) restent en
place.

## 10. Procédure pour ajouter une évolution de base

Pour chaque changement de schéma :

1. Créer un nouveau fichier dans `src/main/resources/db/migration`.
2. Lui attribuer le prochain numéro de version disponible.
3. Écrire le SQL correspondant au changement.
4. Adapter l'entité JPA si le mapping Java change.
5. Démarrer l'application ou lancer les migrations pour appliquer le
   changement.
6. Vérifier que Flyway a exécuté la migration et qu'Hibernate valide le
   mapping.
7. Lancer les tests.

Exemple : pour ajouter une colonne `location`, créer
`V2__add_location_to_equipment.sql`. Ne pas modifier `V1` si elle a déjà
été appliquée dans une base partagée ou conservée.

------------------------------------------------------------------------

## Décisions retenues

- PostgreSQL est notre base de données.
- PostgreSQL tourne dans Docker Compose sur le port hôte `5433`.
- Les identifiants locaux sont stockés dans `.env`, ignoré par Git.
- `Equipment` reste le modèle métier indépendant de JPA.
- `EquipmentEntity` assure le mapping JPA.
- Hibernate lit et écrit les données, mais ne crée ni ne modifie le
  schéma.
- Flyway est l'unique mécanisme de création et d'évolution du schéma.
- Les contraintes d'intégrité SQL sont écrites dans les migrations
  Flyway.
- `spring.jpa.hibernate.ddl-auto=validate` vérifie la compatibilité du
  mapping au démarrage.
