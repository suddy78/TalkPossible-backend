package com.talkpossible.project.domain.repository;

import com.talkpossible.project.domain.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
