package com.talkpossible.project.domain.chatGPT.domain;

import com.talkpossible.project.domain.chatGPT.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "sender_id", nullable = false)
    private Patient patient;

    @Column(name = "TEXT", nullable = false)
    private String content;

    private LocalDateTime send_time;

}
