package github.cauzy.static_checker.partesDoCompilador;

import github.cauzy.static_checker.entidadesDoCompilador.AtomoCangaCode;
import github.cauzy.static_checker.entidadesDoCompilador.Token;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class AnalisadorLexico {

    private final Buffer buffer;
    private final String caminhoArquivo251;

    public String[] aplicarFiltros(){

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

    public List<Token> capturarTokensValidos(String[] linhas, List<AtomoCangaCode> listaCanga) {

        // Mapa: lexema -> AtomoCangaCode
        Map<String, AtomoCangaCode> mapaLexema = new HashMap<>();
        for (AtomoCangaCode atomo : listaCanga) {
            mapaLexema.put(atomo.lexeme(), atomo);
        }

        // Mapa: lexema -> indiceTabelaSimbolo único
        Map<String, Integer> indiceSimbolo = new HashMap<>();
        int contadorIndice = 0;

        List<Token> tokens = new ArrayList<>();
        Pattern padraoToken = Pattern.compile("==|!=|<=|>=|:=|[a-zA-Z_][a-zA-Z0-9_]*|[(){}\\[\\];,:+\\-*/%<>=#?]");

        for (int i = 0; i < linhas.length; i++) {
            String linha = linhas[i];
            Matcher matcher = padraoToken.matcher(linha);

            while (matcher.find()) {
                String lexema = matcher.group();
                if (mapaLexema.containsKey(lexema)) {
                    // Pega ou cria o índice do símbolo
                    int indice;
                    if (indiceSimbolo.containsKey(lexema)) {
                        indice = indiceSimbolo.get(lexema);
                    } else {
                        indice = contadorIndice++;
                        indiceSimbolo.put(lexema, indice);
                    }

                    // Cria token e adiciona à lista
                    Token token = new Token(mapaLexema.get(lexema), indice, i + 1); // linha começa em 1
                    tokens.add(token);
                }
            }
        }

        return tokens;
    }

}
