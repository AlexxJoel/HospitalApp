package com.example.demo.model.services;

import com.example.demo.model.models.MedicalAppointmentModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MedicalAppointmentService {
    Boolean generateAppointment(Long doctorId, Long doctorOfficeNumber, LocalDateTime appointmentDateTime, String patientName);
    Boolean cancelAppointmentDue(Long appointmentId);
    Boolean updateAppointment(Long appointmentId, Long doctorId, Long doctorOfficeNumber, LocalDateTime appointmentDateTime, String patientName);
    List<MedicalAppointmentModel> getAllAppointmentsFiltersDateTimeAndOfficeAndDoctor(
            LocalDate date,
            Long doctorOfficeId,
            Long doctorId
    );
}
