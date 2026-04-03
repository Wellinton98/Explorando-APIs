package com.ExplorandoAPIs.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AgendamentoResponseDTO {

    private Long id;
    private LocalDate data;
    private LocalTime horario;
    private Long clienteId;
    private Long servicoId;
    private String status;
    private String observacao;

    // Construtor vazio
    public AgendamentoResponseDTO() {
    }

    // Construtor completo
    public AgendamentoResponseDTO(Long id, LocalDate data, LocalTime horario,
                                  Long clienteId, Long servicoId,
                                  String status, String observacao) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}