package br.com.fiap.postech.fastfoodproducao.presentation.controller;

import br.com.fiap.postech.fastfoodproducao.application.exception.InvalidStatusException;
import br.com.fiap.postech.fastfoodproducao.application.exception.PedidoNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


public class ControllerErrorTest {

    ControllerErrors controllerErrors = new ControllerErrors();

    @Test
    void deveRetornarErroPedidoNotFound() {

        var controllerResult = controllerErrors.pedidoNotFound(new PedidoNotFoundException(), null);
        assertThat(controllerResult)
                .isNotNull();
        assertThat(controllerResult.getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(404));
    }

    @Test
    void deveRetornarErroIllegalArgumentException() {

        var controllerResult = controllerErrors.invalidArgument(new IllegalArgumentException(), null);
        assertThat(controllerResult)
                .isNotNull();
        assertThat(controllerResult.getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test
    void deveRetornarErroInvalidStatus() {

        var controllerResult = controllerErrors.invalidStatus(new InvalidStatusException(), null);
        assertThat(controllerResult)
                .isNotNull();
        assertThat(controllerResult.getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test
    void deveRetornarErroGeneric() {

        var controllerResult = controllerErrors.pedidoGenericError(new Exception(), null);
        assertThat(controllerResult)
                .isNotNull();
        assertThat(controllerResult.getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(503));
    }

}
