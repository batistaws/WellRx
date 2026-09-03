package batista.WellRx.infra.seguranca;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public ConfigSecurity( @Qualifier("handlerExceptionResolver")HandlerExceptionResolver resolver, @Lazy SecurityFilter securityFilter) {
        this.resolver = resolver;
        this.securityFilter = securityFilter;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(
                        req -> {

                            req.requestMatchers("/usuarios/login", "/usuarios/atualizar-token", "/usuarios/verificar-conta", "/pacientes/cadastrar").permitAll();

                            // ==================== CONSULTAS ====================
                            req.requestMatchers(HttpMethod.GET, "/consultas/listar").hasAnyRole("PACIENTE", "RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.POST, "/consultas/agendar").hasAnyRole("PACIENTE", "RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.PUT, "/consultas/cancelar/*").hasAnyRole("PACIENTE", "RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.GET, "/consultas/*").hasAnyRole("PACIENTE", "RECEPCIONISTA");

                            // ==================== ATENDIMENTOS ====================
                            req.requestMatchers(HttpMethod.POST, "/atendimentos/*/atendimento").hasRole("MEDICO");
                            req.requestMatchers(HttpMethod.GET, "/atendimentos/listar").hasRole("RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.GET, "/atendimentos/listar/*").hasRole("RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.GET, "/atendimentos/*").hasRole("RECEPCIONISTA");

                            // ==================== PRESCRIÇÃO ====================
                            req.requestMatchers(HttpMethod.POST, "/prescricao/cadastrar/*").hasRole("MEDICO");
                            req.requestMatchers(HttpMethod.GET, "/prescricao/detalhar/*").hasAnyRole("MEDICO", "PACIENTE");
                            req.requestMatchers(HttpMethod.GET, "/prescricao/listar").authenticated();
                            req.requestMatchers(HttpMethod.GET, "/prescricao/listar/*").hasRole("RECEPCIONISTA");

                            // ==================== PRONTUÁRIO ====================
                            req.requestMatchers(HttpMethod.POST, "/prontuarios/paciente/*/alergias").hasRole("MEDICO");
                            req.requestMatchers(HttpMethod.POST, "/prontuarios/paciente/*/comorbidades").hasRole("MEDICO");
                            req.requestMatchers(HttpMethod.GET, "/prontuarios/paciente/*").authenticated();

                            // ==================== MÉDICOS ====================
                            req.requestMatchers(HttpMethod.POST, "/medicos/cadastrar").hasRole("ADMIN");
                            req.requestMatchers(HttpMethod.PUT, "/medicos/atualizar").hasRole("MEDICO");
                            req.requestMatchers(HttpMethod.GET, "/medicos").hasRole("RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.GET, "/medicos/*").hasRole("MEDICO");

                            // ==================== PACIENTES ====================
                            req.requestMatchers(HttpMethod.GET, "/pacientes").hasRole("RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.PUT, "/pacientes/atualizar").authenticated();
                            req.requestMatchers(HttpMethod.GET, "/pacientes/*").hasRole("PACIENTE");

                            // ==================== EXAMES ====================
                            req.requestMatchers(HttpMethod.POST, "/exames/atendimentos/*/exames").hasRole("RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.POST, "/exames/*/resultado").hasRole("MEDICO");
                            req.requestMatchers(HttpMethod.PUT, "/exames/*/cancelar").hasRole("RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.GET, "/exames/usuario/*").hasRole("RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.GET, "/exames/*").hasRole("RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.GET, "/exames").hasRole("ADMIN");

                            // ==================== RECEPCIONISTAS ====================
                            req.requestMatchers(HttpMethod.POST, "/recepcionistas/cadastrar").hasRole("ADMIN");
                            req.requestMatchers(HttpMethod.PUT, "/recepcionistas/atualizar").hasRole("RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.GET, "/recepcionistas").hasRole("ADMIN");
                            req.requestMatchers(HttpMethod.GET, "/recepcionistas/*").hasRole("RECEPCIONISTA");

                            // ==================== FARMÁCIA - ESTOQUE ====================
                            req.requestMatchers(HttpMethod.POST, "/estoques/medicamento/*/repor").hasRole("ADMIN");
                            req.requestMatchers(HttpMethod.GET, "/estoques/medicamento/*").hasRole("FARMACEUTICO");

                            // ==================== FARMÁCIA - MEDICAMENTO ====================
                            req.requestMatchers(HttpMethod.POST, "/medicamentos/cadastrar").hasRole("ADMIN");
                            req.requestMatchers(HttpMethod.GET, "/medicamentos/detalhar/*").hasAnyRole("FARMACEUTICO", "MEDICO");
                            req.requestMatchers(HttpMethod.GET, "/medicamentos/listar").hasAnyRole("FARMACEUTICO", "MEDICO");

                            // ==================== FARMÁCIA - DISPENSAÇÃO ====================
                            req.requestMatchers(HttpMethod.POST, "/dispensacao/itens-prescricao/*/dispensacao").hasRole("FARMACEUTICO");
                            req.requestMatchers(HttpMethod.GET, "/dispensacao/detalhar/*").hasRole("FARMACEUTICO");

                            // ==================== FARMÁCIA - FARMACÊUTICO ====================
                            req.requestMatchers(HttpMethod.POST, "/farmaceuticos/cadastrar").hasRole("ADMIN");
                            req.requestMatchers(HttpMethod.GET, "/farmaceuticos/listar").hasRole("RECEPCIONISTA");
                            req.requestMatchers(HttpMethod.GET, "/farmaceuticos/detalhar/*").hasRole("FARMACEUTICO");
                            req.requestMatchers(HttpMethod.PUT, "/farmaceuticos/atualizar").hasRole("FARMACEUTICO");

                            // ==================== QUALQUER OUTRA ROTA ====================
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

    //Usado para autenticar o login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
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
