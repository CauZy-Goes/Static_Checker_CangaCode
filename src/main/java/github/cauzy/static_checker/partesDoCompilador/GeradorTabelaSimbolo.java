package github.cauzy.static_checker.partesDoCompilador;

import github.cauzy.static_checker.entidadesDoCompilador.ItemTabelaSimbolo;

import java.util.*;

public class GeradorTabelaSimbolo {

    private final Map<String, ItemTabelaSimbolo> tabela = new LinkedHashMap<>();
    private final Map<String, String> tiposDeclarados = new HashMap<>();
    private int contador = 1;

    public Map<String, ItemTabelaSimbolo> processarLinhas(String[] linhas) {
        for (int i = 0; i < linhas.length; i++) {
            String linha = linhas[i].trim();
            int numeroLinha = i + 1;

            if (linha.startsWith("integer") || linha.startsWith("string") ||
                    linha.startsWith("char") || linha.startsWith("real") ||
                    linha.startsWith("boolean")) {

                String[] partes = linha.split("\\s+");
                if (partes.length == 2) {
                    String tipo = tipoParaSigla(partes[0]);
                    String id = partes[1];
                    tiposDeclarados.put(id, tipo);
                    adicionarOuAtualizar(id, tipo, numeroLinha);
                }

            } else {
                // Tentar encontrar identificadores declarados usados na linha
                for (String id : tiposDeclarados.keySet()) {
                    if (linha.matches(".*\\b" + id + "\\b.*")) {
                        adicionarOuAtualizar(id, tiposDeclarados.get(id), numeroLinha);
                    }
                }
            }
        }

        return tabela;
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
