package com.example.demo.model.repositories;

import com.example.demo.model.models.DoctorModel;
import com.example.demo.model.models.DoctorOfficeModel;
import com.example.demo.model.models.MedicalAppointmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MedicalAppointmentRepository extends JpaRepository<MedicalAppointmentModel, Long> {


    Boolean existsByDoctorOfficeAndAppointmentDateTime(DoctorOfficeModel doctorOfficeModel, LocalDateTime appointmentDateTime);

    Boolean existsByDoctorAndAppointmentDateTime(DoctorModel doctorModel, LocalDateTime appointmentDateTime);

    Boolean existsByNamePatientAndAppointmentDateTime(String patientName, LocalDateTime appointmentDateTime);

    Boolean existsByNamePatientAndAppointmentDateTimeBetween(String patientName, LocalDateTime twoHoursBefore, LocalDateTime appointmentDateTime);

    Long countByDoctorAndAppointmentDateTimeBetween(DoctorModel doctorModel, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
