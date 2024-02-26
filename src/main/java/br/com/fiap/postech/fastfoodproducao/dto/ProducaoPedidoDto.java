package br.com.fiap.postech.fastfoodproducao.dto;

import java.util.UUID;

public record ProducaoPedidoDto(
        UUID id,
        String status
){}
