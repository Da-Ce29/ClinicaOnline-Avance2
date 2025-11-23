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

    public Iterable<Citas> listarTodas() {
        return citasRepository.findAll();
    }

    public void guardarCitaYNotificar(Citas cita) {
        Pacientes p = cita.getPaciente();
        if (p != null) {
            if (p.getId() == null) {
                p = pacientesRepository.save(p); // crea paciente nuevo
            } else {
                p = pacientesRepository.findById(p.getId()).orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
            }
            cita.setPaciente(p);
        }
        citasRepository.save(cita);
        enviarCorreoConfirmacion(cita);
    }

    public void eliminar(Long id) {
        citasRepository.deleteById(id);
    }

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
