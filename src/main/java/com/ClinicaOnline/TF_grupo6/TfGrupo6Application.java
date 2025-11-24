package com.ClinicaOnline.TF_grupo6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TfGrupo6Application {
    public static void main(String[] args) {
        SpringApplication.run(TfGrupo6Application.class, args);
    }
}
