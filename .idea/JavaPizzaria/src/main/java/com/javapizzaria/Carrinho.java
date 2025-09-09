package com.javapizzaria;

public class Carrinho {
    private Pedido pedidoAtual;

    public Carrinho(Pedido pedidoAtual) {
        this.pedidoAtual = pedidoAtual;
    }

    public Pedido getPedidoAtual() {
        return pedidoAtual;
    }

    public void novoPedido(Pedido pedido) {
        this.pedidoAtual = pedido;
    }
}
