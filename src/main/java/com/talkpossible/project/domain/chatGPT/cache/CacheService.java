package com.talkpossible.project.domain.chatGPT.cache;

import com.talkpossible.project.domain.chatGPT.dto.request.ChatMessage;
import com.talkpossible.project.domain.chatGPT.dto.request.Message;
import com.talkpossible.project.domain.chatGPT.dto.request.RequestMemorizeMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final CacheManager cacheManager;

    private ConcurrentHashMap<String, List<Message>> cache = new ConcurrentHashMap<>();

    public List<Message> getValue(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return cache.get(key);
    }

    public String putValue(List<Message> messages) {
        String id = UUID.randomUUID().toString();

        cache.put(id, messages);
        return id;
    }

    public String createCacheId(){
        return UUID.randomUUID().toString();
    }

    public void putValue(String cacheId, List<Message> messages){
        cache.put(cacheId, messages);
    }
}
