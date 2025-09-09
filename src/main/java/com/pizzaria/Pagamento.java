package com.pizzaria.web;

public class Pagamento {
    private double valor;
    private String metodo;
    private String status;

    public Pagamento(double valor, String metodo) {
        this.valor = valor;
        this.metodo = metodo;
        this.status = "PENDENTE";
    }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean validar() { return valor > 0; }
    public void confirmar() { this.status = "CONFIRMADO"; }
}
