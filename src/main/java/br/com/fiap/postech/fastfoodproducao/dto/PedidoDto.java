package br.com.fiap.postech.fastfoodproducao.dto;

import br.com.fiap.postech.fastfoodproducao.data.entity.ItemPedidoEntity;
import br.com.fiap.postech.fastfoodproducao.data.entity.PedidoEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record PedidoDto(
        UUID id,
        List<ItemPedidoDto> itens,
        String status
) {
    public static PedidoDto fromEntity(PedidoEntity entity) {
        return new PedidoDto(
                UUID.fromString(entity.getId()),
                getItensDto(entity.getItens()),
                entity.getStatus()
        );
    }

    public PedidoDto updateStatus(String newStatus) {
        return new PedidoDto(
                id,
                itens,
                newStatus
        );
    }

    public PedidoEntity toEntity() {
        return PedidoEntity.builder()
                .id(id.toString())
                .itens(getItensEntity())
                .status(status)
                .build();
    }

    private List<ItemPedidoEntity> getItensEntity() {
        if (itens != null) {
            return itens().stream()
                    .map(ItemPedidoDto::toEntity)
                    .toList();
        }
        return Collections.emptyList();
    }

    private static List<ItemPedidoDto> getItensDto(List<ItemPedidoEntity> itensEntity) {
        if (itensEntity != null) {
            return itensEntity.stream()
                    .map(ItemPedidoDto::fromEntity)
                    .toList();
        }
        return Collections.emptyList();
    }
}
