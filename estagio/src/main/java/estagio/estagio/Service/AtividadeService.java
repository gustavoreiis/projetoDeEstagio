package estagio.estagio.Service;

import estagio.estagio.dto.AtividadeDto;
import estagio.estagio.dto.PessoaPresencaDto;
import estagio.estagio.entity.Atividade;
import estagio.estagio.entity.Pessoa;
import estagio.estagio.entity.Presenca;
import estagio.estagio.repository.AtividadeRepository;
import estagio.estagio.repository.PessoaRepository;
import estagio.estagio.repository.PresencaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final PessoaRepository pessoaRepository;
    private final PresencaRepository presencaRepository;

    public AtividadeService(AtividadeRepository atividadeRepository, PessoaRepository pessoaRepository, PresencaRepository presencaRepository) {
        this.atividadeRepository = atividadeRepository;
        this.pessoaRepository = pessoaRepository;
        this.presencaRepository = presencaRepository;
    }

    public List<Atividade> listarAtividades() {
        return atividadeRepository.findAll();
    }

    public Atividade criarAtividade(AtividadeDto atividadeDto) {
        Atividade novaAtividade = new Atividade(atividadeDto);;
        return atividadeRepository.save(novaAtividade);
    }

    @Transactional
    public Atividade atualizarAtividade(Long id, AtividadeDto atividadeDto) {
        Atividade atividade = atividadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada."));

        atividade.setDescricao(atividadeDto.getDescricao());
        atividade.setDataAtividade(atividadeDto.getDataAtividade());
        if (atividade.getGrupoDePessoas() != atividadeDto.getGrupoDePessoas()) {
            presencaRepository.deleteByAtividadeId(id);
            atividade.setAtiva(true);
            atividade.setGrupoDePessoas(atividadeDto.getGrupoDePessoas());
        }


        return atividadeRepository.save(atividade);
    }

    @Transactional
    public void deletarAtividade(Long idAtividade) {
        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada."));

        presencaRepository.deleteByAtividadeId(idAtividade);
        atividadeRepository.delete(atividade);
    }

    public List<PessoaPresencaDto> buscarPessoas(Long idAtividade) {
        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada."));

        List<Pessoa> pessoas;

        if (atividade.getGrupoDePessoas() == Atividade.GrupoDePessoas.PARTICIPANTE) {
            pessoas = pessoaRepository.findByTipo(Pessoa.TipoPessoa.PARTICIPANTE);
        } else if (atividade.getGrupoDePessoas() == Atividade.GrupoDePessoas.SERVO) {
            pessoas = pessoaRepository.findByTipo(Pessoa.TipoPessoa.SERVO);
        } else {
            pessoas = pessoaRepository.findByMinisterio(
                    Pessoa.Ministerio.valueOf(atividade.getGrupoDePessoas().name())
            );
        }

        return pessoas.stream().map(pessoa -> {
            Optional<Presenca> pessoaPresenca = presencaRepository.findByPessoaIdAndAtividadeId(pessoa.getId(), atividade.getId());
            return new PessoaPresencaDto(
                    pessoa.getId(),
                    pessoa.getNome(),
                    pessoaPresenca.map(Presenca::isPresente).orElse(false),
                    pessoaPresenca.map(Presenca::getObservacao).orElse(null)
            );
        }).collect(Collectors.toList());
    }

    public void alterarStatusAtividade(Long idAtividade, boolean ativa) {
        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        atividade.setAtiva(ativa);
        atividadeRepository.save(atividade);
    }
}
