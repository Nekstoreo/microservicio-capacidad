package com.onclass.capacidad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Microservicio Capacidad",
        version = "0.0.1",
        description = "API para gestión de capacidades técnicas"
    )
)
@SpringBootApplication
public class CapacidadApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapacidadApplication.class, args);
	}

}
