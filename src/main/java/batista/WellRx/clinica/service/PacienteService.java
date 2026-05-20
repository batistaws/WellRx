package batista.WellRx.clinica.service;

import batista.WellRx.clinica.database.model.*;
import batista.WellRx.clinica.database.repository.PacienteRepository;
import batista.WellRx.clinica.dto.*;
import batista.WellRx.clinica.database.model.Paciente;
import batista.WellRx.infra.email.EmailService;
import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.infra.exeption.ValidarPermissao;
import batista.WellRx.shared.database.model.PerfilEnum;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.database.repository.PerfilRepository;
import batista.WellRx.shared.database.repository.UsuarioRepository;
import batista.WellRx.shared.service.HierarquiaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PacienteService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final PerfilRepository perfilRepository;
    private final PacienteRepository pacienteRepository;
    private final HierarquiaService hierarquiaService;
    private final ValidarPermissao validar;

    public PacienteService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, EmailService emailService, PerfilRepository perfilRepository, PacienteRepository pacienteRepository, HierarquiaService hierarquiaService, ValidarPermissao validar) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.perfilRepository = perfilRepository;
        this.pacienteRepository = pacienteRepository;
        this.hierarquiaService = hierarquiaService;
        this.validar = validar;
    }

    @Transactional
    public Paciente cadastrar(@Valid CadastroPacienteDto dto) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findByCpfAndVerificadoTrue(dto.cpf());
        if (optionalUsuario.isPresent()) {
            throw new RuntimeException("Já existe uma conta cadastrada com esse cpf");
        }

        if (!dto.senha().equals(dto.confirmacaoSenha())){
            throw new RuntimeException("Senha não bate com a confirmação!");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        var perfil = perfilRepository.findByNome(PerfilEnum.PACIENTE)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        var usuario = new Usuario(dto, senhaCriptografada, perfil);
        emailService.enviarEmailVerificacao(usuario);
        usuarioRepository.save(usuario);

        var paciente = new Paciente(dto, usuario);
        return pacienteRepository.save(paciente);

    }


    public Page listar(Pageable paginacao, Usuario logado) {
        Specification<Paciente> spec = Specification.where(PacienteSpecification.estaAtivo());
        Page<Paciente> paciente = pacienteRepository.findAll(spec, paginacao);
        return paciente.map(ListarPacienteDto::new);
    }

    public Paciente listarPorId(Long id, Usuario logado) {
        var paciente = pacienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Recepcionista não encontrado"));

        validar.validarDonoOuAdmin(paciente.getUsuario().getId(),
                logado,
                "Você não tem permissão de acessar informação de outro paciente");
        return paciente;
    }

    @Transactional
    public Paciente atualizar(@Valid AtualizacaoPacienteDto dto, Usuario logado) {
        var paciente = pacienteRepository.findById(dto.id()).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        validar.validarDonoOuAdmin(paciente.getUsuario().getId(), logado, "Você não tem permissão de atualizar informação de outro paciente");
        return paciente.atualizarInformacoes(dto);
    }

}
