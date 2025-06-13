package github.cauzy.static_checker.partesDoCompilador;

import github.cauzy.static_checker.entidadesDoCompilador.AtomoCangaCode;
import github.cauzy.static_checker.entidadesDoCompilador.Token;
import lombok.Data;

import java.util.*;

@Data
public class AnalisadorLexico {

    private final Buffer buffer;
    private final String caminhoArquivo251;

    private final Map<String, Integer> tabelaSimbolos = new HashMap<>();
    private int proximoIndiceSimbolo = 1;
    private String contexto = "";
    private boolean contextoConsumido = false;

    public String[] aplicarFiltros(List<AtomoCangaCode> listaCanga) {
        String[] linhas = buffer.quebrarTextoEmLinhas(buffer.converterArquivoParaString(caminhoArquivo251));
        linhas = limparComentarios(linhas);
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
                    linha = linha.substring(fimComentario + 2);
                    emComentarioDeBloco = false;
                } else {
                    resultado[i] = "";
                    continue;
                }
            }

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

    public List<Token> capturarTokensValidos(String[] linhas, List<AtomoCangaCode> listaCanga) {
        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < linhas.length; i++) {
            String linha = linhas[i];
            int linhaAtual = i + 1;

            List<String> lexemas = separarLexemas(linha);
            String anterior = "";

            for (String lexeme : lexemas) {
                if (lexeme.isBlank()) continue;

                atualizarContexto(anterior);

                Optional<AtomoCangaCode> reservado = listaCanga.stream()
                        .filter(a -> a.lexeme().equals(lexeme))
                        .findFirst();

                AtomoCangaCode atomo;

                if (reservado.isPresent()) {
                    atomo = reservado.get();
                } else {
                    atomo = classificarIdentificador(lexeme);
                    if (atomo == null || atomo.codigo().equals("AIN02")) {
                        anterior = lexeme;
                        continue;
                    }
                }

                // Verificar se é um identificador válido
                String codigo = atomo.codigo();
                String indice;
                if (codigo.startsWith("IDN")) {
                    indice = String.valueOf(getIndiceTabelaSimbolo(lexeme));
                } else {
                    indice = "-";
                }

                tokens.add(new Token(atomo, indice, linhaAtual));
                anterior = lexeme;
            }

            if (contextoConsumido) {
                contexto = "";
                contextoConsumido = false;
            }
        }
        return tokens;
    }


    private void atualizarContexto(String anterior) {
        switch (anterior) {
            case "program" -> {
                contexto = "programName";
                contextoConsumido = false;
            }
            case "funcType" -> {
                contexto = "functionName";
                contextoConsumido = false;
            }
            case "integer", "real", "string", "character", "boolean" -> {
                contexto = "variable";
                contextoConsumido = false;
            }
            case "endDeclarations", "endFunctions", "endProgram", "start" -> {
                contexto = "";
                contextoConsumido = false;
            }
        }
    }

    private List<String> separarLexemas(String linha) {
        List<String> lexemas = new ArrayList<>();
        StringBuilder atual = new StringBuilder();

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (Character.isWhitespace(c)) {
                if (!atual.isEmpty()) {
                    lexemas.add(atual.toString());
                    atual.setLength(0);
                }
                continue;
            }

            if (c == '"' || c == '\'') {
                char delimitador = c;
                atual.append(c);
                i++;
                while (i < linha.length()) {
                    char nc = linha.charAt(i);
                    atual.append(nc);
                    if (nc == delimitador) break;
                    i++;
                }
                lexemas.add(atual.toString());
                atual.setLength(0);
                continue;
            }

            if (":=<>!#".indexOf(c) != -1 && i + 1 < linha.length()) {
                char next = linha.charAt(i + 1);
                String possivelSimbolo = "" + c + next;
                if (possivelSimbolo.matches(":=|==|!=|<=|>=|#")) {
                    if (!atual.isEmpty()) {
                        lexemas.add(atual.toString());
                        atual.setLength(0);
                    }
                    lexemas.add(possivelSimbolo);
                    i++;
                    continue;
                }
            }

            if (";:(),[]{}+-*/%".indexOf(c) != -1) {
                if (!atual.isEmpty()) {
                    lexemas.add(atual.toString());
                    atual.setLength(0);
                }
                lexemas.add(Character.toString(c));
                continue;
            }

            atual.append(c);
        }

        if (!atual.isEmpty()) {
            lexemas.add(atual.toString());
        }

        return lexemas;
    }

    private AtomoCangaCode classificarIdentificador(String lexeme) {
        if (lexeme.matches("[0-9]+")) return new AtomoCangaCode(lexeme, "IDN04"); // intConst
        if (lexeme.matches("[0-9]+\\.[0-9]+([eE][+-]?[0-9]+)?")) return new AtomoCangaCode(lexeme, "IDN05"); // realConst
        if (lexeme.matches("\".*\"")) return new AtomoCangaCode(lexeme, "IDN06"); // stringConst
        if (lexeme.matches("'[a-z]'")) return new AtomoCangaCode(lexeme, "IDN07"); // charConst

        switch (contexto) {
            case "programName":
                if (!contextoConsumido && lexeme.matches("[a-zA-Z][a-zA-Z0-9]*")) {
                    contextoConsumido = true;
                    return new AtomoCangaCode(lexeme, "IDN01");
                }
                break;
            case "functionName":
                if (!contextoConsumido && lexeme.matches("[a-zA-Z][a-zA-Z0-9]*")) {
                    contextoConsumido = true;
                    return new AtomoCangaCode(lexeme, "IDN03");
                }
                break;
            case "variable":
                if (!contextoConsumido && lexeme.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                    contextoConsumido = true;
                    return new AtomoCangaCode(lexeme, "IDN02");
                }
                break;
        }

        return new AtomoCangaCode(lexeme, "AIN02");
    }

    private int getIndiceTabelaSimbolo(String lexeme) {
        if (!tabelaSimbolos.containsKey(lexeme)) {
            tabelaSimbolos.put(lexeme, proximoIndiceSimbolo++);
        }
        return tabelaSimbolos.get(lexeme);
    }

}
