package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Recepcionista;
import batista.WellRx.clinica.database.model.Sexo;
import batista.WellRx.clinica.database.model.Turno;

import java.time.LocalDate;

public record ListagemRecepcionistaDto(

        Long id,
        String nomeCompleto,
        String email,
        String telefone,
        String cpf,
        String ramal,
        Turno turno,
        LocalDate dataNascimento,
        Sexo sexo,
        CadastroEnderecoDto endereco
) {
    public ListagemRecepcionistaDto(Recepcionista recepcionista) {
        this(
                recepcionista.getId(),
                recepcionista.getNomeCompleto(),
                recepcionista.getUsuario().getEmail(),
                recepcionista.getTelefone(),
                recepcionista.getUsuario().getCpf(),
                recepcionista.getRamal(),
                recepcionista.getTurno(),
                recepcionista.getDataNascimento(),
                recepcionista.getSexo(),
                new CadastroEnderecoDto(recepcionista.getEndereco())
        );
    }
}
