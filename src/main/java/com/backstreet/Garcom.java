package com.backstreet;

public class Garcom extends Usuario {
    private String matricula;
    private String turno;
    public Garcom(){ this.tipo = "GARCOM"; }
    public Garcom(int id, String login, String senha, String nome, String matricula, String turno){
        super(id, login, senha, nome, "GARCOM");
        this.matricula = matricula;
        this.turno = turno;
    }
    public Comanda abrirComanda(Cliente cliente){
        Comanda c = new Comanda(cliente);
        return c;
    }
    public void registrarPedido(Comanda comanda, Pedido pedido){
        comanda.adicionarPedido(pedido);
    }
    public Pagamento fecharComanda(Comanda comanda, String metodo){
        return comanda.fecharComanda(metodo);
    }
    // getters/setters
    public String getMatricula(){ return matricula; }
    public void setMatricula(String m){ this.matricula = m; }
    public String getTurno(){ return turno; }
    public void setTurno(String t){ this.turno = t; }
}
