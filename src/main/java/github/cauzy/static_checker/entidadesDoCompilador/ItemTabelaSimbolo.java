package github.cauzy.static_checker.entidadesDoCompilador;

import lombok.Data;

import java.util.List;

@Data
public class ItemTabelaSimbolo {
    int numeroEntrada;
    String codigo;
    String lexema;
    int tamanhoAntes;
    int tamanhoDepois;
    String tipo;
    List<Integer> linhas;

    public ItemTabelaSimbolo(int numeroEntrada, String codigo, String lexema, int tamanhoAntes,
                             int tamanhoDepois, String tipo, List<Integer> linhas) {
        this.numeroEntrada = numeroEntrada;
        this.codigo = codigo;
        this.lexema = lexema;
        this.tamanhoAntes = tamanhoAntes;
        this.tamanhoDepois = tamanhoDepois;
        this.tipo = tipo;
        this.linhas = linhas;
    }

    @Override
    public String toString() {
        return String.format("[%02d] %s | %s | %d/%d | %s | Linhas: %s",
                numeroEntrada, codigo, lexema, tamanhoAntes, tamanhoDepois, tipo, linhas);
    }
}
