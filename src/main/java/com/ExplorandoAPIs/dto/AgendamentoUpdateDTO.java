package com.ExplorandoAPIs.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class AgendamentoUpdateDTO {

    private LocalDate data;

   
    private LocalTime horario;

    
    private String clienteNome;

   
    private String clienteTelefone;

   
    private String servicoNome;

   
    private String profissionalNome;

    private String observacao;

    
    @Positive(message = "Valor deve ser maior que zero")
    private Double valorServico;

   
    private Long clienteId;

 
    private Long servicoId;

    // getters e setters

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

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getClienteTelefone() {
        return clienteTelefone;
    }

    public void setClienteTelefone(String clienteTelefone) {
        this.clienteTelefone = clienteTelefone;
    }

    public String getServicoNome() {
        return servicoNome;
    }

    public void setServicoNome(String servicoNome) {
        this.servicoNome = servicoNome;
    }

    public String getProfissionalNome() {
        return profissionalNome;
    }

    public void setProfissionalNome(String profissionalNome) {
        this.profissionalNome = profissionalNome;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Double getValorServico() {
        return valorServico;
    }

    public void setValorServico(Double valorServico) {
        this.valorServico = valorServico;
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
}