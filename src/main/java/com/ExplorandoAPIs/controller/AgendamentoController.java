package com.ExplorandoAPIs.controller;

import com.ExplorandoAPIs.dto.AgendamentoRequestDTO;
import com.ExplorandoAPIs.model.Agendamento;
import com.ExplorandoAPIs.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Agendamento> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoRequestDTO dto) {

        Agendamento agendamento = toEntity(dto);
        return ResponseEntity.ok(service.atualizar(id, agendamento));
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