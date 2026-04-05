package com.ExplorandoAPIs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ERROS DE REGRA DE NEGÓCIO (SERVICE)
    @ExceptionHandler(AgendamentoException.class)
    public ResponseEntity<Map<String, Object>> handleAgendamentoException(AgendamentoException ex) {

        Map<String, Object> erro = new HashMap<>();

        erro.put("timestamp", LocalDateTime.now());
        erro.put("erro", "Erro de regra de negócio");
        erro.put("mensagem", ex.getMessage());
        erro.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // ERROS DE VALIDAÇÃO (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, Object> erro = new HashMap<>();

        // pega a primeira mensagem de erro (mais simples para o usuário)
        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Erro de validação");

        erro.put("timestamp", LocalDateTime.now());
        erro.put("erro", "Erro de validação");
        erro.put("mensagem", mensagem);
        erro.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // ERROS GENÉRICOS (NÃO TRATADOS)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {

        Map<String, Object> erro = new HashMap<>();

        erro.put("timestamp", LocalDateTime.now());
        erro.put("erro", "Erro interno do servidor");
        erro.put("mensagem", "Ocorreu um erro inesperado. Verifique os logs.");
        erro.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}