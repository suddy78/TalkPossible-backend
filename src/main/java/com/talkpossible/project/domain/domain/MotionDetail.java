package com.talkpossible.project.domain.domain;


import com.talkpossible.project.domain.domain.common.BaseTimeEntity;
import com.talkpossible.project.domain.dto.motion.request.UserMotionRequest;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MotionDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "motion_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "simulation_id", nullable = false)
    private Simulation simulation;

    private String motionName;

    private Time timestamp;

    @Builder
    private MotionDetail(
            Simulation simulation, String situationDate,
            String motionName, Time timestamp, String videoUrl
    ) {
        this.simulation = simulation;
        this.motionName = motionName;
        this.timestamp = timestamp;
    }

    public static MotionDetail of(final Simulation simulation,
                                  final UserMotionRequest userMotionRequest,
                                  final UserMotionRequest.Motion motion
    ) {
        return MotionDetail.builder()
                .simulation(simulation)
                .motionName(motion.motionName())
                .timestamp(motion.timestamp())
                .videoUrl(userMotionRequest.videoUrl())
                .build();
    }
}
