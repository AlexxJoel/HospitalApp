package com.example.demo.model.services;

import com.example.demo.model.models.MedicalAppointmentModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalAppointmentService {
    Boolean generateAppointment(Long doctorId, Long doctorOfficeNumber, LocalDateTime appointmentDateTime, String patientName);
    Boolean cancelAppointmentDue(Long appointmentId);
    Boolean updateAppointment(Long appointmentId, LocalDateTime newAppointmentDateTime);
    List<MedicalAppointmentModel> getAllAppointmentsFiltersDateTimeAndOfficeAndDoctor(
            LocalDateTime dateTime,
            Long doctorOfficeId,
            Long doctorId
    );
}
