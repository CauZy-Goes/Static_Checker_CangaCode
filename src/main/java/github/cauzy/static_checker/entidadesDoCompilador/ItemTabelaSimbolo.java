package github.cauzy.static_checker.entidadesDoCompilador;

import lombok.Data;

import java.util.List;

@Data
public class ItemTabelaSimbolo {
    AtomoCangaCode atomoCangaCode;
    Integer entrada;
    Integer QtdCharAntesTrunc;
    Integer QtdCharDepoisTrunc;
    String tipoSimbolo;
    List<Integer> linhas;
}
