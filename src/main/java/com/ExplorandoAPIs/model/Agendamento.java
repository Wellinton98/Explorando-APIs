package com.ExplorandoAPIs.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity // Define que é uma entidade do banco
@Table(name = "agendamentos") // Nome da tabela
public class Agendamento {

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long id;

    @Column(nullable = false)
    private LocalDate data; // Data do agendamento

    @Column(nullable = false)
    private LocalTime horario; // Horário do agendamento

    @Column(nullable = false)
    private Long clienteId; // ID da API de Clientes

    @Column(nullable = false)
    private Long servicoId; // ID da API de Serviços

    @Enumerated(EnumType.STRING) // Salva o enum como texto
    @Column(nullable = false)
    private StatusAgendamento status; // Status do agendamento

    @Column(length = 200)
    private String observacao; // Campo opcional

    // Construtor vazio (obrigatório para o JPA)
    public Agendamento() {
    }

    // Construtor completo
    public Agendamento(Long id, LocalDate data, LocalTime horario,
                        Long clienteId, Long servicoId,
                        StatusAgendamento status, String observacao) {
        this.id = id;
        this.data = data;
        this.horario = horario;
        this.clienteId = clienteId;
        this.servicoId = servicoId;
        this.status = status;
        this.observacao = observacao;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}