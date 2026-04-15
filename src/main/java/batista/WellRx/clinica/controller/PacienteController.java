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

@RestController
@RequestMapping("/WellRx/pacientes")
public class PacienteController {


    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemPacienteDto> cadastrar(@RequestBody @Valid CadastroPacienteDto dto) {

        var paciente = service.cadastrar(dto);
        return ResponseEntity.ok(pacienteCadastrado);
    }
}
