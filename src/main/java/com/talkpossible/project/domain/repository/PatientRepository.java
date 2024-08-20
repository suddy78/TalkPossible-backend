package com.talkpossible.project.domain.repository;

import com.talkpossible.project.domain.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findAllBydoctorId(long doctorId);
}
