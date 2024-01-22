package br.com.fiap.postech.fastfoodproducao.presentation.producer;

import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PedidoProducer {

    private static final Logger logger = LoggerFactory.getLogger(PedidoProducer.class);

    @Value("${fast.food.pedido.send.queue.name}")
    private String QUEUE_SEND_STATUS;

    private final SqsTemplate sqsTemplate;

    public void send(final PedidoDto pedido) throws JsonProcessingException {

        logger.info("[send] Enviando Pedido: " + pedido);

        Message<String> message = MessageBuilder.withPayload(new ObjectMapper().writeValueAsString(pedido)).build();

        sqsTemplate.send(QUEUE_SEND_STATUS, message);

        logger.info("[send] Pedido enviado: " + pedido);
    }
}
