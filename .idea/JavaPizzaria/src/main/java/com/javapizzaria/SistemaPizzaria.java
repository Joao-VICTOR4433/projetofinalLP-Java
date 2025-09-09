package com.javapizzaria;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SistemaPizzaria {
    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Produto> produtos = new ArrayList<>();
    private final List<Pedido> pedidosDia = new ArrayList<>();
    private final List<Pedido> historico = new ArrayList<>();
    private final AtomicInteger seqUser = new AtomicInteger(1);
    private final AtomicInteger seqProduto = new AtomicInteger(1);
    private final AtomicInteger seqPedido = new AtomicInteger(1);

    private Carrinho carrinho;

    public SistemaPizzaria() {
        // seed
        Gerente g = new Gerente(seqUser.getAndIncrement(), "Ana Gerente");
        Funcionario f = new Funcionario(seqUser.getAndIncrement(), "Carlos");
        Cliente c = new Cliente(seqUser.getAndIncrement(), "João");
        usuarios.add(g);
        usuarios.add(f);
        usuarios.add(c);

        produtos.add(new Comida(seqProduto.getAndIncrement(), "Pizza Calabresa", 40.0, 50, "/images/pizza.png", "calabresa, queijo, cebola"));
        produtos.add(new Bebida(seqProduto.getAndIncrement(), "Coca-Cola 2L", 12.0, 100, "/images/coca.png", 2000, 0.0));
        produtos.add(new Comida(seqProduto.getAndIncrement(), "Pudim de Leite", 8.0, 30, "/images/sobremesa.png", "leite, ovos, açúcar"));

        Pedido p0 = new Pedido(seqPedido.getAndIncrement(), c);
        this.carrinho = new Carrinho(p0);
        pedidosDia.add(p0);
    }

    public List<Usuario> getUsuarios() { return usuarios; }
    public List<Produto> getProdutos() { return produtos; }
    public List<Pedido> getPedidosDia() { return pedidosDia; }
    public List<Pedido> getHistorico() { return historico; }
    public Carrinho getCarrinho() { return carrinho; }

    public Gerente cadastrarGerente(String nome) {
        Gerente g = new Gerente(seqUser.getAndIncrement(), nome);
        usuarios.add(g);
        return g;
    }

    public Funcionario cadastrarFuncionario(String nome) {
        Funcionario f = new Funcionario(seqUser.getAndIncrement(), nome);
        usuarios.add(f);
        return f;
    }

    public Cliente cadastrarCliente(String nome) {
        Cliente c = new Cliente(seqUser.getAndIncrement(), nome);
        usuarios.add(c);
        return c;
    }

    public Produto cadastrarComida(String nome, double preco, int estoque, String imagem, String ingredientes) {
        Comida c = new Comida(seqProduto.getAndIncrement(), nome, preco, estoque, imagem, ingredientes);
        produtos.add(c);
        return c;
    }

    public Produto cadastrarBebida(String nome, double preco, int estoque, String imagem, double volumeMl, double teorAlcool) {
        Bebida b = new Bebida(seqProduto.getAndIncrement(), nome, preco, estoque, imagem, volumeMl, teorAlcool);
        produtos.add(b);
        return b;
    }

    public Produto buscarProdutoPorId(int id) {
        return produtos.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public Pedido adicionarProdutoAoCarrinho(int produtoId) {
        Produto p = buscarProdutoPorId(produtoId);
        if (p == null) return null;
        carrinho.getPedidoAtual().adicionarProduto(p);
        return carrinho.getPedidoAtual();
    }

    public Pedido finalizarPedidoAtual(Cliente cliente) {
        Pedido atual = carrinho.getPedidoAtual();
        atual.finalizar();
        historico.add(atual);

        // novo pedido para continuar o carrinho
        Pedido novo = new Pedido(seqPedido.getAndIncrement(), cliente);
        carrinho.novoPedido(novo);
        pedidosDia.add(novo);
        return atual;
    }
}
