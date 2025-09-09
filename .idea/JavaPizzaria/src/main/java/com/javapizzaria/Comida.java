package com.javapizzaria;

public class Comida extends Produto {
    private String ingredientes;

    public Comida(int id, String nome, double preco, int estoque, String imagem, String ingredientes) {
        super(id, nome, preco, "COMIDA", estoque, imagem);
        this.ingredientes = ingredientes;
    }

    public String getIngredientes() { return ingredientes; }
}
