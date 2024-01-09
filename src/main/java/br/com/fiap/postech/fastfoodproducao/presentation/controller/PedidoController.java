package br.com.fiap.postech.fastfoodproducao.presentation.controller;

import br.com.fiap.postech.fastfoodproducao.dto.PedidoRecord;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class PedidoController {

    public List<PedidoRecord> getAll() {
        return null;
    }

    public PedidoRecord getPedido(UUID id) {
        return null;
    }
}
