package com.backstreet;

public class MainDemo {
    public static void main(String[] args){
        Cliente cliente = new Cliente(1, "cliente1", "senha", "João", "(88)99999-9999");
        Gerente gerente = new Gerente(2, "gerente", "senha", "Ana", "Admin");
        Garcom garcom = new Garcom(3, "garcom", "senha", "Carlos", "G-01", "NOITE");

        Produto p1 = new Produto(1, "Cerveja Long Neck", 8.5, "Bebidas", 50);
        Produto p2 = new Produto(2, "Porção de Batata", 25.0, "Comidas", 20);

        Comanda comanda = garcom.abrirComanda(cliente);
        Pedido pedido1 = cliente.fazerPedido(comanda, p1, 2);
        Pedido pedido2 = cliente.fazerPedido(comanda, p2, 1);

        System.out.println("Total: R$ " + comanda.calcularTotal());

        Pagamento pagamento = cliente.pagarConta(comanda, "CARTAO");
        SistemaPagamento sp = new SistemaPagamento();
        sp.processarPagamento(pagamento);
        System.out.println("Pagamento status: " + pagamento.getStatus());
    }
}
