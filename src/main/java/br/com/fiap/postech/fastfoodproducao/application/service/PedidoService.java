package br.com.fiap.postech.fastfoodproducao.application.service;

import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.UUID;

public interface PedidoService {

    void salvaPedido(PedidoDto pedido);

    PedidoDto consultaPedido(UUID id);

    List<PedidoDto> listaPedidos();

    List<PedidoDto> listaPedidosPorStatus(String status);

    PedidoDto enviaStatusPedido(UUID id);

    PedidoDto atualizaStatusPedido(UUID id, String status) throws JsonProcessingException;
}
