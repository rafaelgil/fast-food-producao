package br.com.fiap.postech.fastfoodproducao.application.service;

import br.com.fiap.postech.fastfoodproducao.data.entity.PedidoEntity;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface PedidoService {

    void salvaPedido(PedidoRecord pedido);

    PedidoRecord consultaPedido(UUID id);

    List<PedidoRecord> listaPedidos();

    List<PedidoRecord> listaPedidosPorStatus(String status);

    PedidoRecord enviaStatusPedido(UUID id);

    PedidoRecord atualizaStatusPedido(PedidoRecord pedidoRecord, String status) throws JsonProcessingException;
}
