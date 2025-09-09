// Função para preencher automaticamente o formulário ao clicar em um produto
const produtos = document.querySelectorAll('.produto');
const inputProduto = document.querySelector('input[name="produto"]');
const inputValor = document.querySelector('input[name="valor"]');
const selectMetodo = document.querySelector('select[name="metodo"]');
const formPagamento = document.querySelector('form[action="/pay"]');

produtos.forEach(produto => {
   produto.addEventListener('click', () => {
       const nome = produto.querySelector('h3').textContent;
       const valor = produto.querySelector('p').textContent.replace('R$', '').replace(',', '.').trim();

       inputProduto.value = nome;
       inputValor.value = valor;
       inputProduto.focus(); // foco no campo produto para revisão
   });
});

formPagamento.addEventListener('submit', (event) => {
   event.preventDefault(); // impede o envio real por enquanto

   const produto = inputProduto.value.trim();
   const valor = parseFloat(inputValor.value.replace(',', '.'));
   const metodo = selectMetodo.value;

   // Validações
   if (!produto) {
       alert('Por favor, digite o nome do produto.');
       inputProduto.focus();
       return;
   }

   if (!valor || isNaN(valor) || valor <= 0) {
       alert('Por favor, digite um valor válido.');
       inputValor.focus();
       return;
   }

   if (!metodo) {
       alert('Selecione um método de pagamento.');
       selectMetodo.focus();
       return;
   }

   alert('✅ Pagamento de R$' + valor.toFixed(2) + ' pelo produto "' + produto + '" via ' + metodo + ' realizado com sucesso!');
   
   // Se tudo estiver válido, enviar o formulário
   formPagamento.submit();
});