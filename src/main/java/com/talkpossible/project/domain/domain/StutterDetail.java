package com.talkpossible.project.domain.domain;

import com.talkpossible.project.domain.domain.common.BaseTimeEntity;
import com.talkpossible.project.domain.dto.stutter.response.StutterDetailResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StutterDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "stutter_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "simulation_id", nullable = false)
    private Simulation simulation;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String audioUrl;

    @Column(nullable = false)
    private String words; // 말 더듬은 어절

    @Builder
    private StutterDetail (Simulation simulation,
                           String imageUrl, String audioUrl, String words) {
        this.simulation = simulation;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
        this.words = words;
    }

    public static StutterDetail create(Simulation simulation, StutterDetailResponse detailResponse) {
        return StutterDetail.builder()
                .simulation(simulation)
                .imageUrl(detailResponse.getImageUrl())
                .audioUrl(detailResponse.getAudioUrl())
                .words(detailResponse.getWords())
                .build();
    }

}
