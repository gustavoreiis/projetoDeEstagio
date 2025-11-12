package estagio.estagio.Service;

import org.springframework.stereotype.Service;

@Service
public class CpfService {

    public boolean isCpfValido(String cpf) {
        if (cpf == null) return false;

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) return false;

        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int soma = 0, resto;

            for (int i = 1; i <= 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i - 1)) * (11 - i);
            }
            resto = (soma * 10) % 11;
            if (resto == 10 || resto == 11) resto = 0;
            if (resto != Character.getNumericValue(cpf.charAt(9))) return false;


            soma = 0;
            for (int i = 1; i <= 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i - 1)) * (12 - i);
            }
            resto = (soma * 10) % 11;
            if (resto == 10 || resto == 11) resto = 0;
            if (resto != Character.getNumericValue(cpf.charAt(10))) return false;

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
