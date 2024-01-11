package br.com.fiap.postech.fastfoodproducao.data.repository;

import br.com.fiap.postech.fastfoodproducao.data.entity.PedidoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends MongoRepository<PedidoEntity, UUID> {

    List<PedidoEntity> findAll();
}
