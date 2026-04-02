package com.ExplorandoAPIs.model;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer idAgendamento;

    private LocalDate dataAgendamento;
    private LocalDate dataFimAgendamento;
    private String nome;
    private String cpf;


}