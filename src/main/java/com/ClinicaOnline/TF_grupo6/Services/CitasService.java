package com.ClinicaOnline.TF_grupo6.Services;

import com.ClinicaOnline.TF_grupo6.Entitys.Citas;
import com.ClinicaOnline.TF_grupo6.Repositorys.CitasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitasService {

    @Autowired
    private CitasRepository citasRepository;

    @Autowired
    private CorreosService correosService;

    public List<Citas> listarTodas() {
        return citasRepository.findAll();
    }

    public boolean guardar(Citas cita) {
        citasRepository.save(cita);
        return true;
    }

    public Citas buscarPorId(Long id) {
        return citasRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        citasRepository.deleteById(id);
    }

    public void guardarCitaYNotificar(Citas cita) {
        // Guardar la cita en la base de datos
        citasRepository.save(cita);

        // Preparar correo
        String destinatario = cita.getPaciente().getCorreo();
        String asunto = "Confirmación de cita médica";
        String cuerpo = "Hola " + cita.getPaciente().getNombre() + ",<br>"
                + "Tu cita está confirmada para el día: " + cita.getFecha();
        String imagen = "src/main/resources/static/logo_clinica.jpg";

        // Enviar correo
        correosService.enviarCorreo(destinatario, asunto, cuerpo, imagen);
    }
}


