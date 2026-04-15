package batista.WellRx.clinica.service;

import batista.WellRx.clinica.database.model.Paciente;
import batista.WellRx.clinica.database.repository.PacienteRepository;
import batista.WellRx.clinica.dto.CadastroPacienteDto;
import batista.WellRx.clinica.dto.ListagemPacienteDto;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.database.repository.PerfilRepository;
import batista.WellRx.shared.database.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PacienteService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PacienteRepository pacienteRepository;

    public PacienteService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, PacienteRepository pacienteRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public Paciente cadastrar(@Valid CadastroPacienteDto dto) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findByCpfAndVerificadoTrue(dto.email());
        if (optionalUsuario.isPresent()) {
            throw new RuntimeException("Já existe uma conta cadastrada com esse cpf ou nome de usuário!");
        }

        if (dto.senha() != dto.confirmacaoSenha()){
            throw new RuntimeException("Senha não bate com a confirmação!");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        var perfil = perfilRepository.findByNome("PACIENTE");
        var usuario = new Usuario(dto, senhaCriptografada, perfil);
        usuarioRepository.save(usuario);

        var paciente = new Paciente(dto, usuario);
        return pacienteRepository.save(paciente);

    }
}
