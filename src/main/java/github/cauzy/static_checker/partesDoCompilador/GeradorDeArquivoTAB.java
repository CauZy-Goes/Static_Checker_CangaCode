package github.cauzy.static_checker.partesDoCompilador;

import github.cauzy.static_checker.entidadesDoCompilador.AtomoCangaCode;
import github.cauzy.static_checker.entidadesDoCompilador.ItemTabelaSimbolo;
import github.cauzy.static_checker.entidadesDoCompilador.Token;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Data
public class GeradorDeArquivoTAB {

    private final String codigoEquipe;
    private final String [] componentes;
    private final String nomeTextoFonteAnalisado;

    public void gerarArquivoTAB(Map<String, ItemTabelaSimbolo> itens) {
        String caminho = "src/main/java/github/cauzy/static_checker/arquivos/TabelaDeSimbolos.TAB";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {

            // Cabeçalho
            writer.write("Código da Equipe: " + codigoEquipe + "\n");
            writer.write("Componentes:\n");
            for (String componente : componentes) {
                writer.write("    " + componente + ";\n");
            }

            writer.write("\nRELATÓRIO DA ANÁLISE LÉXICA. Texto fonte analisado: " + nomeTextoFonteAnalisado + "\n\n");

            for (ItemTabelaSimbolo item : itens.values()) {
                writer.write(String.format("Entrada: %d, Codigo: %s, Lexeme: %s,\n",
                        item.getNumeroEntrada(), item.getCodigo(), item.getLexema()));
                writer.write(String.format("QtdCharAntesTrunc: %d, QtdCharDepoisTrunc: %d,\n",
                        item.getTamanhoAntes(), item.getTamanhoDepois()));
                writer.write(String.format("TipoSimb: %s, Linhas: %s.\n",
                        item.getTipo(), item.getLinhas()));
                writer.write("-------------------------------------------------------------\n");
            }

            System.out.println("Arquivo .tab gerado com sucesso!");


        } catch (IOException e) {
            System.err.println("Erro ao gerar o arquivo LEX: " + e.getMessage());
        }
    }
}
