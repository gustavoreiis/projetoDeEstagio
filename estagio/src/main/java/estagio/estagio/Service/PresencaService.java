package estagio.estagio.Service;

import estagio.estagio.dto.PresencaDto;
import estagio.estagio.entity.Atividade;
import estagio.estagio.entity.Pessoa;
import estagio.estagio.entity.Presenca;
import estagio.estagio.repository.AtividadeRepository;
import estagio.estagio.repository.PessoaRepository;
import estagio.estagio.repository.PresencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresencaService {
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private AtividadeRepository atividadeRepository;
    @Autowired
    private PresencaRepository presencaRepository;
    @Autowired
    private AtividadeService atividadeService;

    public void salvarPresencas(List<PresencaDto> presencasDto) {
        Atividade atividade = null;
        for (PresencaDto presencaDto : presencasDto ) {
            Pessoa pessoa = pessoaRepository.findById(presencaDto.getIdPessoa())
                    .orElseThrow(() -> new RuntimeException("Pessooa não encontrada."));

            atividade = atividadeRepository.findById(presencaDto.getIdAtividade())
                    .orElseThrow(() -> new RuntimeException("Atividade não encontrada."));

            Presenca presenca = presencaRepository.findByPessoaIdAndAtividadeId(pessoa.getId(), atividade.getId())
                    .orElse(new Presenca());

            presenca.setAtividade(atividade);
            presenca.setPessoa(pessoa);
            presenca.setPresente(presencaDto.isPresente());
            presenca.setObservacao(presencaDto.getObservacao());

            presencaRepository.save(presenca);
        }
        atividadeService.alterarStatusAtividade(atividade.getId(), false);
    }
}
