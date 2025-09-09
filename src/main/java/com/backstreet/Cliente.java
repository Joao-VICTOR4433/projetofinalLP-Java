package com.backstreet;

public class Cliente extends Usuario {
    private String contato;
    public Cliente() { this.tipo = "CLIENTE"; }
    public Cliente(int id, String login, String senha, String nome, String contato){
        super(id, login, senha, nome, "CLIENTE");
        this.contato = contato;
    }
    public Pedido fazerPedido(Comanda comanda, Produto produto, int quantidade){
        Pedido p = new Pedido(produto, quantidade);
        comanda.adicionarPedido(p);
        return p;
    }
    public Pagamento pagarConta(Comanda comanda, String metodo){
        return comanda.fecharComanda(metodo);
    }
    public String getContato(){ return contato; }
    public void setContato(String contato){ this.contato = contato; }
}
