package br.com.fiap.postech.fastfoodproducao.application.sqs.producer;

import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import br.com.fiap.postech.fastfoodproducao.utils.PedidoHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class PedidoProducerTest {

    AutoCloseable mock;

    @Mock
    QueueMessagingTemplate sqsTemplate;

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

        pedidoProducer.send(pedido);

        verify(sqsTemplate, times(1)).send(any(String.class),any(GenericMessage.class));
    }
}
