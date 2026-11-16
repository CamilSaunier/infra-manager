# Comprendre `static`, les instances et les DTO en Java

Cette fiche reprend les notions revues ensemble dans le projet
**infra-manager**.

## 1. Qu'est-ce qu'une instance ?

Une classe est un modèle qui décrit un type d'objet.

Par exemple, `Equipment` décrit les informations et le comportement d'un
équipement réseau.

Pour créer un objet concret à partir de cette classe, on utilise le
mot-clé `new` :

``` java
Equipment equipment = new Equipment(
    "Routeur principal",
    "Cisco",
    "Catalyst 9200"
);
```

Dans cet exemple :

-   `Equipment` est le type de la variable.
-   `equipment` est le nom de la variable.
-   `new Equipment(...)` crée une nouvelle instance de la classe
    `Equipment`.
-   Les valeurs entre parenthèses sont transmises au constructeur.

Une **instance** est donc un objet concret créé à partir d'une classe.

## 2. Qu'est-ce que `static` ?

Une méthode déclarée `static` appartient à la classe elle-même, et non à
une instance particulière.

Exemple :

``` java
public class Calculator {

    public static int add(int a, int b) {
        return a + b;
    }
}
```

On peut appeler cette méthode directement depuis la classe :

``` java
int result = Calculator.add(5, 3);
```

On n'a pas besoin de créer un objet `Calculator` pour appeler cette
méthode.

### Une méthode non statique

``` java
public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }
}
```

Cette fois, la méthode appartient à une instance. Il faut donc créer un
objet avant de l'appeler :

``` java
Calculator calculator = new Calculator();

int result = calculator.add(5, 3);
```

### À retenir

  -----------------------------------------------------------------------
  Méthode `static`                    Méthode non `static`
  ----------------------------------- -----------------------------------
  Appartient à la classe              Appartient à une instance

  Appel : `Classe.methode()`          Appel : `objet.methode()`

  Pas besoin de créer une instance    Il faut disposer d'une instance
  pour appeler la méthode             pour appeler la méthode
  -----------------------------------------------------------------------

Attention : `static` ne signifie pas qu'une méthode ne peut pas
manipuler d'objets. Une méthode statique peut recevoir un objet en
paramètre, utiliser ses méthodes et même créer de nouveaux objets.

## 3. Une méthode `static` peut créer un objet

Prenons cet exemple :

``` java
public class User {

    public static User createUser() {
        return new User();
    }
}
```

La méthode `createUser()` est statique : on peut l'appeler directement
depuis la classe.

Mais à l'intérieur de cette méthode, `new User()` crée bien une nouvelle
instance de `User`.

Il faut donc distinguer deux opérations :

1.  `static` permet d'appeler la méthode depuis la classe.
2.  `new` crée un nouvel objet.

## 4. Qu'est-ce qu'un DTO ?

DTO signifie **Data Transfer Object**, c'est-à-dire « objet de transfert
de données ».

Un DTO sert à transporter des données entre différentes parties d'une
application, par exemple entre notre API et le client qui l'utilise.

Dans notre projet, nous avons deux objets qui ont des rôles différents :

### `Equipment` : l'objet métier

La classe `Equipment` représente un équipement dans notre application.

Elle contient ses données et les règles métier associées. Par exemple :

-   vérifier que le nom n'est pas vide ;
-   vérifier que le fabricant et le modèle sont renseignés ;
-   vérifier que l'adresse IP est valide.

Elle protège donc la cohérence de nos objets métier.

### `EquipmentResponse` : le DTO de réponse

`EquipmentResponse` représente les données que nous choisissons
d'exposer dans une réponse de notre API.

Il peut reprendre certaines informations de `Equipment`, mais il n'a pas
pour rôle de porter les règles métier de l'équipement.

Cela permet notamment de choisir précisément les informations transmises
au client. Un champ interne à `Equipment` n'a pas besoin d'être présent
dans `EquipmentResponse`.

Les deux objets peuvent donc contenir des données similaires sans être
des doublons inutiles : ils répondent à des responsabilités différentes.

## 5. Pourquoi `EquipmentResponse` est-il un `record` ?

En Java, un `record` est un type conçu principalement pour transporter
des données.

Dans notre projet :

``` java
public record EquipmentResponse(
        Long id,
        String name,
        String manufacturer,
        String model,
        String ipAddress
) {
}
```

Java génère automatiquement plusieurs éléments :

-   un constructeur correspondant aux composants déclarés ;
-   des accesseurs ;
-   les méthodes `equals()`, `hashCode()` et `toString()`.

