package com.backstreet.rest;

import com.backstreet.Comanda;
import com.backstreet.Pedido;
import com.backstreet.Produto;
import com.backstreet.Cliente;
import com.backstreet.Pagamento;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comandas")
public class ComandaController {

    @PostMapping("/abrir")
    public Comanda abrirComanda(@RequestBody Cliente cliente){
        // criar e persistir comanda
        return new Comanda(cliente);
    }

    @PostMapping("/{id}/pedidos")
    public Pedido adicionarPedido(@PathVariable int id, @RequestBody Pedido pedido){
        // localizar comanda por id e adicionar pedido
        return pedido;
    }

    @GetMapping("/{id}/total")
    public double calcularTotal(@PathVariable int id){
        // retornar total
        return 0.0;
    }

    @PostMapping("/{id}/fechar")
    public Pagamento fechar(@PathVariable int id, @RequestParam String metodo){
        // fechar comanda e processar pagamento
        return new Pagamento();
    }
}
