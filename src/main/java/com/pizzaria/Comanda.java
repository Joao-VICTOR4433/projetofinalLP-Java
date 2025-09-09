package com.backstreet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comanda {
    private static int COUNTER = 1;
    private int id;
    private int numero;
    private Cliente cliente;
    private List<Pedido> pedidos;
    private String status; // ABERTA, FECHADA
    private Date dataAbertura;

    public Comanda(Cliente cliente){
        this.id = COUNTER++;
        this.numero = this.id;
        this.cliente = cliente;
        this.pedidos = new ArrayList<>();
        this.status = "ABERTA";
        this.dataAbertura = new Date();
    }

    public void adicionarPedido(Pedido p){
        if(p == null) return;
        pedidos.add(p);
    }

    public double calcularTotal(){
        return pedidos.stream().mapToDouble(Pedido::calcularSubtotal).sum();
    }

    public Pagamento fecharComanda(String metodo){
        double total = calcularTotal();
        Pagamento pag = new Pagamento(total, metodo);
        this.status = "FECHADA";
        return pag;
    }

    // getters/setters
    public int getId(){ return id; }
    public int getNumero(){ return numero; }
    public Cliente getCliente(){ return cliente; }
    public List<Pedido> getPedidos(){ return pedidos; }
    public String getStatus(){ return status; }
    public Date getDataAbertura(){ return dataAbertura; }
}
