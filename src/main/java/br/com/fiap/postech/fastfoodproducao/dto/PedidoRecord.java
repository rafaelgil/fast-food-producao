package br.com.fiap.postech.fastfoodproducao.dto;

import br.com.fiap.postech.fastfoodproducao.data.entity.PedidoEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoRecord(
        String idObject,
        UUID id,
        List<ProdutoRecord> produtos,
        LocalDateTime dataRecebimento,
        String status
) {
    public static PedidoRecord fromEntity(PedidoEntity entity) {
        return new PedidoRecord(
                entity.getIdObject(),
                UUID.fromString(entity.getId()),
                null,
                entity.getData(),
                entity.getStatus()
        );
    }

    public PedidoRecord updateStatus(String newStatus) {
        return new PedidoRecord(
                idObject,
                id,
                null,
                dataRecebimento,
                newStatus
        );
    }

    public PedidoEntity toEntity() {
        return PedidoEntity.builder()
                .idObject(idObject)
                .id(id.toString())
                .data(dataRecebimento)
                .status(status)
                .build();
    }
}
