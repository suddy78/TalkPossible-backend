package com.talkpossible.project.domain.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Situation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "situation_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "TEXT", nullable = false)
    private String thumbnail_url;

    @Column(nullable = false)
    private String description;
}
