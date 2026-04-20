package batista.WellRx.clinica.controller;

import batista.WellRx.clinica.dto.CadastroRecepcionistaDto;
import batista.WellRx.clinica.dto.ListagemRecepcionistaDto;
import batista.WellRx.clinica.service.RecepcionistaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/recepcionistas")
public class RecepcionistaController {

    private final RecepcionistaService service;

    public RecepcionistaController(RecepcionistaService service) {
        this.service = service;
    }


    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemRecepcionistaDto> cadastrar(@RequestBody @Valid CadastroRecepcionistaDto dto, UriComponentsBuilder componentsBuilder) {
        var recepcionista = service.cadastrar(dto);
        var uri = componentsBuilder.path("/recepcionistas/{nomeCompleto}").buildAndExpand(recepcionista.getId()).toUri();
        return ResponseEntity.created(uri).body(new ListagemRecepcionistaDto(recepcionista));
    }
}
