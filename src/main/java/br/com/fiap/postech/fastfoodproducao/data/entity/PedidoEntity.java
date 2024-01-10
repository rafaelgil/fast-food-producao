package br.com.fiap.postech.fastfoodproducao.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document("fastfood")
public class PedidoEntity {

    @Id
    private UUID id;

    private List<ProdutoEntity> produtos;

    private LocalDateTime data;

    private String status;
}
