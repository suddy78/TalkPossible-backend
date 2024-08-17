package com.talkpossible.project.domain.repository;

import com.talkpossible.project.domain.domain.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {
}
