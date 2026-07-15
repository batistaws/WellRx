package batista.WellRx.clinica.controller;

import batista.WellRx.clinica.dto.AgendamentoConsultaDto;
import batista.WellRx.clinica.dto.CancelamentoConsultaDto;
import batista.WellRx.clinica.dto.DetalharConsultaDto;
import batista.WellRx.clinica.dto.ListarConsultaDto;
import batista.WellRx.clinica.service.ConsultaService;
import batista.WellRx.shared.database.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('PACIENTE','RECEPCIONISTA')")
    @GetMapping("/listar")
    public ResponseEntity<Page<ListarConsultaDto>> listarConsultas(@PageableDefault(size = 20) Pageable paginacao, @AuthenticationPrincipal Usuario logado) {
        var pagina = service.listar(paginacao,logado);
        return ResponseEntity.ok(pagina);
    }

    @PreAuthorize("hasAnyRole('PACIENTE','RECEPCIONISTA')")
    @PostMapping("/agendar")
    public ResponseEntity<ListarConsultaDto> agendarConsulta(@RequestBody @Valid AgendamentoConsultaDto dto, UriComponentsBuilder uriBuilder, @AuthenticationPrincipal Usuario logado) {

        var consulta = service.agendar(dto,logado);
        var uri = uriBuilder.path("/consultas/{id}").buildAndExpand(consulta.getId()).toUri();
        return ResponseEntity.ok(new ListarConsultaDto(consulta));
    }

    @PreAuthorize("hasAnyRole('PACIENTE','RECEPCIONISTA')")
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Void> cancelarConsulta(@PathVariable @Valid CancelamentoConsultaDto dto, @AuthenticationPrincipal Usuario logado) {
        service.cancelar(dto, logado);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('PACIENTE','RECEPCIONISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<DetalharConsultaDto> detalharConsulta(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        var consulta = service.detalhar(id, logado);
        return ResponseEntity.ok(new DetalharConsultaDto(consulta));
    }


}
