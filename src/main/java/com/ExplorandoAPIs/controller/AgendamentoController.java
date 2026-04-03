package com.ExplorandoAPIs.controller;

import com.ExplorandoAPIs.dto.AgendamentoRequestDTO;
import com.ExplorandoAPIs.dto.AgendamentoResponseDTO;
import com.ExplorandoAPIs.model.Agendamento;
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

    // Conversão Entity -> ResponseDTO
    private AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
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

    // Conversão RequestDTO -> Entity (somente mapeamento de dados)
    private Agendamento toEntity(AgendamentoRequestDTO dto) {

        Agendamento agendamento = new Agendamento();

        agendamento.setData(dto.getData());
        agendamento.setHorario(dto.getHorario());
        agendamento.setClienteId(dto.getClienteId());
        agendamento.setServicoId(dto.getServicoId());
        agendamento.setObservacao(dto.getObservacao());

        return agendamento;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criar(
            @Valid @RequestBody AgendamentoRequestDTO dto) {

        Agendamento agendamento = toEntity(dto);
        Agendamento criado = service.criar(agendamento);

        return ResponseEntity.ok(toResponseDTO(criado));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listar() {

        List<AgendamentoResponseDTO> lista = service.listar()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {

        Agendamento agendamento = service.buscarPorId(id);

        return ResponseEntity.ok(toResponseDTO(agendamento));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoRequestDTO dto) {

        Agendamento novo = toEntity(dto);
        Agendamento atualizado = service.atualizar(id, novo);

        return ResponseEntity.ok(toResponseDTO(atualizado));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}