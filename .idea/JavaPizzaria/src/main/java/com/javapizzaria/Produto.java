package com.javapizzaria;

public abstract class Produto {
    protected int id;
    protected String nome;
    protected double preco;
    protected String categoria; // COMIDA, BEBIDA
    protected int estoque;
    protected String imagem; // caminho público

    public Produto(int id, String nome, double preco, String categoria, int estoque, String imagem) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.estoque = estoque;
        this.imagem = imagem;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public String getCategoria() { return categoria; }
    public int getEstoque() { return estoque; }
    public String getImagem() { return imagem; }

    public void setEstoque(int estoque) { this.estoque = estoque; }
}
