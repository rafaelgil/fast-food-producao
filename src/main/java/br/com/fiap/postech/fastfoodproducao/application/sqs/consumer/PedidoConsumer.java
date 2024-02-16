package br.com.fiap.postech.fastfoodproducao.application.sqs.consumer;

import br.com.fiap.postech.fastfoodproducao.application.service.PedidoService;
import br.com.fiap.postech.fastfoodproducao.dto.PedidoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PedidoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PedidoConsumer.class);

    private final PedidoService pedidoService;

    private final ObjectMapper objectMapper;

    @SqsListener(value = "${fast.food.pedido.recieve.queue.name}")
    public void recieveMessage(String message) {

        try{
            PedidoDto pedidoDto = objectMapper.readValue(message, PedidoDto.class);

            logger.info("[recieveMessage]Mensagem recebida: {}", message );

            var pedidoFound = pedidoService.consultaPedido(pedidoDto.id());
            if (Objects.nonNull(pedidoFound)) {
                logger.info("[recieveMessage]Pedido já existe: {}", pedidoFound.id());
                return;
            }

            pedidoDto = new PedidoDto(pedidoDto.idObject(), pedidoDto.id(), pedidoDto.itens(), pedidoDto.dataRecebimento(), "RECEBIDO");

            pedidoService.salvaPedido(pedidoDto);

            logger.info("[recieveMessage]Mensagem processada: {}", message );
        } catch (Exception e) {
            logger.error("[recieveMessage]Erro ao processar mensagem: {}", e.getMessage());
        }

    }
}
