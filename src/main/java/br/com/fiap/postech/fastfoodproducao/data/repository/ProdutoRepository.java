package br.com.fiap.postech.fastfoodproducao.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ProdutoRepository extends MongoRepository<ProdutoRepository, UUID> {
}
