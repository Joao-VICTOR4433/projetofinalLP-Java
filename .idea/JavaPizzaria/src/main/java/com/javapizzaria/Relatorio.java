package com.javapizzaria;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Relatorio {
    public static File salvarRelatorioDiario(List<Pedido> pedidosDia, String baseDir) throws IOException {
        String data = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        File dir = new File(baseDir, "reports");
        if (!dir.exists()) dir.mkdirs();
        File arquivo = new File(dir, "relatorio-" + data + ".txt");

        double totalDia = 0.0;
        try (FileWriter w = new FileWriter(arquivo, false)) {
            w.write("Relatório JavaPizzaria - " + data + System.lineSeparator());
            w.write("======================================" + System.lineSeparator());

            for (Pedido p : pedidosDia) {
                w.write("Pedido #" + p.getId() + " | Cliente: " + p.getCliente().getNome() + " | " + p.getCriadoEm() + System.lineSeparator());
                for (Produto item : p.getItens()) {
                    w.write("  - " + item.getNome() + " (R$ " + String.format("%.2f", item.getPreco()) + ")" + System.lineSeparator());
                }
                w.write("  Total Pedido: R$ " + String.format("%.2f", p.getTotal()) + System.lineSeparator());
                w.write(System.lineSeparator());
                totalDia += p.getTotal();
            }
            w.write("--------------------------------------" + System.lineSeparator());
            w.write("TOTAL DO DIA: R$ " + String.format("%.2f", totalDia) + System.lineSeparator());
        }
        return arquivo;
    }
}
