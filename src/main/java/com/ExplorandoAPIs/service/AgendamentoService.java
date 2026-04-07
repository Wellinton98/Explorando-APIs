package com.ExplorandoAPIs.service;

import com.ExplorandoAPIs.dto.AgendamentoRequestDTO;
import com.ExplorandoAPIs.exception.AgendamentoException;
import com.ExplorandoAPIs.model.Agendamento;
import com.ExplorandoAPIs.model.StatusAgendamento;
import com.ExplorandoAPIs.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final RestTemplate restTemplate;

    public AgendamentoService(AgendamentoRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    // 🔥 NOVO MÉTODO COM DTO
    public Agendamento criar(AgendamentoRequestDTO dto) {

      
        //BUSCAR CLIENTE
     
        String urlCliente = "https://api-clientes.com/clientes/" + dto.getClienteId();

        Map<String, Object> cliente =
                restTemplate.getForObject(urlCliente, Map.class);

        if (cliente == null) {
            throw new AgendamentoException("Cliente não encontrado");
        }

       
        //BUSCAR SERVIÇO
      
        String urlServico = "https://api-servicos.com/servicos/" + dto.getServicoId();

        Map<String, Object> servico =
                restTemplate.getForObject(urlServico, Map.class);

        if (servico == null) {
            throw new AgendamentoException("Serviço não encontrado");
        }

      
        //MONTAR AGENDAMENTO
       
        Agendamento agendamento = new Agendamento();

        agendamento.setData(dto.getData());
        agendamento.setHorario(dto.getHorario());

        // vindo da API externa
        agendamento.setClienteNome((String) cliente.get("nome"));
        agendamento.setClienteTelefone((String) cliente.get("telefone"));

        agendamento.setServicoNome((String) servico.get("descricao"));
        agendamento.setValorServico(
                Double.valueOf(servico.get("valor").toString())
        );

        // vindo do DTO
        agendamento.setProfissionalNome(dto.getProfissionalNome());
        agendamento.setObservacao(dto.getObservacao());

        
        //SUA VALIDAÇÃO ORIGINAL
       
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

    public Agendamento atualizar(Long id, Agendamento novo) {

        Agendamento existente = buscarPorId(id);

        validarAgendamento(novo, id);

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