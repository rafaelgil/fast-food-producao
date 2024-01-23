package br.com.fiap.postech.fastfoodproducao.presentation.producer;

import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import br.com.fiap.postech.fastfoodproducao.utils.PedidoHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class PedidoProducerTest {

    AutoCloseable mock;

    @Mock
    SqsTemplate sqsTemplate;

    PedidoProducer pedidoProducer;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        this.pedidoProducer = new PedidoProducer(sqsTemplate);
        ReflectionTestUtils.setField(pedidoProducer, "QUEUE_SEND_STATUS", "it's a security key");
    }

    @Test
    void enviaMensagemDePedidoComSucesso() throws JsonProcessingException {

        var pedido = PedidoHelper.gerarPedido();

        GenericMessage<PedidoDto> message = new GenericMessage<>(pedido);

        SendResult<PedidoDto> result = new SendResult<>(UUID.randomUUID(), "QUEUE_SEND_STATUS", message, null );

        when(sqsTemplate.send(any(String.class), any(GenericMessage.class)))
                .thenReturn(result);

        pedidoProducer.send(pedido);

        verify(sqsTemplate, times(1)).send(any(String.class),any(GenericMessage.class));
    }
}
