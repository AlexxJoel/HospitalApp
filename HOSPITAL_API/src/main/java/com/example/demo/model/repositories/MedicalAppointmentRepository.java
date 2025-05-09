package com.example.demo.model.repositories;

import com.example.demo.model.models.DoctorModel;
import com.example.demo.model.models.DoctorOfficeModel;
import com.example.demo.model.models.MedicalAppointmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalAppointmentRepository extends JpaRepository<MedicalAppointmentModel, Long> {


    Boolean existsByDoctorOfficeAndAppointmentDateTime(DoctorOfficeModel doctorOfficeModel, LocalDateTime appointmentDateTime);

    Boolean existsByDoctorAndAppointmentDateTime(DoctorModel doctorModel, LocalDateTime appointmentDateTime);


    Boolean existsByNamePatientAndAppointmentDateTimeBetween(String patientName, LocalDateTime twoHoursBefore, LocalDateTime appointmentDateTime);

    Long countByDoctorAndAppointmentDateTimeBetween(DoctorModel doctorModel, LocalDateTime startOfDay, LocalDateTime endOfDay);


    Boolean existsByDoctorOfficeAndAppointmentDateTimeAndIdNot(DoctorOfficeModel doctorOfficeModel, LocalDateTime appointmentDateTime, Long appointmentId);

    Boolean existsByDoctorAndAppointmentDateTimeAndIdNot(DoctorModel doctorModel, LocalDateTime appointmentDateTime, Long appointmentId);

    Boolean existsByNamePatientAndAppointmentDateTimeBetweenAndIdNot(String patientName, LocalDateTime twoHoursBefore, LocalDateTime appointmentDateTime, Long appointmentId);



    List<MedicalAppointmentModel> findAllByDoctorOfficeAndDoctor(DoctorOfficeModel doctorOfficeModel, DoctorModel doctorModel);


    List<MedicalAppointmentModel> findAllByDoctorOffice(DoctorOfficeModel doctorOfficeModel);

    List<MedicalAppointmentModel> findAllByDoctor(DoctorModel doctorModel);

    List<MedicalAppointmentModel> findAllByAppointmentDateTimeBetweenAndDoctorOfficeAndDoctor(LocalDateTime startOfDay, LocalDateTime endOfDay, DoctorOfficeModel doctorOfficeModel, DoctorModel doctorModel);

    List<MedicalAppointmentModel> findAllByAppointmentDateTimeBetweenAndDoctorOffice(LocalDateTime startOfDay, LocalDateTime endOfDay, DoctorOfficeModel doctorOfficeModel);

    List<MedicalAppointmentModel> findAllByAppointmentDateTimeBetweenAndDoctor(LocalDateTime startOfDay, LocalDateTime endOfDay, DoctorModel doctorModel);

    List<MedicalAppointmentModel> findAllByAppointmentDateTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
