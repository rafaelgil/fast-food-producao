package br.com.fiap.postech.fastfoodproducao.presentation.controller;

import br.com.fiap.postech.fastfoodproducao.dto.PedidoRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("producao")
public class PedidoController {

    @GetMapping
    public List<PedidoRecord> getAll() {
        return null;
    }

    @GetMapping("/{id}")
    public PedidoRecord getPedido(@PathVariable UUID id) {
        return null;
    }
}
