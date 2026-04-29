package batista.WellRx.clinica.service;

import batista.WellRx.clinica.database.model.Recepcionista;
import batista.WellRx.clinica.database.model.RecepcionistaSpecification;
import batista.WellRx.clinica.database.repository.RecepcionistaRepository;
import batista.WellRx.clinica.dto.AtualizacaoRecepcionistaDto;
import batista.WellRx.clinica.dto.CadastroRecepcionistaDto;
import batista.WellRx.clinica.dto.ListarRecepcionistaDto;
import batista.WellRx.infra.email.EmailService;
import batista.WellRx.shared.database.model.PerfilEnum;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.database.repository.PerfilRepository;
import batista.WellRx.shared.database.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public RecepcionistaService(RecepcionistaRepository recepcionistaRepository, EmailService emailService, PasswordEncoder passwordEncoder, PerfilRepository perfilRepository, UsuarioRepository usuarioRepository) {
        this.recepcionistaRepository = recepcionistaRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Recepcionista cadastrar(CadastroRecepcionistaDto dto) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findByCpfAndVerificadoTrue(dto.cpf());

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

    public Recepcionista listarPorId(Long id) {
        var recepcionista = recepcionistaRepository.findById(id).orElseThrow(() -> new RuntimeException("Recepcionista não encontrado"));

        //if (!recepcionista.getUsuario().getId().equals(logado.getId())) {
        //   throw new RuntimeException("Acesso negado");
        //}
        return recepcionista;
    }

    @Transactional
    public Recepcionista atualizar(AtualizacaoRecepcionistaDto dto) {
        var recepcionista = recepcionistaRepository.findById(dto.id()).orElseThrow(() -> new RuntimeException("Recepcionista não encontrado"));
        return recepcionista.atualizarInformacoes(dto);
    }
}
