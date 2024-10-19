package com.talkpossible.project.domain.repository;

import com.talkpossible.project.domain.domain.MotionDetail;
import com.talkpossible.project.domain.domain.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotionDetailRepository extends JpaRepository<MotionDetail, Long> {

    List<MotionDetail> findAllBySimulationIdOrderByTimestampAsc(Long simulationId);

    List<MotionDetail> findAllBySimulation(Simulation simulation);

    long countBySimulationId(long simulationId);

}
