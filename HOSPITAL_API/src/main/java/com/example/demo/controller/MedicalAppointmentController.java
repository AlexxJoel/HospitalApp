package com.example.demo.controller;

import com.example.demo.controller.dtos.CreateAppointmentRequest;
import com.example.demo.model.services.MedicalAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class MedicalAppointmentController  {
    private final MedicalAppointmentService medicalAppointmentService;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody CreateAppointmentRequest request) {
        // Validate format of schedule
        LocalDateTime dateTime;
        try {
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid schedule format");
        }

        try{
            Boolean isCreated = medicalAppointmentService.generateAppointment(
                    request.getIdDoctor(),
                    request.getIdDoctorOffice(),
                    dateTime,
                    request.getNamePatient()
            );
            return ResponseEntity.ok().body(isCreated ? "Appointment created successfully" : "Failed to create appointment");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error creating appointment: " + e.getMessage());
        }
    }

}
