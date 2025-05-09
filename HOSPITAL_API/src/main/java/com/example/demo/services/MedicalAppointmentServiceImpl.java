package com.example.demo.services;

import com.example.demo.model.models.DoctorModel;
import com.example.demo.model.models.DoctorOfficeModel;
import com.example.demo.model.models.MedicalAppointmentModel;
import com.example.demo.model.repositories.DoctorOfficeRepository;
import com.example.demo.model.repositories.DoctorRepository;
import com.example.demo.model.repositories.MedicalAppointmentRepository;
import com.example.demo.model.services.MedicalAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicalAppointmentServiceImpl implements MedicalAppointmentService {

     private final MedicalAppointmentRepository medicalAppointmentRepository;
     private final DoctorOfficeRepository doctorOfficeRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public Boolean generateAppointment(Long doctorId, Long doctorOfficeNumber, LocalDateTime appointmentDateTime, String patientName) {
        //check if doctorId is null
        if (doctorId == null) throw new RuntimeException("Doctor id is null");
        //check if doctorOfficeNumber is null
        if (doctorOfficeNumber == null) throw new RuntimeException("Doctor office number is null");

        //get doctor by doctorId
        DoctorModel doctorModel = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        //Long to int
        int officeNumber = doctorOfficeNumber.intValue();
        //get doctor office by doctorOfficeNumber
        DoctorOfficeModel doctorOfficeModel = doctorOfficeRepository.findByOfficeNumber(officeNumber)
                .orElseThrow(() -> new RuntimeException("Doctor office not found"));


        // check if exists doctor office with the same date and time
        Boolean existsAppointmentInDoctorOfficeWithSameDateTime = medicalAppointmentRepository.existsByDoctorOfficeAndAppointmentDateTime(doctorOfficeModel, appointmentDateTime);
        if (existsAppointmentInDoctorOfficeWithSameDateTime) throw new RuntimeException("doctor's office (place) has appointment in the same date and time");

        // check if exists the doctor has appointment in the same date and time
        Boolean existsAppointmentInDoctorWithSameDateTime = medicalAppointmentRepository.existsByDoctorAndAppointmentDateTime(doctorModel, appointmentDateTime);
        if (existsAppointmentInDoctorWithSameDateTime) throw new RuntimeException("Doctor has appointment in the same date and time");

        // check if exists appointment with the patient in the same date and time or less than 2 hours
        long minutes = 60 + 59;
        LocalDateTime twoHoursBefore = appointmentDateTime.minusMinutes(minutes);
        Boolean existsAppointmentWithPatientInSameDateTimeOrLessThanTwoHours = medicalAppointmentRepository.existsByNamePatientAndAppointmentDateTimeBetween(patientName, twoHoursBefore, appointmentDateTime);
        if (existsAppointmentWithPatientInSameDateTimeOrLessThanTwoHours) throw new RuntimeException("Patient has appointment in the same date and time or less than 2 hours");

        //check if the doctor hax maximum 8 appointments in the same day
        LocalDateTime startOfDay = appointmentDateTime.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = appointmentDateTime.withHour(23).withMinute(59).withSecond(59);
        Long countAppointmentsInSameDay = medicalAppointmentRepository.countByDoctorAndAppointmentDateTimeBetween(doctorModel, startOfDay, endOfDay);
        if (countAppointmentsInSameDay >= 8) throw new RuntimeException("Doctor has maximum 8 appointments in the same day");


        //save appointment
        MedicalAppointmentModel medicalAppointmentModel = medicalAppointmentRepository.saveAndFlush(
                new MedicalAppointmentModel(
                        doctorOfficeModel,
                        doctorModel,
                        appointmentDateTime,
                        patientName
                )
        );
        return medicalAppointmentModel.getId() != null;
    }

    @Override
    public Boolean cancelAppointmentDue(Long appointmentId) {


        return true;
    }

    @Override
    public Boolean updateAppointment(Long appointmentId, LocalDateTime newAppointmentDateTime) {
        return null;
    }

    @Override
    public List<MedicalAppointmentModel> getAllAppointmentsFiltersDateTimeAndOfficeAndDoctor(LocalDateTime dateTime, Long doctorOfficeId, Long doctorId) {
        return List.of();
    }
}
