package batista.WellRx.clinica.service;

import batista.WellRx.clinica.database.model.Medico;
import batista.WellRx.clinica.database.model.MedicoSpecification;
import batista.WellRx.clinica.database.repository.MedicoRepository;
import batista.WellRx.clinica.dto.*;
import batista.WellRx.infra.email.EmailService;
import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.infra.exeption.ValidarPermissao;
import batista.WellRx.shared.database.model.PerfilEnum;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.database.repository.PerfilRepository;
import batista.WellRx.shared.database.repository.UsuarioRepository;
import batista.WellRx.shared.service.HierarquiaService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicoService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final PerfilRepository perfilRepository;
    private final MedicoRepository MedicoRepository;
    private final HierarquiaService hierarquiaService;
    private final ValidarPermissao validar;

    public MedicoService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, EmailService emailService, PerfilRepository perfilRepository, MedicoRepository medicoRepository, HierarquiaService hierarquiaService, ValidarPermissao validar) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.perfilRepository = perfilRepository;
        this.MedicoRepository = medicoRepository;
        this.hierarquiaService = hierarquiaService;
        this.validar = validar;
    }

    @Transactional
    public Medico cadastrar( CadastroMedicoDto dto, Usuario logado) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByCpfAndVerificadoTrue(dto.cpf());

        if (!hierarquiaService.usuarioTemPermissao(logado, "ROLE_ADMIN")) {
            throw new RegraNegocioException("Acesso negado: Apenas administradores podem cadastrar novos médicos.");
        }

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
        emailService.enviarEmailVerificacao(usuario);
        usuarioRepository.save(usuario);

        var medico = new Medico(dto, usuario);
        return MedicoRepository.save(medico);

        }

    public Page listar(Pageable paginacao, Usuario logado) {

        if (!hierarquiaService.usuarioTemPermissao(logado, "ROLE_RECEPCIONISTA")) {
            throw new RegraNegocioException("Apenas recepcionistas podem ver os médicos.");
        }
        Specification<Medico> spec = Specification.where(MedicoSpecification.estaAtivo());
        Page<Medico> medico = MedicoRepository.findAll(spec, paginacao);
        return medico.map(ListarMedicoDto::new);
    }

    public Medico listarPorId(Long id, Usuario logado) {
        var medico = MedicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Médico não encontrado"));

        validar.validarDonoOuAdmin(medico.getUsuario().getId(),
                logado,
                "Você não tem permissão para ver informações deste medico" );
        return medico;
    }

    public Medico atualizar(AtualizacaoMedicoDto dto, Usuario logado) {
        var medico = MedicoRepository.findById(dto.id()).orElseThrow(() -> new RuntimeException("Médico não encontrado"));

        validar.validarDonoOuAdmin(medico.getUsuario().getId(), logado, "Você não tem permissão de atualizar informação de outro médico");

        return medico.atualizarInformacoes(dto);
    }
}

