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

    // Listar todas
    public Iterable<Citas> listarTodas() {
        return citasRepository.findAll();
    }

    // Guardar cita y enviar correo
    public void guardarCitaYNotificar(Citas cita) {

        Pacientes paciente = cita.getPaciente();

        // Si el paciente es nuevo, lo guardamos
        if (paciente.getId() == null) {
            paciente = pacientesRepository.save(paciente);
            cita.setPaciente(paciente);
        } else {
            // Si existe, lo obtenemos desde la BD
            paciente = pacientesRepository.findById(paciente.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
            cita.setPaciente(paciente);
        }

        // Guardar cita
        citasRepository.save(cita);

        // Enviar correo
        enviarCorreoConfirmacion(cita);
    }

    // Eliminar
    public void eliminar(Long id) {
        citasRepository.deleteById(id);
    }

    // Enviar correo
    private void enviarCorreoConfirmacion(Citas cita) {
        try {
            String destinatario = cita.getPaciente().getCorreo();
            String asunto = "Confirmación de cita médica";
            String cuerpo = "Su cita ha sido registrada para el día: " + cita.getFecha();

            correosService.enviarCorreo(destinatario, asunto, cuerpo);

            System.out.println("Correo enviado correctamente a: " + destinatario);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }
}
