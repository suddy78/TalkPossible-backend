package com.talkpossible.project.domain.repository;

import com.talkpossible.project.domain.domain.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {

    List<Simulation> findAllByPatientId(long patientdId);
}