package com.backstreet;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    private String categoria;
    private int estoque;

    public Produto() {}
    public Produto(int id, String nome, double preco, String categoria, int estoque){
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.estoque = estoque;
    }
    public boolean isDisponivel(){ return estoque > 0; }
    public void atualizarEstoque(int qtd){ this.estoque -= qtd; }

    // getters/setters
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public String getNome(){ return nome; }
    public void setNome(String nome){ this.nome = nome; }
    public double getPreco(){ return preco; }
    public void setPreco(double preco){ this.preco = preco; }
    public String getCategoria(){ return categoria; }
    public void setCategoria(String categoria){ this.categoria = categoria; }
    public int getEstoque(){ return estoque; }
    public void setEstoque(int estoque){ this.estoque = estoque; }
}
