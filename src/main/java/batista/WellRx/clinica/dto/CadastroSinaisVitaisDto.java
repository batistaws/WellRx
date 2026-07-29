package batista.WellRx.clinica.dto;

import jakarta.validation.constraints.*;

public record CadastroSinaisVitaisDto(

        @NotNull(message = "O peso é obrigatório.")
        @Positive(message = "O peso deve ser um valor maior que zero.")
        @Max(value = 500, message = "O peso não pode ser superior a 500 kg.")
        Double peso,

        @NotNull(message = "A altura é obrigatória.")
        @Positive(message = "A altura deve ser um valor maior que zero.")
        @Max(value = 3, message = "A altura deve ser informada em metros (ex: 1.75) e menor que 3m.")
        Double altura,

        @NotNull(message = "A temperatura é obrigatória.")
        @Min(value = 25, message = "A temperatura corporal mínima aceitável é 30°C.")
        @Max(value = 45, message = "A temperatura corporal máxima aceitável é 45°C.")
        Double temperatura,

        @NotBlank(message = "A pressão arterial é obrigatória.")
        @Pattern(
                regexp = "^\\d{1,3}[xX/]\\d{1,3}$",
                message = "A pressão arterial deve estar no formato '12x8' ou '120/80'.")
        String pressaoArterial,

        @NotNull(message = "A frequência cardíaca é obrigatória.")
        @Min(value = 20, message = "A frequência cardíaca mínima aceitável é 20 bpm.")
        @Max(value = 300, message = "A frequência cardíaca máxima aceitável é 300 bpm.")
        Integer frequenciaCardiaca
) {
}
