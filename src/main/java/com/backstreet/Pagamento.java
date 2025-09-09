package com.backstreet;

import java.util.Date;

public class Pagamento {

    private static int COUNTER = 1;
    private int id;
    private double valor;
    private String metodo;
    private Date data;
    private StatusPagamento status; // ✅ usa enum

    public Pagamento(double valor, String metodo) {
        this.id = COUNTER++;
        this.valor = valor;
        this.metodo = metodo;
        this.data = new Date();
        this.status = StatusPagamento.PENDENTE;
    }

    public boolean validar() { return valor > 0; }

    public void confirmar() { this.status = StatusPagamento.CONFIRMADO; }

    public void recusar() { this.status = StatusPagamento.RECUSADO; }

    public StatusPagamento getStatus() { return status; }

    public int getId() { return id; }
    public double getValor() { return valor; }
    public String getMetodo() { return metodo; }
    public Date getData() { return data; }
}
