package com.ExplorandoAPIs.controller;

import com.ExplorandoAPIs.dto.AgendamentoRequestDTO;
import com.ExplorandoAPIs.dto.AgendamentoResponseDTO;
import com.ExplorandoAPIs.model.Agendamento;
import com.ExplorandoAPIs.model.StatusAgendamento;
import com.ExplorandoAPIs.service.AgendamentoService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    // Converter Entity para ResponseDTO
    private AgendamentoResponseDTO converterParaDTO(Agendamento agendamento) {
        return new AgendamentoResponseDTO(
                agendamento.getId(),
                agendamento.getData(),
                agendamento.getHorario(),
                agendamento.getClienteId(),
                agendamento.getServicoId(),
                agendamento.getStatus().name(),
                agendamento.getObservacao()
        );
    }

    // Converter DTO para Entity
   private Agendamento converterParaEntity(AgendamentoRequestDTO dto) {

    //  Não aceita null ou vazio
    if (dto.getStatus() == null || dto.getStatus().isBlank()) {
        throw new RuntimeException("O status é obrigatório");
    }

    StatusAgendamento statusConvertido;

    try {
        statusConvertido = StatusAgendamento.valueOf(dto.getStatus().toUpperCase());
    } catch (Exception e) {
        throw new RuntimeException("Status inválido. Use: AGENDADO, CANCELADO ou CONCLUIDO");
    }

    Agendamento agendamento = new Agendamento();
    agendamento.setData(dto.getData());
    agendamento.setHorario(dto.getHorario());
    agendamento.setClienteId(dto.getClienteId());
    agendamento.setServicoId(dto.getServicoId());
    agendamento.setStatus(statusConvertido);
    agendamento.setObservacao(dto.getObservacao());

    return agendamento;
}
    // POST criar
    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criar(@Valid @RequestBody AgendamentoRequestDTO dto) {

        Agendamento agendamento = converterParaEntity(dto);
        Agendamento salvo = service.criar(agendamento);

        return ResponseEntity.ok(converterParaDTO(salvo));
    }

    // GET listar todos
    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listar() {

        List<AgendamentoResponseDTO> lista = service.listar()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // GET por ID
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {

        Agendamento agendamento = service.buscarPorId(id);
        return ResponseEntity.ok(converterParaDTO(agendamento));
    }

    // PUT atualizar
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoRequestDTO dto) {

        Agendamento novo = converterParaEntity(dto);
        Agendamento atualizado = service.atualizar(id, novo);

        return ResponseEntity.ok(converterParaDTO(atualizado));
    }

    // DELETE excluir
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        service.deletar(id);
        return ResponseEntity.noContent().build();
    
    }
}