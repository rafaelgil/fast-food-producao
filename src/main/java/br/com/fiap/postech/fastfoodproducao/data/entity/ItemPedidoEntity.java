package br.com.fiap.postech.fastfoodproducao.data.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemPedidoEntity {
    private String id;
    private ProdutoEntity produto;
    private int quantidade;
}
