package com.talkpossible.project.domain.controller;

import com.talkpossible.project.domain.dto.stutter.response.StutterDetailResponse;
import com.talkpossible.project.domain.service.StutterDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StutterDetailController {

    private final StutterDetailService stutterDetailService;

    @GetMapping("/simulations/{simulationId}/stutter")
    public ResponseEntity<Map<String, List<StutterDetailResponse>>> getStutterDetails(
            @PathVariable("simulationId") Long simulationId
    ) {
        // 서비스에서 StutterDetail 정보 조회
        List<StutterDetailResponse> stutterDetails = stutterDetailService.getStutterDetails(simulationId);

        // 응답 데이터 구성
        Map<String, List<StutterDetailResponse>> response = new HashMap<>();
        response.put("stutterList", stutterDetails);

        return ResponseEntity.ok(response);
    }
}
