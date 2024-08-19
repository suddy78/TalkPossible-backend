package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.dto.situation.response.SituationListResponse;
import com.talkpossible.project.domain.repository.SituationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SituationService {

    private final SituationRepository situationRepository;

    public SituationListResponse getSituationList() {
        return SituationListResponse.of(situationRepository.findAll());
    }
}
