package com.ExplorandoAPIs.unitario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ExplorandoAPIs.model.StatusAgendamento;
import com.ExplorandoAPIs.controller.AgendamentoController;
import com.ExplorandoAPIs.dto.AgendamentoRequestDTO;
import com.ExplorandoAPIs.model.Agendamento;
import com.ExplorandoAPIs.service.AgendamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgendamentoController.class)
class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendamentoService service;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock de retorno
    private Agendamento criarAgendamento() {
        Agendamento ag = new Agendamento();
        ag.setId(1L);
        ag.setClienteNome("Mateus");
        ag.setStatus(StatusAgendamento.AGENDADO);
        return ag;
    }

    // DTO COMPLETO (resolve TODOS os 400)
    private AgendamentoRequestDTO criarDTOCompleto() {
        AgendamentoRequestDTO dto = new AgendamentoRequestDTO();
        dto.setData(LocalDate.now().plusDays(1));
        dto.setHorario(LocalTime.of(10, 0));
        dto.setClienteNome("Mateus");
        dto.setClienteTelefone("999999999");
        dto.setServicoNome("Corte");
        dto.setProfissionalNome("Wellinton");
        dto.setObservacao("Teste");
        dto.setValorServico(50.0);

        //CAMPOS OBRIGATÓRIOS QUE FALTAVAM
        dto.setClienteId(1L);
        dto.setServicoId(1L);

        return dto;
    }

    @Test
    void deveCriarAgendamento() throws Exception {

        Mockito.when(service.criar(Mockito.any()))
                .thenReturn(criarAgendamento());

        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarDTOCompleto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteNome").value("Mateus"));
    }

    @Test
    void deveListarAgendamentos() throws Exception {

        Mockito.when(service.listar())
                .thenReturn(List.of(criarAgendamento()));

        mockMvc.perform(get("/agendamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteNome").value("Mateus"));
    }

    @Test
    void deveBuscarPorId() throws Exception {

        Mockito.when(service.buscarPorId(1L))
                .thenReturn(criarAgendamento());

        mockMvc.perform(get("/agendamentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveAtualizarAgendamento() throws Exception {

        var atualizado = new Agendamento();
        atualizado.setId(1L);
        atualizado.setClienteNome("Maira");
        atualizado.setStatus(StatusAgendamento.AGENDADO);

        Mockito.when(service.atualizar(Mockito.eq(1L), Mockito.any()))
                .thenReturn(atualizado);

        mockMvc.perform(put("/agendamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarDTOCompleto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteNome").value("Maira"));
    }

    @Test
    void deveDeletarAgendamento() throws Exception {

        mockMvc.perform(delete("/agendamentos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveCancelarAgendamento() throws Exception {

        var cancelado = criarAgendamento();
        cancelado.setStatus(StatusAgendamento.CANCELADO);

        Mockito.when(service.cancelar(1L))
                .thenReturn(cancelado);

        mockMvc.perform(patch("/agendamentos/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADO"));
    }

    @Test
    void deveConcluirAgendamento() throws Exception {

        var concluido = criarAgendamento();
        concluido.setStatus(StatusAgendamento.CONCLUIDO);

        Mockito.when(service.concluir(1L))
                .thenReturn(concluido);

        mockMvc.perform(patch("/agendamentos/1/concluir"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDO"));
    }
}