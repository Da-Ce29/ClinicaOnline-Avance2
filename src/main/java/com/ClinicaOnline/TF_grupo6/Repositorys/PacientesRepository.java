package com.ClinicaOnline.TF_grupo6.Repositorys;

import com.ClinicaOnline.TF_grupo6.Entitys.Pacientes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacientesRepository extends JpaRepository<Pacientes, Long> {
}
