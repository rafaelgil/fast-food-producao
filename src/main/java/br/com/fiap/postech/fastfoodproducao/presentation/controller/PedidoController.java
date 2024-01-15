package br.com.fiap.postech.fastfoodproducao.presentation.controller;

import br.com.fiap.postech.fastfoodproducao.application.exception.InvalidStatusException;
import br.com.fiap.postech.fastfoodproducao.application.exception.PedidoNotFoundException;
import br.com.fiap.postech.fastfoodproducao.application.service.PedidoService;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;


    @GetMapping
    public List<PedidoDto> getAll() {

        return pedidoService.listaPedidos();
    }

    @GetMapping("/{id}")
    public PedidoDto getPedido(@PathVariable UUID id) throws PedidoNotFoundException {

        return pedidoService.consultaPedido(id);
    }

    @GetMapping("/status/{status}")
    public List<PedidoDto> getPedidosByStatus(@PathVariable String status) {

        return pedidoService.listaPedidosPorStatus(status);
    }

    @PatchMapping("/{id}/status/{status}")
    public PedidoDto atualizaStatusPedido(@PathVariable UUID id, @PathVariable String status) throws JsonProcessingException, InvalidStatusException, PedidoNotFoundException {

        return pedidoService.atualizaStatusPedido(id, status);
    }
}
