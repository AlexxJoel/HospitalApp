package com.example.demo.controller.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateAppointmentRequest {
    @Schema(description = "Id del doctor", example = "1")
    private Long idDoctor;
    @Schema(description = "numero de oficina del doctor", example = "101")
    private Long idDoctorOffice;
    @Schema(description = "Formato: dd/MM/yyyy HH:mm", example = "01/01/2023 12:00")
    private String schedule;
    @Schema(description = "Nombre del paciente", example = "Juan Perez")
    private String namePatient;
}