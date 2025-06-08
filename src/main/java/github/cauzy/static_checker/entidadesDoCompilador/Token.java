package github.cauzy.static_checker.entidadesDoCompilador;

public record Token(
        AtomoCangaCode atomoCangaCode,
        Integer indiceTabelaSimbolo,
        Integer linha) {
}