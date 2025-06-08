package github.cauzy.static_checker.partesDoCompilador;

import github.cauzy.static_checker.entidadesDoCompilador.AtomoCangaCode;
import github.cauzy.static_checker.entidadesDoCompilador.Token;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Data
public class GeradorDeArquivoLEX {

    private final String codigoEquipe;
    private final String [] componentes;
    private final String nomeTextoFonteAnalisado;

    public void gerarArquivoLEX(List<Token> tokens) {
        String caminho = "src/main/java/github/cauzy/static_checker/arquivos/RelatorioLexico.LEX";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {

            // Cabeçalho
            writer.write("Código da Equipe: " + codigoEquipe + "\n");
            writer.write("Componentes:\n");
            for (String componente : componentes) {
                writer.write("    " + componente + ";\n");
            }

            writer.write("\nRELATÓRIO DA ANÁLISE LÉXICA. Texto fonte analisado: " + nomeTextoFonteAnalisado + "\n\n");

            // Tokens
            for (Token token : tokens) {
                AtomoCangaCode atomo = token.atomoCangaCode();
                String lexeme = atomo.lexeme();
                String codigo = atomo.codigo();
                String indice = token.indiceTabelaSimbolo() == null ? "-" : token.indiceTabelaSimbolo().toString();
                String linha = token.linha().toString();

                writer.write(String.format("Lexeme: %-17s Código: %-7s ÍndiceTabSimb: %-3s Linha: %s.\n",
                        lexeme, codigo, indice, linha));
            }

        } catch (IOException e) {
            System.err.println("Erro ao gerar o arquivo LEX: " + e.getMessage());
        }
    }
}
