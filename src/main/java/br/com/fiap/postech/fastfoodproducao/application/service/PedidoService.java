package br.com.fiap.postech.fastfoodproducao.application.service;

import br.com.fiap.postech.fastfoodproducao.dto.PedidoRecord;

import java.util.UUID;

public interface PedidoService {

    void salvaPedido(PedidoRecord pedido);

    PedidoRecord consultaPedido(UUID id);

    PedidoRecord enviaStatusPedido(UUID id);

    PedidoRecord atualizaStatusPedido(UUID id, String status);
}
