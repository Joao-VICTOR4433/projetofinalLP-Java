package com.javapizzaria;

public abstract class Usuario {
    protected int id;
    protected String nome;
    protected String tipo; // GERENTE, FUNCIONARIO, CLIENTE

    public Usuario(int id, String nome, String tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
}
