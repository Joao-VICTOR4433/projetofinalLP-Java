-- Schema SQL para Backstreet (MySQL)
CREATE TABLE usuario (
  id INT AUTO_INCREMENT PRIMARY KEY,
  login VARCHAR(100) NOT NULL,
  senha VARCHAR(100) NOT NULL,
  nome VARCHAR(150) NOT NULL,
  tipo VARCHAR(20) NOT NULL
);

CREATE TABLE cliente (
  id INT PRIMARY KEY,
  contato VARCHAR(50),
  FOREIGN KEY (id) REFERENCES usuario(id)
);

CREATE TABLE produto (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  preco DECIMAL(10,2) NOT NULL,
  categoria VARCHAR(50),
  estoque INT DEFAULT 0
);

CREATE TABLE comanda (
  id INT AUTO_INCREMENT PRIMARY KEY,
  numero INT NOT NULL,
  cliente_id INT,
  status VARCHAR(20),
  dataAbertura DATETIME,
  FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE pedido (
  id INT AUTO_INCREMENT PRIMARY KEY,
  comanda_id INT,
  produto_id INT,
  quantidade INT,
  subtotal DECIMAL(10,2),
  FOREIGN KEY (comanda_id) REFERENCES comanda(id),
  FOREIGN KEY (produto_id) REFERENCES produto(id)
);

CREATE TABLE pagamento (
  id INT AUTO_INCREMENT PRIMARY KEY,
  comanda_id INT,
  valor DECIMAL(10,2),
  metodo VARCHAR(50),
  data DATETIME,
  status VARCHAR(20),
  FOREIGN KEY (comanda_id) REFERENCES comanda(id)
);
