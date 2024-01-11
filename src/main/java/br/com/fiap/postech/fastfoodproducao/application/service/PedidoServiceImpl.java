package br.com.fiap.postech.fastfoodproducao.application.service;

import br.com.fiap.postech.fastfoodproducao.application.StatusPedido;
import br.com.fiap.postech.fastfoodproducao.data.entity.PedidoEntity;
import br.com.fiap.postech.fastfoodproducao.data.repository.PedidoRepository;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoRecord;
import br.com.fiap.postech.fastfoodproducao.presentation.consumer.PedidoConsumer;
import br.com.fiap.postech.fastfoodproducao.presentation.producer.PedidoProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService{

    private static final Logger logger = LoggerFactory.getLogger(PedidoServiceImpl.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoProducer pedidoProducer;

    @Override
    public void salvaPedido(PedidoRecord pedido) {

        var pedidoEntity = PedidoEntity.builder()
                .id(pedido.id().toString())
                .data(pedido.dataRecebimento())
                .status(pedido.status())
                .build();

        pedidoRepository.save(pedidoEntity);

        logger.info("ID Object Pedido: " + pedidoEntity.getIdObject());
    }

    @Override
    public PedidoRecord consultaPedido(UUID id) {
        if (Objects.isNull(id)) {
            return null;
        }
        var pedidoEntity = pedidoRepository.findByIdPedido(id);

        if (Objects.nonNull(pedidoEntity)) {
            return PedidoRecord.fromEntity(pedidoEntity);
        }

        return null;
    }

    @Override
    public List<PedidoRecord> listaPedidos() {
        var pedidosEntity = pedidoRepository.findAll();

        if (pedidosEntity != null && pedidosEntity.isEmpty()) {
            return null;
        }

        var pedidos = pedidosEntity.stream()
                .map(pedidoEntity -> PedidoRecord.fromEntity(pedidoEntity))
                .collect(Collectors.toList());


        return pedidos;
    }

    public List<PedidoRecord> listaPedidosPorStatus(String status) {
        var pedidosEntity = pedidoRepository.findByStatus(status);
        var pedidos = pedidosEntity.stream()
                .map(pedidoEntity -> PedidoRecord.fromEntity(pedidoEntity))
                .collect(Collectors.toList());

        return pedidos;
    }

    @Override
    public PedidoRecord enviaStatusPedido(UUID id) {
        return null;
    }

    @Override
    public PedidoRecord atualizaStatusPedido(PedidoRecord pedidoRecord, String status) throws JsonProcessingException {
        var statusPedido = StatusPedido.valueOf(pedidoRecord.status());
        var novoStatus = statusPedido.avancaPedido();
        if (novoStatus.name().equals(status)) {
            pedidoRecord = pedidoRecord.updateStatus(status);
            pedidoRepository.save(pedidoRecord.toEntity());

            pedidoProducer.send(pedidoRecord);
        }
        return pedidoRecord;
    }
}
