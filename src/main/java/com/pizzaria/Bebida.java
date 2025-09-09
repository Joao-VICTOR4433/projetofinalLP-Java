package com.backstreet;

public class Bebida extends Produto {
    private double teorAlcool;
    private double volume;
    public Bebida(){ super(); }
    public Bebida(int id, String nome, double preco, String categoria, int estoque, double teorAlcool, double volume){
        super(id, nome, preco, categoria, estoque);
        this.teorAlcool = teorAlcool;
        this.volume = volume;
    }
    public boolean isAlcoolica(){ return teorAlcool > 0; }
    // getters/setters
    public double getTeorAlcool(){ return teorAlcool; }
    public void setTeorAlcool(double t){ this.teorAlcool = t; }
    public double getVolume(){ return volume; }
    public void setVolume(double v){ this.volume = v; }
}
