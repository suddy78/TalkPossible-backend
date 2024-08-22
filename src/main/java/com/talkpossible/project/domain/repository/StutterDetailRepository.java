package com.talkpossible.project.domain.repository;

import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.domain.StutterDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StutterDetailRepository extends JpaRepository<StutterDetail, Long> {
    List<StutterDetail> findBySimulation(Simulation simulation);
}
