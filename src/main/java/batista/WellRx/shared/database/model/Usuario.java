package batista.WellRx.shared.database.model;

import batista.WellRx.clinica.dto.CadastroMedicoDto;
import batista.WellRx.clinica.dto.CadastroPacienteDto;
import batista.WellRx.clinica.dto.CadastroRecepcionistaDto;
import batista.WellRx.infra.exeption.RegraNegocioException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;
    private String cpf;
    private String email;
    private String senha;
    private Boolean ativo;
    private LocalDateTime expiracaoToken;
    private String token;
    private Boolean verificado;

    @ManyToMany(fetch = FetchType.EAGER) //quando for carregar o usuario, ele ja vai carregar os perfis junto, pq o spring security precisa disso pra fazer a autenticação
    @JoinTable(name = "usuarios_perfis",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id"))
    private List<Perfil> perfis = new ArrayList<>();

    public Usuario(CadastroPacienteDto dto, String senhaCriptografada, Perfil perfil) {

         this.nomeCompleto = dto.nomeCompleto();
         this.email = dto.email();
         this.senha = senhaCriptografada;
         this.cpf = dto.cpf();
         this.verificado = false;
         this.token = java.util.UUID.randomUUID().toString();
         this.expiracaoToken = LocalDateTime.now().plusMinutes(50);
         this.ativo = false;
         this.perfis.add(perfil);
    }

    public Usuario(CadastroMedicoDto dto, String senhaCriptografada, Perfil perfil) {

        this.nomeCompleto = dto.nomeCompleto();
        this.email = dto.email();
        this.senha = senhaCriptografada;
        this.cpf = dto.cpf();
        this.verificado = false;
        this.token = java.util.UUID.randomUUID().toString();
        this.expiracaoToken = LocalDateTime.now().plusMinutes(50);
        this.ativo = false;
        this.perfis.add(perfil);
    }

    public Usuario(CadastroRecepcionistaDto dto, String senhaCriptografada, Perfil perfil) {

        this.nomeCompleto = dto.nomeCompleto();
        this.email = dto.email();
        this.senha = senhaCriptografada;
        this.cpf = dto.cpf();
        this.verificado = false;
        this.token = java.util.UUID.randomUUID().toString();
        this.expiracaoToken = LocalDateTime.now().plusMinutes(50);
        this.ativo = false;
        this.perfis.add(perfil);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return cpf;
    }

    public void verificar() {
        if (expiracaoToken.isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException("O token de verificação expirou!");
        }

        this.verificado = true;
    }

    public void adicionarPerfil(Optional<Perfil> perfil) {
        perfis.add(perfil.orElseThrow(() -> new RegraNegocioException("Perfil não encontrado!")));
    }

    public void removerPerfil(Optional<Perfil> perfil) {
        perfis.remove(perfil.orElseThrow(() -> new RegraNegocioException("Perfil não encontrado!")));
    }
}
