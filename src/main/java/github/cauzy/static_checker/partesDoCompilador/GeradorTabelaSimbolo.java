package github.cauzy.static_checker.partesDoCompilador;

import github.cauzy.static_checker.entidadesDoCompilador.AtomoCangaCode;
import github.cauzy.static_checker.entidadesDoCompilador.ItemTabelaSimbolo;
import github.cauzy.static_checker.entidadesDoCompilador.Token;

import java.util.*;

public class GeradorTabelaSimbolo {

    private final Map<String, ItemTabelaSimbolo> tabela = new LinkedHashMap<>();
    private final Map<String, String> tiposDeclarados = new HashMap<>();
    private int contador = 1;

    public Map<String, ItemTabelaSimbolo> processarTokens(List<Token> tokens, List<AtomoCangaCode> listaCanga) {
        for (Token token : tokens) {
            String codigoAtomo = token.atomoCangaCode().codigo();

            // Só processa identificadores
            if (codigoAtomo.startsWith("IDN")) {
                String lexemaOriginal = token.atomoCangaCode().lexeme();
                int linha = token.linha();

                // Truncagem só para armazenamento (depois da classificação)
                String lexemaTruncado = aplicarTruncagem(lexemaOriginal, codigoAtomo);

                int tamAntes = lexemaOriginal.length();
                int tamDepois = lexemaTruncado.length();

                String tipoSimbolo = determinarTipoSimbolo(codigoAtomo);

                // Chave baseada no lexema original + código para garantir unicidade real
                String chave = lexemaOriginal + "-" + codigoAtomo;

                if (!tabela.containsKey(chave)) {
                    List<Integer> linhas = new ArrayList<>();
                    linhas.add(linha);
                    tabela.put(chave, new ItemTabelaSimbolo(
                            contador++,
                            codigoAtomo,
                            lexemaTruncado,
                            tamAntes,
                            tamDepois,
                            tipoSimbolo,
                            linhas
                    ));
                } else {
                    List<Integer> linhasExistentes = tabela.get(chave).getLinhas();
                    if (linhasExistentes.size() < 5 && !linhasExistentes.contains(linha)) {
                        linhasExistentes.add(linha);
                    }
                }
            }
        }

        preencherTiposDasVariaveis(tokens);
        return tabela;
    }

    private String aplicarTruncagem(String lexema, String codigoAtomo) {
        int tamAntes = lexema.length();

        // Se for String (IDN06) ou Char (IDN07), incluir as aspas no limite dos 35 caracteres
        if (codigoAtomo.equals("IDN06") || codigoAtomo.equals("IDN07")) {
            if (tamAntes > 35) {
                // Mantém a primeira aspas, corta o conteúdo e mantém a última aspas dentro dos 35
                return lexema.substring(0, 34) + "\"";
            } else {
                return lexema;
            }
        } else {
            // Demais tipos: só cortar puro
            return tamAntes > 35 ? lexema.substring(0, 35) : lexema;
        }
    }

    public void preencherTiposDasVariaveis(List<Token> tokens) {
        String tipoAtual = null;

        for (Token token : tokens) {
            String codigo = token.atomoCangaCode().codigo();
            String lexema = token.atomoCangaCode().lexeme();

            // Verificar se é declaração de tipo
            switch (lexema) {
                case "integer" -> tipoAtual = "IN";
                case "string" -> tipoAtual = "ST";
                case "char" -> tipoAtual = "CH";
                case "real" -> tipoAtual = "FP";
                case "boolean" -> tipoAtual = "BL";
                default -> {
                    if (tipoAtual != null && codigo.equals("IDN02")) {
                        for (ItemTabelaSimbolo item : tabela.values()) {
                            // Compare pelo lexema original (chave da tabela)
                            if (item.getLexema().equals(aplicarTruncagem(lexema, codigo)) && item.getCodigo().equals(codigo)) {
                                item.setTipo(tipoAtual);
                            }
                        }
                    }

                    if (!codigo.startsWith("IDN")) {
                        tipoAtual = null;
                    }
                }
            }
        }
    }

    private String determinarTipoSimbolo(String codigoAtomo) {
        return switch (codigoAtomo) {
            case "IDN01" -> "VD";  // Nome de programa
            case "IDN02" -> "-";   // Variáveis (preenchido depois)
            case "IDN03" -> "VD";  // Nome de função
            case "IDN04" -> "IN";  // Integer
            case "IDN05" -> "FP";  // Floating Point
            case "IDN06" -> "ST";  // String
            case "IDN07" -> "CH";  // Char
            default -> "-";        // Qualquer outro
        };
    }

    public void imprimir() {
        for (ItemTabelaSimbolo item : tabela.values()) {
            System.out.println(item);
        }
    }

    public Collection<ItemTabelaSimbolo> getTabela() {
        return tabela.values();
    }
}
