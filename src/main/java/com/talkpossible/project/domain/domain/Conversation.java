package com.talkpossible.project.domain.domain;

import com.talkpossible.project.domain.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Conversation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "conversation_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "simulation_id", nullable = false)
    private Simulation simulation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sender_id")
    private Patient patient;

    @Column(name = "TEXT", nullable = false)
    private String content;

    private LocalDateTime sendTime;

    @Builder
    private Conversation(Simulation simulation, Patient patient, String content, LocalDateTime sendTime) {
        this.simulation = simulation;
        this.patient = patient;
        this.content = content;
        this.sendTime = sendTime;
    }

    public static Conversation create(Simulation simulation, Patient patient, String content, LocalDateTime sendTime) {
        return Conversation.builder()
                .simulation(simulation)
                .patient(patient)
                .content(content)
                .sendTime(sendTime)
                .build();
    }
}
