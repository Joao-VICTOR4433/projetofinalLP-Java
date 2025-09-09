package com.javapizzaria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<Produto> itens = new ArrayList<>();
    private double total = 0.0;
    private LocalDateTime criadoEm = LocalDateTime.now();
    private String status = "ABERTO"; // ABERTO | FINALIZADO

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
    }

    public void adicionarProduto(Produto p) {
        if (p == null) return;
        itens.add(p);
        total += p.getPreco();
    }

    public void finalizar() { this.status = "FINALIZADO"; }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<Produto> getItens() { return itens; }
    public double getTotal() { return total; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public String getStatus() { return status; }
}
