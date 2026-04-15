package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Endereco;
import batista.WellRx.clinica.database.model.Paciente;
import batista.WellRx.clinica.database.model.Sexo;

import java.time.LocalDate;

public record ListagemPacienteDto(

        Long id,
        String nomeCompleto,
        String email,
        String telefone,
        String cpf,
        LocalDate dataNascimento,
        CadastroEnderecoDto endereco,
        Sexo sexo
) {
    public ListagemPacienteDto(Paciente paciente) {
        this(
                paciente.getId(),
                paciente.getNomeCompleto(),
                paciente.getUsuario().getEmail(),
                paciente.getTelefone(),
                paciente.getUsuario().getCpf(),
                paciente.getDataNascimento(),
                new CadastroEnderecoDto(paciente.getEndereco()),
                paciente.getSexo()
        );
    }
}