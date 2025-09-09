🍕 JavaPizzaria

JavaPizzaria é um sistema completo de gerenciamento de pizzaria, desenvolvido em Java com Javalin, que permite cadastrar produtos, realizar pedidos, processar pagamentos (PIX/cartão), gerenciar funcionários e gerar relatórios diários. O front-end é moderno, responsivo e totalmente funcional.

🌟 Visão Geral

O projeto simula uma pizzaria profissional, contemplando cadastro e gerenciamento de produtos (comidas e bebidas), carrinho de compras com cálculo automático de total, pagamentos online via PIX ou cartão, painel administrativo para gerentes e funcionários e relatórios diários de vendas. O front-end é responsivo e moderno. O sistema utiliza armazenamento local em memória, permitindo fácil manutenção e estudo.

🎯 Funcionalidades Principais

Front-end (Cliente): Exibição de cardápio completo (com imagens, nomes e preços), adição de produtos ao carrinho, visualização do resumo de pedidos, pagamento via PIX e cartão com QR Code e histórico de pedidos.

Painel Administrativo: Cadastro de novos produtos (comidas e bebidas), cadastro de funcionários e gerentes, visualização de pedidos do dia e geração de relatórios simples de vendas.

Sistema de Pagamento: Pagamento com validação do status (PENDENTE, CONFIRMADO, RECUSADO), registro de transações e integração com front-end para simular pagamento.

🛠 Tecnologias Utilizadas

Java 24, Javalin 5.6.1, Jetty Server, HTML5, CSS3, JavaScript e Maven para gerenciamento de dependências.

🏗 Arquitetura do Projeto
JavaPizzaria/
 ├── src/
 │    ├── main/java/com/javapizzaria/  # Classes Java (Produto, Carrinho, Pedido, Cliente, Gerente, Garcom, Pagamento)
 │    └── main/resources/public/       # Front-end (HTML, CSS, JS)
 ├── target/                            # Build do Maven
 ├── pom.xml                            # Configuração Maven
 └── README.md                          # Documentação


Produto: Classe abstrata, base para Comida e Bebida

Carrinho: Gerencia o pedido atual

Pedido: Contém os produtos e calcula subtotal

Usuario: Base para Cliente, Garcom e Gerente

SistemaPizzaria: Controla produtos, carrinho e histórico

SistemaPagamento: Processa pagamentos simulados

MainJavalin: Ponto de entrada, inicializa servidor Javalin e APIs REST

🚀 Como Executar

Clone o repositório:

git clone https://github.com/Joao-VICTOR4433/projetofinalLP-Java.git


Abra o projeto no IntelliJ IDEA

Compile e execute a classe MainJavalin.java

Abra o navegador e acesse:

http://localhost:7000


Explore o cardápio, painel admin e sistema de pagamento


👨‍💻 Autores

João Victor Nogueira Alves,LEONARDO LUCENA BIZERRIL DE BRITO, RENATO FELIX DA SILVA NETO,JOAO VICTOR NOGUEIRA ALVES

📄 Licença

Projeto livre para estudo e aprendizado.
