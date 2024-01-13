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
        String idObject,
        UUID id,
        List<ItemPedidoDto> itens,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime dataRecebimento,
        String status
) {
    public static PedidoDto fromEntity(PedidoEntity entity) {
        return new PedidoDto(
                entity.getIdObject(),
                UUID.fromString(entity.getId()),
                getItensDto(entity.getItens()),
                entity.getData(),
                entity.getStatus()
        );
    }

    public PedidoDto updateStatus(String newStatus) {
        return new PedidoDto(
                idObject,
                id,
                itens,
                dataRecebimento,
                newStatus
        );
    }

    public PedidoEntity toEntity() {
        return PedidoEntity.builder()
                .idObject(idObject)
                .id(id.toString())
                .itens(getItensEntity())
                .data(dataRecebimento)
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
