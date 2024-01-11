package br.com.fiap.postech.fastfoodproducao.presentation.consumer;

import br.com.fiap.postech.fastfoodproducao.application.service.PedidoService;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PedidoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PedidoConsumer.class);

    @Autowired
    private PedidoService pedidoService;

    @SqsListener("fastfood-pedido")
    public void recieveMessage(Message<PedidoRecord> message) {
        PedidoRecord pedidoRecord = message.getPayload();

        var pedidoFound = pedidoService.consultaPedido(pedidoRecord.id());
        if (Objects.nonNull(pedidoFound)) {
            logger.info("Pedido já existe");
            return;
        }

        pedidoService.salvaPedido(pedidoRecord);

        logger.info("Mensagem recebida: "+ message.getPayload() );
    }
}
