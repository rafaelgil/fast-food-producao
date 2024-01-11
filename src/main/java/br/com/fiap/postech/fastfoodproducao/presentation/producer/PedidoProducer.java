package br.com.fiap.postech.fastfoodproducao.presentation.producer;

import br.com.fiap.postech.fastfoodproducao.dto.PedidoRecord;
import br.com.fiap.postech.fastfoodproducao.presentation.consumer.PedidoConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class PedidoProducer {

    private static final Logger logger = LoggerFactory.getLogger(PedidoConsumer.class);
    @Value("${fast.food.pedido.send.queue.name}")
    private String QUEUE_SEND_STATUS;
    @Autowired
    private SqsTemplate sqsTemplate;

    public void send(final PedidoRecord pedido) throws JsonProcessingException {

        logger.info("Enviando Pedido: " + pedido);

        Message<String> message = MessageBuilder.withPayload(new ObjectMapper().writeValueAsString(pedido)).build();

        sqsTemplate.send(QUEUE_SEND_STATUS, message);

        logger.info("Pedido enviado: " + pedido);
    }
}
