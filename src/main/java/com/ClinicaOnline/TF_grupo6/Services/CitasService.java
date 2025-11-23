package com.ClinicaOnline.TF_grupo6.Services;

import com.ClinicaOnline.TF_grupo6.Entitys.Citas;
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

    Pacientes p = cita.getPaciente();

    // Si el paciente no tiene ID, se crea uno nuevo
    if (p.getId() == null) {
        pacientesRepository.save(p);
    } else {
        // Si viene con ID (editar o reusar), cargarlo de la BD
        p = pacientesRepository.findById(p.getId())
            .orElseThrow(() ->
                new IllegalArgumentException("Paciente no encontrado")
            );
        cita.setPaciente(p);
    }

    // Guardar la cita
    citasRepository.save(cita);

    // Enviar correo
    enviarCorreoConfirmacion(cita);
}

    // Guardar la cita
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
