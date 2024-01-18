# language: pt
Funcionalidade: : API Pedidos

  Cenario: Listar Mensagens
    Quando listar todos os pedidos
    Entao a resposta contem 5 pedidos

  Cenario: Consulta Pedido
    Dado que o pedido existe
    Quando que o pedido existe
    Entao pedido é encontrado