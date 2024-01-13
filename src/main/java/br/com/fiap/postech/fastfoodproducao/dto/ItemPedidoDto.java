package br.com.fiap.postech.fastfoodproducao.dto;

import br.com.fiap.postech.fastfoodproducao.data.entity.ItemPedidoEntity;

public record ItemPedidoDto(
        String id,
        ProdutoDto produto,
        int quantidade
) {
    public ItemPedidoEntity toEntity() {
        return ItemPedidoEntity.builder()
                .id(id)
                .produto(produto.toEntity())
                .quantidade(quantidade)
                .build();
    }

    public static ItemPedidoDto fromEntity(ItemPedidoEntity entity) {
        return new ItemPedidoDto(
                entity.getId(),
                ProdutoDto.fromEntity(entity.getProduto()),
                entity.getQuantidade()
        );
    }
}
