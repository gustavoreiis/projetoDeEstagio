package estagio.estagio.Service;

import estagio.estagio.dto.PessoaNomeDto;
import estagio.estagio.entity.Grupo;
import estagio.estagio.entity.Pessoa;
import estagio.estagio.entity.PessoaGrupo;
import estagio.estagio.repository.EncontroRepository;
import estagio.estagio.repository.GrupoRepository;
import estagio.estagio.repository.PessoaGrupoRepository;
import estagio.estagio.repository.PessoaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PessoaGrupoService {

    private final PessoaRepository pessoaRepository;
    private final GrupoRepository grupoRepository;
    private final PessoaGrupoRepository pessoaGrupoRepository;
    private final EncontroRepository encontroRepository;

    public PessoaGrupoService(PessoaRepository pessoaRepository, GrupoRepository grupoRepository, PessoaGrupoRepository pessoaGrupoRepository, EncontroRepository encontroRepository) {
        this.pessoaRepository = pessoaRepository;
        this.grupoRepository = grupoRepository;
        this.pessoaGrupoRepository = pessoaGrupoRepository;
        this.encontroRepository = encontroRepository;
    }

    public List<PessoaNomeDto> listarParticipantesDoGrupo(Long idgrupo) {
        if (grupoRepository.findById(idgrupo).isEmpty()) {
            throw new RuntimeException("Grupo não encontrado");
        }

        List<PessoaGrupo> listaPessoaGrupo = pessoaGrupoRepository.findByGrupoIdAndLiderFalse(idgrupo);
        List<PessoaNomeDto> listaParticipantes = new ArrayList<>();

        for (PessoaGrupo pessoaGrupo : listaPessoaGrupo) {
            Pessoa participante = pessoaGrupo.getPessoa();
            PessoaNomeDto pessoaNomeDto = new PessoaNomeDto(participante.getId(), participante.getNome());
            listaParticipantes.add(pessoaNomeDto);
        }
        return listaParticipantes;
    }

    public List<PessoaNomeDto> listarParticipantesSemGrupo(Long idEncontro) {
        List<Object[]> lista = pessoaGrupoRepository.findParticipantesSemGrupo(idEncontro);

        return lista.stream()
                .map(obj -> new PessoaNomeDto(
                        ((Number) obj[0]).longValue(), // id_pessoa
                        (String) obj[1]                // nome
                ))
                .toList();
    }

    public PessoaGrupo adicionarPessoa(Long idPessoa, Long idGrupo) {
        Pessoa pessoa = pessoaRepository.findById(idPessoa)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada."));
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado."));

        PessoaGrupo pessoaGrupo = PessoaGrupo.builder()
                .pessoa(pessoa)
                .grupo(grupo)
                .lider(false)
                .build();
        return pessoaGrupoRepository.save(pessoaGrupo);
    }

    public void removerPessoa(Long idParticipante, Long idGrupo) {
        PessoaGrupo pessoaGrupo = pessoaGrupoRepository.findByPessoaIdAndGrupoId(idParticipante, idGrupo);
        pessoaGrupoRepository.delete(pessoaGrupo);
    }

}