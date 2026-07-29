package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.SinaisVitais;

public record DadosSinaisVitaisDto(
        Double peso,
        Double altura,
        String pressaoArterial,
        Double temperatura,
        Integer frequenciaCardiaca) {

    public DadosSinaisVitaisDto(SinaisVitais sinaisVitais) {
        this(sinaisVitais.getPeso(),
                sinaisVitais.getAltura(),
                sinaisVitais.getPressaoArterial(),
                sinaisVitais.getTemperatura(),
                sinaisVitais.getFrequenciaCardiaca());
    }
}
