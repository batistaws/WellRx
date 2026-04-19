package batista.WellRx.clinica.dto;

import batista.WellRx.shared.database.model.Usuario;

import java.util.List;

public record ListagemUsuarioDto(

        Long id,
        String nomeCompleto,
        String email,
        String cpf,
        String ativo,
        List<String> perfis

) {


    public ListagemUsuarioDto(Usuario usuario) {
        this(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail(), usuario.getCpf(), usuario.getAtivo() ? "Ativo" : "Inativo",
                usuario.getAuthorities().stream().map(auth -> auth.getAuthority()).toList());
    }
}
