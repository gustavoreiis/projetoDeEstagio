package estagio.estagio.Service;

import estagio.estagio.dto.InscricaoRequest;
import estagio.estagio.entity.*;
import estagio.estagio.repository.InscricaoRepository;
import estagio.estagio.repository.PessoaRepository;
import estagio.estagio.repository.ResponsavelRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class InscricaoService {
    @Autowired
    private InscricaoRepository inscricaoRepository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private EncontroService encontroService;
    @Autowired
    private ResponsavelRepository responsavelRepository;
    @Autowired
    private ResponsavelService responsavelService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AuthService authService;

    public Inscricao inscreverParticipante(InscricaoRequest request) {

        if (!authService.isCpfValido(request.getPessoa().getCpf())) {
            throw new RuntimeException("Formato de CPF inválido");
        }

        Encontro encontro = encontroService.buscarEncontroPorId(request.getEncontroId())
                .orElseThrow(() -> new RuntimeException("Encontro não encontrado"));

        Pessoa pessoaRequest = request.getPessoa();
        Responsavel responsavelRequest = request.getResponsavel();

        Optional<Pessoa> pessoaExistenteOpt = pessoaRepository.findByCpf(pessoaRequest.getCpf());
        Pessoa pessoa;

        if (pessoaExistenteOpt.isPresent()) {
            Pessoa existente = pessoaExistenteOpt.get();
            pessoa = pessoaService.atualizarPessoa(existente.getId(), pessoaRequest);
        } else {
            pessoa = pessoaService.criarPessoa(pessoaRequest);
        }

        cadastrarInformacoesResponsavel(pessoaRequest, responsavelRequest);

        verificarJaInscrito(pessoa, encontro.getId());

        Inscricao inscricao = new Inscricao();
        inscricao.setPessoa(pessoa);
        inscricao.setEncontro(encontro);
        inscricao.setDataInscricao(LocalDateTime.now());

        //enviarInformacoesPagamento(request.pessoa, request.encontroId);

        return inscricaoRepository.save(inscricao);
    }

    public void enviarInformacoesPagamento(Pessoa destinatario, Long encontroId) {
        Encontro encontro = encontroService.buscarEncontroPorId(encontroId).orElseThrow();
        try {
            MimeMessage email = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");

            helper.setTo(destinatario.getEmail());
            helper.setSubject("Inscrição no Encontro");
            String corpo = "<html><body>"
                    + "<h2>Olá, " + destinatario.getNome() + "!</h2>"
                    + "<p>Sua inscrição para o evento <strong>" + encontro.getTitulo() + "</strong> foi confirmada.</p>"
                    + "<p><strong>Valor a ser pago:</strong> R$ " + encontro.getPreco() + "</p>"
                    + "<p>Por favor, envie o comprovante de pagamento para um dos seguintes contatos:</p>"
                    + "<ul>"
                    + "<li>Email: Teste</li>"
                    + "<li>WhatsApp: Teste</li>"
                    + "</ul>"
                    + "<p>Após o recebimento, sua inscrição será totalmente validada.</p>"
                    + "<br>"
                    + "<p>Atenciosamente,</p>"
                    + "<p>Equipe Grupo Musa</p>"
                    + "</body></html>";
            helper.setText(corpo, true);
            mailSender.send(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verificarJaInscrito(Pessoa pessoa, Long encontroId) {
        boolean jaInscrito = inscricaoRepository.existsByPessoaIdAndEncontroId(pessoa.getId(), encontroId);
        if (jaInscrito) {
            throw new RuntimeException("O CPF " + pessoa.getCpf() + " já está inscrito nesse encontro.");
        }
    }

    public void cadastrarInformacoesResponsavel(Pessoa pessoa, Responsavel responsavelRequest) {
        LocalDate hoje = LocalDate.now();
        Period idade = Period.between(pessoa.getNascimento(), hoje);

        if (idade.getYears() < 18) {
            if (responsavelRequest == null) {
                throw new RuntimeException("Responsável deve ser informado para menores de idade.");
            }

            Optional<Responsavel> responsavelExistente = responsavelRepository.findByCpf(responsavelRequest.getCpf());
            Responsavel responsavel;

            if (responsavelExistente.isPresent()) {
                responsavel = responsavelService.atualizarResponsavel(responsavelExistente.get().getId(), responsavelRequest);
            } else {
                responsavel = responsavelService.criarResponsavel(responsavelRequest);
            }
            pessoa.setResponsavel(responsavel);
            pessoaService.atualizarPessoa(pessoa.getId(), pessoa);
        }
    }


    public void cancelarInscricao(Long inscricaoId) {
        inscricaoRepository.deleteById(inscricaoId);
    }

    public List<Inscricao> buscarInscricoesPorEncontro(Long encontroId) {
        return inscricaoRepository.findByEncontroId(encontroId);
    }

    public List<Inscricao> buscarInscricoesPorPessoa(Long pessoaId) {
        return inscricaoRepository.findByPessoaId(pessoaId);
    }
}
