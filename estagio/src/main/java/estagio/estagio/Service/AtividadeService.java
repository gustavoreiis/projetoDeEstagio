package estagio.estagio.Service;

import estagio.estagio.dto.AtividadeDto;
import estagio.estagio.dto.PessoaResumoDto;
import estagio.estagio.entity.Atividade;
import estagio.estagio.entity.Pessoa;
import estagio.estagio.repository.AtividadeRepository;
import estagio.estagio.repository.PessoaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final PessoaRepository pessoaRepository;

    public AtividadeService(AtividadeRepository atividadeRepository, PessoaRepository pessoaRepository) {
        this.atividadeRepository = atividadeRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<Atividade> listarAtividades() {
        return atividadeRepository.findAll();
    }

    public Atividade criarAtividade(AtividadeDto atividadeDto) {
        Atividade novaAtividade = new Atividade(atividadeDto);;
        return atividadeRepository.save(novaAtividade);
    }

    public Atividade atualizarAtividade(Long id, AtividadeDto atividadeDto) {
        Atividade atividade = atividadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada."));

        atividade.setDescricao(atividadeDto.getDescricao());
        atividade.setDataAtividade(atividadeDto.getDataAtividade());
        atividade.setGrupoDePessoas(atividadeDto.getGrupoDePessoas());

        return atividadeRepository.save(atividade);
    }

    public void excluirAtividade(Long idAtividade) {
        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada."));

        atividadeRepository.delete(atividade);
    }

    public List<PessoaResumoDto> buscarPessoas(Long idAtividade) {
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

        return pessoas.stream()
                .map(pessoa -> new PessoaResumoDto(pessoa.getId(), pessoa.getNome()))
                .collect(Collectors.toList());
    }

    public void alterarstatusAtividade(Long idAtividade, Atividade.StatusAtividade statusAtividade) {
        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        atividade.setStatusAtividade(statusAtividade);
        atividadeRepository.save(atividade);
    }
}
