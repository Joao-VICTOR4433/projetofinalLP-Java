package com.pizzaria;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private List<Produto> itens = new ArrayList<>();
    private double total = 0;

    public void adicionarProduto(Produto p) {
        itens.add(p);
        total += p.getPreco();
    }

    public List<Produto> getItens() {
        return itens;
    }

    public double getTotal() {
        return total;
    }
}
