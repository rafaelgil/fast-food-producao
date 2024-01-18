package br.com.fiap.postech.fastfoodproducao.application.service;

import br.com.fiap.postech.fastfoodproducao.application.StatusPedido;
import br.com.fiap.postech.fastfoodproducao.application.exception.PedidoNotFoundException;
import br.com.fiap.postech.fastfoodproducao.data.entity.PedidoEntity;
import br.com.fiap.postech.fastfoodproducao.data.repository.PedidoRepository;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import br.com.fiap.postech.fastfoodproducao.presentation.producer.PedidoProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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

        assertThat(pedidoEncontrado.status()).isEqualTo(StatusPedido.RECEBIDO.getStatus());
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

        assertThat(pedidoEncontrado.status()).isEqualTo(StatusPedido.RECEBIDO.getStatus());
    }

    @Test
    void consultaPedidoValidoComPedidoNaoEncontrado() {
        var pedido = gerarPedido();

        when(pedidoRepository.findByIdPedido(pedido.id()))
                .thenReturn(null);

        assertThatThrownBy(() -> pedidoService.consultaPedidoValido(pedido.id()))
                .isInstanceOf(PedidoNotFoundException.class);
    }

    private PedidoDto gerarPedido() {
        return new PedidoDto(
                "123654789",
                UUID.randomUUID(),
                null,
                LocalDateTime.now(),
                StatusPedido.RECEBIDO.getStatus()
        );
    }

    private PedidoEntity mockPedidoEntity(UUID id) {
        return PedidoEntity.builder()
                .id("12345678")
                .id(id.toString())
                .status(StatusPedido.RECEBIDO.getStatus())
                .build();
    }
}
