package com.javapizzaria;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainJavalin {
    public static void main(String[] args) {
        SistemaPizzaria sistema = new SistemaPizzaria();

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/public";
                staticFiles.location = Location.CLASSPATH;
                staticFiles.precompress = false;
            });
            config.plugins.enableCors(cors -> {
                cors.add(CorsPluginConfig::anyHost);
            });
        }).start(7000);

        app.get("/api/usuarios", ctx -> ctx.json(sistema.getUsuarios()));
        app.post("/api/usuarios/gerente", ctx -> {
            String nome = ctx.formParam("nome");
            ctx.json(sistema.cadastrarGerente(nome));
        });
        app.post("/api/usuarios/funcionario", ctx -> {
            String nome = ctx.formParam("nome");
            ctx.json(sistema.cadastrarFuncionario(nome));
        });
        app.post("/api/usuarios/cliente", ctx -> {
            String nome = ctx.formParam("nome");
            ctx.json(sistema.cadastrarCliente(nome));
        });

        app.get("/api/produtos", ctx -> ctx.json(sistema.getProdutos()));
        app.post("/api/produtos/comida", ctx -> {
            String nome = ctx.formParam("nome");
            double preco = Double.parseDouble(ctx.formParam("preco"));
            int estoque = Integer.parseInt(ctx.formParam("estoque"));
            String imagem = ctx.formParam("imagem");
            String ingredientes = ctx.formParam("ingredientes");
            ctx.json(sistema.cadastrarComida(nome, preco, estoque, imagem, ingredientes));
        });
        app.post("/api/produtos/bebida", ctx -> {
            String nome = ctx.formParam("nome");
            double preco = Double.parseDouble(ctx.formParam("preco"));
            int estoque = Integer.parseInt(ctx.formParam("estoque"));
            String imagem = ctx.formParam("imagem");
            double volumeMl = Double.parseDouble(ctx.formParam("volumeMl"));
            double teorAlcool = Double.parseDouble(ctx.formParam("teorAlcool"));
            ctx.json(sistema.cadastrarBebida(nome, preco, estoque, imagem, volumeMl, teorAlcool));
        });

        app.get("/api/carrinho", ctx -> ctx.json(sistema.getCarrinho().getPedidoAtual()));
        app.post("/api/carrinho/add/:idProduto", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("idProduto"));
            var pedido = sistema.adicionarProdutoAoCarrinho(id);
            if (pedido == null) ctx.status(404).result("Produto não encontrado");
            else ctx.json(pedido);
        });

        app.post("/api/finalizar", ctx -> {
            String nomeCliente = ctx.formParam("cliente");
            Cliente c = new Cliente(9999, nomeCliente == null ? "Cliente" : nomeCliente);
            var pedidoFinal = sistema.finalizarPedidoAtual(c);
            ctx.json(pedidoFinal);
        });

        app.get("/api/historico", ctx -> ctx.json(sistema.getHistorico()));

        app.post("/api/relatorio/salvar", ctx -> {
            File f = Relatorio.salvarRelatorioDiario(sistema.getHistorico(), System.getProperty("user.dir"));
            Map<String, String> r = new HashMap<>();
            r.put("arquivo", f.getAbsolutePath());
            ctx.json(r);
        });
    }
}
