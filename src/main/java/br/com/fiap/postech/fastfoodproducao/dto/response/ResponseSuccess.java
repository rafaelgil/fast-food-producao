package br.com.fiap.postech.fastfoodproducao.dto.response;

import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;

import java.util.List;

public record ResponseSuccess(
        List<PedidoDto> payload,
        ResponseMeta meta
) {
}
