# Commandes utiles — Infra Manager

Ce document regroupe les commandes courantes pour travailler sur le projet `infra-manager` : démarrer PostgreSQL, accéder à la base, lancer Spring Boot et exécuter les tests.

> Exécuter les commandes depuis la racine du projet, sauf indication contraire.

## 1. Démarrer et arrêter PostgreSQL avec Docker

Démarrer le conteneur en arrière-plan :

```bash
docker compose up -d
```

Vérifier l'état du conteneur :

```bash
docker compose ps
```

Consulter les logs PostgreSQL :

```bash
docker compose logs -f postgres
```

Arrêter le conteneur sans supprimer les données :

```bash
docker compose stop
```

Redémarrer un conteneur arrêté :

```bash
docker compose start
```

Arrêter et supprimer le conteneur et le réseau du projet :

```bash
docker compose down
```

Supprimer également le volume contenant les données :

```bash
docker compose down -v
```

**Attention :** `docker compose down -v` supprime le volume `postgres_data` et donc les données de la base. À ne pas utiliser si l'on souhaite conserver ses équipements.

## 2. Vérifier que PostgreSQL répond

```bash
docker compose exec postgres pg_isready -U infra_user -d infra_manager
```

Un résultat contenant `accepting connections` indique que PostgreSQL accepte les connexions.

## 3. Se connecter à la base de données

Ouvrir une session PostgreSQL (`psql`) dans le conteneur :

```bash
docker compose exec postgres psql -U infra_user -d infra_manager
```

Une fois dans `psql`, les commandes suivantes sont disponibles.

Afficher les tables :

```sql
\dt
```

Afficher la structure de la table `equipment` :

```sql
\d equipment
```

Afficher les contraintes de la table `equipment` :

```sql
SELECT
    conname,
    pg_get_constraintdef(oid)
FROM pg_constraint
WHERE conrelid = 'equipment'::regclass;
```

Afficher l'historique des migrations Flyway :

```sql
SELECT version, description, success
FROM flyway_schema_history
ORDER BY installed_rank;
```

Afficher les équipements (la table peut être vide pour le moment) :

```sql
SELECT * FROM equipment;
```

Quitter `psql` :

```sql
\q
```

## 4. Lancer l'application Spring Boot

Vérifier que PostgreSQL est démarré, puis lancer l'application depuis la racine du projet :

```bash
mvn spring-boot:run
```

Pour arrêter l'application :

```text
Ctrl + C
```

Par défaut, le serveur web Spring Boot écoute sur le port `8080`.

> **État actuel du projet :** la connexion à PostgreSQL et la migration Flyway `V1` ont été vérifiées. Le démarrage complet de l'application échoue encore tant que l'interface métier `EquipmentRepository` n'a pas d'implémentation Spring déclarée. Cette erreur est indépendante de la connexion à la base. Une fois le repository branché, `mvn spring-boot:run` devra démarrer normalement.

## 5. Exécuter les tests

Lancer tous les tests :

```bash
mvn test
```

Lancer un test précis (adapter le nom de classe) :

```bash
mvn -Dtest=EquipmentTest test
```

Nettoyer les fichiers générés puis lancer les tests et les vérifications du cycle Maven :

```bash
mvn clean verify
```

Les rapports de tests Maven sont générés dans :

```text
target/surefire-reports/
```

## 6. Commandes Maven pratiques

Compiler le projet sans lancer les tests :

```bash
mvn -DskipTests compile
```

Construire le package JAR :

```bash
mvn package
```

Nettoyer le répertoire de build `target/` :

```bash
mvn clean
```

## 7. Rappel de la configuration locale

Les identifiants de connexion sont définis dans le fichier `.env` à la racine du projet. Le fichier `.env` ne doit pas être ajouté à Git.

Le fichier `.env.example` sert de modèle pour créer son propre `.env`.

La configuration Spring se trouve dans :

```text
src/main/resources/application.properties
```

La connexion utilise actuellement :

- Hôte : `localhost`
- Port côté machine : `5433`
- Base : `infra_manager`
- Utilisateur : `infra_user`

Le port `5433` côté machine est redirigé vers le port `5432` de PostgreSQL dans le conteneur Docker.

## 8. Démarrage rapide — routine habituelle

Pour une session de développement classique :

**Terminal 1 — démarrer la base :**

```bash
docker compose up -d
```

**Terminal 2 — lancer l'application :**

```bash
mvn spring-boot:run
```

**Terminal 3 — accéder à PostgreSQL si nécessaire :**

```bash
docker compose exec postgres psql -U infra_user -d infra_manager
```

**Dans un terminal — lancer les tests :**

```bash
mvn test
```
