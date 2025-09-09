package com.backstreet;

public class SistemaPagamento {

    public boolean processarPagamento(Pagamento p) {
        if (p.validar()) {
            p.confirmar();
        } else {
            p.recusar();
        }
        registrarTransacao(p);
        return p.getStatus() == StatusPagamento.CONFIRMADO;
    }

    private void registrarTransacao(Pagamento p) {
        System.out.println("Pagamento ID: " + p.getId() + " | Status: " + p.getStatus());
    }
}
