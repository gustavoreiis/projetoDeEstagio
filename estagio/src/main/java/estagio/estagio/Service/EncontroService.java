package estagio.estagio.Service;

import estagio.estagio.entity.Encontro;
import estagio.estagio.entity.Grupo;
import estagio.estagio.repository.EncontroRepository;
import estagio.estagio.repository.GrupoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EncontroService {

    private final EncontroRepository encontroRepository;
    private final GrupoRepository grupoRepository;
    private final GrupoService grupoService;
    private final String uploadDir = "uploads"; // Pasta de destino

    public EncontroService(GrupoService grupoService, EncontroRepository encontroRepository, GrupoRepository grupoRepository) {
        this.grupoService = grupoService;
        this.encontroRepository = encontroRepository;
        this.grupoRepository = grupoRepository;
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

        encontro.setTitulo(titulo);
        encontro.setDescricao(descricao);
        encontro.setDataHoraInicio(dataHoraInicio);
        encontro.setDataHoraFim(dataHoraFim);
        encontro.setLocal(local);
        encontro.setPreco(preco);

        if (capa != null && !capa.isEmpty()) {
            deletarArquivo(encontro.getCapa());
            String caminhoImagem = salvarArquivo(capa);
            encontro.setCapa(caminhoImagem);
        }
        return encontroRepository.save(encontro);
    }

    public void deletarEncontro(Long idEncontro) {
        Encontro encontro = encontroRepository.findById(idEncontro)
                .orElseThrow(() -> new RuntimeException("Encontro não encontrado."));
        List<Grupo> grupos = grupoRepository.findByEncontroId(idEncontro);

        deletarArquivo(encontro.getCapa());
        for (Grupo grupo : grupos) {
            grupoService.deletarGrupo(grupo.getId());
        }
        encontroRepository.delete(encontro);
    }

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
