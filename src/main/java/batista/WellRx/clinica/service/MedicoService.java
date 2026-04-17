package batista.WellRx.clinica.service;

import batista.WellRx.clinica.database.model.Medico;
import batista.WellRx.clinica.database.repository.MedicoRepository;
import batista.WellRx.clinica.dto.CadastroMedicoDto;
import batista.WellRx.shared.database.model.PerfilEnum;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.database.repository.PerfilRepository;
import batista.WellRx.shared.database.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicoService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final MedicoRepository MedicoRepository;

    public MedicoService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, MedicoRepository medicoRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        MedicoRepository = medicoRepository;
    }

    @Transactional
    public Medico cadastrar(@Valid CadastroMedicoDto dto) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByCpfAndVerificadoTrue(dto.email());
        if (optionalUsuario.isPresent()) {
            throw new RuntimeException("Já existe uma conta cadastrada com esse cpf");
        }

        if (!dto.senha().equals(dto.confirmacaoSenha())){
            throw new RuntimeException("Senha não bate com a confirmação!");
        }
        Optional<Medico> optionaCrm = MedicoRepository.findByCrm(dto.crm());
        if (optionaCrm.isPresent()) {
            throw new RuntimeException("Já existe um médico cadastrado com esse CRM");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        var perfil = perfilRepository.findByNome(PerfilEnum.MEDICO)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
        var usuario = new Usuario(dto, senhaCriptografada, perfil);
        usuarioRepository.save(usuario);

        var medico = new Medico(dto, usuario);
        return MedicoRepository.save(medico);

        }
    }

