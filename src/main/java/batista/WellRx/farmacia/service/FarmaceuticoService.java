package batista.WellRx.farmacia.service;

import batista.WellRx.farmacia.database.model.Farmaceutico;
import batista.WellRx.farmacia.database.repository.FarmaceuticoRepository;
import batista.WellRx.farmacia.dto.AtualizacaoFarmaceuticoDto;
import batista.WellRx.farmacia.dto.CadastroFarmaceuticoDto;
import batista.WellRx.farmacia.dto.ListagemFarmaceuticoDto;
import batista.WellRx.farmacia.dto.ListarFarmaceuticoDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FarmaceuticoService {

    private final HierarquiaService hierarquiaService;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final EmailService emailService;
    private final FarmaceuticoRepository farmaceuticoRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidarPermissao validar;



    public FarmaceuticoService(HierarquiaService hierarquiaService, UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, EmailService emailService, FarmaceuticoRepository farmaceuticoRepository, PasswordEncoder passwordEncoder, ValidarPermissao validar) {
        this.hierarquiaService = hierarquiaService;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.emailService = emailService;
        this.farmaceuticoRepository = farmaceuticoRepository;
        this.passwordEncoder = passwordEncoder;
        this.validar = validar;
    }

    @Transactional
    public ListagemFarmaceuticoDto cadastrar(@Valid CadastroFarmaceuticoDto dto, Usuario logado) {
         Optional<Usuario> optionalUsuario = usuarioRepository.findByCpfAndVerificadoTrue(dto.cpf());

            if (!hierarquiaService.usuarioTemPermissao(logado, "ROLE_ADMIN")) {
                throw new RegraNegocioException("Acesso negado: Apenas administradores podem cadastrar novos médicos.");
            }

            if (optionalUsuario.isPresent()) {
                throw new RegraNegocioException("Já existe uma conta cadastrada com esse cpf");
            }

            if (!dto.senha().equals(dto.confirmacaoSenha())){
                throw new RegraNegocioException("Senha não bate com a confirmação!");
            }

            String senhaCriptografada = passwordEncoder.encode(dto.senha());
            var perfil = perfilRepository.findByNome(PerfilEnum.MEDICO)
                    .orElseThrow(() -> new RegraNegocioException("Perfil não encontrado"));
            var usuario = new Usuario(dto, senhaCriptografada, perfil);
            emailService.enviarEmailVerificacao(usuario);
            usuarioRepository.save(usuario);

            var farmaceutico = new Farmaceutico(dto, usuario);
            farmaceuticoRepository.save(farmaceutico);
            return new ListagemFarmaceuticoDto(farmaceutico);

        }

    public Page<ListarFarmaceuticoDto> listar(Pageable paginacao, Usuario logado) {

        if (!hierarquiaService.usuarioTemPermissao(logado, "ROLE_RECEPCIONISTA")) {
            throw new RegraNegocioException("Acesso negado: Apenas recepcionistas podem listar os médicos.");
        }

        var pagina = farmaceuticoRepository.findAll(paginacao).map(ListarFarmaceuticoDto::new);
        return pagina;
    }

    public ListagemFarmaceuticoDto detalhar(Long id, Usuario logado) {

        if (!hierarquiaService.usuarioTemPermissao(logado, "ROLE_FARMACEUTICO")) {
            throw new RegraNegocioException("Acesso negado: Apenas farmacêuticos podem detalhar os médicos.");
        }

        var farmaceutico = farmaceuticoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Farmacêutico não encontrado"));

        return new ListagemFarmaceuticoDto(farmaceutico);
    }

    public ListagemFarmaceuticoDto atualizar(@Valid AtualizacaoFarmaceuticoDto dto, Usuario logado) {

        var farmaceutico = farmaceuticoRepository.findById(dto.id()).orElseThrow(() -> new RegraNegocioException("Farmaceutico não encontrado"));

        validar.validarDonoOuAdmin(farmaceutico.getUsuario().getId(), logado, "Você não tem permissão de atualizar informação de outro farmaceutico");

        return new ListagemFarmaceuticoDto(farmaceutico.atualizarInformacoes(dto));
    }
}

