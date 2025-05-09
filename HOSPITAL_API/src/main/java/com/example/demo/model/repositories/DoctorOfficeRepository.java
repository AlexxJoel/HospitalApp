package com.example.demo.model.repositories;

import com.example.demo.model.models.DoctorOfficeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorOfficeRepository extends JpaRepository<DoctorOfficeModel, Long> {
}
