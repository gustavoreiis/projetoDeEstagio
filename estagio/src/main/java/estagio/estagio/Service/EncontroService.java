package estagio.estagio.Service;

import estagio.estagio.entity.Encontro;
import estagio.estagio.repository.EncontroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EncontroService {

    private final EncontroRepository encontroRepository;
    private final String uploadDir = "uploads"; // Pasta de destino

    public EncontroService(EncontroRepository encontroRepository) {
        this.encontroRepository = encontroRepository;
    }

    public Encontro criarEncontro(String titulo, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, String local, float preco, String descricao, MultipartFile capa) {
        Encontro encontro = new Encontro();
        encontro.setTitulo(titulo);
        encontro.setDataHoraInicio(dataHoraInicio);
        encontro.setDataHoraFim(dataHoraFim);
        encontro.setLocal(local);
        encontro.setPreco(preco);
        encontro.setDescricao(descricao);
        encontro.setStatus(Encontro.StatusEncontro.ABERTO);

        if (capa != null && !capa.isEmpty()) {
            String caminhoCapa = salvarArquivo(capa);
            encontro.setCapa(caminhoCapa);
        }

        return encontroRepository.save(encontro);
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

    public java.util.Optional<Encontro> buscarEncontroPorId(Long idEncontro) {
        return encontroRepository.findById(idEncontro);
    }

    public java.util.List<Encontro> buscarEncontros() {
        return encontroRepository.findAll();
    }

    public Encontro atualizarEncontro(Long idEncontro,
                                      String titulo,
                                      String descricao,
                                      LocalDateTime dataHoraInicio,
                                      LocalDateTime dataHoraFim,
                                      String local,
                                      float preco,
                                      MultipartFile capa) throws IOException {

        Encontro encontro = encontroRepository.findById(idEncontro)
                .orElseThrow(() -> new EntityNotFoundException("Encontro não encontrado"));

        // Atualiza campos básicos
        encontro.setTitulo(titulo);
        encontro.setDescricao(descricao);
        encontro.setDataHoraInicio(dataHoraInicio);
        encontro.setDataHoraFim(dataHoraFim);
        encontro.setLocal(local);
        encontro.setPreco(preco);

        // Atualiza imagem só se veio no request
        if (capa != null && !capa.isEmpty()) {
            String caminhoImagem = salvarArquivo(capa);
            encontro.setCapa(caminhoImagem);
        }

        return encontroRepository.save(encontro);
    }

    public void excluirEncontro(String idEncontro) {
        Optional<Encontro> encontroRemover = encontroRepository.findById(Long.parseLong(idEncontro));
        if (encontroRemover.isPresent()) {
            Encontro encontro = encontroRemover.get();

            Path pastaUploads = Paths.get(System.getProperty("user.dir"), "imagens");

            try {
                // Extrai somente o nome do arquivo da URL
                String url = encontro.getCapa();
                String nomeArquivo;

                // Verifica se a URL contém '/imagens/' e extrai o nome do arquivo
                if (url.contains("/imagens/")) {
                    nomeArquivo = url.substring(url.lastIndexOf("/") + 1);
                } else {
                    nomeArquivo = url; // fallback: assume que o campo já é só o nome do arquivo
                }

                Path caminhoImagem = pastaUploads.resolve(nomeArquivo);

                Files.deleteIfExists(caminhoImagem);
            } catch (Exception e) {
                System.err.println("Erro ao excluir imagem: " + e.getMessage());
            }

            encontroRepository.deleteById(encontro.getId());
        } else {
            System.err.println("Encontro não encontrado.");
        }
    }
}
