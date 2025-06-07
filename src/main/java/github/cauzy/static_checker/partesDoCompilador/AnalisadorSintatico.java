package github.cauzy.static_checker.partesDoCompilador;

import github.cauzy.static_checker.entidadesDoCompilador.AtomosCangaCode;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSintatico {

    public static void main(String[] args) {
        List<AtomosCangaCode> listaCanga = preencherListaCanga();

        String caminhoDoArquivo = "src\\main\\java\\github\\cauzy\\static_checker\\arquivos\\input.251";

        Buffer buffer251 = new Buffer();

        String[] textoQuebrado = buffer251.quebrarTextoEmLinhas(buffer251.converterArquivoParaString(caminhoDoArquivo));

        for (int i = 0; i < textoQuebrado.length; i++) {
            System.out.println("Linhas = " + i + " "+ textoQuebrado[i]);
        }
    }

    public static List<AtomosCangaCode> preencherListaCanga() {
        List<AtomosCangaCode> listaCanga = new ArrayList<>();

        // Palavras Reservadas (azul e verde)
        listaCanga.add(new AtomosCangaCode("integer", "PRS01"));
        listaCanga.add(new AtomosCangaCode("real", "PRS02"));
        listaCanga.add(new AtomosCangaCode("character", "PRS03"));
        listaCanga.add(new AtomosCangaCode("string", "PRS04"));
        listaCanga.add(new AtomosCangaCode("boolean", "PRS05"));
        listaCanga.add(new AtomosCangaCode("void", "PRS06"));
        listaCanga.add(new AtomosCangaCode("true", "PRS07"));
        listaCanga.add(new AtomosCangaCode("false", "PRS08"));
        listaCanga.add(new AtomosCangaCode("varType", "PRS09"));
        listaCanga.add(new AtomosCangaCode("funcType", "PRS10"));
        listaCanga.add(new AtomosCangaCode("paramType", "PRS11"));
        listaCanga.add(new AtomosCangaCode("declarations", "PRS12"));
        listaCanga.add(new AtomosCangaCode("endDeclarations", "PRS13"));
        listaCanga.add(new AtomosCangaCode("program", "PRS14"));
        listaCanga.add(new AtomosCangaCode("endProgram", "PRS15"));
        listaCanga.add(new AtomosCangaCode("functions", "PRS16"));
        listaCanga.add(new AtomosCangaCode("endFunctions", "PRS17"));
        listaCanga.add(new AtomosCangaCode("endFunction", "PRS18"));
        listaCanga.add(new AtomosCangaCode("return", "PRS19"));
        listaCanga.add(new AtomosCangaCode("if", "PRS20"));
        listaCanga.add(new AtomosCangaCode("else", "PRS21"));
        listaCanga.add(new AtomosCangaCode("endIf", "PRS22"));
        listaCanga.add(new AtomosCangaCode("while", "PRS23"));
        listaCanga.add(new AtomosCangaCode("endWhile", "PRS24"));
        listaCanga.add(new AtomosCangaCode("break", "PRS25"));
        listaCanga.add(new AtomosCangaCode("print", "PRS26"));

        // Símbolos Reservados (laranja e amarelo)
        listaCanga.add(new AtomosCangaCode(";", "SRS01"));
        listaCanga.add(new AtomosCangaCode(",", "SRS02"));
        listaCanga.add(new AtomosCangaCode(":", "SRS03"));
        listaCanga.add(new AtomosCangaCode(":=", "SRS04"));
        listaCanga.add(new AtomosCangaCode("?", "SRS05"));
        listaCanga.add(new AtomosCangaCode("(", "SRS06"));
        listaCanga.add(new AtomosCangaCode(")", "SRS07"));
        listaCanga.add(new AtomosCangaCode("[", "SRS08"));
        listaCanga.add(new AtomosCangaCode("]", "SRS09"));
        listaCanga.add(new AtomosCangaCode("{", "SRS10"));
        listaCanga.add(new AtomosCangaCode("}", "SRS11"));
        listaCanga.add(new AtomosCangaCode("+", "SRS12"));
        listaCanga.add(new AtomosCangaCode("-", "SRS13"));
        listaCanga.add(new AtomosCangaCode("*", "SRS14"));
        listaCanga.add(new AtomosCangaCode("/", "SRS15"));
        listaCanga.add(new AtomosCangaCode("%", "SRS16"));
        listaCanga.add(new AtomosCangaCode("==", "SRS17"));
        listaCanga.add(new AtomosCangaCode("!=", "SRS18"));
        listaCanga.add(new AtomosCangaCode("#", "SRS18")); // hash (alias for !=)
        listaCanga.add(new AtomosCangaCode("<", "SRS19"));
        listaCanga.add(new AtomosCangaCode("<=", "SRS20"));
        listaCanga.add(new AtomosCangaCode(">", "SRS21"));
        listaCanga.add(new AtomosCangaCode(">=", "SRS22"));

        return listaCanga;
    }
}
