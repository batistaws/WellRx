package batista.WellRx.clinica.service;

import batista.WellRx.clinica.database.model.Recepcionista;
import batista.WellRx.clinica.database.repository.RecepcionistaRepository;
import batista.WellRx.clinica.dto.CadastroRecepcionistaDto;
import batista.WellRx.shared.database.model.PerfilEnum;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.database.repository.PerfilRepository;
import batista.WellRx.shared.database.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecepcionistaService {

    private final RecepcionistaRepository recepcionistaRepository;
    private final PasswordEncoder passwordEncoder;
    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public RecepcionistaService(RecepcionistaRepository recepcionistaRepository, PasswordEncoder passwordEncoder, PerfilRepository perfilRepository, UsuarioRepository usuarioRepository) {
        this.recepcionistaRepository = recepcionistaRepository;
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
        usuarioRepository.save(usuario);

        var recepcionista = new Recepcionista(dto, usuario);
        return recepcionistaRepository.save(recepcionista);

    }
}
