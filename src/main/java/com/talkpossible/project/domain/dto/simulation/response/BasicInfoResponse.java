package com.talkpossible.project.domain.dto.simulation.response;

import com.talkpossible.project.domain.domain.Patient;
import com.talkpossible.project.domain.domain.Simulation;
import lombok.Builder;
import lombok.Getter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 피드백 조회 DTO (시뮬레이션 정보 & 영상)
 */
@Getter
@Builder
public class BasicInfoResponse {

    private Body body;
    private Header header;

    public static BasicInfoResponse from(Simulation simulation, Patient patient, long motionCount){
        return BasicInfoResponse.builder()
                .header(Header.create(patient))
                .body(Body.create(simulation, patient, motionCount))
                .build();
    }

    @Getter
    @Builder
    public static class Header {
        private Long patientId;

        public static Header create(Patient patient){
            return Header.builder()
                    .patientId(patient.getId())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Body {

        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 M월 d일");
        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("mm:ss");

        private String patientName;
        private String runDate;
        private String situation;
        private String totalTime;
        private Integer wordsPerMin;
        private String videoUrl;
        private int motionCount;

        public static Body create(Simulation simulation, Patient patient, long motionCount){
            return Body.builder()
                    .patientName(patient.getName())
                    .runDate(formatDate(simulation.getRunDate()))
                    .situation(simulation.getSituation().getTitle())
                    .totalTime(formatTime(simulation.getTotalTime()))
                    .wordsPerMin(simulation.getWordsPerMin())
                    .videoUrl(simulation.getVideoUrl())
                    .motionCount((int) motionCount)
                    .build();
        }

        private static String formatDate(LocalDate date) {
            return date != null ? date.format(DATE_FORMATTER) : null;
        }

        private static String formatTime(Time time) {
            return time != null ? time.toLocalTime().format(TIME_FORMATTER) : null;
        }
    }
}
