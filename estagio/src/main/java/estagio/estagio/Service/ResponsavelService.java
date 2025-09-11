package estagio.estagio.Service;

import estagio.estagio.entity.Responsavel;
import estagio.estagio.repository.ResponsavelRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResponsavelService {
    private ResponsavelRepository responsavelRepository;

    public ResponsavelService(ResponsavelRepository responsavelRepository) {
        this.responsavelRepository = responsavelRepository;
    }

    public Responsavel criarResponsavel(Responsavel responsavel) {
        return responsavelRepository.save(responsavel);
    }

    public Responsavel buscarResponsavelporId(Long id) {
        Optional<Responsavel> responsavel = responsavelRepository.findById(id);
        return responsavel.orElse(null);
    }

    public Responsavel atualizarResponsavel(Long id, Responsavel responsavel) {
        Responsavel novoResponsavel = responsavelRepository.findById(id).get();
        novoResponsavel.setNome(responsavel.getNome());
        novoResponsavel.setTelefone(responsavel.getTelefone());

        return responsavelRepository.save(novoResponsavel);
    }

    public void excluirResponsavel(Long id) {
        responsavelRepository.deleteById(id);
    }


}
