package com.ExplorandoAPIs.repository;

import com.ExplorandoAPIs.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Verifica se já existe um agendamento na mesma data e horário
    Optional<Agendamento> findByDataAndHorario(LocalDate data, LocalTime horario);

}