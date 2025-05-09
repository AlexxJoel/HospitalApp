package com.example.demo.model.repositories;

import com.example.demo.model.models.MedicalAppointmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalAppointmentRepository extends JpaRepository<MedicalAppointmentModel, Long> {
}
