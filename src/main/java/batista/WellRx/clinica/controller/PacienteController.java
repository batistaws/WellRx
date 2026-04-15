package batista.WellRx.clinica.controller;


import batista.WellRx.clinica.dto.CadastroPacienteDto;
import batista.WellRx.clinica.dto.ListagemPacienteDto;
import batista.WellRx.clinica.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/WellRx/pacientes")
public class PacienteController {


    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemPacienteDto> cadastrar(@RequestBody @Valid CadastroPacienteDto dto, UriComponentsBuilder componentsBuilder) {
        var paciente = service.cadastrar(dto);
        var uri = componentsBuilder.path("/WellRx/pacientes/{nomeCompleto}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new ListagemPacienteDto(paciente));
    }
}
