package com.ExplorandoAPIs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExplorandoApIsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExplorandoApIsApplication.class, args);
		System.out.println("API de Agendamento iniciada com sucesso!");
		System.out.println("Endpoints disponíveis:");
		System.out.println("POST   /api/agendamentos");
		System.out.println("GET    /api/agendamentos");
		System.out.println("PUT    /api/agendamentos/{id}");
		System.out.println("DELETE /api/agendamentos/{id}");

	}

}
