package com.pizzaria;

public class Produto {
    private String nome;
    private double preco;
    private String imagem;

    public Produto(String nome, double preco, String imagem) {
        this.nome = nome;
        this.preco = preco;
        this.imagem = imagem;
    }

    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public String getImagem() { return imagem; }

    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setImagem(String imagem) { this.imagem = imagem; }
}
