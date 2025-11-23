package com.ClinicaOnline.TF_grupo6.Controllers;

import com.ClinicaOnline.TF_grupo6.Entitys.Pacientes;
import com.ClinicaOnline.TF_grupo6.Entitys.Citas;
import com.ClinicaOnline.TF_grupo6.Entitys.Medicos;
import com.ClinicaOnline.TF_grupo6.Repositorys.MedicosRepository;
import com.ClinicaOnline.TF_grupo6.Services.CitasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/citas")
public class CitasController {

    @Autowired
    private CitasService citasService;

    @Autowired
    private MedicosRepository medicosRepository;

    // LISTAR TODAS LAS CITAS
    @GetMapping
    public String listarCitas(Model model) {
        model.addAttribute("citas", citasService.listarTodas());
        return "lista_citas";
    }

    // FORMULARIO NUEVA CITA
    @GetMapping("/nueva")
    public String mostrarFormularioNuevaCita(Model model) {
        Citas nuevaCita = new Citas();
        nuevaCita.setPaciente(new Pacientes());
        model.addAttribute("cita", nuevaCita);
        model.addAttribute("medicos", medicosRepository.findAll());
        return "nueva_cita";
    }

    @GetMapping("/guardar")
    public String redirigirDesdeGet() {
        return "redirect:/citas/nueva";
    }

    // GUARDAR CITA
    @PostMapping("/guardar")
    public String guardarCita(
            @ModelAttribute("cita") Citas cita,
            @RequestParam("medico") Long medicoId
    ) {
        try {
            // Asignar médico
            Medicos medico = medicosRepository.findById(medicoId)
                    .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));
            cita.setMedico(medico);

            // Validación mínima
            if (cita.getPaciente().getCorreo() == null ||
                cita.getPaciente().getCorreo().isEmpty()) {
                return "redirect:/citas?nopaciente";
            }

            // Guardar y notificar
            citasService.guardarCitaYNotificar(cita);

            return "redirect:/citas?exito";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/citas?error";
        }
    }

    // ELIMINAR CITA
    @GetMapping("/eliminar/{id}")
    public String eliminarCita(@PathVariable Long id) {
        citasService.eliminar(id);
        return "redirect:/citas";
    }
}

