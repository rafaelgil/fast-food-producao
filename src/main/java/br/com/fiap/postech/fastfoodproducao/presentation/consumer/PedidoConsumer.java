package br.com.fiap.postech.fastfoodproducao.presentation.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;

public class PedidoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PedidoConsumer.class);

    @SqsListener("recebe-pedido")
    public void recieveMessage(String stringJson) {

    }
}
