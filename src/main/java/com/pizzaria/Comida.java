package com.backstreet;

public class Comida extends Produto {
    private double peso;
    private String ingredientes;
    public Comida(){ super(); }
    public Comida(int id, String nome, double preco, String categoria, int estoque, double peso, String ingredientes){
        super(id, nome, preco, categoria, estoque);
        this.peso = peso;
        this.ingredientes = ingredientes;
    }
    public double calcularCalorias(){ return peso * 2.5; }
    // getters/setters
    public double getPeso(){ return peso; }
    public void setPeso(double p){ this.peso = p; }
    public String getIngredientes(){ return ingredientes; }
    public void setIngredientes(String i){ this.ingredientes = i; }
}
