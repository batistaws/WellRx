package batista.WellRx.clinica.service;

import batista.WellRx.clinica.database.model.Recepcionista;
import batista.WellRx.clinica.database.model.RecepcionistaSpecification;
import batista.WellRx.clinica.database.repository.RecepcionistaRepository;
import batista.WellRx.clinica.dto.AtualizacaoRecepcionistaDto;
import batista.WellRx.clinica.dto.CadastroRecepcionistaDto;
import batista.WellRx.clinica.dto.ListarRecepcionistaDto;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
public class RecepcionistaService {

    private final RecepcionistaRepository recepcionistaRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final HierarquiaService hierarquiaService;
    private final ValidarPermissao validar;

    public RecepcionistaService(RecepcionistaRepository recepcionistaRepository, EmailService emailService, PasswordEncoder passwordEncoder, PerfilRepository perfilRepository, UsuarioRepository usuarioRepository, HierarquiaService hierarquiaService, ValidarPermissao validarPermissao, ValidarPermissao validar) {
        this.recepcionistaRepository = recepcionistaRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.hierarquiaService = hierarquiaService;
        this.validar = validar;

    }

    @Transactional
    public Recepcionista cadastrar(CadastroRecepcionistaDto dto, @AuthenticationPrincipal Usuario logado) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findByCpfAndVerificadoTrue(dto.cpf());

        if (!hierarquiaService.usuarioTemPermissao(logado, "ROLE_ADMIN")) {
            throw new RegraNegocioException("Acesso negado: Apenas administradores podem cadastrar novos recepcionistas.");
        }

        if (optionalUsuario.isPresent()) {
            throw new RuntimeException("Já existe uma conta cadastrada com esse cpf");
        }

        if (!dto.senha().equals(dto.confirmacaoSenha())){
            throw new RuntimeException("Senha não bate com a confirmação!");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        var perfil = perfilRepository.findByNome(PerfilEnum.RECEPCIONISTA)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        var usuario = new Usuario(dto, senhaCriptografada, perfil);
        emailService.enviarEmailVerificacao(usuario);
        usuarioRepository.save(usuario);

        var recepcionista = new Recepcionista(dto, usuario);
        return recepcionistaRepository.save(recepcionista);

    }

    public Page<ListarRecepcionistaDto> listar( Pageable paginacao) {
        Specification<Recepcionista> spec = Specification.where(RecepcionistaSpecification.estaAtivo());
        Page<Recepcionista> recepcionistas = recepcionistaRepository.findAll(spec, paginacao);
        return recepcionistas.map(ListarRecepcionistaDto::new);
    }

    public Recepcionista listarPorId(Long id, Usuario logado) {
        var recepcionista = recepcionistaRepository.findById(id).orElseThrow(() -> new RuntimeException("Recepcionista não encontrado"));

        validar.validarDonoOuAdmin(recepcionista.getUsuario().getId(),
                logado,
                "Você não tem permissão para ver informações deste recepcionista");

        return recepcionista;
    }

    @Transactional
    public Recepcionista atualizar(AtualizacaoRecepcionistaDto dto, Usuario logado) {

        var recepcionista = recepcionistaRepository.findById(dto.id()).orElseThrow(() -> new RuntimeException("Recepcionista não encontrado"));

        validar.validarDonoOuAdmin(recepcionista.getUsuario().getId(),
                logado,
                "Você não tem permissão para ver informações deste recepcionista");

        return recepcionista.atualizarInformacoes(dto);
    }
}
