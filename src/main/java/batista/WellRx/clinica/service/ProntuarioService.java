package batista.WellRx.clinica.service;

import batista.WellRx.clinica.database.model.Alergia;
import batista.WellRx.clinica.database.model.Comorbidade;
import batista.WellRx.clinica.database.repository.MedicoRepository;
import batista.WellRx.clinica.database.repository.PacienteRepository;
import batista.WellRx.clinica.database.repository.ProntuarioRepository;
import batista.WellRx.clinica.dto.*;
import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.service.HierarquiaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

    @Service
    public class ProntuarioService {

        private final ProntuarioRepository prontuarioRepository;
        private final MedicoRepository medicoRepository;
        private final PacienteRepository pacienteRepository;
        private final HierarquiaService hierarquiaService;

        public ProntuarioService(ProntuarioRepository prontuarioRepository,
                                 MedicoRepository medicoRepository,
                                 PacienteRepository pacienteRepository,
                                 HierarquiaService hierarquiaService) {
            this.prontuarioRepository = prontuarioRepository;
            this.medicoRepository = medicoRepository;
            this.pacienteRepository = pacienteRepository;
            this.hierarquiaService = hierarquiaService;
        }

        public DetalharProntuarioDto detalharPorPacienteId(Long pacienteId, Usuario logado) {

            var prontuario = prontuarioRepository.findByPacienteId(pacienteId)
                    .orElseThrow(() -> new RegraNegocioException("Prontuário não encontrado"));

            if (hierarquiaService.usuarioTemPermissao(logado,"ROLE_MEDICO")) {
                var medico = medicoRepository.findByUsuarioId(logado.getId()).orElseThrow(() -> new RegraNegocioException("Médico não encontrado"));
                return new DetalharProntuarioDto(prontuario);
            }

            if (hierarquiaService.usuarioTemPermissao(logado,"ROLE_PACIENTE")) {
                var paciente = pacienteRepository.findByUsuarioId(logado.getId()).orElseThrow(() -> new RegraNegocioException("Paciente não encontrado"));
                if (paciente.getId().equals(pacienteId)) {
                    return new DetalharProntuarioDto(prontuario);
                }
                throw new RegraNegocioException("Você não tem permissão para acessar este prontuário");
            }

            throw new RegraNegocioException("Você não tem permissão para listar prontuário");
        }

        @Transactional
        public DetalharProntuarioDto adicionarAlergia(Long pacienteId, CadastroAlergiaDto dto, Usuario logado) {

            boolean podeAdicionar = hierarquiaService.usuarioTemPermissao(logado, "ROLE_MEDICO");

            if (!podeAdicionar) {
                throw new RegraNegocioException("Apenas médico ou admin podem registrar alergias.");
            }

            var prontuario = prontuarioRepository.findByPacienteId(pacienteId)
                    .orElseThrow(() -> new RegraNegocioException("Prontuário não encontrado"));

            new Alergia(prontuario, dto.substancia(), dto.gravidade());

            return new DetalharProntuarioDto(prontuario);
        }

        @Transactional
        public DetalharProntuarioDto adicionarComorbidade(Long pacienteId, CadastroComorbidadeDto dto, Usuario logado) {

            boolean podeAdicionar = hierarquiaService.usuarioTemPermissao(logado, "ROLE_MEDICO");

            if (!podeAdicionar) {
                throw new RegraNegocioException("Apenas médico ou admin podem registrar comorbidades.");
            }

            var prontuario = prontuarioRepository.findByPacienteId(pacienteId)
                    .orElseThrow(() -> new RegraNegocioException("Prontuário não encontrado"));

            new Comorbidade(prontuario, dto.condicao(), dto.dataDiagnostico());

            return new DetalharProntuarioDto(prontuario);
        }

        private void validarAcesso(Long pacienteId, Usuario logado) {

            if (hierarquiaService.usuarioTemPermissao(logado, "ROLE_MEDICO")) {
                return;
            }

            var paciente = pacienteRepository.findByUsuarioId(logado.getId()).orElse(null);
            boolean ehOProprioPaciente = paciente != null && paciente.getId().equals(pacienteId);

            if (!ehOProprioPaciente) {
                throw new RegraNegocioException("Você não tem permissão para acessar este prontuário.");
            }
        }
    }

