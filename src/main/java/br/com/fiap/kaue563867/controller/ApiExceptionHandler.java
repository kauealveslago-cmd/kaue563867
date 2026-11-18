package br.com.fiap.kaue563867.controller;

import br.com.fiap.kaue563867.dto.ErroDTO;
import br.com.fiap.kaue563867.exception.RecursoNaoEncontradoException;
import br.com.fiap.kaue563867.exception.RegraNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    // 400: violação das validações do DTO (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> validacao(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(erro -> campos.merge(erro.getField(), erro.getDefaultMessage(), (a, b) -> a + " " + b));
        return resposta(HttpStatus.BAD_REQUEST,
                new ErroDTO(HttpStatus.BAD_REQUEST.value(), "Dados inválidos. Verifique os campos informados.", campos));
    }

    // 400: JSON malformado ou data fora do formato 2026-11-01T17:20:00
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroDTO> jsonInvalido(HttpMessageNotReadableException ex) {
        return erro(HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido. Verifique o JSON e o formato da data (ex.: 2026-11-01T17:20:00).");
    }

    // 400: id da URL que não é número
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroDTO> parametroInvalido(MethodArgumentTypeMismatchException ex) {
        return erro(HttpStatus.BAD_REQUEST, "Parâmetro inválido: " + ex.getName() + ".");
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroDTO> naoEncontrado(RecursoNaoEncontradoException ex) {
        return erro(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 409: regras R2 e R3
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroDTO> regraNegocio(RegraNegocioException ex) {
        return erro(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroDTO> integridade(DataIntegrityViolationException ex) {
        return erro(HttpStatus.CONFLICT, "A operação viola a integridade dos dados cadastrados.");
    }

    private ResponseEntity<ErroDTO> erro(HttpStatus status, String mensagem) {
        return resposta(status, new ErroDTO(status.value(), mensagem));
    }

    private ResponseEntity<ErroDTO> resposta(HttpStatus status, ErroDTO corpo) {
        return ResponseEntity.status(status).body(corpo);
    }
}
