package batista.WellRx.clinica.controller;

import batista.WellRx.clinica.dto.CadastroMedicoDto;
import batista.WellRx.clinica.dto.ListagemMedicoDto;
import batista.WellRx.clinica.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicos")
public class MedicoController {


    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemMedicoDto> cadastrar(@RequestBody @Valid CadastroMedicoDto dto, UriComponentsBuilder componentsBuilder) {
        var medico = service.cadastrar(dto);
        var uri = componentsBuilder.path("/medicos/{nomeCompleto}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new ListagemMedicoDto(medico));
    }
}
