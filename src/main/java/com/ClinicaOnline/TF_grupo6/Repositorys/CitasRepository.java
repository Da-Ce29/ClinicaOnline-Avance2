package com.ClinicaOnline.TF_grupo6.Repositorys;

import com.ClinicaOnline.TF_grupo6.Entitys.Citas;
import com.ClinicaOnline.TF_grupo6.Entitys.Medicos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface CitasRepository extends JpaRepository<Citas, Long> {
    List<Citas> findByMedicoAndFecha(Medicos medico, LocalDate fecha);
}
