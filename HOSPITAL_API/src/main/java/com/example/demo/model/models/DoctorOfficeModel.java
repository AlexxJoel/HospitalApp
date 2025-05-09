package com.example.demo.model.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctor_offices")
public class DoctorOfficeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int officeNumber;
    private int floorNumber;

    public DoctorOfficeModel(int officeNumber, int floorNumber) {
        this.officeNumber = officeNumber;
        this.floorNumber = floorNumber;
    }
}
