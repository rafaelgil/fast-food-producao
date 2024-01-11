package br.com.fiap.postech.fastfoodproducao.dto;

import br.com.fiap.postech.fastfoodproducao.data.entity.PedidoEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoRecord(
        UUID id,
        List<ProdutoRecord> produtos,
        LocalDateTime dataRecebimento,
        String status
) {
    public static PedidoRecord fromEntity(PedidoEntity entity) {
        return new PedidoRecord(
                UUID.fromString(entity.getId()),
                null,
                entity.getData(),
                entity.getStatus()
        );
    }

}
