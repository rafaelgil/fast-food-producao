package br.com.fiap.postech.fastfoodproducao.application;

public enum StatusPedido {

    EM_PREPARACAO("Em preparação") {
        @Override
        public StatusPedido avancaPedido() {
            return PRONTO;
        }
    },
    PRONTO("Pronto") {
        @Override
        public StatusPedido avancaPedido() {
            return this;
        }
    };

    private String status;

    StatusPedido(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public abstract StatusPedido avancaPedido();

    public static StatusPedido getByStatus(String status) {

        for (StatusPedido statusPedido : StatusPedido.values()) {
            if (statusPedido.name().equals(status)) {
                return statusPedido;
            }
        }

        return null;
    }
}
