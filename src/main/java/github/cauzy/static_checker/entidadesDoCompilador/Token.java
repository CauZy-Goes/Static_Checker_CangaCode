package github.cauzy.static_checker.entidadesDoCompilador;

public record Token(
        AtomoCangaCode atomoCangaCode,
        String indiceTabelaSimbolo,
        Integer linha) {
}