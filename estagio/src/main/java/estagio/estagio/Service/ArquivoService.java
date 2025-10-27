package estagio.estagio.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ArquivoService {

    public void deletarArquivo(String urlImagem) {
        try {
            String nomeArquivo = urlImagem.substring(urlImagem.lastIndexOf("/") + 1);
            nomeArquivo = URLDecoder.decode(nomeArquivo, StandardCharsets.UTF_8);

            Path caminhoArquivo = Paths.get(System.getProperty("user.dir"), "imagens", nomeArquivo);
            Files.deleteIfExists(caminhoArquivo);
        } catch (Exception e) {
            System.out.println("Erro ao deletar imagem: " + e.getMessage());
        }
    }

    public String salvarArquivo(MultipartFile arquivo) {
        Path pastaUploads = Paths.get(System.getProperty("user.dir"), "imagens");
        try {
            if (!Files.exists(pastaUploads)) {
                Files.createDirectories(pastaUploads);
            }
            String nomeArquivo = System.currentTimeMillis() + "_" + arquivo.getOriginalFilename();
            Path arquivoDestino = pastaUploads.resolve(nomeArquivo);
            arquivo.transferTo(arquivoDestino.toFile());

            return "http://localhost:8080/imagens/" + nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar o arquivo.", e);
        }
    }
}
