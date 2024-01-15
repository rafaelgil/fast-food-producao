package br.com.fiap.postech.fastfoodproducao.application.service;

import br.com.fiap.postech.fastfoodproducao.application.exception.InvalidStatusException;
import br.com.fiap.postech.fastfoodproducao.application.exception.PedidoNotFoundException;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PedidoService {

    void salvaPedido(PedidoDto pedido);

    PedidoDto consultaPedido(UUID id) throws PedidoNotFoundException;

    Page<PedidoDto> listaPedidos(Pageable pageable);

    Page<PedidoDto> listaPedidosPorStatus(String status, Pageable pageable);

    PedidoDto enviaStatusPedido(UUID id);

    PedidoDto atualizaStatusPedido(UUID id, String status) throws JsonProcessingException, InvalidStatusException, PedidoNotFoundException;
}
