package br.com.fiap.postech.fastfoodproducao.application.service;

import br.com.fiap.postech.fastfoodproducao.application.StatusPedido;
import br.com.fiap.postech.fastfoodproducao.application.exception.InvalidStatusException;
import br.com.fiap.postech.fastfoodproducao.application.exception.PedidoNotFoundException;
import br.com.fiap.postech.fastfoodproducao.data.entity.PedidoEntity;
import br.com.fiap.postech.fastfoodproducao.data.repository.PedidoRepository;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import br.com.fiap.postech.fastfoodproducao.application.sqs.producer.PedidoProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService{

    private static final Logger logger = LoggerFactory.getLogger(PedidoServiceImpl.class);

    private final PedidoRepository pedidoRepository;

    private final PedidoProducer pedidoProducer;

    @Override
    public PedidoEntity salvaPedido(PedidoDto pedido) {

        var pedidoEntity = pedido.toEntity();

        pedidoRepository.save(pedidoEntity);

        logger.info("[salvaPedido] ID Object Pedido: " + pedidoEntity.getId());
        return pedidoEntity;
    }

    @Override
    public PedidoDto consultaPedido(UUID id) {
        logger.info("[consultaPedido] Consultando pedido com id: {}", id);
        var pedidoEntity = pedidoRepository.findByIdPedido(id);

        if (Objects.nonNull(pedidoEntity)) {
            logger.info("[consultaPedido] Pedido encontrado: {}", pedidoEntity);
            return PedidoDto.fromEntity(pedidoEntity);
        }

        return null;
    }

    @Override
    public PedidoDto consultaPedidoValido(UUID id) throws PedidoNotFoundException {
        var pedido = this.consultaPedido(id);
        if (Objects.isNull(pedido)) {
            logger.error("[consultaPedidoValido] Pedido não encontrado {}", id);
            throw new PedidoNotFoundException();
        }
        return pedido;
    }

    @Override
    public Page<PedidoDto> listaPedidos(Pageable pageable) {
        logger.info("[listaPedidos] Listando pedidos - página {} - qtde {1}", pageable.getPageNumber(), pageable.getPageSize());
        var pedidosEntity = pedidoRepository.findAll(pageable);

        logger.info("[listaPedidos] Total de pedidos encontrados {}", pedidosEntity.getTotalElements());
        var pedidosDto = pedidosEntity.stream().map(PedidoDto::fromEntity).toList();

        return new PageImpl<>(pedidosDto,pageable, pedidosEntity.getTotalPages());
    }

    public Page<PedidoDto> listaPedidosPorStatus(String status, Pageable pageable) {
        logger.info("[listaPedidosPorStatus] Listando pedidos - página {} - qtde {1}", pageable.getPageNumber(), pageable.getPageSize());
        var pedidosEntity = pedidoRepository.findByStatus(status, pageable);

        logger.info("[listaPedidosPorStatus] Total de pedidos encontrados {}", pedidosEntity.getTotalElements());
        var pedidos = pedidosEntity.stream()
                .map(PedidoDto::fromEntity)
                .toList();
        return new PageImpl<>(pedidos,pageable, pedidosEntity.getTotalPages());
    }

    @Override
    public PedidoDto atualizaStatusPedido(UUID id, String status) throws JsonProcessingException, PedidoNotFoundException, InvalidStatusException {
        logger.info("[atualizaStatusPedido] ID de pedido recebido: {}", id);
        var pedidoFound = this.consultaPedidoValido(id);

        logger.info("[atualizaStatusPedido] Status recebido: {}", status);
        var newStatus = StatusPedido.getByStatus(status);
        if (Objects.isNull(newStatus)) {
            logger.error("[atualizaStatusPedido] Status inválido");
            throw new InvalidStatusException();
        }

        logger.info("[atualizaStatusPedido] Status atual do pedido: {}", pedidoFound.status());
        var statusPedido = StatusPedido.valueOf(pedidoFound.status());
        var novoStatus = statusPedido.avancaPedido();

        if (!novoStatus.name().equals(status)) {
            logger.error("[atualizaStatusPedido] Status atual não pode avançar para o Status {}", status);
            throw new InvalidStatusException();
        }
        pedidoFound = pedidoFound.updateStatus(status);

        logger.info("[atualizaStatusPedido] Atualizando pedido");
        pedidoRepository.save(pedidoFound.toEntity());

        logger.info("[atualizaStatusPedido] Enviando pedido atualizado");
        pedidoProducer.send(pedidoFound);

        return pedidoFound;
    }
}
