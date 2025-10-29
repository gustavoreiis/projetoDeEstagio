package estagio.estagio.Service;

import estagio.estagio.dto.*;
import estagio.estagio.entity.*;
import estagio.estagio.repository.EncontroRepository;
import estagio.estagio.repository.InscricaoRepository;
import estagio.estagio.repository.PessoaRepository;
import estagio.estagio.repository.PresencaRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private PresencaService presencaService;
    @Autowired
    private EncontroRepository encontroRepository;
    @Autowired
    private InscricaoRepository inscricaoRepository;
    @Autowired
    private PresencaRepository presencaRepository;

    public Pessoa criarPessoa(Pessoa pessoa) {
        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new RuntimeException("O CPF informado já está cadastrado.");
        }

        if (pessoa.getSenha() != null && !pessoa.getSenha().isEmpty()) {
            String hashed = BCrypt.hashpw(pessoa.getSenha(), BCrypt.gensalt());
            pessoa.setSenha(hashed);
        }

        return pessoaRepository.save(pessoa);
    }

    public Optional<Pessoa> buscarPessoaPorId(Long id) {
        return pessoaRepository.findById(id);
    }

    public Optional<Pessoa> buscarPessoaPorCpf(String cpf) {
        return pessoaRepository.findPessoaByCpf(cpf);
    }

    public Pessoa atualizarPessoa(Long id, Pessoa pessoa) {
        var pessoaExistente = pessoaRepository.findById(id);

        if (pessoaExistente.isEmpty()) return null;

        var atual = pessoaExistente.get();

        if (pessoa.getNome() != null) atual.setNome(pessoa.getNome());
        if (pessoa.getCpf() != null) atual.setCpf(pessoa.getCpf());
        if (pessoa.getTelefone() != null) atual.setTelefone(pessoa.getTelefone());
        if (pessoa.getEmail() != null) atual.setEmail(pessoa.getEmail());
        if (pessoa.getEndereco() != null) atual.setEndereco(pessoa.getEndereco());
        if (pessoa.getNascimento() != null) atual.setNascimento(pessoa.getNascimento());

        if (pessoa.getSenha() != null && !pessoa.getSenha().isEmpty()) {
            if (!pessoa.getSenha().startsWith("$2a$")) {
                String hashed = BCrypt.hashpw(pessoa.getSenha(), BCrypt.gensalt());
                atual.setSenha(hashed);
            } else {
                atual.setSenha(pessoa.getSenha());
            }
        }

        if (pessoa.getResponsavel() != null) atual.setResponsavel(pessoa.getResponsavel());
        if (pessoa.getTipo() != null) atual.setTipo(pessoa.getTipo());
        if (pessoa.getSexo() != null) atual.setSexo(pessoa.getSexo());
        if (pessoa.getMinisterio() != null) atual.setMinisterio(pessoa.getMinisterio());
        if (pessoa.getObservacao() != null) atual.setObservacao(pessoa.getObservacao());
        if (pessoa.getCoordenador() != null) atual.setCoordenador(pessoa.getCoordenador());

        return pessoaRepository.save(atual);
    }


    public void excluirPessoa(Long id) {
        pessoaRepository.findById(id).ifPresent(pessoaRepository::delete);
    }

    public Optional<Pessoa> findByCpf(String cpf) {
        return pessoaRepository.findPessoaByCpf(cpf);
    }

    public List<Pessoa> buscarLideres(Long idEncontro, Pessoa.TipoPessoa tipo) {
        return pessoaRepository.findLideresDisponiveis(idEncontro, tipo);
    }

    public List<ParticipanteTabelaDto> listarPessoas() {
        List<Pessoa> pessoas = pessoaRepository.findAll();

        return pessoas.stream().map(pessoa -> {

            int frequencia = presencaService.calcularFrequencia(pessoa.getId());

            String grupo;
            if (pessoa.getTipo() == Pessoa.TipoPessoa.SERVO) {
                if (pessoa.getMinisterio() != null) grupo = pessoa.getMinisterio().getNomeFormatado();
                else grupo = "Servo";
            } else {
                grupo = "Participante";
            }

            return new ParticipanteTabelaDto(
                    pessoa.getId(),
                    pessoa.getNome(),
                    pessoa.getCpf(),
                    pessoa.getTelefone(),
                    grupo,
                    pessoa.getNascimento(),
                    frequencia
            );
        }).toList();
    }

    public Map<String, Object> buscarHistoricoPessoa(Long idPessoa) {

        List<Presenca> presencas = presencaRepository.findByPessoaId(idPessoa);
        List<HistoricoAtividadeDto> atividades = presencas.stream()
                .map(presenca -> {
                    Atividade atividade = presenca.getAtividade();
                    return new HistoricoAtividadeDto(
                            atividade.getDescricao(),
                            atividade.getDataAtividade(),
                            presenca.isPresente()
                    );
                }).collect(Collectors.toList());

        List<Inscricao> incricoes = inscricaoRepository.findByPessoaId(idPessoa);
        List<HistoricoEncontroDto> encontros = incricoes.stream()
                .map(inscricao -> {
                    Encontro encontro = inscricao.getEncontro();
                    return new HistoricoEncontroDto(
                            encontro.getTitulo(),
                            encontro.getDataHoraInicio()
                    );
                }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("atividades", atividades);
        response.put("encontros", encontros);

        return response;
    }

    public Map<String, Object> buscarSolicitacoesCoordenadores() {
        List<Pessoa> pessoas = pessoaRepository.findByCoordenadorIsNull();
        List<SolicitacaoCoordenadorDto> pendentes = new ArrayList<>();
        List<SolicitacaoCoordenadorDto> aprovados = new ArrayList<>();
        List<SolicitacaoCoordenadorDto> negados = new ArrayList<>();

        for (Pessoa pessoa : pessoas) {
            switch (pessoa.getCoordenador()) {
                case PENDENTE -> pendentes.add(new SolicitacaoCoordenadorDto(pessoa.getId(), pessoa.getNome(), pessoa.getCoordenador()));
                case COORDENADOR -> aprovados.add(new SolicitacaoCoordenadorDto(pessoa.getId(), pessoa.getNome(), pessoa.getCoordenador()));
                case NEGADO -> negados.add(new SolicitacaoCoordenadorDto(pessoa.getId(), pessoa.getNome(), pessoa.getCoordenador()));
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("pendentes", pendentes);
        response.put("aprovados", aprovados);
        response.put("negados", negados);

        return response;
    }
}
