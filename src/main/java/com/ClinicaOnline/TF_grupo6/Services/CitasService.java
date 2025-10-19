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
}

