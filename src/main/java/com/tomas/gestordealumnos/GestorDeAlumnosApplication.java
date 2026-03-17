package com.tomas.gestordealumnos;

import org.springdoc.core.configuration.SpringDocHateoasConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {SpringDocHateoasConfiguration.class})
public class GestorDeAlumnosApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestorDeAlumnosApplication.class, args);
    }

}
