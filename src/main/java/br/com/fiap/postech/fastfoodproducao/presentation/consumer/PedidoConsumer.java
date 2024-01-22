package br.com.fiap.postech.fastfoodproducao.presentation.consumer;

import br.com.fiap.postech.fastfoodproducao.application.service.PedidoService;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PedidoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PedidoConsumer.class);

    private final PedidoService pedidoService;

    @SqsListener("fastfood-pedido")
    public void recieveMessage(Message<PedidoDto> message) {
        PedidoDto pedidoDto = message.getPayload();

        logger.info("[recieveMessage]Mensagem recebida: {0}", message.getPayload() );

        var pedidoFound = pedidoService.consultaPedido(pedidoDto.id());
        if (Objects.nonNull(pedidoFound)) {
            logger.info("[recieveMessage]Pedido já existe: {0}", pedidoFound.id());
            return;
        }

        pedidoService.salvaPedido(pedidoDto);

        logger.info("[recieveMessage]Mensagem processada: {0}", message.getPayload() );
    }
}
