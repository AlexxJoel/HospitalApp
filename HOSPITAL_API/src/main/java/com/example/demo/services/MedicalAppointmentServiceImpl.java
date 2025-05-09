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

import java.time.LocalDate;
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
        if (existsAppointmentInDoctorOfficeWithSameDateTime)
            throw new RuntimeException("doctor's office (place) has appointment in the same date and time");

        // check if exists the doctor has appointment in the same date and time
        Boolean existsAppointmentInDoctorWithSameDateTime = medicalAppointmentRepository.existsByDoctorAndAppointmentDateTime(doctorModel, appointmentDateTime);
        if (existsAppointmentInDoctorWithSameDateTime)
            throw new RuntimeException("Doctor has appointment in the same date and time");

        // check if exists appointment with the patient in the same date and time or less than 2 hours
        long minutes = 60 + 59;
        LocalDateTime twoHoursBefore = appointmentDateTime.minusMinutes(minutes);
        Boolean existsAppointmentWithPatientInSameDateTimeOrLessThanTwoHours = medicalAppointmentRepository.existsByNamePatientAndAppointmentDateTimeBetween(patientName, twoHoursBefore, appointmentDateTime);
        if (existsAppointmentWithPatientInSameDateTimeOrLessThanTwoHours)
            throw new RuntimeException("Patient has appointment in the same date and time or less than 2 hours");

        //check if the doctor hax maximum 8 appointments in the same day
        hasDoctorMaximumAppointment(appointmentDateTime, doctorModel);


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

        //check if appointmentId is null
        if (appointmentId == null) throw new RuntimeException("Appointment id is null");
        //get appointment by appointmentId
        MedicalAppointmentModel medicalAppointmentModel = medicalAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));


        LocalDateTime nowDateTime = LocalDateTime.now();
        //check if appointment is in the past
        if (medicalAppointmentModel.getAppointmentDateTime().isBefore(nowDateTime)) {
            throw new RuntimeException("Appointment is in the past, only future appointments can be cancelled");
        }

        //check if appointment is in process
        if (medicalAppointmentModel.getAppointmentDateTime().isEqual(nowDateTime)) {
            throw new RuntimeException("Appointment is in process, only future appointments can be cancelled");
        }

        //the future appointments can be cancelled
        medicalAppointmentRepository.deleteById(appointmentId);
        return true;
    }

    @Override
    public Boolean updateAppointment(Long appointmentId, Long doctorId, Long doctorOfficeNumber, LocalDateTime appointmentDateTime, String patientName) {
        //check if appointmentId is null
        if (appointmentId == null) throw new RuntimeException("Appointment id is null");
        //check if doctorId is null
        if (doctorId == null) throw new RuntimeException("Doctor id is null");
        //check if doctorOfficeNumber is null
        if (doctorOfficeNumber == null) throw new RuntimeException("Doctor office number is null");


        //get appointment by appointmentId
        MedicalAppointmentModel medicalAppointmentModel = medicalAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        //get doctor by doctorId
        DoctorModel doctorModel = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        //Long to int
        int officeNumber = doctorOfficeNumber.intValue();
        //get doctor office by doctorOfficeNumber
        DoctorOfficeModel doctorOfficeModel = doctorOfficeRepository.findByOfficeNumber(officeNumber)
                .orElseThrow(() -> new RuntimeException("Doctor office not found"));


        int minutes = 60 + 59;
        LocalDateTime twoHoursBefore = appointmentDateTime.minusMinutes(minutes);


        Boolean existsAppointmentInDoctorOfficeWithSameDateTime = medicalAppointmentRepository.existsByDoctorOfficeAndAppointmentDateTimeAndIdNot(
                doctorOfficeModel,
                appointmentDateTime,
                appointmentId
        );
        if (existsAppointmentInDoctorOfficeWithSameDateTime)
            throw new RuntimeException("The doctor's office has an appointment at the same date and time.");


        Boolean existsAppointmentInDoctorWithSameDateTime = medicalAppointmentRepository.existsByDoctorAndAppointmentDateTimeAndIdNot(
                doctorModel,
                appointmentDateTime,
                appointmentId
        );
        if (existsAppointmentInDoctorWithSameDateTime)
            throw new RuntimeException("The doctor has an appointment at the same date and time.");

        // check if exists appointment with the patient in the same date and time or less than 2 hours
        Boolean existsAppointmentWithPatientInSameDateTimeOrLessThanTwoHours = medicalAppointmentRepository.existsByNamePatientAndAppointmentDateTimeBetweenAndIdNot(
                patientName,
                twoHoursBefore,
                appointmentDateTime,
                appointmentId
        );
        if (existsAppointmentWithPatientInSameDateTimeOrLessThanTwoHours)
            throw new RuntimeException("The patient has an appointment at the same date and time or less than 2 hours before.");


        //check if the doctor hax maximum 8 appointments in the same day
        hasDoctorMaximumAppointment(appointmentDateTime, doctorModel);
        //update appointment
        medicalAppointmentModel.setDoctorOffice(doctorOfficeModel);
        medicalAppointmentModel.setDoctor(doctorModel);
        medicalAppointmentModel.setAppointmentDateTime(appointmentDateTime);
        medicalAppointmentModel.setNamePatient(patientName);
        medicalAppointmentRepository.saveAndFlush(medicalAppointmentModel);
        return true;
    }


    @Override
    public List<MedicalAppointmentModel> getAllAppointmentsFiltersDateTimeAndOfficeAndDoctor(LocalDate date, Long doctorOfficeNumber, Long doctorId) {
        // if all parameters are null, we return all appointments
        if (date == null && doctorOfficeNumber == null && doctorId == null) {
            return medicalAppointmentRepository.findAll();
        }

        // if date is null, we set startOfDay and endOfDay to null
        LocalDateTime startOfDay = null;
        LocalDateTime endOfDay = null;
        if (date != null) {
            startOfDay = date.atStartOfDay();  // 00:00:00
            endOfDay = date.atTime(23, 59, 59);  // 23:59:59
        }

        // if doctorOfficeNumber is null, we set doctorOfficeModel to null
        DoctorOfficeModel doctorOfficeModel = null;
        if (doctorOfficeNumber != null) {
            int officeNumber = doctorOfficeNumber.intValue();
            doctorOfficeModel = doctorOfficeRepository.findByOfficeNumber(officeNumber)
                    .orElseThrow(() -> new RuntimeException("Doctor office not found"));
        }

        // if doctorId is null, we set doctorModel to null
        DoctorModel doctorModel = null;
        if (doctorId != null) {
            doctorModel = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
        }

        // check if all parameters are null
        if (startOfDay != null && doctorOfficeModel != null && doctorModel != null) {
            return medicalAppointmentRepository.findAllByAppointmentDateTimeBetweenAndDoctorOfficeAndDoctor(startOfDay, endOfDay, doctorOfficeModel, doctorModel);
        } else if (startOfDay != null && doctorOfficeModel != null) {
            return medicalAppointmentRepository.findAllByAppointmentDateTimeBetweenAndDoctorOffice(startOfDay, endOfDay, doctorOfficeModel);
        } else if (startOfDay != null && doctorModel != null) {
            return medicalAppointmentRepository.findAllByAppointmentDateTimeBetweenAndDoctor(startOfDay, endOfDay, doctorModel);
        } else if (doctorOfficeModel != null && doctorModel != null) {
            return medicalAppointmentRepository.findAllByDoctorOfficeAndDoctor(doctorOfficeModel, doctorModel);
        } else if (startOfDay != null) {
            return medicalAppointmentRepository.findAllByAppointmentDateTimeBetween(startOfDay, endOfDay);
        } else if (doctorOfficeModel != null) {
            return medicalAppointmentRepository.findAllByDoctorOffice(doctorOfficeModel);
        } else if (doctorModel != null) {
            return medicalAppointmentRepository.findAllByDoctor(doctorModel);
        }

        // if all parameters are null, we return all appointments
        return medicalAppointmentRepository.findAll();
    }


    private void hasDoctorMaximumAppointment(LocalDateTime appointmentDateTime, DoctorModel doctorModel) {
        LocalDateTime startOfDay = appointmentDateTime.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = appointmentDateTime.withHour(23).withMinute(59).withSecond(59);
        Long countAppointmentsInSameDay = medicalAppointmentRepository.countByDoctorAndAppointmentDateTimeBetween(doctorModel, startOfDay, endOfDay);
        if (countAppointmentsInSameDay >= 8)
            throw new RuntimeException("Doctor has maximum 8 appointments in the same day");
    }
}
