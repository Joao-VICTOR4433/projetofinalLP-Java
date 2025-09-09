// ====== State (browser) ======
let carrinhoLocal = JSON.parse(localStorage.getItem("carrinho")) || [];

function saveLocal() {
  localStorage.setItem("carrinho", JSON.stringify(carrinhoLocal));
}

// ====== UI: Lista de produtos ======
async function carregarProdutos() {
  const resp = await fetch('/api/produtos');
  const produtos = await resp.json();
  const lista = document.getElementById('lista-produtos');
  if (!lista) return;
  lista.innerHTML = '';
  produtos.forEach(p => {
    const div = document.createElement('div');
    div.className = 'product';
    div.innerHTML = `
      <img src="${p.imagem || '/images/pizza.png'}" alt="${p.nome}">
      <h3>${p.nome}</h3>
      <p class="price">R$ ${p.preco.toFixed(2)}</p>
      <button onclick="addCarrinhoAPI(${p.id})">Adicionar</button>
    `;
    lista.appendChild(div);
  });
}

async function addCarrinhoAPI(idProduto){
  await fetch('/api/carrinho/add/' + idProduto, { method: 'POST' });
  alert('✅ Produto adicionado ao carrinho!');
  // guarda também localmente para o resumo
  const resp = await fetch('/api/carrinho');
  const pedido = await resp.json();
  carrinhoLocal = pedido.itens || [];
  // Map to lightweight local
  carrinhoLocal = pedido.itens.map(i => ({ produto: i.nome, preco: i.preco, data: new Date().toLocaleString() }));
  saveLocal();
}

// ====== Admin ======
async function mostrarProdutosAdmin(){
  await carregarProdutos();
  const resp = await fetch('/api/produtos');
  const produtos = await resp.json();
  const lista = document.getElementById('lista-admin');
  if (!lista) return;
  lista.innerHTML = '';
  produtos.forEach(p => {
    const div = document.createElement('div');
    div.className = 'item';
    div.innerHTML = `<img src="${p.imagem}" alt=""><div><b>${p.nome}</b><br>R$ ${p.preco.toFixed(2)} - ${p.categoria}</div>`;
    lista.appendChild(div);
  });
}

async function cadastrarComida(ev){
  ev.preventDefault();
  const body = new URLSearchParams();
  body.append('nome', document.getElementById('c_nome').value);
  body.append('preco', document.getElementById('c_preco').value);
  body.append('estoque', document.getElementById('c_estoque').value);
  body.append('imagem', document.getElementById('c_imagem').value);
  body.append('ingredientes', document.getElementById('c_ingredientes').value);
  await fetch('/api/produtos/comida', { method:'POST', body });
  alert('🍕 Comida cadastrada!');
  mostrarProdutosAdmin();
}

async function cadastrarBebida(ev){
  ev.preventDefault();
  const body = new URLSearchParams();
  body.append('nome', document.getElementById('b_nome').value);
  body.append('preco', document.getElementById('b_preco').value);
  body.append('estoque', document.getElementById('b_estoque').value);
  body.append('imagem', document.getElementById('b_imagem').value);
  body.append('volumeMl', document.getElementById('b_volume').value);
  body.append('teorAlcool', document.getElementById('b_teor').value || '0');
  await fetch('/api/produtos/bebida', { method:'POST', body });
  alert('🥤 Bebida cadastrada!');
  mostrarProdutosAdmin();
}

async function cadastrarGerente(ev){
  ev.preventDefault();
  const body = new URLSearchParams();
  body.append('nome', document.getElementById('g_nome').value);
  await fetch('/api/usuarios/gerente', { method:'POST', body });
  alert('👤 Gerente cadastrado!');
}

async function cadastrarFuncionario(ev){
  ev.preventDefault();
  const body = new URLSearchParams();
  body.append('nome', document.getElementById('f_nome').value);
  await fetch('/api/usuarios/funcionario', { method:'POST', body });
  alert('👤 Funcionário cadastrado!');
}

// ====== Checkout PIX ======
function mostrarResumo() {
  let lista = document.getElementById("resumo-pedido");
  let total = 0;
  if (!lista) return;

  lista.innerHTML = "";
  carrinhoLocal.forEach(item => {
      let li = document.createElement("li");
      li.textContent = `${item.produto} - R$ ${item.preco}`;
      lista.appendChild(li);
      total += item.preco;
  });
  document.getElementById("total").textContent = `Total: R$ ${total.toFixed(2)}`;
}

function copiarPix() {
  let payload = document.getElementById("pix-payload").textContent;
  navigator.clipboard.writeText(payload).then(() => alert("📋 Código PIX copiado!"));
}

// ====== Histórico via API ======
async function mostrarHistoricoAPI(){
  const historico = document.getElementById('historico');
  const resp = await fetch('/api/historico');
  const pedidos = await resp.json();
  if (!pedidos.length){ historico.innerHTML = '<li>Nenhum pedido ainda.</li>'; return; }
  historico.innerHTML = '';
  pedidos.forEach(p => {
    const li = document.createElement('li');
    const itens = (p.itens || []).map(i => i.nome + ' (R$ ' + i.preco.toFixed(2) + ')').join(', ');
    li.textContent = `#${p.id} - ${p.cliente.nome} - ${itens} => Total R$ ${p.total.toFixed(2)}`;
    historico.appendChild(li);
  });
}

// ====== Home init ======
window.addEventListener('DOMContentLoaded', carregarProdutos);
