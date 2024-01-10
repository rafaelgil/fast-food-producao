package br.com.fiap.postech.fastfoodproducao.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoRecord(
        UUID id,
        List<ProdutoRecord> produtos,
        LocalDateTime dataRecebimento
) {
}
