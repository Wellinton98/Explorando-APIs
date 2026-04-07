package com.ExplorandoAPIs.integraçao;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.ExplorandoAPIs.dto.AgendamentoRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AgendamentoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // DTO COMPLETO E CORRETO
    private AgendamentoRequestDTO criarDTO(int hora) {
        AgendamentoRequestDTO dto = new AgendamentoRequestDTO();
        dto.setData(LocalDate.now().plusDays(1));
        dto.setHorario(LocalTime.of(hora, 0));
        dto.setClienteNome("Mateus");
        dto.setClienteTelefone("91-92859742");
        dto.setServicoNome("Corte");
        dto.setProfissionalNome("Wellinton");
        dto.setObservacao("Corte de cabelo");
        dto.setValorServico(50.0);
        dto.setClienteId(1L);
        dto.setServicoId(1L);
        return dto;
    }

    @Test
    void deveCriarAgendamento() throws Exception {
        var dto = criarDTO(10);

        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                // CREATE usa MOCK
                .andExpect(jsonPath("clienteNome").value("Cliente Teste"))
                .andExpect(jsonPath("status").value("AGENDADO"));
    }

    @Test
    void deveListarAgendamentos() throws Exception {
        mockMvc.perform(get("/agendamentos"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorId() throws Exception {
        var dto = criarDTO(11);

        String response = mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/agendamentos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id));
    }

    @Test
    void deveAtualizarAgendamento() throws Exception {
        var dto = criarDTO(12);

        String response = mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // ALTERA PELO DTO
        dto.setClienteNome("Maira");

        mockMvc.perform(put("/agendamentos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                // UPDATE usa DTO
                .andExpect(jsonPath("clienteNome").value("Maira"));
    }

    @Test
    void deveCancelarAgendamento() throws Exception {
        var dto = criarDTO(13);

        String response = mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(patch("/agendamentos/{id}/cancelar", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("CANCELADO"));
    }

    @Test
    void deveDeletarAgendamento() throws Exception {
        var dto = criarDTO(14);

        String response = mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/agendamentos/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void naoDeveCriarAgendamentoNoPassado() throws Exception {
        var dto = criarDTO(15);
        dto.setData(LocalDate.now().minusDays(1));

        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDevePermitirHorarioDuplicado() throws Exception {
        var dto = criarDTO(16);

        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}