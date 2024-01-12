package br.com.fiap.postech.fastfoodproducao.dto;

public record ItemPedidoDto(
        String id,
        ProdutoDto produto,
        int quantidade
) {
}
