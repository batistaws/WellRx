package batista.WellRx.farmacia.dto;

import batista.WellRx.clinica.database.model.Sexo;
import batista.WellRx.clinica.dto.CadastroEnderecoDto;
import batista.WellRx.farmacia.database.model.Farmaceutico;

import java.time.LocalDate;

public record ListagemFarmaceuticoDto (

        Long id,
        String nomeCompleto,
        String email,
        String cpf,
        String telefone,
        LocalDate dataNascimento,
        CadastroEnderecoDto endereco,
        Sexo sexo
) {
    public ListagemFarmaceuticoDto(Farmaceutico farmaceutico) {
        this(
                farmaceutico.getId(),
                farmaceutico.getNomeCompleto(),
                farmaceutico.getUsuario().getEmail(),
                farmaceutico.getCpf(),
                farmaceutico.getTelefone(),
                farmaceutico.getDataNascimento(),
                new CadastroEnderecoDto(farmaceutico.getEndereco()), farmaceutico.getSexo());
    }
}