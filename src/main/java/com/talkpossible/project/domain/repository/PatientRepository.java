package com.talkpossible.project.domain.repository;

import com.talkpossible.project.domain.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
