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

    private float wordsPerMin;

    private Boolean isStutter;

    private String videoUrl;

    public void updateRunDateAndTotalTime(
            final String runDate,
            final String totalTime,
            final String videoUrl
    ) {
        this.runDate = LocalDate.parse(runDate);
        this.totalTime = Time.valueOf(totalTime);
        this.videoUrl = videoUrl;
    }

    public void updateWordsPerMin(float wordsPerMin) {
        this.wordsPerMin = wordsPerMin;
    }

}
