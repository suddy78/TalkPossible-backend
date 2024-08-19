package com.talkpossible.project.domain.domain;

import com.talkpossible.project.domain.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    private String voiceFileName;

    private String content;

    public void updateSimulationDetails(String voiceFileName, String content) {
        this.voiceFileName = voiceFileName;
        this.content = content;
    }

    public void updateRunDateAndTotalTime(
            final String runDate, final String totalTime
    ) {
        this.runDate = LocalDate.parse(runDate);
        this.totalTime = Time.valueOf(totalTime);
    }

}
