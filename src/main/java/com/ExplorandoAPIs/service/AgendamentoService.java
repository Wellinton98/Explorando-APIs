package com.ExplorandoAPIs.service;

import com.ExplorandoAPIs.exception.AgendamentoException;
import com.ExplorandoAPIs.model.Agendamento;
import com.ExplorandoAPIs.model.StatusAgendamento;
import com.ExplorandoAPIs.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }

    public Agendamento criar(Agendamento agendamento) {

        validarAgendamento(agendamento);

        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setCriadoEm(LocalDateTime.now());
        agendamento.setAtualizadoEm(LocalDateTime.now());

        return repository.save(agendamento);
    }

    public List<Agendamento> listar() {
        return repository.findAll();
    }

    public Agendamento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AgendamentoException("Agendamento não encontrado"));
    }

    public Agendamento atualizar(Long id, Agendamento novo) {

        Agendamento existente = buscarPorId(id);

        validarAgendamento(novo);

        existente.setData(novo.getData());
        existente.setHorario(novo.getHorario());
        existente.setClienteNome(novo.getClienteNome());
        existente.setClienteTelefone(novo.getClienteTelefone());
        existente.setServicoNome(novo.getServicoNome());
        existente.setProfissionalNome(novo.getProfissionalNome());
        existente.setObservacao(novo.getObservacao());
        existente.setValorServico(novo.getValorServico());

        existente.setAtualizadoEm(LocalDateTime.now());

        return repository.save(existente);
    }

    public Agendamento cancelar(Long id) {

        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new AgendamentoException("Agendamento já está cancelado");
        }

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new AgendamentoException("Não é possível cancelar um agendamento concluído");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamento.setAtualizadoEm(LocalDateTime.now());

        return repository.save(agendamento);
    }

    public void deletar(Long id) {

        if (!repository.existsById(id)) {
            throw new AgendamentoException("Agendamento não encontrado para exclusão");
        }

        repository.deleteById(id);
    }

    private void validarAgendamento(Agendamento agendamento) {

        LocalDate hoje = LocalDate.now();
        LocalDate limiteFuturo = hoje.plusYears(1);

        if (agendamento.getData() == null ||
            agendamento.getHorario() == null ||
            agendamento.getClienteNome() == null ||
            agendamento.getClienteTelefone() == null ||
            agendamento.getServicoNome() == null ||
            agendamento.getProfissionalNome() == null) {

            throw new AgendamentoException("Campos obrigatórios não preenchidos");
        }

        if (agendamento.getData().isBefore(hoje)) {
            throw new AgendamentoException("Não é permitido agendar no passado");
        }

        if (agendamento.getData().isAfter(limiteFuturo)) {
            throw new AgendamentoException("Agendamento permitido até 1 ano no futuro");
        }

        if (agendamento.getHorario().isBefore(LocalTime.of(8, 0)) ||
            agendamento.getHorario().isAfter(LocalTime.of(20, 0))) {

            throw new AgendamentoException("Horário fora do funcionamento (08:00 às 20:00)");
        }

        repository.findByDataAndHorario(agendamento.getData(), agendamento.getHorario())
                .ifPresent(a -> {
                    throw new AgendamentoException("Horário já está ocupado");
                });
    }
}