package com.ClinicaOnline.TF_grupo6.Entitys;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "citas")
public class Citas {

    // dentro de la clase Citas, antes de los getters/setters
    public Citas() {
    this.paciente = new Pacientes(); // evita NullPointer al bind con Thymeleaf
    // opcional: this.medico = new Medicos();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medicos medico;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paciente_id")
    private Pacientes paciente;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    private String estado = "Pendiente";

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Medicos getMedico() { return medico; }
    public void setMedico(Medicos medico) { this.medico = medico; }

    public Pacientes getPaciente() { return paciente; }
    public void setPaciente(Pacientes paciente) { this.paciente = paciente; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

