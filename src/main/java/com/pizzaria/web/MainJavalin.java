package com.pizzaria.web;

import io.javalin.Javalin;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainJavalin {
    public static void main(String[] args) throws Exception {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
        }).start(7000);

        // Página inicial
        app.get("/", ctx -> {
            ctx.result(new String(Files.readAllBytes(Paths.get("src/main/resources/public/index.html"))));
        });

        // Rota de pagamento
        app.post("/pay", ctx -> {
            String metodo = ctx.formParam("metodo");
            String produto = ctx.formParam("produto");
            String valor = ctx.formParam("valor");

            Pagamento p = new Pagamento(Double.parseDouble(valor), metodo);

            if ("PIX".equalsIgnoreCase(metodo)) {
                String pixPayload = "PIX|chave:pixpizzaria@exemplo.com|valor:" + valor + "|produto:" + produto;
                ctx.redirect("/pix?data=" + URLEncoder.encode(pixPayload, "UTF-8"));
            } else {
                if (p.validar()) {
                    p.confirmar();
                    ctx.html("<h2>✅ Pedido de " + produto + " pago com " + metodo + ".</h2><a href='/'>Voltar</a>");
                } else {
                    p.setStatus("RECUSADO");
                    ctx.html("<h2>❌ Pagamento recusado.</h2><a href='/'>Voltar</a>");
                }
            }
        });

        // Página PIX
        app.get("/pix", ctx -> {
            String data = ctx.queryParam("data");
            if (data == null) { ctx.status(400).result("Dados do PIX não fornecidos"); return; }
            String page = new String(Files.readAllBytes(Paths.get("src/main/resources/public/pix.html")));
            page = page.replace("{{PIX_PAYLOAD}}", data);
            page = page.replace("{{QR_SRC}}", "/qrcode?data=" + URLEncoder.encode(data, "UTF-8"));
            ctx.html(page);
        });

        // Gerar QR code
        app.get("/qrcode", ctx -> {
            String data = ctx.queryParam("data");
            if (data == null) { ctx.status(400).result("Sem dados para gerar QR"); return; }
            byte[] img = QRUtil.generateQRCodeImage(data, 300, 300);
            ctx.contentType("image/png").result(img);
        });
    }
}
