package br.com.fiap.postech.fastfoodproducao.application.service;

import br.com.fiap.postech.fastfoodproducao.data.entity.PedidoEntity;
import br.com.fiap.postech.fastfoodproducao.data.repository.PedidoRepository;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService{

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void salvaPedido(PedidoRecord pedido) {

    }

    @Override
    public PedidoRecord consultaPedido(UUID id) {
        return null;
    }

    @Override
    public List<PedidoRecord> listaPedidos() {
        var pedidosEntity = pedidoRepository.findAll();

        if (pedidosEntity != null && pedidosEntity.isEmpty()) {
            return null;
        }

        var pedidos = pedidosEntity.stream()
                .map(pedidoEntity ->
                     new PedidoRecord(
                            UUID.fromString(pedidoEntity.getId()),
                            null,
                            pedidoEntity.getData()
                    )
                )
                .collect(Collectors.toList());


        return pedidos;
    }

    @Override
    public PedidoRecord enviaStatusPedido(UUID id) {
        return null;
    }

    @Override
    public PedidoRecord atualizaStatusPedido(UUID id, String status) {
        return null;
    }
}
