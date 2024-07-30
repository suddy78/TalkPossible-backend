package com.talkpossible.project.domain.chatGPT.dto.chat.request;

import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {

    private String model;
    private List<Message> messages;
    private int n;
    private double temperature;

    public ChatRequest(String model, List<Message> messages) {
        this.model = model;

        this.messages = messages;

        this.n = 1;
        this.temperature = 0.5;
    }
}