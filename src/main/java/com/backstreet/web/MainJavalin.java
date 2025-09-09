package com.backstreet.web;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MainJavalin {
    private static final List<Cliente> clientes = new ArrayList<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    // Simulação de banco de dados de usuários
    private static final Map<String, Usuario> usuarios = new HashMap<>();

    // Sessões ativas (em produção, use um sistema de sessão adequado)
    private static final Map<String, String> sessoesAtivas = new HashMap<>();

    public static void main(String[] args) {
        // Criar a pasta public se não existir
        createPublicFolderIfNotExists();

        // Inicializar usuários de exemplo
        inicializarUsuarios();

        // Tentar portas diferentes se a 7000 estiver ocupada
        int port = 7000;
        Javalin app = null;

        while (app == null && port < 7020) {
            try {
                final int currentPort = port;
                app = Javalin.create(config -> {
                    config.staticFiles.add(staticFiles -> {
                        staticFiles.directory = "src/main/resources/public";
                        staticFiles.location = Location.EXTERNAL;
                        staticFiles.hostedPath = "/";
                    });
                }).start(currentPort);

                System.out.println("Servidor rodando em http://localhost:" + currentPort);
                port = currentPort;
            } catch (Exception e) {
                if (e.getMessage().contains("Port already in use") ||
                        e.getMessage().contains("Address already in use")) {
                    System.out.println("Porta " + port + " já está em uso, tentando porta " + (port + 1));
                    port++;
                } else {
                    e.printStackTrace();
                    return;
                }
            }
        }

        if (app == null) {
            System.err.println("Não foi possível iniciar o servidor em nenhuma porta entre 7000-7019");
            return;
        }

        // Middleware para verificar autenticação
        app.before(ctx -> {
            String path = ctx.path();
            if (!path.equals("/login") && !path.equals("/auth") && !path.startsWith("/public/")) {
                String sessionId = ctx.cookie("session_id");
                if (sessionId == null || !sessoesAtivas.containsKey(sessionId)) {
                    ctx.redirect("/login");
                }
            }
        });

        // Rota principal - redireciona para index.html
        app.get("/", ctx -> ctx.redirect("/index.html"));

        // Página de login
        app.get("/login", ctx -> {
            ctx.html(createLoginPage());
        });

        // Autenticação
        app.post("/auth", ctx -> {
            String email = ctx.formParam("email");
            String senha = ctx.formParam("senha");

            if (autenticarUsuario(email, senha)) {
                String sessionId = java.util.UUID.randomUUID().toString();
                sessoesAtivas.put(sessionId, email);
                ctx.cookie("session_id", sessionId, 3600); // 1 hora
                ctx.redirect("/");
            } else {
                ctx.html(createLoginPage("Email ou senha inválidos!"));
            }
        });

        // Logout
        app.get("/logout", ctx -> {
            String sessionId = ctx.cookie("session_id");
            if (sessionId != null) {
                sessoesAtivas.remove(sessionId);
            }
            ctx.removeCookie("session_id");
            ctx.redirect("/login");
        });

        // Página de sobre
        app.get("/sobre", ctx -> ctx.html(
                "<h2>Sobre o Bar do Seu Zé</h2>" +
                        "<p>Local na UFPB com bebidas e petiscos.</p>" +
                        "<a href='/'>Voltar</a> | <a href='/logout'>Sair</a>"
        ));

        // Endpoint JSON básico - lista todos clientes
        app.get("/api/clientes", ctx -> ctx.json(clientes));

        // Criar cliente (POST)
        app.post("/api/clientes", ctx -> {
            Cliente novo = ctx.bodyAsClass(Cliente.class);
            novo.setId(idGenerator.getAndIncrement());
            clientes.add(novo);
            ctx.status(201).json(novo);
        });

        // Buscar cliente por ID
        app.get("/api/clientes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            clientes.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .ifPresentOrElse(
                            ctx::json,
                            () -> ctx.status(404).result("Cliente não encontrado")
                    );
        });

        // Atualizar cliente
        app.put("/api/clientes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Cliente atualizado = ctx.bodyAsClass(Cliente.class);
            boolean found = false;
            for (int i = 0; i < clientes.size(); i++) {
                if (clientes.get(i).getId() == id) {
                    atualizado.setId(id);
                    clientes.set(i, atualizado);
                    found = true;
                    break;
                }
            }
            if (found) ctx.json(atualizado);
            else ctx.status(404).result("Cliente não encontrado");
        });

        // Deletar cliente
        app.delete("/api/clientes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean removed = clientes.removeIf(c -> c.getId() == id);
            if (removed) ctx.result("Cliente removido");
            else ctx.status(404).result("Cliente não encontrado");
        });

        // Rota para processar pagamentos
        app.post("/pay", ctx -> {
            String produto = ctx.formParam("produto");
            String valor = ctx.formParam("valor");
            String metodo = ctx.formParam("metodo");

            // Simular processamento de pagamento
            String resposta = "Pagamento processado: " + produto +
                    " | Valor: R$ " + valor +
                    " | Método: " + metodo;

            ctx.html("<html><head><title>Pagamento Processado</title>" +
                    "<style>body { font-family: Arial, sans-serif; padding: 20px; }</style></head>" +
                    "<body><h2>Pagamento Processado</h2><p>" + resposta + "</p>" +
                    "<a href='/index.html'>Voltar ao Bar</a> | <a href='/logout'>Sair</a></body></html>");
        });
    }

    private static void inicializarUsuarios() {
        // Usuários de exemplo (em produção, use banco de dados com senhas criptografadas)
        usuarios.put("admin@bar.com", new Usuario("admin@bar.com", "admin123", "Administrador"));
        usuarios.put("cliente@email.com", new Usuario("cliente@email.com", "cliente123", "João Cliente"));
        usuarios.put("garcom@bar.com", new Usuario("garcom@bar.com", "garcom123", "Carlos Garçom"));
    }

    private static boolean autenticarUsuario(String email, String senha) {
        Usuario usuario = usuarios.get(email);
        return usuario != null && usuario.getSenha().equals(senha);
    }

    private static String createLoginPage() {
        return createLoginPage("");
    }

    private static String createLoginPage(String mensagemErro) {
        String estiloErro = mensagemErro.isEmpty() ? "display:none;" : "color:red;";

        return "<!DOCTYPE html>\n" +
                "<html lang=\"pt-br\">\n" +
                "<head>\n" +
                "   <meta charset=\"UTF-8\">\n" +
                "   <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "   <title>Login - Bar do Seu Zé</title>\n" +
                "   <style>\n" +
                "       body { \n" +
                "           font-family: Arial, sans-serif; \n" +
                "           background-color: #f8f3e6; \n" +
                "           margin: 0; \n" +
                "           padding: 0; \n" +
                "           display: flex; \n" +
                "           justify-content: center; \n" +
                "           align-items: center; \n" +
                "           height: 100vh; \n" +
                "       }\n" +
                "       .login-container { \n" +
                "           background: white; \n" +
                "           padding: 2rem; \n" +
                "           border-radius: 10px; \n" +
                "           box-shadow: 0 0 10px rgba(0,0,0,0.1); \n" +
                "           width: 300px; \n" +
                "       }\n" +
                "       h2 { \n" +
                "           text-align: center; \n" +
                "           color: #8b0000; \n" +
                "           margin-bottom: 1.5rem; \n" +
                "       }\n" +
                "       .form-group { \n" +
                "           margin-bottom: 1rem; \n" +
                "       }\n" +
                "       label { \n" +
                "           display: block; \n" +
                "           margin-bottom: 0.5rem; \n" +
                "           font-weight: bold; \n" +
                "       }\n" +
                "       input[type=\"email\"], input[type=\"password\"] { \n" +
                "           width: 100%; \n" +
                "           padding: 0.8rem; \n" +
                "           border: 1px solid #ddd; \n" +
                "           border-radius: 4px; \n" +
                "           box-sizing: border-box; \n" +
                "       }\n" +
                "       button { \n" +
                "           width: 100%; \n" +
                "           background-color: #8b0000; \n" +
                "           color: white; \n" +
                "           padding: 1rem; \n" +
                "           border: none; \n" +
                "           border-radius: 4px; \n" +
                "           cursor: pointer; \n" +
                "           font-size: 1rem; \n" +
                "       }\n" +
                "       button:hover { \n" +
                "           background-color: #9b0000; \n" +
                "       }\n" +
                "       .error { \n" +
                "           color: red; \n" +
                "           text-align: center; \n" +
                "           margin-bottom: 1rem; \n" +
                "       }\n" +
                "   </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "   <div class=\"login-container\">\n" +
                "       <h2>Bar do Seu Zé 🍹</h2>\n" +
                "       <div class=\"error\" style=\"" + estiloErro + "\">" + mensagemErro + "</div>\n" +
                "       <form action=\"/auth\" method=\"post\">\n" +
                "           <div class=\"form-group\">\n" +
                "               <label for=\"email\">Email:</label>\n" +
                "               <input type=\"email\" id=\"email\" name=\"email\" required>\n" +
                "           </div>\n" +
                "           <div class=\"form-group\">\n" +
                "               <label for=\"senha\">Senha:</label>\n" +
                "               <input type=\"password\" id=\"senha\" name=\"senha\" required>\n" +
                "           </div>\n" +
                "           <button type=\"submit\">Entrar</button>\n" +
                "       </form>\n" +
                "       <div style=\"margin-top: 1rem; text-align: center;\">\n" +
                "           <small>Usuários de teste:<br>\n" +
                "           admin@bar.com / admin123<br>\n" +
                "           cliente@email.com / cliente123</small>\n" +
                "       </div>\n" +
                "   </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private static void createPublicFolderIfNotExists() {
        try {
            java.nio.file.Path publicPath = Paths.get("src/main/resources/public");
            if (!Files.exists(publicPath)) {
                Files.createDirectories(publicPath);

                // Criar subpastas
                Files.createDirectories(publicPath.resolve("css"));
                Files.createDirectories(publicPath.resolve("images"));
                Files.createDirectories(publicPath.resolve("js"));

                // Criar arquivo CSS básico
                String cssContent = "body {\n" +
                        "    font-family: Arial, sans-serif;\n" +
                        "    background-color: #f8f3e6;\n" +
                        "    color: #333;\n" +
                        "    margin: 0;\n" +
                        "    padding: 0;\n" +
                        "}\n" +
                        "header {\n" +
                        "    background-color: #8b0000;\n" +
                        "    color: #fff;\n" +
                        "    padding: 20px;\n" +
                        "    text-align: center;\n" +
                        "    display: flex;\n" +
                        "    justify-content: space-between;\n" +
                        "    align-items: center;\n" +
                        "}\n" +
                        "header img {\n" +
                        "    margin-top: 10px;\n" +
                        "}\n" +
                        "main {\n" +
                        "    padding: 20px;\n" +
                        "}\n" +
                        ".cardapio h2, .pagamento h2 {\n" +
                        "    text-align: center;\n" +
                        "    margin-bottom: 15px;\n" +
                        "}\n" +
                        ".produtos {\n" +
                        "    display: flex;\n" +
                        "    justify-content: space-around;\n" +
                        "    flex-wrap: wrap;\n" +
                        "}\n" +
                        ".produto {\n" +
                        "    border: 1px solid #ccc;\n" +
                        "    padding: 10px;\n" +
                        "    margin: 10px;\n" +
                        "    text-align: center;\n" +
                        "    width: 150px;\n" +
                        "    background-color: #fff8f0;\n" +
                        "    border-radius: 8px;\n" +
                        "    cursor: pointer;\n" +
                        "}\n" +
                        ".produto:hover {\n" +
                        "    background-color: #ffe6cc;\n" +
                        "}\n" +
                        ".produto img {\n" +
                        "    width: 100%;\n" +
                        "    height: auto;\n" +
                        "    border-radius: 5px;\n" +
                        "}\n" +
                        "footer {\n" +
                        "    background-color: #8b0000;\n" +
                        "    color: #fff;\n" +
                        "    text-align: center;\n" +
                        "    padding: 10px;\n" +
                        "    margin-top: 20px;\n" +
                        "}\n" +
                        ".placeholder-img {\n" +
                        "    width: 100%;\n" +
                        "    height: 100px;\n" +
                        "    background-color: #ddd;\n" +
                        "    display: flex;\n" +
                        "    align-items: center;\n" +
                        "    justify-content: center;\n" +
                        "    color: #777;\n" +
                        "    font-size: 12px;\n" +
                        "}\n" +
                        ".user-info {\n" +
                        "    font-size: 0.9rem;\n" +
                        "}\n" +
                        ".user-info a {\n" +
                        "    color: #fff;\n" +
                        "    text-decoration: none;\n" +
                        "    margin-left: 10px;\n" +
                        "}\n" +
                        ".user-info a:hover {\n" +
                        "    text-decoration: underline;\n" +
                        "}";

                Files.write(publicPath.resolve("css/style.css"), cssContent.getBytes());

                // Criar arquivo JavaScript
                String jsContent = "// Função para preencher automaticamente o formulário ao clicar em um produto\n" +
                        "const produtos = document.querySelectorAll('.produto');\n" +
                        "const inputProduto = document.querySelector('input[name=\"produto\"]');\n" +
                        "const inputValor = document.querySelector('input[name=\"valor\"]');\n" +
                        "const selectMetodo = document.querySelector('select[name=\"metodo\"]');\n" +
                        "const formPagamento = document.querySelector('form[action=\"/pay\"]');\n" +
                        "\n" +
                        "produtos.forEach(produto => {\n" +
                        "   produto.addEventListener('click', () => {\n" +
                        "       const nome = produto.querySelector('h3').textContent;\n" +
                        "       const valor = produto.querySelector('p').textContent.replace('R$', '').replace(',', '.').trim();\n" +
                        "\n" +
                        "       inputProduto.value = nome;\n" +
                        "       inputValor.value = valor;\n" +
                        "       inputProduto.focus(); // foco no campo produto para revisão\n" +
                        "   });\n" +
                        "});\n" +
                        "\n" +
                        "formPagamento.addEventListener('submit', (event) => {\n" +
                        "   event.preventDefault(); // impede o envio real por enquanto\n" +
                        "\n" +
                        "   const produto = inputProduto.value.trim();\n" +
                        "   const valor = parseFloat(inputValor.value.replace(',', '.'));\n" +
                        "   const metodo = selectMetodo.value;\n" +
                        "\n" +
                        "   // Validações\n" +
                        "   if (!produto) {\n" +
                        "       alert('Por favor, digite o nome do produto.');\n" +
                        "       inputProduto.focus();\n" +
                        "       return;\n" +
                        "   }\n" +
                        "\n" +
                        "   if (!valor || isNaN(valor) || valor <= 0) {\n" +
                        "       alert('Por favor, digite um valor válido.');\n" +
                        "       inputValor.focus();\n" +
                        "       return;\n" +
                        "   }\n" +
                        "\n" +
                        "   if (!metodo) {\n" +
                        "       alert('Selecione um método de pagamento.');\n" +
                        "       selectMetodo.focus();\n" +
                        "       return;\n" +
                        "   }\n" +
                        "\n" +
                        "   alert('✅ Pagamento de R$' + valor.toFixed(2) + ' pelo produto \"' + produto + '\" via ' + metodo + ' realizado com sucesso!');\n" +
                        "   \n" +
                        "   // Se tudo estiver válido, enviar o formulário\n" +
                        "   formPagamento.submit();\n" +
                        "});";

                Files.write(publicPath.resolve("js/pagamento.js"), jsContent.getBytes());

                // Criar HTML básico
                String htmlContent = "<!DOCTYPE html>\n" +
                        "<html lang=\"pt-br\">\n" +
                        "<head>\n" +
                        "   <meta charset=\"UTF-8\">\n" +
                        "   <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "   <title>Bar do Seu Zé UFPB</title>\n" +
                        "   <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<header>\n" +
                        "   <h1>Bar do Seu Zé 🍹 UFPB</h1>\n" +
                        "   <div class=\"user-info\">\n" +
                        "       <span id=\"user-email\"></span>\n" +
                        "       <a href=\"/sobre\">Sobre</a>\n" +
                        "       <a href=\"/logout\">Sair</a>\n" +
                        "   </div>\n" +
                        "</header>\n" +
                        "<main>\n" +
                        "   <section class=\"cardapio\">\n" +
                        "       <h2>Cardápio</h2>\n" +
                        "       <div class=\"produtos\">\n" +
                        "           <div class=\"produto\">\n" +
                        "               <div class=\"placeholder-img\">CHOPP</div>\n" +
                        "               <h3>Chopp</h3>\n" +
                        "               <p>R$10,00</p>\n" +
                        "           </div>\n" +
                        "           <div class=\"produto\">\n" +
                        "               <div class=\"placeholder-img\">CAIPIRINHA</div>\n" +
                        "               <h3>Caipirinha</h3>\n" +
                        "               <p>R$15,00</p>\n" +
                        "           </div>\n" +
                        "           <div class=\"produto\">\n" +
                        "               <div class=\"placeholder-img\">BATATA FRITA</div>\n" +
                        "               <h3>Batata Frita</h3>\n" +
                        "               <p>R$12,00</p>\n" +
                        "           </div>\n" +
                        "       </div>\n" +
                        "   </section>\n" +
                        "   <section class=\"pagamento\">\n" +
                        "       <h2>Pagamento</h2>\n" +
                        "       <form action=\"/pay\" method=\"post\">\n" +
                        "           Produto: <input type=\"text\" name=\"produto\" placeholder=\"Ex: Chopp\"><br><br>\n" +
                        "           Valor: <input type=\"text\" name=\"valor\" placeholder=\"Ex: 10\"><br><br>\n" +
                        "           Método:\n" +
                        "           <select name=\"metodo\">\n" +
                        "               <option value=\"\">Selecione...</option>\n" +
                        "               <option value=\"PIX\">PIX</option>\n" +
                        "               <option value=\"Cartao\">Cartão</option>\n" +
                        "               <option value=\"Dinheiro\">Dinheiro</option>\n" +
                        "           </select><br><br>\n" +
                        "           <button type=\"submit\">Pagar</button>\n" +
                        "       </form>\n" +
                        "   </section>\n" +
                        "</main>\n" +
                        "<footer>\n" +
                        "   <p>© 2025 Bar do Seu Zé UFPB - Todos os direitos reservados</p>\n" +
                        "</footer>\n" +
                        "<script src=\"js/pagamento.js\"></script>\n" +
                        "<script>\n" +
                        "   // Simular informação do usuário logado\n" +
                        "   document.getElementById('user-email').textContent = 'Usuário: ' + (localStorage.getItem('userEmail') || 'Visitante');\n" +
                        "</script>\n" +
                        "</body>\n" +
                        "</html>";

                Files.write(publicPath.resolve("index.html"), htmlContent.getBytes());

                System.out.println("Pasta public e arquivos básicos criados automaticamente.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar pasta public: " + e.getMessage());
        }
    }

    public static class Cliente {
        private int id;
        private String nome;
        private String email;

        public Cliente() { }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class Usuario {
        private String email;
        private String senha;
        private String nome;

        public Usuario(String email, String senha, String nome) {
            this.email = email;
            this.senha = senha;
            this.nome = nome;
        }

        public String getEmail() { return email; }
        public String getSenha() { return senha; }
        public String getNome() { return nome; }
    }
}