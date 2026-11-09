# Roadmap – Créer un projet Java de A à Z

## Objectif

Être capable de créer un projet Java professionnel depuis zéro, de configurer son environnement, de gérer ses dépendances, de compiler son code et de versionner son travail avec Git et GitHub.

Cette documentation sert de référence. Il n'est pas nécessaire de mémoriser toutes les commandes ou les configurations XML.

---

# 1. Comprendre les outils de l'environnement Java

Avant de créer un projet, il faut comprendre le rôle de chaque outil.

| Outil         | Rôle                                                                  |
|---------------|-----------------------------------------------------------------------|
| Java (JDK)    | Langage et outils nécessaires pour développer et compiler             |
| IntelliJ IDEA | IDE permettant d'écrire, organiser et déboguer le code                |
| Maven         | Gestion des dépendances, compilation, tests et construction du projet |
| Git           | Gestion de l'historique et des versions du code                       |
| GitHub        | Hébergement distant du dépôt Git                                      |

Ces outils sont complémentaires, mais ne font pas la même chose.

## 1.1 Java et le JDK

Le JDK signifie *Java Development Kit*.

Il contient notamment :
- Le compilateur Java (`javac`).
- Les outils de développement.
- La JVM, qui permet d'exécuter le bytecode Java.

Quelques commandes utiles :

```bash
java --version
```

Affiche la version de Java utilisée pour exécuter les programmes.

```bash
javac --version
```

Affiche la version du compilateur Java.

```bash
echo $JAVA_HOME
```

Affiche le chemin de la variable d'environnement JAVA_HOME, si elle est configurée.

## 1.2 IntelliJ IDEA

IntelliJ est un IDE (*Integrated Development Environment*).

Il permet notamment :
- D'écrire du code avec coloration syntaxique.
- De détecter certaines erreurs.
- De naviguer entre les classes.
- De lancer et déboguer les applications.
- De gérer les projets Maven.

IntelliJ ne remplace ni Java, ni Maven, ni Git.

## 1.3 Maven

Maven est un outil de gestion et de construction de projets Java.

Il permet de :
- Télécharger les bibliothèques externes.
- Gérer les versions des dépendances.
- Compiler le projet.
- Exécuter les tests.
- Construire un fichier distribuable (JAR ou WAR).

Sans Maven, on devrait gérer manuellement une bonne partie de ces opérations.

---

# 2. Créer un projet Java avec Maven

Il existe plusieurs façons de créer un projet Maven :

- Utiliser l'assistant de création d'IntelliJ.
- Utiliser Maven directement depuis le terminal.
- Partir d'un projet existant.

Pour notre apprentissage, nous utilisons IntelliJ.

## 2.1 Configuration initiale

Lors de la création du projet :

| Paramètre    | Valeur           |
|--------------|------------------|
| Name         | infra-manager    |
| Build system | Maven            |
| JDK          | Java 21          |
| GroupId      | com.inframanager |
| ArtifactId   | infra-manager    |
| Version      | 1.0-SNAPSHOT     |

### Signification des paramètres

**Name**

Nom du projet dans l'IDE.

**GroupId**

Identifiant du groupe ou de l'organisation auquel appartient le projet.

Exemple : `com.inframanager`

**ArtifactId**

Identifiant du projet ou de l'artefact produit.

Exemple : `infra-manager`

**Version**

Version actuelle du projet.

Exemple : `1.0-SNAPSHOT`

Le suffixe SNAPSHOT indique une version de développement, susceptible d'évoluer.

## 2.2 Structure standard Maven

Maven utilise une convention d'organisation des fichiers.

```text
mon-projet/
│
├── pom.xml
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│       ├── java/
│       └── resources/
│
└── target/
```

Explication :

| Dossier / fichier  | Fonction                                |
|--------------------|-----------------------------------------|
| pom.xml            | Configuration du projet Maven           |
| src/main/java      | Code source Java de l'application       |
| src/main/resources | Fichiers de configuration et ressources |
| src/test/java      | Tests automatisés                       |
| src/test/resources | Ressources nécessaires aux tests        |
| target             | Résultats générés par Maven             |

**Important :** `target/` est généré automatiquement. On ne doit pas y écrire notre code.

---

# 3. Comprendre le fichier pom.xml

Le fichier `pom.xml` signifie *Project Object Model*.

C'est le fichier central de configuration de Maven.

Il définit :
- Les informations du projet.
- La version de Java.
- Les dépendances.
- Les plugins de construction.
- Les configurations de compilation et de tests.

Voici notre configuration actuelle :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.inframanager</groupId>
    <artifactId>infra-manager</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.release>21</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

</project>
```

## 3.1 Décomposition du XML

### Déclaration XML

```
<?xml version="1.0" encoding="UTF-8"?>
```

Indique la version XML et l'encodage du fichier.

### Élément project

```xml
<project>
    ...