Par exemple, on peut accéder au nom avec :

``` java
response.name();
```

Avec une classe Java classique, on utilise souvent un getter comme
`getName()`. Avec un record, l'accesseur porte directement le nom du
composant : `name()`.

Les composants d'un record sont finals : on ne peut pas les réaffecter
après la création du record. Un record est donc adapté à un objet de
données que l'on souhaite garder immuable.

Un record peut aussi contenir des méthodes. Il n'est pas limité à une
simple liste de champs.

## 6. La méthode de mapping `EquipmentResponse.from()`

Voici le code que nous avons créé :

``` java
package com.inframanager.equipment.dto;

import com.inframanager.equipment.domain.Equipment;

public record EquipmentResponse(
        Long id,
        String name,
        String manufacturer,
        String model,
        String ipAddress
) {

    public static EquipmentResponse from(Equipment equipment) {

        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getManufacturer(),
                equipment.getModel(),
                equipment.getIpAddress()
        );
    }
}
```

Cette méthode réalise un **mapping** : elle transforme les données d'un
objet `Equipment` en un nouvel objet `EquipmentResponse`.

Décomposons sa déclaration :

``` java
public static EquipmentResponse from(Equipment equipment)
```

-   `public` : la méthode est accessible depuis les autres classes.
-   `static` : on peut l'appeler directement depuis la classe
    `EquipmentResponse`.
-   `EquipmentResponse` : le type de valeur que la méthode retourne.
-   `from` : le nom de la méthode.
-   `Equipment equipment` : l'objet `Equipment` reçu en paramètre.

On l'appelle ainsi :

``` java
EquipmentResponse response = EquipmentResponse.from(equipment);
```

On utilise `EquipmentResponse` parce que la méthode `from()` appartient
à cette classe. On ne l'appelle donc pas depuis l'instance `equipment`.

### Que fait la méthode exactement ?

1.  Elle reçoit une instance de `Equipment`.
2.  Elle récupère les valeurs avec les getters de cette instance.
3.  Elle crée un nouvel objet avec `new EquipmentResponse(...)`.
4.  Elle retourne ce nouvel objet avec `return`.

Le `new` est nécessaire pour construire le DTO. Le fait que `from()`
soit `static` n'empêche absolument pas la création d'objets à
l'intérieur de la méthode.

## 7. Attention à l'ordre des opérations

Nous avons rencontré un petit problème dans notre test, qui permet de
comprendre un point important.

Ce code est dans le mauvais ordre :

``` java
EquipmentResponse response = EquipmentResponse.from(equipment);

equipment.setIpAddress("192.168.1.1");
```

Au moment où `from()` est appelée, l'adresse IP de `equipment` vaut
encore `null`. Le DTO reçoit donc `null`.

Modifier l'entité après la création du DTO ne met pas automatiquement le
DTO à jour : ce sont deux objets distincts.

Il faut renseigner l'équipement avant de construire le DTO :

``` java
equipment.setIpAddress("192.168.1.1");

EquipmentResponse response = EquipmentResponse.from(equipment);
```

Le DTO récupère ainsi l'adresse IP renseignée.

## 8. Résumé des notions

  -----------------------------------------------------------------------
  Notion                              À retenir
  ----------------------------------- -----------------------------------
  Classe                              Modèle qui décrit un type d'objet

  Instance                            Objet concret créé à partir d'une
                                      classe

  `new`                               Crée une nouvelle instance

  `static`                            Permet d'appeler une méthode
                                      directement depuis sa classe

  DTO                                 Objet qui transporte des données

  `record`                            Type Java adapté aux objets de
                                      données, avec constructeur et
                                      accesseurs générés

  Mapping                             Transformation des données d'un
                                      objet vers un autre

  `return`                            Renvoie une valeur depuis une
                                      méthode
  -----------------------------------------------------------------------

### Les deux lignes à bien distinguer

``` java
Equipment equipment = new Equipment(...);
```

Crée une instance de la classe métier `Equipment`.

``` java
EquipmentResponse response = EquipmentResponse.from(equipment);
```

Appelle une méthode statique de `EquipmentResponse`, qui crée et
retourne un nouvel objet DTO à partir de l'équipement.

------------------------------------------------------------------------

## Où nous en sommes dans le projet

Nous avons créé et testé `EquipmentResponse` ainsi que sa méthode de
mapping `from()`.

La prochaine étape est d'utiliser ce DTO dans `EquipmentController`,
afin que notre endpoint `/api/equipment` retourne un objet que Spring
Boot pourra sérialiser en JSON.
