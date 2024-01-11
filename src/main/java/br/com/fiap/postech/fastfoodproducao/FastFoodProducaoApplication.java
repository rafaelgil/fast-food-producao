package br.com.fiap.postech.fastfoodproducao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class FastFoodProducaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastFoodProducaoApplication.class, args);
	}

}
