package estagio.estagio.Service;

import estagio.estagio.dto.AtividadeDto;
import estagio.estagio.entity.Atividade;
import estagio.estagio.repository.AtividadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    public AtividadeService(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    public List<Atividade> listarAtividades() {
        return atividadeRepository.findAll();
    }

    public Atividade criarAtividade(AtividadeDto atividadeDto) {
        Atividade novaAtividade = new Atividade();
        novaAtividade.setDescricao(atividadeDto.getDescricao());
        novaAtividade.setDataAtividade(atividadeDto.getDataAtividade());
        novaAtividade.setGrupoDePessoas(atividadeDto.getGrupoDePessoas());

        return atividadeRepository.save(novaAtividade);
    }
}
