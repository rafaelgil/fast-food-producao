package br.com.fiap.postech.fastfoodproducao.bdd;

import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class PedidoStepDefinition {

    private String ENDPOINT_PRODUCAO_PEDIDOS = "http://localhost:8080/producao/pedidos?page=0&size=5";

    private Response response;

    private PedidoDto pedidoDto;

    @Quando("listar todos os pedidos")
    public void listarTodosOsPedidos() {

        response = when()
                .get(ENDPOINT_PRODUCAO_PEDIDOS);
    }

    @Entao("a resposta contem 5 pedidos")
    public void aRespostaContem5Pedidos() {
        response.then()
                .body(matchesJsonSchemaInClasspath("schema/pedido.schema.json"));
    }


    @Dado("que o pedido existe")
    public void pedidoExiste() {
        listarTodosOsPedidos();
        var pedidos = response.then().extract().as(String.class);
//        pedidoDto = (PedidoDto) pedidos.get(0);
    }

    @Quando("consulta pedido")
    public void consultaPedido() {
        response = when()
                .get(ENDPOINT_PRODUCAO_PEDIDOS + "/{id}", pedidoDto.id());
    }

    @Entao("pedido é encontrado")
    public void pedidoEhEncontrado() {
        response.then()
                .body(matchesJsonSchemaInClasspath("schema/pedido.schema.json"));
    }
}
