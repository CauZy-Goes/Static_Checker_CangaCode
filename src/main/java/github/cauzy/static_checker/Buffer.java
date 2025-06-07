package github.cauzy.static_checker;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Data
public class Buffer {

    public String converterArquivoParaString(String caminhoDoArquivo251) {
        try {
            return Files.readString(Path.of(caminhoDoArquivo251));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return "";
        }
    }

    public String[] quebrarTextoEmLinhas(String texto){
        return texto.split("\\r?\\n");
    }
}
