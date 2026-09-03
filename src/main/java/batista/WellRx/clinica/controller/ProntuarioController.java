package batista.WellRx.clinica.controller;

import batista.WellRx.clinica.dto.CadastroAlergiaDto;
import batista.WellRx.clinica.dto.CadastroComorbidadeDto;
import batista.WellRx.clinica.dto.DetalharProntuarioDto;
import batista.WellRx.clinica.service.ProntuarioService;
import batista.WellRx.shared.database.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    private final ProntuarioService service;

    public ProntuarioController(ProntuarioService service) {
        this.service = service;
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<DetalharProntuarioDto> detalharPorPacienteId(@PathVariable Long pacienteId, @AuthenticationPrincipal Usuario logado) {
        var prontuario = service.detalharPorPacienteId(pacienteId, logado);
        return ResponseEntity.ok(prontuario);
    }

    @PostMapping("/paciente/{pacienteId}/alergias")
    public ResponseEntity<DetalharProntuarioDto> adicionarAlergia(@PathVariable Long pacienteId, @RequestBody @Valid CadastroAlergiaDto dto, @AuthenticationPrincipal Usuario logado) {
        var prontuario = service.adicionarAlergia(pacienteId, dto, logado);
        return ResponseEntity.ok(prontuario);
    }

    @PostMapping("/paciente/{pacienteId}/comorbidades")
    public ResponseEntity<DetalharProntuarioDto> adicionarComorbidade(@PathVariable Long pacienteId, @RequestBody @Valid CadastroComorbidadeDto dto, @AuthenticationPrincipal Usuario logado) {
        var prontuario = service.adicionarComorbidade(pacienteId, dto, logado);
        return ResponseEntity.ok(prontuario);
    }
}

