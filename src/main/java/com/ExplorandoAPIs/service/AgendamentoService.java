package com.ExplorandoAPIs.service;

import com.ExplorandoAPIs.model.Agendamento;
import com.ExplorandoAPIs.model.StatusAgendamento;
import com.ExplorandoAPIs.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }

    // 🔹 Método auxiliar para tratar status (aceita minúsculo)
    private StatusAgendamento converterStatus(String status) {
        try {
            return StatusAgendamento.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Status inválido. Use: AGENDADO, CANCELADO ou CONCLUIDO");
        }
    }

    // 🔹 Criar agendamento
    public Agendamento criar(Agendamento agendamento) {

        LocalDate hoje = LocalDate.now();
        LocalDate limiteFuturo = hoje.plusYears(1);

        //  Nulls
        if (agendamento.getData() == null || agendamento.getHorario() == null ||
            agendamento.getClienteId() == null || agendamento.getServicoId() == null) {
            throw new RuntimeException("Campos obrigatórios não preenchidos");
        }

        //  Data passada
        if (agendamento.getData().isBefore(hoje)) {
            throw new RuntimeException("Não é permitido agendar no passado");
        }

        //  Data muito no futuro
        if (agendamento.getData().isAfter(limiteFuturo)) {
            throw new RuntimeException("Agendamento permitido até 1 ano no futuro");
        }

        //  Horário inválido
        if (agendamento.getHorario().isBefore(java.time.LocalTime.of(8, 0)) ||
            agendamento.getHorario().isAfter(java.time.LocalTime.of(20, 0))) {
            throw new RuntimeException("Horário fora do funcionamento (08:00 às 20:00)");
        }

        //  Horário duplicado
        repository.findByDataAndHorario(agendamento.getData(), agendamento.getHorario())
                .ifPresent(a -> {
                    throw new RuntimeException("Horário já está ocupado");
                });

        //  Se vier status, converte (aceita minúsculo)
        if (agendamento.getStatus() != null) {
            agendamento.setStatus(
                converterStatus(agendamento.getStatus().name())
            );
        } else {
            agendamento.setStatus(StatusAgendamento.AGENDADO);
        }

        return repository.save(agendamento);
    }

    // 🔹 Listar
    public List<Agendamento> listar() {
        return repository.findAll();
    }

    // 🔹 Buscar por ID
    public Agendamento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
    }

    // 🔹 Atualizar
    public Agendamento atualizar(Long id, Agendamento novo) {

        Agendamento existente = buscarPorId(id);

        LocalDate hoje = LocalDate.now();
        LocalDate limiteFuturo = hoje.plusYears(1);

        //  Data inválida
        if (novo.getData().isBefore(hoje)) {
            throw new RuntimeException("Não pode atualizar para data passada");
        }

        if (novo.getData().isAfter(limiteFuturo)) {
            throw new RuntimeException("Data muito distante");
        }

        //  Horário duplicado
        repository.findByDataAndHorario(novo.getData(), novo.getHorario())
                .ifPresent(a -> {
                    if (!a.getId().equals(id)) {
                        throw new RuntimeException("Horário já ocupado");
                    }
                });

        existente.setData(novo.getData());
        existente.setHorario(novo.getHorario());
        existente.setClienteId(novo.getClienteId());
        existente.setServicoId(novo.getServicoId());
        existente.setObservacao(novo.getObservacao());

    

        return repository.save(existente);

    }

    // 🔹 Cancelar
    public Agendamento cancelar(Long id) {

        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new RuntimeException("Agendamento já está cancelado");
        }

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new RuntimeException("Não é possível cancelar um agendamento concluído");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);

        return repository.save(agendamento);
    }

    // 🔹 Deletar
    public void deletar(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Agendamento não encontrado para exclusão");
        }

        repository.deleteById(id);
    }
    
}