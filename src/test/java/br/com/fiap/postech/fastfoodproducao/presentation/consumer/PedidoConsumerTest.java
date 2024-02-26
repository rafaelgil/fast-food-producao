package br.com.fiap.postech.fastfoodproducao.presentation.consumer;

import br.com.fiap.postech.fastfoodproducao.application.service.PedidoService;
import br.com.fiap.postech.fastfoodproducao.application.sqs.consumer.PedidoConsumer;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import br.com.fiap.postech.fastfoodproducao.utils.PedidoHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.support.GenericMessage;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoConsumerTest {


    AutoCloseable mock;

    @Mock
    PedidoService pedidoService;

    PedidoConsumer pedidoConsumer;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        this.pedidoConsumer = new PedidoConsumer(pedidoService, new ObjectMapper());
    }

    @Test
    public void DeveReceberNovoPedidoComSucesso() throws JsonProcessingException {
        var pedido = PedidoHelper.gerarPedido();

        when(pedidoService.consultaPedido(any(UUID.class)))
                .thenReturn(null);
        when(pedidoService.salvaPedido(any(PedidoDto.class)))
                .thenReturn(PedidoHelper.mockPedidoEntity(pedido.id()));

        //GenericMessage<PedidoDto> message = new GenericMessage<>(pedido);

        pedidoConsumer.recieveMessage(new ObjectMapper().writeValueAsString(pedido));
        verify(pedidoService, times(1)).consultaPedido(any(UUID.class));
        verify(pedidoService, times(1)).salvaPedido(any(PedidoDto.class));
    }

    @Test
    public void DeveReceberPedidoExistenteComSucesso() throws JsonProcessingException {
        var pedido = PedidoHelper.gerarPedido();

        when(pedidoService.consultaPedido(any(UUID.class)))
                .thenReturn(pedido);
        when(pedidoService.salvaPedido(any(PedidoDto.class)))
                .thenReturn(PedidoHelper.mockPedidoEntity(pedido.id()));

        //GenericMessage<PedidoDto> message = new GenericMessage<>(pedido);

        pedidoConsumer.recieveMessage(new ObjectMapper().writeValueAsString(pedido));
        verify(pedidoService, times(1)).consultaPedido(any(UUID.class));
        verify(pedidoService, times(0)).salvaPedido(any(PedidoDto.class));
    }
}
