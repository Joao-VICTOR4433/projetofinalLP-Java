package com.backstreet;

import java.util.List;

public class Gerente extends Usuario {
    private String area;
    public Gerente(){ this.tipo = "GERENTE"; }
    public Gerente(int id, String login, String senha, String nome, String area){
        super(id, login, senha, nome, "GERENTE");
        this.area = area;
    }
    public void cadastrarProduto(Produto p){ /* persistir produto */ }
    public Relatorio gerarRelatorio(Periodo periodo){ return new Relatorio(); }
    // getters/setters
    public String getArea(){ return area; }
    public void setArea(String area){ this.area = area; }
}
