package br.com.niceshoes.controller;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
@RestControllerAdvice
public class ErroController {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> erro(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("status", e.getStatusCode().value(), "mensagem", Objects.toString(e.getReason(), "Erro")));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacao(MethodArgumentNotValidException e) {
        Map<String, String> campos = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(c -> campos.put(c.getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of("status", 400, "mensagem", "Dados inválidos.", "campos", campos));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridade(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("status", 409, "mensagem", "Operação viola a integridade dos dados."));
    }
}
