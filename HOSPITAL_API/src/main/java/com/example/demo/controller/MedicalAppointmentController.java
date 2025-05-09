package com.example.demo.controller;

import com.example.demo.controller.dtos.CreateAppointmentRequest;
import com.example.demo.model.services.MedicalAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class MedicalAppointmentController {
    private final MedicalAppointmentService medicalAppointmentService;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody CreateAppointmentRequest request) {
        try {

            // Validate format of schedule
            LocalDateTime dateTime = validateDate(request);

            Boolean isCreated = medicalAppointmentService.generateAppointment(
                    request.getIdDoctor(),
                    request.getIdDoctorOffice(),
                    dateTime,
                    request.getNamePatient()
            );
            return ResponseEntity.ok().body(isCreated ? "Appointment created successfully" : "Failed to create appointment");
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format, expected dd/MM/yyyy HH:mm");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating appointment: " + e.getMessage());
        }
    }

    @GetMapping("/filters")
    public ResponseEntity<?> getAppointments(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Long doctorOfficeNumber,
            @RequestParam(required = false) Long doctorId
    ) {
        try {
            // Convertir fecha de String a LocalDate
            LocalDate dateParsed = null;
            if (date != null && !date.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                dateParsed = LocalDate.parse(date, formatter);
            }

            return ResponseEntity.ok(
                    medicalAppointmentService.getAllAppointmentsFiltersDateTimeAndOfficeAndDoctor(
                            dateParsed,
                            doctorOfficeNumber,
                            doctorId
                    )
            );
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format, expected yyyy-MM-dd");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching appointments: " + e.getMessage());
        }
    }


    @DeleteMapping
    public ResponseEntity<?> cancelAppointment(@RequestParam Long appointmentId) {
        try {
            Boolean isCancelled = medicalAppointmentService.cancelAppointmentDue(appointmentId);
            return ResponseEntity.ok().body(isCancelled ? "Appointment cancelled successfully" : "Failed to cancel appointment");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error cancelling appointment: " + e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateAppointment(
            @RequestParam Long appointmentId,
            @RequestBody CreateAppointmentRequest request
    ) {
        try {
            // Validate format of schedule

            LocalDateTime dateTime = validateDate(request);

            Boolean isUpdated = medicalAppointmentService.updateAppointment(
                    appointmentId,
                    request.getIdDoctor(),
                    request.getIdDoctorOffice(),
                    dateTime,
                    request.getNamePatient()
            );
            return ResponseEntity.ok().body(isUpdated ? "Appointment updated successfully" : "Failed to update appointment");
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format, expected dd/MM/yyyy HH:mm");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating appointment: " + e.getMessage());
        }
    }

    private LocalDateTime validateDate(@RequestBody CreateAppointmentRequest request) {
        LocalDateTime dateTime;

        String[] parts = request.getSchedule().split(" ");
        String date = parts[0];
        String time = parts[1];
        String[] timeParts = time.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        String[] dateParts = date.split("/");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        dateTime = LocalDateTime.of(year, month, day, hour, minute);

        return dateTime;
    }

}
