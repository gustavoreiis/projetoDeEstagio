package estagio.estagio.Service;

import estagio.estagio.entity.Encontro;
import estagio.estagio.entity.Grupo;
import estagio.estagio.entity.Inscricao;
import estagio.estagio.repository.EncontroRepository;
import estagio.estagio.repository.GrupoRepository;
import estagio.estagio.repository.InscricaoRepository;
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
    private final InscricaoRepository inscricaoRepository;
    private final ArquivoService arquivoService;

    public EncontroService(GrupoService grupoService, EncontroRepository encontroRepository, GrupoRepository grupoRepository, InscricaoRepository inscricaoRepository, ArquivoService arquivoService) {
        this.grupoService = grupoService;
        this.encontroRepository = encontroRepository;
        this.grupoRepository = grupoRepository;
        this.inscricaoRepository = inscricaoRepository;
        this.arquivoService = arquivoService;
    }

    public Encontro criarEncontro(String titulo, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, String local, float preco, String descricao, MultipartFile capa) {
        Encontro encontro = new Encontro();
        encontro.setTitulo(titulo);
        encontro.setDataHoraInicio(dataHoraInicio);
        encontro.setDataHoraFim(dataHoraFim);
        encontro.setLocal(local);
        encontro.setPreco(preco);
        encontro.setDescricao(descricao);
        encontro.setAberto(true);

        if (capa != null && !capa.isEmpty()) {
            String caminhoCapa = arquivoService.salvarArquivo(capa);
            encontro.setCapa(caminhoCapa);
        }
        return encontroRepository.save(encontro);
    }

    public java.util.Optional<Encontro> buscarEncontroPorId(Long idEncontro) {
        return encontroRepository.findById(idEncontro);
    }

    public java.util.List<Encontro> buscarEncontros() {
        return encontroRepository.findByAbertoTrue();
    }

    public Encontro atualizarEncontro(Long idEncontro,
                                      String titulo,
                                      String descricao,
                                      LocalDateTime dataHoraInicio,
                                      LocalDateTime dataHoraFim,
                                      String local,
                                      float preco,
                                      MultipartFile capa,
                                      boolean aberto) throws IOException {

        Encontro encontro = encontroRepository.findById(idEncontro)
                .orElseThrow(() -> new EntityNotFoundException("Encontro não encontrado"));

        encontro.setTitulo(titulo);
        encontro.setDescricao(descricao);
        encontro.setDataHoraInicio(dataHoraInicio);
        encontro.setDataHoraFim(dataHoraFim);
        encontro.setLocal(local);
        encontro.setPreco(preco);
        encontro.setAberto(aberto);

        if (capa != null && !capa.isEmpty()) {
            arquivoService.deletarArquivo(encontro.getCapa());
            String caminhoImagem = arquivoService.salvarArquivo(capa);
            encontro.setCapa(caminhoImagem);
        }
        return encontroRepository.save(encontro);
    }

    public void deletarEncontro(Long idEncontro) {
        Encontro encontro = encontroRepository.findById(idEncontro)
                .orElseThrow(() -> new RuntimeException("Encontro não encontrado."));
        List<Grupo> grupos = grupoRepository.findByEncontroId(idEncontro);
        List<Inscricao> inscricoes = inscricaoRepository.findByEncontroId(idEncontro);

        arquivoService.deletarArquivo(encontro.getCapa());
        for (Grupo grupo : grupos) {
            grupoService.excluirGrupo(grupo.getId());
        }
        inscricaoRepository.deleteAll(inscricoes);
        encontroRepository.delete(encontro);
    }


}
