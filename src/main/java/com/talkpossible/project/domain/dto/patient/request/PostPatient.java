package com.talkpossible.project.domain.dto.patient.request;

public record PostPatient(
        String name,
        String birthday,
        boolean gender,
        String phoneNum
) {
}