</project>
```

Élément racine du fichier Maven.

Les autres éléments de configuration sont placés à l'intérieur.

### modelVersion

```xml
<modelVersion>4.0.0</modelVersion>
```

Version du modèle POM utilisé par Maven.

Ce n'est pas la version de Java ni celle de notre application.

### Identité du projet

```xml
<groupId>com.inframanager</groupId>
<artifactId>infra-manager</artifactId>
<version>1.0-SNAPSHOT</version>
```

Permet d'identifier précisément notre projet.

### Properties

```xml
<properties>
    <maven.compiler.release>21</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

`properties` permet de définir des propriétés réutilisables dans la configuration Maven.

Ici :

- `maven.compiler.release` indique la version Java cible pour la compilation.
- `project.build.sourceEncoding` définit l'encodage des sources.

## 3.2 Ajouter une dépendance

Une dépendance est une bibliothèque externe dont notre application a besoin.

Par exemple, lorsque nous utiliserons Spring Boot, nous ajouterons des dépendances dans cette section :

```xml
<dependencies>
    <!-- Les bibliothèques externes seront déclarées ici -->
</dependencies>
```

Maven téléchargera automatiquement les bibliothèques nécessaires depuis les dépôts configurés.

On ne télécharge donc pas manuellement chaque bibliothèque.

## 3.3 Les repositories

Un repository Maven est un dépôt contenant des bibliothèques et des artefacts.

Il en existe plusieurs types :

- Local : stocké sur notre machine.
- Central : dépôt public Maven Central.
- Distant : dépôt hébergé par une organisation ou un fournisseur.

Maven utilise notamment son repository local pour éviter de télécharger plusieurs fois les mêmes dépendances.

Par défaut, il se trouve généralement dans :

```text
~/.m2/repository/
```

---

# 4. Comprendre les commandes Maven

Maven fonctionne avec des phases appelées *Lifecycle*.

Les principales phases sont :

| Phase    | Fonction                                     |
|----------|----------------------------------------------|
| validate | Vérifie la structure et la configuration     |
| compile  | Compile le code principal                    |
| test     | Compile et exécute les tests                 |
| package  | Construit le JAR ou WAR                      |
| verify   | Effectue les vérifications supplémentaires   |
| install  | Installe l'artefact dans le repository local |
| deploy   | Publie l'artefact dans un repository distant |

### Commandes courantes

Compiler :

```bash
mvn compile
```

Nettoyer les anciens fichiers générés :

```bash
mvn clean
```

Nettoyer puis compiler :

```bash
mvn clean compile
```

Lancer les tests :

```bash
mvn test
```

Construire le projet :

```bash
mvn package
```

Installer le projet dans le repository local :

```bash
mvn install
```

### Comprendre BUILD SUCCESS

Lorsque Maven affiche :

```text
BUILD SUCCESS
```

Cela signifie que la commande demandée s'est terminée correctement.

Attention : `BUILD SUCCESS` après `mvn compile` ne signifie pas que l'application a été testée ou qu'elle fonctionne correctement. Cela signifie uniquement que la compilation a réussi.

---

# 5. Comprendre Git et GitHub

Maven gère la construction du projet.

Git gère l'historique de notre code.

GitHub héberge notre dépôt distant.

## 5.1 Les trois zones de Git

Git fonctionne avec trois zones principales :

1. Working directory : les fichiers sur lesquels on travaille.
2. Staging area : les modifications sélectionnées pour le prochain commit.
3. Repository : l'historique des commits enregistrés.

## 5.2 Initialiser un dépôt

Dans notre cas, le dépôt a déjà été créé sur GitHub puis cloné sur notre machine.

Pour un nouveau projet indépendant :

```bash
git init
```

Cette commande initialise un dépôt Git local.

Pour récupérer un dépôt existant :

```bash
git clone URL_DU_DEPOT
```

Cette commande télécharge le dépôt distant et crée une copie locale.

## 5.3 Vérifier l'état du dépôt

```bash
git status
```

Affiche les fichiers modifiés, supprimés ou non suivis.

## 5.4 Préparer des modifications

```bash
git add .
```

Ajoute les modifications au staging.

On peut aussi sélectionner un fichier :

```bash
git add pom.xml
```

## 5.5 Enregistrer un commit

```bash
git commit -m "chore: initialize Java Maven project"
```

Un commit enregistre un état du projet dans son historique.

Les messages doivent décrire clairement les modifications.

Exemples :

```text
feat: add equipment management
fix: correct IP address validation
test: add equipment service tests
chore: update Maven configuration
```

## 5.6 Envoyer sur GitHub

```bash
git push
```

Envoie les commits locaux vers le dépôt distant.

Pour le premier push d'une branche :

```bash
git push -u origin main
```

