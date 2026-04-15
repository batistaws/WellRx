package batista.WellRx.infra.seguranca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfigSecurity {

    private final SecurityFilter securityFilter;

    public ConfigSecurity(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    //Usado para autenticar o login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity



                .csrf(AbstractHttpConfigurer::disable)  //informações enviadas via formulário precisa ter o csrf ativado.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //adicionar a cadeia de filtro do token ates da cadeia de filtro do spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) //
                .build();
    }
    @Bean
    public PasswordEncoder encripitador(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy hierarquiaPerfis() {
        String hierarquia = "ROLE_ADMIN > ROLE_MEDICO\n" +
                "ROLE_ADMIN > ROLE_FARMACEUTICO\n" +
                "ROLE_MEDICO > ROLE_RECEPCIONISTA\n" +
                "ROLE_ADMIN > ROLE_PACIENTE";
        return RoleHierarchyImpl.fromHierarchy(hierarquia);
    }
}
