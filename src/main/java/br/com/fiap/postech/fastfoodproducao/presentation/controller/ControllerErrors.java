package br.com.fiap.postech.fastfoodproducao.presentation.controller;

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
}
