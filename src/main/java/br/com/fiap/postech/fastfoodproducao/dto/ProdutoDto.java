package br.com.fiap.postech.fastfoodproducao.dto;

import br.com.fiap.postech.fastfoodproducao.data.entity.ProdutoEntity;

public record ProdutoDto(
        String id,
        String descricao,
        String categoria
) {
    public ProdutoEntity toEntity() {
        return ProdutoEntity.builder()
                .id(id)
                .descricao(descricao)
                .categoria(categoria)
                .build();
    }

    public static ProdutoDto fromEntity(ProdutoEntity entity) {
        return new ProdutoDto(
                entity.getId(),
                entity.getDescricao(),
                entity.getCategoria()
        );
    }
}
