package com.talkpossible.project.domain.repository;

import com.talkpossible.project.domain.domain.Simulation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {

    List<Simulation> findAllByPatientId(long patientdId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Simulation s WHERE s.id = :id")
    Optional<Simulation> findByIdWithLock(@Param("id") Long id);

}