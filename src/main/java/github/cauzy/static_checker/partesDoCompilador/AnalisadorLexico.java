package github.cauzy.static_checker.partesDoCompilador;

import github.cauzy.static_checker.entidadesDoCompilador.AtomoCangaCode;
import github.cauzy.static_checker.entidadesDoCompilador.Token;
import lombok.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class AnalisadorLexico {

    private final Buffer buffer;
    private final String caminhoArquivo251;

    private final Map<String, Integer> tabelaSimbolos = new HashMap<>();
    private int proximoIndiceSimbolo = 1;
    private String contexto = "";

    public String[] aplicarFiltros(List<AtomoCangaCode> listaCanga){

        String[] linhas = buffer.quebrarTextoEmLinhas(buffer.converterArquivoParaString(caminhoArquivo251));

        linhas = limparComentarios(linhas);

        linhas = eliminarAtomosInvalidos(linhas, listaCanga);

        return linhas;
    }

    public String[] limparComentarios(String[] linhas) {

        String[] resultado = new String[linhas.length];
        boolean emComentarioDeBloco = false;

        for (int i = 0; i < linhas.length; i++) {
            String linha = linhas[i];

            if (emComentarioDeBloco) {
                int fimComentario = linha.indexOf("*/");
                if (fimComentario != -1) {
                    linha = linha.substring(fimComentario + 2); // Remove até o fim do comentário
                    emComentarioDeBloco = false;
                } else {
                    resultado[i] = ""; // linha totalmente comentada
                    continue;
                }
            }

            // Agora, remover novos comentários (se ainda tiver na linha)
            while (true) {
                int inicioComentarioBloco = linha.indexOf("/*");
                int inicioComentarioLinha = linha.indexOf("//");

                if (inicioComentarioBloco != -1 && (inicioComentarioLinha == -1 || inicioComentarioBloco < inicioComentarioLinha)) {
                    int fimComentarioBloco = linha.indexOf("*/", inicioComentarioBloco + 2);
                    if (fimComentarioBloco != -1) {
                        linha = linha.substring(0, inicioComentarioBloco) + linha.substring(fimComentarioBloco + 2);
                    } else {
                        linha = linha.substring(0, inicioComentarioBloco);
                        emComentarioDeBloco = true;
                        break;
                    }
                } else if (inicioComentarioLinha != -1) {
                    linha = linha.substring(0, inicioComentarioLinha);
                    break;
                } else {
                    break;
                }
            }

            resultado[i] = linha.trim();
        }

        return resultado;
    }

    public String[] eliminarAtomosInvalidos(String[] linhas, List<AtomoCangaCode> listaCanga) {
        List<String> resultado = new ArrayList<>();

        for (String linha : linhas) {
            StringBuilder novaLinha = new StringBuilder();
            for (String lexeme : linha.split("\\s+")) {
                if (lexeme.isBlank()) continue;

                boolean valido = listaCanga.stream()
                        .anyMatch(a -> a.lexeme().equals(lexeme));

                if (valido || ehLexemaIdentificadorValido(lexeme)) {
                    novaLinha.append(lexeme).append(" ");
                }
            }
            resultado.add(novaLinha.toString().trim());
        }

        return resultado.toArray(new String[0]);
    }

    private boolean ehLexemaIdentificadorValido(String lexeme) {
        return lexeme.matches("[a-zA-Z_][a-zA-Z0-9_]*") ||       // identificadores
                lexeme.matches("[0-9]+") ||                       // intConst
                lexeme.matches("[0-9]+\\.[0-9]+([eE][0-9]+)?") || // realConst
                lexeme.matches("\".*\"") ||                       // stringConst
                lexeme.matches("'.'");                            // charConst
    }


    public List<Token> capturarTokensValidos(String[] linhas, List<AtomoCangaCode> listaCanga) {
        List<Token> tokens = new ArrayList<>();

         final Map<String, Integer> tabelaSimbolos = new HashMap<>();
         int proximoIndiceSimbolo = 1;
         String contexto = "";

        for (int i = 0; i < linhas.length; i++) {
            String[] palavras = linhas[i].split("\\s+");
            int linhaAtual = i + 1;

            for (String lexeme : palavras) {
                if (lexeme.isBlank()) continue;

                // Atualizar contexto
                if (lexeme.equals("program")) {
                    contexto = "program";
                } else if (lexeme.equals("functions")) {
                    contexto = "functions";
                }

                // Palavras e símbolos reservados
                Optional<AtomoCangaCode> reservado = listaCanga.stream()
                        .filter(a -> a.lexeme().equals(lexeme))
                        .findFirst();

                if (reservado.isPresent()) {
                    tokens.add(new Token(reservado.get(), getIndiceTabelaSimbolo(lexeme), linhaAtual));
                } else {
                    // Identificadores com escopo
                    AtomoCangaCode identificador = classificarIdentificador(lexeme);
                    if (identificador != null) {
                        tokens.add(new Token(identificador, getIndiceTabelaSimbolo(lexeme), linhaAtual));
                    }
                }
            }
        }

        return tokens;
    }

    private AtomoCangaCode classificarIdentificador(String lexeme) {
        if (lexeme.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return switch (contexto) {
                case "program" -> new AtomoCangaCode(lexeme, "IDN01"); // programName
                case "functions" -> new AtomoCangaCode(lexeme, "IDN03"); // functionName
                default -> new AtomoCangaCode(lexeme, "IDN02"); // variable
            };
        } else if (lexeme.matches("[0-9]+")) {
            return new AtomoCangaCode(lexeme, "IDN04"); // intConst
        } else if (lexeme.matches("[0-9]+\\.[0-9]+([eE][0-9]+)?")) {
            return new AtomoCangaCode(lexeme, "IDN05"); // realConst
        } else if (lexeme.matches("\".*\"")) {
            return new AtomoCangaCode(lexeme, "IDN06"); // stringConst
        } else if (lexeme.matches("'.'")) {
            return new AtomoCangaCode(lexeme, "IDN07"); // charConst
        }
        return null;
    }

    private int getIndiceTabelaSimbolo(String lexeme) {
        return tabelaSimbolos.computeIfAbsent(lexeme, k -> proximoIndiceSimbolo++);
    }

}
