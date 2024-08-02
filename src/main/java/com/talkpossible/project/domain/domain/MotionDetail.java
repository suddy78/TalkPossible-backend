package com.talkpossible.project.domain.domain;


import com.talkpossible.project.domain.domain.common.BaseTimeEntity;
import com.talkpossible.project.domain.dto.motion.request.UserMotionRequest;
import jakarta.persistence.*;
import lombok.*;

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

    private String situationDate;

    private String motionName;

    private String timestamp;

    private String video_url;

    @Builder
    private MotionDetail(
            Simulation simulation, String situationDate,
            String motionName, String timestamp, String video_url
    ) {
        this.simulation = simulation;
        this.situationDate = situationDate;
        this.motionName = motionName;
        this.timestamp = timestamp;
        this.video_url = video_url;
    }

    public static MotionDetail of(Simulation simulation, UserMotionRequest userMotionRequest, UserMotionRequest.Motion motion) {
        return MotionDetail.builder()
                .simulation(simulation)
                .situationDate(userMotionRequest.situationDate())
                .motionName(motion.motionName())
                .timestamp(motion.timestamp())
                .video_url(userMotionRequest.videoUrl())
                .build();
    }
}
