package com.inframanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// l'annotation @SpringBootApplication indique à Spring que cete classe est le point de départ de l'application
// elle regroupe 3 mecanismes :
// @SpringBootConfiguration : identifie la configuration principale.
// @EnableAutoConfiguration : demande à Spring de configurer automatiquement les composants nécessaires selon les dépendances présentes.
// @ComponentScan : demande à Spring de rechercher les composants à gérer, notamment dans le package courant et ses sous-packages.
@SpringBootApplication
public class InfraManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfraManagerApplication.class, args);
    }
}