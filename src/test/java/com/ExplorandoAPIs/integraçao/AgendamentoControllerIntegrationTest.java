package com.ExplorandoAPIs.integração;

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
class AgendamentoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
        // comflito de horario, ele não permite criar ou atualizar para um horário já existente, 
        //para corrigir, é necessário criar um novo DTO com um horário diferente para cada teste que envolva 
        //criação ou atualização de agendamento
        //
    private AgendamentoRequestDTO criarDTO() {
        AgendamentoRequestDTO dto = new AgendamentoRequestDTO();
        dto.setData(LocalDate.now().plusDays(1));
        dto.setHorario(LocalTime.of(11, 0));
        dto.setClienteNome("Mateus");
        dto.setClienteTelefone("91-92859742");
        dto.setServicoNome("Corte");
        dto.setProfissionalNome("Wellinton");
        dto.setObservacao("Corte de cabelo");
        dto.setValorServico(50.0);
        return dto;
    }

    @Test
    void deveCriarAgendamento() throws Exception {
        var dto = criarDTO();

        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("clienteNome").value("Mateus"))
                .andExpect(jsonPath("status").value("AGENDADO"));
    }

    @Test
    void deveListarAgendamentos() throws Exception {
        mockMvc.perform(get("/agendamentos"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorId() throws Exception {
        var dto = criarDTO();

        String response = mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/agendamentos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id));
    }
  // erro ao atualizar, devido a validação de horário duplicado, o teste tenta atualizar para um horário já existente
  // para corrigir, é necessário criar um novo DTO com um horário diferente para a atualização
    @Test
    void deveAtualizarAgendamento() throws Exception {
        var dto = criarDTO();

        String response = mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        dto.setClienteNome("Maira");

        mockMvc.perform(put("/agendamentos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("clienteNome").value("Maira"));
    }

    @Test
    void deveCancelarAgendamento() throws Exception {
        var dto = criarDTO();

        String response = mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
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
        var dto = criarDTO();

        String response = mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/agendamentos/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void naoDeveCriarAgendamentoNoPassado() throws Exception {
        var dto = criarDTO();
        dto.setData(LocalDate.now().minusDays(1));

        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDevePermitirHorarioDuplicado() throws Exception {
        var dto = criarDTO();

        // cria primeiro
        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // tenta duplicar
        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
