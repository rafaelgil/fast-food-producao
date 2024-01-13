package br.com.fiap.postech.fastfoodproducao.data.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProdutoEntity {
    private String id;
    private String descricao;
    private String categoria;
}
