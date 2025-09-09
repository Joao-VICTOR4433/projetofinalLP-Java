package com.backstreet;

public abstract class Usuario {
    protected int id;
    protected String login;
    protected String senha;
    protected String nome;
    protected String tipo; // CLIENTE, GARCOM, GERENTE

    public Usuario() {}

    public Usuario(int id, String login, String senha, String nome, String tipo) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.tipo = tipo;
    }

    public boolean autenticar(String login, String senha){
        return this.login.equals(login) && this.senha.equals(senha);
    }

    // getters and setters
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public String getLogin(){ return login; }
    public void setLogin(String login){ this.login = login; }
    public String getSenha(){ return senha; }
    public void setSenha(String senha){ this.senha = senha; }
    public String getNome(){ return nome; }
    public void setNome(String nome){ this.nome = nome; }
    public String getTipo(){ return tipo; }
    public void setTipo(String tipo){ this.tipo = tipo; }
}
