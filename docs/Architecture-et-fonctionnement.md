# Architecture et fonctionnement

## 1. Objectif du document

Ce document décrit l'architecture logicielle du projet **Infra Manager**, le rôle de ses différentes couches et les
choix techniques réalisés au cours du développement.

L'objectif est de construire une application backend maintenable, évolutive et testable, en appliquant progressivement
les bonnes pratiques de développement Java.

L'architecture retenue repose sur une organisation **Package by Feature**, avec une séparation des responsabilités au
sein de chaque fonctionnalité.

---

## 2. Organisation d'une fonctionnalité

Chaque fonctionnalité possède ses propres packages :

```text
equipment/
├── controller/
├── domain/
├── dto/
├── repository/
└── service/
```

Chaque package possède un rôle précis.

### domain

Contient les objets métier et leurs règles de validation.

Exemple :

- `Equipment` : représente un équipement informatique ou réseau.
- Validation des propriétés obligatoires.
- Validation du format des adresses IP.

Le domaine ne doit pas dépendre des technologies de stockage ou de présentation.

### service

Contient la logique applicative et orchestre les opérations métier.

Exemple :

- Création d'un équipement.
- Application des règles nécessaires à une opération.
- Coordination entre le domaine et les repositories.

Le service ne doit pas connaître les détails techniques de persistance.

### repository

Définit les contrats d'accès aux données.

Exemple :

- `EquipmentRepository` : définit les opérations disponibles sur les équipements.

Les implémentations concrètes seront chargées d'exécuter réellement les opérations de persistance.

### dto

Contient les objets utilisés pour échanger des données avec l'extérieur de l'application.

Les DTO permettent de contrôler les informations reçues et retournées par l'API sans exposer directement les objets
métier.

### controller

Contient les points d'entrée de l'API REST.

Les contrôleurs recevront les requêtes HTTP, transmettront les informations nécessaires aux services et construiront les
réponses adaptées.

---

## 3. Fonctionnement actuel de la création d'un équipement

La première opération développée est la création d'un équipement.

Le fonctionnement actuel est le suivant :

```text
EquipmentService
      |
      | createEquipment()
      v
Equipment
      |
      | Construction et validation
      v
EquipmentRepository
      |
      | save()
      v
Implémentation de persistance
      |
      v
Base de données
```

À ce stade, le contrat du repository et le service sont développés.

L'implémentation réelle de persistance et la connexion à PostgreSQL restent à mettre en place.

### Equipment

La classe `Equipment` représente notre modèle métier.

Elle contient notamment :

- Un identifiant (`id`).
- Un nom.
- Un fabricant.
- Un modèle.
- Une adresse IP facultative.

Les validations sont effectuées directement dans le domaine afin de garantir la cohérence de l'objet.

L'identifiant est destiné à être généré par la base de données lors de la persistance.

### EquipmentService

Le service orchestre la création d'un équipement.

```java
public Equipment createEquipment(
        String name,
        String manufacturer,
        String model
) {
    Equipment newEquipment = new Equipment(name, manufacturer, model);
    return equipmentRepository.save(newEquipment);
}
```

Le service :

1. Construit un nouvel équipement.
2. Laisse le domaine effectuer ses validations.
3. Transmet l'objet au repository pour son enregistrement.
   4