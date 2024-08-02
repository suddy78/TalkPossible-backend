package com.talkpossible.project.domain.domain;

import com.talkpossible.project.domain.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    private LocalDate run_date;

    private Time total_time;

    private int words_per_min;

    private Boolean is_stutter;

}
