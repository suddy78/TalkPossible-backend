package com.talkpossible.project.domain.repository;

import com.talkpossible.project.domain.domain.MotionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotionDetailRepository extends JpaRepository<MotionDetail, Long> {

    List<MotionDetail> findAllBySimulationId(long simulationId);
}
