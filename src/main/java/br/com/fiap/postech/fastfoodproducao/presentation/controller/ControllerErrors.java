package br.com.fiap.postech.fastfoodproducao.presentation.controller;

import br.com.fiap.postech.fastfoodproducao.application.exception.InvalidStatusException;
import br.com.fiap.postech.fastfoodproducao.application.exception.PedidoNotFoundException;
import br.com.fiap.postech.fastfoodproducao.dto.response.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerErrors {

    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<ResponseError> pedidoNotFound(PedidoNotFoundException exception, WebRequest request) {
        var responseError = new ResponseError("Pedido não encontrado");
        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<ResponseError> invalidStatus(InvalidStatusException exception, WebRequest request) {
        var responseError = new ResponseError("Status inválido");
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseError> invalidArgument(IllegalArgumentException exception, WebRequest request) {
        var responseError = new ResponseError("Requisição inválida");
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> pedidoGenericError(Exception exception, WebRequest request) {
        var responseError = new ResponseError("Erro no servidor");
        return new ResponseEntity<>(responseError, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
