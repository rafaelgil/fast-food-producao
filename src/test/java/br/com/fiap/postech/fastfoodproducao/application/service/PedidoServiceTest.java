package br.com.fiap.postech.fastfoodproducao.application.service;

import br.com.fiap.postech.fastfoodproducao.application.StatusPedido;
import br.com.fiap.postech.fastfoodproducao.application.exception.InvalidStatusException;
import br.com.fiap.postech.fastfoodproducao.application.exception.PedidoNotFoundException;
import br.com.fiap.postech.fastfoodproducao.data.entity.PedidoEntity;
import br.com.fiap.postech.fastfoodproducao.data.repository.PedidoRepository;
import br.com.fiap.postech.fastfoodproducao.dto.ItemPedidoDto;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import br.com.fiap.postech.fastfoodproducao.dto.ProdutoDto;
import br.com.fiap.postech.fastfoodproducao.application.sqs.producer.PedidoProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PedidoServiceTest {

    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoProducer pedidoProducer;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        this.pedidoService = new PedidoServiceImpl(pedidoRepository, pedidoProducer);
    }

    @Test
    void salvaPedidoComSucesso() {

        var pedido = gerarPedido();
        when(pedidoRepository.save(any(PedidoEntity.class)))
                .thenAnswer(i -> i.getArgument(0));

        var pedidoSalvo = pedidoService.salvaPedido(pedido);

        assertThat(pedidoSalvo)
                .isInstanceOf(PedidoEntity.class)
                .isNotNull();

        assertThat(pedidoSalvo.getItens().size())
                .isEqualTo(1);

        assertThat(pedidoSalvo.getItens().get(0).getId())
                .isEqualTo(pedido.itens().get(0).id());

        assertThat(pedidoSalvo.getItens().get(0).getQuantidade())
                .isEqualTo(10);

        assertThat(pedidoSalvo.getItens().get(0).getProduto().getId())
                .isEqualTo(pedido.itens().get(0).produto().id());

        assertThat(pedidoSalvo.getItens().get(0).getProduto().getDescricao())
                .isEqualTo("teste");

        assertThat(pedidoSalvo.getItens().get(0).getProduto().getCategoria())
                .isEqualTo("teste");
    }

    @Test
    void consultaPedidoComSucesso() {
        var pedido = gerarPedido();

        when(pedidoRepository.findByIdPedido(pedido.id()))
                .thenReturn(mockPedidoEntity(pedido.id()));

        var pedidoEncontrado = pedidoService.consultaPedido(pedido.id());

        assertThat(pedidoEncontrado)
                .isInstanceOf(PedidoDto.class)
                .isNotNull();

        assertThat(pedidoEncontrado.status()).isEqualTo(StatusPedido.EM_PREPARACAO.name());
    }

    @Test
    void consultaPedidoComPedidoNaoEncontrado() {
        var pedido = gerarPedido();

        when(pedidoRepository.findByIdPedido(pedido.id()))
                .thenReturn(null);

        var pedidoNaoEncontrado = pedidoService.consultaPedido(pedido.id());

        assertThat(pedidoNaoEncontrado).isNull();
    }

    @Test
    void consultaPedidoValidoComSucesso() throws PedidoNotFoundException {
        var pedido = gerarPedido();

        when(pedidoRepository.findByIdPedido(pedido.id()))
                .thenReturn(mockPedidoEntity(pedido.id()));

        var pedidoEncontrado = pedidoService.consultaPedidoValido(pedido.id());

        assertThat(pedidoEncontrado)
                .isInstanceOf(PedidoDto.class)
                .isNotNull();

        assertThat(pedidoEncontrado.status()).isEqualTo(StatusPedido.EM_PREPARACAO.name());
    }

    @Test
    void consultaPedidoValidoComPedidoNaoEncontrado() {
        var pedido = gerarPedido();

        when(pedidoRepository.findByIdPedido(pedido.id()))
                .thenReturn(null);

        assertThatThrownBy(() -> pedidoService.consultaPedidoValido(pedido.id()))
                .isInstanceOf(PedidoNotFoundException.class);
    }

    @Test
    void listaPedidoComSucesso() {
        var pedidos = Arrays.asList(mockPedidoEntity(UUID.randomUUID()),
                mockPedidoEntity(UUID.randomUUID()),
                mockPedidoEntity(UUID.randomUUID()),
                mockPedidoEntity(UUID.randomUUID()),
                mockPedidoEntity(UUID.randomUUID()));
        var pedidoEntityPage = new PageImpl<>(pedidos);

        when(pedidoRepository.findAll(any(Pageable.class)))
                .thenReturn(pedidoEntityPage);

        var paginador = PageRequest.of(1,5);

        var pedidosResultado = pedidoService.listaPedidos(paginador);

        assertThat(pedidosResultado).size().isEqualTo(5);
    }

    @Test
    void listaPedidoPorStatusComSucesso() {
        var pedidos = Arrays.asList(mockPedidoEntity(UUID.randomUUID()),
                mockPedidoEntity(UUID.randomUUID()),
                mockPedidoEntity(UUID.randomUUID()));

        var pedidoEntityPage = new PageImpl<>(pedidos);

        when(pedidoRepository.findByStatus(any(String.class), any(Pageable.class)))
                .thenReturn(pedidoEntityPage);

        var paginador = PageRequest.of(1,5);

        var pedidosResultado = pedidoService.listaPedidosPorStatus(StatusPedido.EM_PREPARACAO.getStatus(), paginador);

        assertThat(pedidosResultado).size().isEqualTo(3);
    }

    @Test
    void atualizaPedidoComSucesso() throws InvalidStatusException, PedidoNotFoundException, JsonProcessingException {
        var pedidoEntity = mockPedidoEntity(UUID.randomUUID());
        when(pedidoRepository.findByIdPedido(any(UUID.class)))
                .thenReturn(pedidoEntity);

        var dto = pedidoService.atualizaStatusPedido(UUID.fromString(pedidoEntity.getId()), StatusPedido.PRONTO.name());

        assertThat(dto)
                .isInstanceOf(PedidoDto.class)
                .isNotNull();
        assertThat(dto.status()).isEqualTo(StatusPedido.PRONTO.name());
    }

    @Test
    void atualizaPedidoComStatusNulo() throws InvalidStatusException, PedidoNotFoundException, JsonProcessingException {
        var pedidoEntity = mockPedidoEntity(UUID.randomUUID());
        when(pedidoRepository.findByIdPedido(any(UUID.class)))
                .thenReturn(pedidoEntity);

        assertThatThrownBy(() -> pedidoService.atualizaStatusPedido(UUID.fromString(pedidoEntity.getId()), null))
                .isInstanceOf(InvalidStatusException.class);
    }

    @Test
    void atualizaPedidoComStatusIncorreto() throws InvalidStatusException, PedidoNotFoundException, JsonProcessingException {
        var pedidoEntity = mockPedidoEntity(UUID.randomUUID());
        when(pedidoRepository.findByIdPedido(any(UUID.class)))
                .thenReturn(pedidoEntity);

        assertThatThrownBy(() -> pedidoService.atualizaStatusPedido(UUID.fromString(pedidoEntity.getId()), StatusPedido.EM_PREPARACAO.name()))
                .isInstanceOf(InvalidStatusException.class);
    }

    private PedidoDto gerarPedido() {

        var produto = new ProdutoDto(
                UUID.randomUUID().toString(),
                "teste",
                "teste"
        );

        var item = new ItemPedidoDto(
                UUID.randomUUID().toString(),
                produto,
                10
        );

        return new PedidoDto(
                UUID.randomUUID(),
                Arrays.asList(item),
                StatusPedido.EM_PREPARACAO.name()
        );
    }

    private PedidoEntity mockPedidoEntity(UUID id) {
        return PedidoEntity.builder()
                .id("12345678")
                .id(id.toString())
                .status(StatusPedido.EM_PREPARACAO.name())
                .build();
    }
}
