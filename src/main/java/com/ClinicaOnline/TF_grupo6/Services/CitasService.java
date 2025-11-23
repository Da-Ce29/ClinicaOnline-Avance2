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

    // Listar todas
    public Iterable<Citas> listarTodas() {
        return citasRepository.findAll();
    }

    // Guardar cita y enviar correo
    public void guardarCitaYNotificar(Citas cita) {
        citasRepository.save(cita);
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

            correosService.enviarCorreo(destinatario, asunto, cuerpo, null);

            System.out.println("Correo enviado correctamente a: " + destinatario);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }
}
