package com.talkpossible.project.domain.chatGPT.domain;


import com.talkpossible.project.domain.chatGPT.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "mission_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "situation_id", nullable = false)
    private Situation situation;

    @Column(nullable = false)
    private String content;

}
