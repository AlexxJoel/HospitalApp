package com.example.demo.config;

import com.example.demo.model.models.DoctorModel;
import com.example.demo.model.models.DoctorOfficeModel;
import com.example.demo.model.repositories.DoctorOfficeRepository;
import com.example.demo.model.repositories.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
public class InitialDataDB implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final DoctorOfficeRepository doctorOfficeRepository;

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void run(String... args) throws Exception {
        // Save initial data to the database
//        saveDoctor("John", "Doe", "Smith", "Cardiology");
//        saveDoctor("Jane", "Doe", "Johnson", "Neurology");
//        saveDoctor("Jim", "Beam", "Brown", "Pediatrics");
//
//        saveDoctorOffice(101, 1);
//        saveDoctorOffice(102, 1);
//        saveDoctorOffice(201, 2);

    }


    @Transactional
    public void saveDoctor(String name, String surname, String lastname, String specialty) {
        doctorRepository.saveAndFlush(
                new DoctorModel(name, surname, lastname, specialty)
        );
    }

    @Transactional
    public void saveDoctorOffice(int officeNumber, int floorNumber) {
        doctorOfficeRepository.saveAndFlush(
                new DoctorOfficeModel(officeNumber, floorNumber)
        );
    }
}
