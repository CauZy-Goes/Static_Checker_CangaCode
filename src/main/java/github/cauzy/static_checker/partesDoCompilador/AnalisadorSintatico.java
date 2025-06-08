package github.cauzy.static_checker.partesDoCompilador;

import github.cauzy.static_checker.entidadesDoCompilador.AtomoCangaCode;
import github.cauzy.static_checker.entidadesDoCompilador.Token;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSintatico {

    public static void main(String[] args) {
        List<AtomoCangaCode> listaCanga = preencherListaCanga();

        String caminhoDoArquivo = "src\\main\\java\\github\\cauzy\\static_checker\\arquivos\\input.251";

        Buffer buffer251 = new Buffer();

        AnalisadorLexico analisadorLexico = new AnalisadorLexico(buffer251, caminhoDoArquivo);

        String [] linhas = analisadorLexico.aplicarFiltros(listaCanga);

        for (String linha : linhas) {
            System.out.println(linha);
        }

        List<Token> listaDeTokens = analisadorLexico.capturarTokensValidos(linhas, listaCanga);

        String[] componentes = {
                "CAUÃ GOES FARIAS; caua.farias@ucsal.edu.br; 71993209370",
                "GUILHERME ANDRADE DE LACERDA; guilhermeandrade.lacerda@ucsal.edu.br; 71988602565",
                "KAILAN DE SOUZA DIAS; kailan.dias@ucsal.edu.br; 75988059380"
        };

        GeradorDeArquivoLEX geradorDeArquivoLEX = new GeradorDeArquivoLEX("EQ01", componentes, "input.251");
        geradorDeArquivoLEX.gerarArquivoLEX(listaDeTokens);
    }

    public static List<AtomoCangaCode> preencherListaCanga() {
        List<AtomoCangaCode> listaCanga = new ArrayList<>();

        // Palavras Reservadas (azul e verde)
        listaCanga.add(new AtomoCangaCode("integer", "PRS01"));
        listaCanga.add(new AtomoCangaCode("real", "PRS02"));
        listaCanga.add(new AtomoCangaCode("character", "PRS03"));
        listaCanga.add(new AtomoCangaCode("string", "PRS04"));
        listaCanga.add(new AtomoCangaCode("boolean", "PRS05"));
        listaCanga.add(new AtomoCangaCode("void", "PRS06"));
        listaCanga.add(new AtomoCangaCode("true", "PRS07"));
        listaCanga.add(new AtomoCangaCode("false", "PRS08"));
        listaCanga.add(new AtomoCangaCode("varType", "PRS09"));
        listaCanga.add(new AtomoCangaCode("funcType", "PRS10"));
        listaCanga.add(new AtomoCangaCode("paramType", "PRS11"));
        listaCanga.add(new AtomoCangaCode("declarations", "PRS12"));
        listaCanga.add(new AtomoCangaCode("endDeclarations", "PRS13"));
        listaCanga.add(new AtomoCangaCode("program", "PRS14"));
        listaCanga.add(new AtomoCangaCode("endProgram", "PRS15"));
        listaCanga.add(new AtomoCangaCode("functions", "PRS16"));
        listaCanga.add(new AtomoCangaCode("endFunctions", "PRS17"));
        listaCanga.add(new AtomoCangaCode("endFunction", "PRS18"));
        listaCanga.add(new AtomoCangaCode("return", "PRS19"));
        listaCanga.add(new AtomoCangaCode("if", "PRS20"));
        listaCanga.add(new AtomoCangaCode("else", "PRS21"));
        listaCanga.add(new AtomoCangaCode("endIf", "PRS22"));
        listaCanga.add(new AtomoCangaCode("while", "PRS23"));
        listaCanga.add(new AtomoCangaCode("endWhile", "PRS24"));
        listaCanga.add(new AtomoCangaCode("break", "PRS25"));
        listaCanga.add(new AtomoCangaCode("print", "PRS26"));

        // Símbolos Reservados (laranja e amarelo)
        listaCanga.add(new AtomoCangaCode(";", "SRS01"));
        listaCanga.add(new AtomoCangaCode(",", "SRS02"));
        listaCanga.add(new AtomoCangaCode(":", "SRS03"));
        listaCanga.add(new AtomoCangaCode(":=", "SRS04"));
        listaCanga.add(new AtomoCangaCode("?", "SRS05"));
        listaCanga.add(new AtomoCangaCode("(", "SRS06"));
        listaCanga.add(new AtomoCangaCode(")", "SRS07"));
        listaCanga.add(new AtomoCangaCode("[", "SRS08"));
        listaCanga.add(new AtomoCangaCode("]", "SRS09"));
        listaCanga.add(new AtomoCangaCode("{", "SRS10"));
        listaCanga.add(new AtomoCangaCode("}", "SRS11"));
        listaCanga.add(new AtomoCangaCode("+", "SRS12"));
        listaCanga.add(new AtomoCangaCode("-", "SRS13"));
        listaCanga.add(new AtomoCangaCode("*", "SRS14"));
        listaCanga.add(new AtomoCangaCode("/", "SRS15"));
        listaCanga.add(new AtomoCangaCode("%", "SRS16"));
        listaCanga.add(new AtomoCangaCode("==", "SRS17"));
        listaCanga.add(new AtomoCangaCode("!=", "SRS18"));
        listaCanga.add(new AtomoCangaCode("#", "SRS18")); // hash (alias for !=)
        listaCanga.add(new AtomoCangaCode("<", "SRS19"));
        listaCanga.add(new AtomoCangaCode("<=", "SRS20"));
        listaCanga.add(new AtomoCangaCode(">", "SRS21"));
        listaCanga.add(new AtomoCangaCode(">=", "SRS22"));

        return listaCanga;
    }
}
