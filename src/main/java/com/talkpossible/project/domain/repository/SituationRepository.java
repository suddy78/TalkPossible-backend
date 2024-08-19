package com.talkpossible.project.domain.repository;

import com.talkpossible.project.domain.domain.Situation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SituationRepository extends JpaRepository<Situation, Long> {
}
