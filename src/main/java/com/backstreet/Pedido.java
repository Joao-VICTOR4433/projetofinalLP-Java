package com.backstreet;

public class Pedido {
    private static int COUNTER = 1;
    private int id;
    private Produto produto;
    private int quantidade;
    private double subtotal;

    public Pedido(){ this.id = COUNTER++; }
    public Pedido(Produto produto, int quantidade){
        this.id = COUNTER++;
        this.produto = produto;
        this.quantidade = quantidade;
        this.subtotal = calcularSubtotal();
    }
    public double calcularSubtotal(){
        if(produto == null) return 0.0;
        return produto.getPreco() * quantidade;
    }
    // getters/setters
    public int getId(){ return id; }
    public Produto getProduto(){ return produto; }
    public void setProduto(Produto p){ this.produto = p; }
    public int getQuantidade(){ return quantidade; }
    public void setQuantidade(int q){ this.quantidade = q; }
    public double getSubtotal(){ return subtotal; }
    public void setSubtotal(double s){ this.subtotal = s; }
}
