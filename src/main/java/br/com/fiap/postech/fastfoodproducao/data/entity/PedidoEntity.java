package br.com.fiap.postech.fastfoodproducao.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document("pedido")
@Data
public class PedidoEntity {

    @Id
    private String idObject;

    private UUID id;

    private List<ProdutoEntity> produtos;

    private String data;

    private String status;
}
