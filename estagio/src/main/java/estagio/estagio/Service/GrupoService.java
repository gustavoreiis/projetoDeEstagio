package estagio.estagio.Service;

import estagio.estagio.dto.GrupoDto;
import estagio.estagio.dto.GrupoExibirDto;
import estagio.estagio.entity.Encontro;
import estagio.estagio.entity.Grupo;
import estagio.estagio.entity.Pessoa;
import estagio.estagio.entity.PessoaGrupo;
import estagio.estagio.repository.EncontroRepository;
import estagio.estagio.repository.GrupoRepository;
import estagio.estagio.repository.PessoaGrupoRepository;
import estagio.estagio.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;
    @Autowired
    private EncontroRepository encontroRepository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private PessoaGrupoRepository pessoaGrupoRepository;

    public Grupo criarGrupo(GrupoDto grupoDto) {
        Encontro encontro = encontroRepository.findById(grupoDto.getIdEncontro())
                .orElseThrow(() -> new RuntimeException("Encontro não encontrado"));

        Grupo grupo = new Grupo();
        grupo.setNome(grupoDto.getNome());
        grupo.setEncontro(encontro);
        grupo.setCor(grupoDto.getCor());

        Grupo grupoSalvo = grupoRepository.save(grupo);

        for (Long idLider : grupoDto.getIdLideres()) {
            Pessoa pessoa = pessoaRepository.findById(idLider)
                    .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

            if (pessoaGrupoRepository.existsByPessoaIdAndGrupoEncontroIdAndLiderTrue(idLider, encontro.getId())) {
                throw new RuntimeException("O líder " + pessoa.getNome() + " já está em outro grupo");
            }

            PessoaGrupo pessoaGrupo = new PessoaGrupo();
            pessoaGrupo.setGrupo(grupoSalvo);
            pessoaGrupo.setPessoa(pessoa);
            pessoaGrupo.setLider(true);

            pessoaGrupoRepository.save(pessoaGrupo);
        }

        return grupoSalvo;
    }

    public Grupo atualizarGrupo(Long idGrupo, GrupoDto grupoDto) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("grupo não encontrado"));

        grupo.setNome(grupoDto.getNome());
        grupo.setCor(grupoDto.getCor());

        List<PessoaGrupo> lideresAtuais = pessoaGrupoRepository.findBygrupoIdAndLiderTrue(idGrupo);
        List<Long> idNovosLideres = grupoDto.getIdLideres();

        for (PessoaGrupo pessoaGrupo : lideresAtuais) {
            if (!idNovosLideres.contains(pessoaGrupo.getPessoa().getId())) {
                pessoaGrupoRepository.delete(pessoaGrupo);
            } else {
                idNovosLideres.remove(pessoaGrupo.getPessoa().getId());
            }
        }

        for (Long idLider : idNovosLideres) {
            Pessoa lider = pessoaRepository.findById(idLider)
                    .orElseThrow(() -> new RuntimeException("Pessoa não encontrada."));
            PessoaGrupo pessoaGrupo = new PessoaGrupo();
            pessoaGrupo.setGrupo(grupo);
            pessoaGrupo.setPessoa(lider);
            pessoaGrupo.setLider(true);

            pessoaGrupoRepository.save(pessoaGrupo);
        }
        return grupoRepository.save(grupo);
    }

    @Transactional
    public void excluirGrupo(Long idGrupo) {
        pessoaGrupoRepository.deleteByGrupoId(idGrupo);
        grupoRepository.deleteById(idGrupo);
    }

    public List<GrupoExibirDto> listarGrupos(Long idEncontro) {
        List<Grupo> grupos = grupoRepository.findByEncontroId(idEncontro);

        return grupos.stream().map(grupo -> {
            GrupoExibirDto grupoDto = new GrupoExibirDto();
            grupoDto.setId(grupo.getId());
            grupoDto.setNome(grupo.getNome());
            grupoDto.setCor(grupo.getCor());
            grupoDto.setTituloEncontro(grupo.getEncontro().getTitulo());

            List<GrupoExibirDto.LiderDto> lideres = pessoaGrupoRepository
                    .findBygrupoIdAndLiderTrue(grupo.getId())
                    .stream()
                    .map(pessoaGrupo -> {
                        GrupoExibirDto.LiderDto lider = new GrupoExibirDto.LiderDto();
                        lider.setId(pessoaGrupo.getPessoa().getId());
                        lider.setNome(pessoaGrupo.getPessoa().getNome());
                        return lider;
                    }).toList();
            grupoDto.setLideres(lideres);

            int quantidadeParticipantes = pessoaGrupoRepository
                    .findByGrupoIdAndLiderFalse(grupo.getId())
                    .size();
            grupoDto.setQuantidadeParticipantes(quantidadeParticipantes);
            return grupoDto;
        }).toList();
    }

    public Grupo buscarGrupoPorId(Long idGrupo) {
        return grupoRepository.findById(idGrupo).orElse(null);
    }
}
