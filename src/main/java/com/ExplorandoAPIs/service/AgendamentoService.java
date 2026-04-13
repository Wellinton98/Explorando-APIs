
package com.ExplorandoAPIs.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ExplorandoAPIs.dto.AgendamentoRequestDTO;
import com.ExplorandoAPIs.dto.AgendamentoUpdateDTO;
import com.ExplorandoAPIs.exception.AgendamentoException;
import com.ExplorandoAPIs.model.Agendamento;
import com.ExplorandoAPIs.model.enums.StatusAgendamento;
import com.ExplorandoAPIs.repository.AgendamentoRepository;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final RestTemplate restTemplate;

    public AgendamentoService(AgendamentoRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public Agendamento criar(AgendamentoRequestDTO dto) {

        //MOCK CLIENTE (TEMPORÁRIO)
        /*
        String urlCliente = "https://api-clientes.com/clientes/" + dto.getClienteId();

        Map<String, Object> cliente =
                restTemplate.getForObject(urlCliente, Map.class);

        if (cliente == null) {
            throw new AgendamentoException("Cliente não encontrado");
        }
        */

        Map<String, Object> cliente = Map.of(
                "nome", "Cliente Teste",
                "telefone", "999999999"
        );

        // MOCK SERVIÇO (TEMPORÁRIO)
        /*
        String urlServico = "https://api-servicos.com/servicos/" + dto.getServicoId();

        Map<String, Object> servico =
                restTemplate.getForObject(urlServico, Map.class);

        if (servico == null) {
            throw new AgendamentoException("Serviço não encontrado");
        }
        */

        Map<String, Object> servico = Map.of(
                "descricao", "Corte de cabelo",
                "valor", 50.0
        );

        //MONTAR AGENDAMENTO

        Agendamento agendamento = new Agendamento();

        agendamento.setData(dto.getData());
        agendamento.setHorario(dto.getHorario());

        agendamento.setClienteNome((String) cliente.get("nome"));
        agendamento.setClienteTelefone((String) cliente.get("telefone"));

        agendamento.setServicoNome((String) servico.get("descricao"));
        agendamento.setValorServico(
                Double.valueOf(servico.get("valor").toString())
        );

        agendamento.setProfissionalNome(dto.getProfissionalNome());
        agendamento.setObservacao(dto.getObservacao());

        // SUA VALIDAÇÃO ORIGINAL

        validarAgendamento(agendamento, null);

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
    
    public Agendamento atualizar(Long id, AgendamentoUpdateDTO dto) {

        Agendamento agendamento = buscarPorId(id);

        if (dto.getData() != null) {
            agendamento.setData(dto.getData());
        }

        if (dto.getHorario() != null) {
            agendamento.setHorario(dto.getHorario());
        }

        if (dto.getClienteNome() != null) {
            agendamento.setClienteNome(dto.getClienteNome());
        }

        if (dto.getClienteTelefone() != null) {
            agendamento.setClienteTelefone(dto.getClienteTelefone());
        }

        if (dto.getServicoNome() != null) {
            agendamento.setServicoNome(dto.getServicoNome());
        }

        if (dto.getProfissionalNome() != null) {
            agendamento.setProfissionalNome(dto.getProfissionalNome());
        }

        if (dto.getObservacao() != null) {
            agendamento.setObservacao(dto.getObservacao());
        }

        if (dto.getValorServico() != null) {
            agendamento.setValorServico(dto.getValorServico());
        }

        validarAgendamento(agendamento, id);

        agendamento.setAtualizadoEm(LocalDateTime.now());

        return repository.save(agendamento);
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

    // 🔥 NOVO MÉTODO CONCLUIR
    public Agendamento concluir(Long id) {

        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new AgendamentoException("Não é possível concluir um agendamento cancelado");
        }

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new AgendamentoException("Agendamento já está concluído");
        }

        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        agendamento.setAtualizadoEm(LocalDateTime.now());

        return repository.save(agendamento);
    }

    public void deletar(Long id) {

        if (!repository.existsById(id)) {
            throw new AgendamentoException("Agendamento não encontrado para exclusão");
        }

        repository.deleteById(id);
    }

    private void validarAgendamento(Agendamento agendamento, Long idAtual) {

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

        if (idAtual == null) {
            repository.findByDataAndHorario(agendamento.getData(), agendamento.getHorario())
                    .ifPresent(a -> {
                        throw new AgendamentoException("Horário já está ocupado");
                    });
        } else {
            repository.findByDataAndHorarioAndIdNot(
                    agendamento.getData(),
                    agendamento.getHorario(),
                    idAtual
            ).ifPresent(a -> {
                throw new AgendamentoException("Horário já está ocupado");
            });
        }
    }

    
}
