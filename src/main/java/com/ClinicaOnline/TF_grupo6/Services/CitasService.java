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
            // Validar si el correo ya existe
            if (pacientesRepository.existsByCorreo(p.getCorreo())) {
                // Recuperar el paciente existente
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

    citasRepository.save(cita);
    enviarCorreoConfirmacion(cita);
    }

}
