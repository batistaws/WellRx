package batista.WellRx.infra.seguranca;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ConfigSecurity {


    private final HandlerExceptionResolver resolver;
    private final SecurityFilter securityFilter;

    public ConfigSecurity( @Qualifier("handlerExceptionResolver")HandlerExceptionResolver resolver, SecurityFilter securityFilter) {
        this.resolver = resolver;
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
                .authorizeHttpRequests(
                        req -> {
                            req.requestMatchers("/usuarios/login", "/usuarios/atualizar-token", "/usuarios/verificar-conta", "/pacientes/cadastrar").permitAll();

                            req.anyRequest().authenticated();

                })
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            // Em vez de usar .getMessage() puro, monte o JSON com a sua mensagem customizada
                            String jsonBody = "{\"status\": 403, \"error\": \"Forbidden\", \"message\": \"Acesso negado: você não possui permissão para acessar este recurso.\"}";

                            response.getWriter().write(jsonBody);
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(authException.getMessage());
                        })
                )

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
                "ROLE_RECEPCIONISTA > ROLE_PACIENTE";
        return RoleHierarchyImpl.fromHierarchy(hierarquia);
    }
}
