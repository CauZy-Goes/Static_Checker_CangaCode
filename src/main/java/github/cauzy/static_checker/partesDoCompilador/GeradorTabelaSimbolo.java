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
                String lexema = token.atomoCangaCode().lexeme();
                int linha = token.linha();

                String lexemaTruncado = lexema.length() > 35 ? lexema.substring(0, 35) : lexema;
                int tamAntes = lexema.length();
                int tamDepois = lexemaTruncado.length();

                String tipoSimbolo = determinarTipoSimbolo(codigoAtomo);

                String chave = lexema + "-" + codigoAtomo;

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

    public void preencherTiposDasVariaveis(List<Token> tokens) {
        String tipoAtual = null;

        for (Token token : tokens) {
            String codigo = token.atomoCangaCode().codigo();
            String lexema = token.atomoCangaCode().lexeme();

            // Verificar se o token é um tipo primitivo (ex: integer, string, etc)
            switch (lexema) {
                case "integer" -> tipoAtual = "IN";
                case "string" -> tipoAtual = "ST";
                case "char" -> tipoAtual = "CH";
                case "real" -> tipoAtual = "FP";
                case "boolean" -> tipoAtual = "BL";
                default -> {
                    // Se o token for identificador de variável, e temos um tipo corrente
                    if (tipoAtual != null && codigo.equals("IDN02")) {
                        for (ItemTabelaSimbolo item : tabela.values()) {
                            if (item.getLexema().equals(lexema) && item.getCodigo().equals(codigo)) {
                                item.setTipo(tipoAtual);
                            }
                        }
                    }

                    // Se chegarmos num token que não é tipo nem IDN, resetar tipoAtual
                    if (!codigo.startsWith("IDN")) {
                        tipoAtual = null;
                    }
                }
            }
        }
    }


    private String determinarTipoSimbolo(String codigoAtomo) {
        return switch (codigoAtomo) {
            case "IDN01" -> "VD";  // Nome de programa (assumindo tipo Void/VD por enquanto)
            case "IDN02" -> "-";   // Variáveis: tipo pode ser preenchido depois por outra fase do compilador
            case "IDN03" -> "VD";  // Nome de função (Void por padrão aqui)
            case "IDN04" -> "IN";  // Inteiro
            case "IDN05" -> "FP";  // Floating Point
            case "IDN06" -> "ST";  // String
            case "IDN07" -> "CH";  // Char
            default -> "-";        // Qualquer outro (por segurança)
        };
    }

    private void adicionarOuAtualizar(String id, String tipo, int linha) {
        String lexema = id.length() > 35 ? id.substring(0, 35) : id;
        int tamAntes = id.length();
        int tamDepois = lexema.length();

        if (!tabela.containsKey(id)) {
            List<Integer> linhas = new ArrayList<>();
            linhas.add(linha);
            tabela.put(id, new ItemTabelaSimbolo(contador++, "IDE", lexema, tamAntes, tamDepois, tipo, linhas));
        } else {
            List<Integer> linhasExistentes = tabela.get(id).getLinhas();
            if (linhasExistentes.size() < 5 && !linhasExistentes.contains(linha)) {
                linhasExistentes.add(linha);
            }
        }
    }

    private String tipoParaSigla(String tipo) {
        return switch (tipo) {
            case "integer" -> "IN";
            case "string" -> "ST";
            case "char" -> "CH";
            case "real" -> "RE";
            case "boolean" -> "BO";
            default -> "??";
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
