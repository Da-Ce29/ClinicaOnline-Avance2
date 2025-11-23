package com.ClinicaOnline.TF_grupo6.Services;

import com.ClinicaOnline.TF_grupo6.Entitys.Citas;
import com.ClinicaOnline.TF_grupo6.Repositorys.CitasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CitasService {

    @Autowired
    private CitasRepository citasRepository;

    @Autowired
    private CorreosService correosService;

    // Listar todas las citas
    public Iterable<Citas> listarTodas() {
        return citasRepository.findAll();
    }

    // Guardar cita normal (sin correo)
    public Citas guardar(Citas cita) {
        return citasRepository.save(cita);
    }

    // Guardar cita y enviar correo
    public void guardarCitaYNotificar(Citas cita) {
        try {
            // Guarda la cita primero
            Citas citaGuardada = citasRepository.save(cita);

            // Validaciones antes de enviar correo
            if (citaGuardada.getPaciente() == null) {
                System.out.println("ERROR: La cita no tiene paciente. No se envió correo.");
                return;
            }

            if (citaGuardada.getPaciente().getCorreo() == null ||
                citaGuardada.getPaciente().getCorreo().isEmpty()) {
                System.out.println("ERROR: El paciente no tiene correo. No se envió correo.");
                return;
            }

            String correoDestino = citaGuardada.getPaciente().getCorreo();
            String asunto = "Confirmación de Cita Médica";
            String cuerpo = "Su cita fue registrada con éxito.\n\n" +
                    "Fecha: " + citaGuardada.getFecha() + "\n" +
                    "Médico: " + citaGuardada.getMedico().getNombre() + "\n" +
                    "Estado: " + citaGuardada.getEstado();

            // Enviar correo
            correosService.enviarCorreo(correoDestino, asunto, cuerpo, null);

            System.out.println("Correo enviado correctamente al paciente: " + correoDestino);

        } catch (Exception e) {
            System.out.println("Error en guardarCitaYNotificar(): " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Eliminar cita
    public void eliminar(Long id) {
        citasRepository.deleteById(id);
    }
}
