package estagio.estagio.Service;

import estagio.estagio.dto.*;
import estagio.estagio.entity.*;
import estagio.estagio.repository.InscricaoRepository;
import estagio.estagio.repository.PessoaRepository;
import estagio.estagio.repository.PresencaRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private PresencaService presencaService;
    @Autowired
    private InscricaoRepository inscricaoRepository;
    @Autowired
    private PresencaRepository presencaRepository;
    @Autowired
    private CpfService cpfService;

    public Pessoa criarPessoa(Pessoa pessoa) {
        validarCpf(pessoa.getCpf());
        pessoa.setAtivo(true);
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

        if (pessoa.getCpf() != null && !pessoa.getCpf().equals(atual.getCpf())) {
            validarCpf(pessoa.getCpf());
            atual.setCpf(pessoa.getCpf());
        }

        if (pessoa.getNome() != null) atual.setNome(pessoa.getNome());
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
        if (pessoa.getMinisterio() != null) atual.setMinisterio(pessoa.getMinisterio());
        if (pessoa.getTipo() == Pessoa.TipoPessoa.PARTICIPANTE) atual.setMinisterio(null);
        if (pessoa.getSexo() != null) atual.setSexo(pessoa.getSexo());

        if (pessoa.getObservacao() != null) atual.setObservacao(pessoa.getObservacao());
        if (pessoa.getAtivo() != null) {
            atual.setAtivo(pessoa.getAtivo());
        }
        if (pessoa.getCoordenador() != null) atual.setCoordenador(pessoa.getCoordenador());

        return pessoaRepository.save(atual);
    }


    public void excluirPessoa(Long id) {
        pessoaRepository.findById(id).ifPresent(pessoaRepository::delete);
    }

    public List<PessoaNomeDto> buscarLideres(Long idEncontro, Pessoa.TipoPessoa tipo) {
        List<Pessoa> pessoas = pessoaRepository.findLideresDisponiveis(idEncontro, tipo);

        List<PessoaNomeDto> lideres = new ArrayList<>();
        for (Pessoa pessoa : pessoas) {
            PessoaNomeDto lider = new PessoaNomeDto(pessoa.getId(), pessoa.getNome());
            lideres.add(lider);
        }
        return lideres;
    }

    public Page<ParticipanteTabelaDto> listarPessoas(String nome, String cpf, String grupo, Pageable pageable) {

        String nomeLike = (nome == null || nome.isBlank()) ? null : "%" + nome.toLowerCase() + "%";
        String cpfLike  = (cpf == null || cpf.isBlank()) ? null : "%" + cpf + "%";

        Pessoa.TipoPessoa tipo = null;
        Pessoa.Ministerio ministerio = null;

        if (grupo != null && !grupo.isBlank()) {
            if (grupo.equalsIgnoreCase("Participante")) {
                tipo = Pessoa.TipoPessoa.PARTICIPANTE;
            }
            else if (grupo.equalsIgnoreCase("Servo")) {
                tipo = Pessoa.TipoPessoa.SERVO;
            }
            else {
                try {
                    ministerio = Arrays.stream(Pessoa.Ministerio.values())
                            .filter(m -> m.getNomeFormatado().equalsIgnoreCase(grupo))
                            .findFirst()
                            .orElse(null);

                    if (ministerio != null) {
                        tipo = Pessoa.TipoPessoa.SERVO;
                    }

                } catch (Exception ignored) {}
            }
        }

        Page<Pessoa> page = pessoaRepository.buscarPessoas(
                nomeLike,
                cpfLike,
                tipo,
                ministerio,
                pageable
        );

        return page.map(pessoa -> {

            int frequencia = presencaService.calcularFrequencia(pessoa.getId());

            String grupoPessoa;
            if (pessoa.getMinisterio() != null) {
                grupoPessoa = pessoa.getMinisterio().getNomeFormatado();
            }
            else
                grupoPessoa = pessoa.getTipo().name().charAt(0) + pessoa.getTipo().name().substring(1).toLowerCase();

            return new ParticipanteTabelaDto(
                    pessoa.getId(),
                    pessoa.getNome(),
                    pessoa.getCpf(),
                    pessoa.getTelefone(),
                    grupoPessoa,
                    pessoa.getNascimento(),
                    frequencia
            );
        });
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
        List<Pessoa> pessoas = pessoaRepository.findByCoordenadorIsNotNull();
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

    public void alterarStatusCoordenador(List<SolicitacaoAtualizacaoCoordenadorDto> solicitacoes) {
        for (SolicitacaoAtualizacaoCoordenadorDto solicitacao : solicitacoes) {
            Pessoa pessoa = pessoaRepository.findById(solicitacao.getIdPessoa())
                    .orElseThrow(() -> new RuntimeException("Pessoa não encontrada."));

            try {
                Pessoa.StatusCoordenador novoStatus = Pessoa.StatusCoordenador.valueOf(String.valueOf(solicitacao.getNovoStatus()));
                pessoa.setCoordenador(novoStatus);
                pessoaRepository.save(pessoa);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao atualizar status coordenador.", e);
            }
        }
    }

    public DetalhesPessoaDto buscarDetalhesPessoa(Long idPessoa) {
        Pessoa pessoa = pessoaRepository.findById(idPessoa)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada."));

        Responsavel responsavel = pessoa.getResponsavel();

        String nomeResponsavel = responsavel != null ? responsavel.getNome() : null;
        String telefoneResponsavel = responsavel != null ? responsavel.getTelefone() : null;
        String cpfResponsavel = responsavel != null ? responsavel.getCpf() : null;

        String ministerioFormatado = pessoa.getMinisterio() != null
                ? pessoa.getMinisterio().getNomeFormatado()
                : null;

        return new DetalhesPessoaDto(
                pessoa.getId(),
                pessoa.getEmail(),
                pessoa.getEndereco(),
                pessoa.getTipo(),
                ministerioFormatado,
                pessoa.getSexo(),
                pessoa.getCoordenador(),
                nomeResponsavel,
                telefoneResponsavel,
                cpfResponsavel,
                pessoa.getObservacao()
        );
    }

    public List<PessoaStatusDto> buscarInativos() {
        List<Pessoa> pessoas = pessoaRepository.findByAtivo(false);
        List<PessoaStatusDto> inativos = new ArrayList<>();
        for (Pessoa pessoa : pessoas) {
            inativos.add(new PessoaStatusDto(
                    pessoa.getId(),
                    pessoa.getNome()
            ));
        }

        return inativos;
    }

    public void validarCpf(String cpf) {
        if (!cpfService.isCpfValido(cpf)) {
            throw new RuntimeException("Formato de CPF inválido.");
        }

        if (pessoaRepository.existsByCpf(cpf)) {
            throw new RuntimeException("CPF já cadastrado.");
        }
    }
}
