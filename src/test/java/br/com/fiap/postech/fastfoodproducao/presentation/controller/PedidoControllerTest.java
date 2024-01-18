package br.com.fiap.postech.fastfoodproducao.presentation.controller;

import br.com.fiap.postech.fastfoodproducao.application.StatusPedido;
import br.com.fiap.postech.fastfoodproducao.application.exception.PedidoNotFoundException;
import br.com.fiap.postech.fastfoodproducao.application.service.PedidoService;
import br.com.fiap.postech.fastfoodproducao.utils.PedidoHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PedidoControllerTest {

    private MockMvc mockMvc;

    AutoCloseable mock;

    @Mock
    PedidoService pedidoService;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        PedidoController pedidoController = new PedidoController(pedidoService);
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                })
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void deveListarPedidosComSucesso() throws Exception {
        var pedido = PedidoHelper.gerarPedido();
        var page = new PageImpl<>(Collections.singletonList(pedido));

        when(pedidoService.listaPedidos(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/pedidos")
                        .param("page", "0")
                        .param("size", "10"))
//                        .andDo(print())
                .andExpect(status().isOk());

        verify(pedidoService, times(1)).listaPedidos(any(Pageable.class));
    }

    @Test
    void deveConsultarPedidoComSucesso() throws Exception {
        var pedido = PedidoHelper.gerarPedido();

        when(pedidoService.consultaPedidoValido(any(UUID.class)))
                .thenReturn(pedido);

        mockMvc.perform(get("/pedidos/{id}",pedido.id().toString())).andExpect(status().isOk());
        verify(pedidoService, times(1)).consultaPedidoValido(any(UUID.class));
    }

    @Test
    void deveListarPedidosPorStatusComSucesso() throws Exception {
        var pedido = PedidoHelper.gerarPedido();
        var page = new PageImpl<>(Collections.singletonList(pedido));

        when(pedidoService.listaPedidosPorStatus(any(String.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/pedidos/status/{status}", pedido.status())
                        .param("page", "0")
                        .param("size", "10"))
//                        .andDo(print())
                .andExpect(status().isOk());

        verify(pedidoService, times(1)).listaPedidosPorStatus(any(String.class), any(Pageable.class));
    }

    @Test
    void deveAtualizarPedidoComSucesso() throws Exception {
        var pedido = PedidoHelper.gerarPedido();

        when(pedidoService.atualizaStatusPedido(any(UUID.class), any(String.class)))
                .thenReturn(pedido);

        mockMvc.perform(patch("/pedidos/{id}/status/{status}", pedido.id(), StatusPedido.EM_PREPARACAO.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pedido)))
                .andExpect(status().isOk());
        verify(pedidoService, times(1)).atualizaStatusPedido(any(UUID.class), any(String.class));
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
