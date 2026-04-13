package com.ExplorandoAPIs.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ExplorandoAPIs.dto.AgendamentoRequestDTO;
import com.ExplorandoAPIs.dto.AgendamentoResponseDTO;
import com.ExplorandoAPIs.dto.AgendamentoUpdateDTO;
import com.ExplorandoAPIs.model.Agendamento;
import com.ExplorandoAPIs.service.AgendamentoService;

import jakarta.validation.Valid;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    // Conversão DTO -> Entity
    private Agendamento toEntity(AgendamentoRequestDTO dto) {

        Agendamento agendamento = new Agendamento();

        agendamento.setData(dto.getData());
        agendamento.setHorario(dto.getHorario());
        agendamento.setClienteNome(dto.getClienteNome());
        agendamento.setClienteTelefone(dto.getClienteTelefone());
        agendamento.setServicoNome(dto.getServicoNome());
        agendamento.setProfissionalNome(dto.getProfissionalNome());
        agendamento.setObservacao(dto.getObservacao());
        agendamento.setValorServico(dto.getValorServico());

        return agendamento;
    }

    @PostMapping
    public ResponseEntity<Agendamento> criar(
            @Valid @RequestBody AgendamentoRequestDTO dto) {

        Agendamento criado = service.criar(dto);
        return ResponseEntity.ok(criado);
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

  @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoUpdateDTO dto) {

        Agendamento atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(toResponseDTO(atualizado));
    }

    // Conversão Entity -> DTO de resposta
    private AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        return new AgendamentoResponseDTO(
        );
    }

   

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Agendamento> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    // NOVO MÉTODO
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Agendamento> concluir(@PathVariable Long id) {
        return ResponseEntity.ok(service.concluir(id));
    }
}