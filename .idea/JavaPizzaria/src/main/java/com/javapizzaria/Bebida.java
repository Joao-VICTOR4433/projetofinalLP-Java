package com.javapizzaria;

public class Bebida extends Produto {
    private double volumeMl;
    private double teorAlcool;

    public Bebida(int id, String nome, double preco, int estoque, String imagem, double volumeMl, double teorAlcool) {
        super(id, nome, preco, "BEBIDA", estoque, imagem);
        this.volumeMl = volumeMl;
        this.teorAlcool = teorAlcool;
    }

    public double getVolumeMl() { return volumeMl; }
    public double getTeorAlcool() { return teorAlcool; }
    public boolean isAlcoolica() { return teorAlcool > 0.0; }
}
