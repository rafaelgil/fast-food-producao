package br.com.fiap.postech.fastfoodproducao.presentation.controller;

import br.com.fiap.postech.fastfoodproducao.application.exception.InvalidStatusException;
import br.com.fiap.postech.fastfoodproducao.application.exception.PedidoNotFoundException;
import br.com.fiap.postech.fastfoodproducao.application.service.PedidoService;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import br.com.fiap.postech.fastfoodproducao.dto.response.ResponseMeta;
import br.com.fiap.postech.fastfoodproducao.dto.response.ResponseSuccess;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<ResponseSuccess> getAll(@RequestParam(required = false) Integer page,
                                                 @RequestParam(required = false) Integer size) {

        Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10);
        var pedidos = pedidoService.listaPedidos(pageable);

        var meta = new ResponseMeta(pedidos.getContent().size(), pageable.getPageNumber());

        var response = new ResponseSuccess(pedidos.getContent(), meta);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public PedidoDto getPedido(@PathVariable UUID id) throws PedidoNotFoundException {
        logger.info("[getPedido] id de pedido recebido: {0}", id);
        return pedidoService.consultaPedidoValido(id);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseSuccess> getPedidosByStatus(@PathVariable String status,
                                                              @RequestParam(required = false) Integer page,
                                                              @RequestParam(required = false) Integer size) {
        Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10);

        var pedidos = pedidoService.listaPedidosPorStatus(status, pageable);

        var meta = new ResponseMeta(pedidos.getContent().size(), pageable.getPageNumber());

        var response = new ResponseSuccess(pedidos.getContent(), meta);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status/{status}")
    public PedidoDto atualizaStatusPedido(@PathVariable UUID id, @PathVariable String status) throws JsonProcessingException, InvalidStatusException, PedidoNotFoundException {

        return pedidoService.atualizaStatusPedido(id, status);
    }
}