`origin` représente le dépôt distant et `main` le nom de la branche.

## 5.7 Récupérer les modifications distantes

```bash
git pull
```

Récupère les modifications du dépôt distant et les intègre à la branche actuelle.

---

# 6. Comprendre le fichier .gitignore

Le fichier `.gitignore` indique à Git quels fichiers et dossiers ne doivent pas être suivis.

Exemple pour un projet Java avec IntelliJ et Maven :

```gitignore
# IntelliJ IDEA
.idea/
*.iml

# Maven
target/

# Java
*.class

# Logs
*.log

# Environnement
.env
```

Pourquoi ignorer ces fichiers ?

- `.idea/` : configuration locale de l'IDE.
- `target/` : fichiers générés automatiquement par Maven.
- `*.class` : fichiers compilés Java.
- `.env` : peut contenir des informations sensibles.

Attention : `.gitignore` n'efface pas les fichiers déjà suivis par Git. Il empêche principalement les nouveaux fichiers correspondants d'être ajoutés.

---

# 7. Démarrer le développement Java

Une fois le projet configuré, on peut commencer à développer.

Dans :

```text
src/main/java/
```

On crée notre package principal :

```text
com/inframanager/
```

Puis notre classe principale :

```java
package com.inframanager;

public class Main { 
    
   /* problème mettre public en V21 mais soulève une erreur en V27*/
   static void main(String[] args) {
        System.out.println("Infra Manager started!");
    }

}
```

Cette classe contient le point d'entrée de notre application Java.

La méthode `main` est le point de départ classique d'un programme Java autonome.

Pour un projet Java simple, on peut ensuite créer d'autres packages :

```text
com/inframanager/
├── Main.java
├── model/
├── service/
├── repository/
└── util/
```

L'organisation exacte dépendra de l'architecture choisie.

---

# 8. Workflow de développement quotidien

Voici le cycle de travail classique sur notre projet :

### 1. Récupérer les dernières modifications

```bash
git pull
```

### 2. Développer une fonctionnalité

Créer ou modifier les classes Java nécessaires.

### 3. Compiler

```bash
mvn compile
```

### 4. Exécuter les tests

```bash
mvn test
```

### 5. Vérifier les modifications

```bash
git status
```

### 6. Préparer les modifications

```bash
git add .
```

### 7. Créer un commit

```bash
git commit -m "feat: describe the new feature"
```

### 8. Envoyer les modifications sur GitHub

```bash
git push
```

---

# 9. Les prochaines étapes de formation

Voici les compétences à acquérir progressivement pour pouvoir créer un projet Java professionnel en autonomie.

| Étape | Compétence         | Objectif                              |
|-------|--------------------|---------------------------------------|
| 1     | Java et JDK        | Comprendre l'environnement Java       |
| 2     | IntelliJ           | Créer, organiser et lancer un projet  |
| 3     | Maven              | Gérer les dépendances et compiler     |
| 4     | Git                | Versionner le code                    |
| 5     | Packages           | Organiser les classes                 |
| 6     | POO et SOLID       | Concevoir une application maintenable |
| 7     | JUnit              | Écrire des tests automatisés          |
| 8     | HTTP et REST       | Comprendre les API backend            |
| 9     | Spring Boot        | Construire une API Java               |
| 10    | PostgreSQL         | Stocker et manipuler les données      |
| 11    | JPA / Hibernate    | Relier Java à la base de données      |
| 12    | Sécurité           | Authentification et autorisations     |
| 13    | Docker             | Conteneuriser l'application           |
| 14    | Clean Architecture | Structurer un projet professionnel    |

---

# 10. Ce qu'il faut savoir faire seul à terme

À la fin de cette première partie, tu devrais être capable de :

- Installer et vérifier un JDK.
- Créer un projet Java avec IntelliJ.
- Expliquer le rôle de Maven.
- Comprendre les grandes sections d'un `pom.xml`.
- Ajouter une dépendance Maven.
- Expliquer la structure `src/main` et `src/test`.
- Compiler un projet avec Maven.
- Créer un dépôt Git.
- Utiliser `add`, `commit`, `push`, `pull` et `status`.
- Configurer un `.gitignore`.
- Créer une classe Java et lancer son `main`.

**Important :** l'objectif n'est pas de connaître toutes les commandes par cœur. L'objectif est de comprendre ce qu'elles font, de savoir quand les utiliser et de pouvoir retrouver leur syntaxe dans cette documentation.

---

# Conclusion

Un projet Java professionnel ne se résume pas à écrire des classes et des méthodes.

Il faut aussi savoir organiser son code, gérer les dépendances, tester son application et suivre son évolution.

Maven, Git, GitHub et IntelliJ sont des outils qui facilitent ce travail.

Nous allons apprendre à les utiliser progressivement en développant Infra Manager, sans séparer artificiellement la théorie de la pratique.