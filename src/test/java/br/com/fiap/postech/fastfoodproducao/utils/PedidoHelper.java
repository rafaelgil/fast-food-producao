package br.com.fiap.postech.fastfoodproducao.utils;

import br.com.fiap.postech.fastfoodproducao.application.StatusPedido;
import br.com.fiap.postech.fastfoodproducao.data.entity.PedidoEntity;
import br.com.fiap.postech.fastfoodproducao.data.repository.PedidoRepository;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class PedidoHelper {

    private static final String idPedido = "ccc51a9a-b3dc-4447-8c85-9610ff3c2142";

    public static PedidoDto geraPedido(PedidoRepository pedidoRepository) {

        var pedido = pedidoRepository.findByIdPedido(UUID.fromString(idPedido));

        if (Objects.nonNull(pedido)) {
            return PedidoDto.fromEntity(pedido);
        }

        return new PedidoDto("123456", UUID.fromString(idPedido), null, LocalDateTime.now(), StatusPedido.RECEBIDO.getStatus());
    }

    public static PedidoDto gerarPedido() {
        return new PedidoDto(
                "123654789",
                UUID.randomUUID(),
                null,
                LocalDateTime.now(),
                StatusPedido.RECEBIDO.name()
        );
    }

    public static PedidoEntity mockPedidoEntity(UUID id) {
        return PedidoEntity.builder()
                .id("12345678")
                .id(id.toString())
                .status(StatusPedido.RECEBIDO.name())
                .build();
    }
}
