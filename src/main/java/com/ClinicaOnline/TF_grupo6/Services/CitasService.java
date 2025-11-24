package com.ClinicaOnline.TF_grupo6.Services;

import com.ClinicaOnline.TF_grupo6.Entitys.Citas;
import com.ClinicaOnline.TF_grupo6.Entitys.Pacientes;
import com.ClinicaOnline.TF_grupo6.Repositorys.PacientesRepository;
import com.ClinicaOnline.TF_grupo6.Repositorys.CitasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CitasService {

    @Autowired
    private CitasRepository citasRepository;

    @Autowired
    private CorreosService correosService;

    @Autowired
    private PacientesRepository pacientesRepository;

    // Listar todas las citas
    public Iterable<Citas> listarTodas() {
        return citasRepository.findAll();
    }

    // Guardar cita y notificar
    public void guardarCitaYNotificar(Citas cita) throws IllegalArgumentException {
        Pacientes p = cita.getPaciente();

        if (p != null) {
            if (p.getId() == null) {
                // Validar si el correo ya existe
                if (pacientesRepository.existsByCorreo(p.getCorreo())) {
                    // Recuperar paciente existente
                    p = pacientesRepository.findByCorreo(p.getCorreo());
                } else {
                    // Crear paciente nuevo
                    p = pacientesRepository.save(p);
                }
            } else {
                p = pacientesRepository.findById(p.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
            }
            cita.setPaciente(p);
        }

        // Guardar cita
        citasRepository.save(cita);

        // Enviar correo de confirmación
        enviarCorreoConfirmacion(cita);
    }

    // Eliminar cita
    public void eliminar(Long id) {
        citasRepository.deleteById(id);
    }

    // Enviar correo de confirmación (privado)
    private void enviarCorreoConfirmacion(Citas cita) {
        try {
            String destinatario = cita.getPaciente().getCorreo();
            String asunto = "Confirmación de cita médica";
            String cuerpo = "Su cita ha sido registrada para el día: " + cita.getFecha();
            correosService.enviarCorreo(destinatario, asunto, cuerpo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
