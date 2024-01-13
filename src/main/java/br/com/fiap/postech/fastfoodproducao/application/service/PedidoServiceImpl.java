package br.com.fiap.postech.fastfoodproducao.application.service;

import br.com.fiap.postech.fastfoodproducao.application.StatusPedido;
import br.com.fiap.postech.fastfoodproducao.data.repository.PedidoRepository;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import br.com.fiap.postech.fastfoodproducao.presentation.producer.PedidoProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public void salvaPedido(PedidoDto pedido) {

        var pedidoEntity = pedido.toEntity();

        pedidoRepository.save(pedidoEntity);

        logger.info("ID Object Pedido: " + pedidoEntity.getIdObject());
    }

    @Override
    public PedidoDto consultaPedido(UUID id) {
        if (Objects.isNull(id)) {
            return null;
        }
        var pedidoEntity = pedidoRepository.findByIdPedido(id);

        if (Objects.nonNull(pedidoEntity)) {
            return PedidoDto.fromEntity(pedidoEntity);
        }

        return null;
    }

    @Override
    public List<PedidoDto> listaPedidos() {
        var pedidosEntity = pedidoRepository.findAll();



        if (pedidosEntity == null || pedidosEntity.isEmpty()) {
            return Collections.emptyList();
        }

        return pedidosEntity.stream()
                .map(PedidoDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PedidoDto> listaPedidosPorStatus(String status) {
        var pedidosEntity = pedidoRepository.findByStatus(status);
        return pedidosEntity.stream()
                .map(PedidoDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PedidoDto enviaStatusPedido(UUID id) {
        return null;
    }

    @Override
    public PedidoDto atualizaStatusPedido(PedidoDto pedidoDto, String status) throws JsonProcessingException {
        var statusPedido = StatusPedido.valueOf(pedidoDto.status());
        var novoStatus = statusPedido.avancaPedido();
        if (novoStatus.name().equals(status)) {
            pedidoDto = pedidoDto.updateStatus(status);
            pedidoRepository.save(pedidoDto.toEntity());

            pedidoProducer.send(pedidoDto);
        }
        return pedidoDto;
    }
}
