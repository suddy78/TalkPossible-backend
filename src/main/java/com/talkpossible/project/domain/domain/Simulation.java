package com.talkpossible.project.domain.domain;

import com.talkpossible.project.domain.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Simulation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "simulation_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "situation_id", nullable = false)
    private Situation situation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private LocalDate runDate;

    private Time totalTime;

    private int wordsPerMin;

    private Boolean isStutter;

    public void updateRunDateAndTotalTime(
            final String runDate, final String totalTime
    ) {
        this.runDate = LocalDate.parse(runDate);
        this.totalTime = Time.valueOf(totalTime);
    }

}
