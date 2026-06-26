package com.danish.backendcopilot.controller;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.danish.backendcopilot.prompt.SystemPrompts;
import com.danish.backendcopilot.tool.DeveloperTools;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CopilotController {

	private final ChatClient chatClient;
	private final DeveloperTools developerTools;

	public CopilotController(ChatClient chatClient, DeveloperTools developerTools) {
		this.chatClient = chatClient;
		this.developerTools = developerTools;
	}

	@GetMapping("/chat")
	public Flux<String> chat(@RequestParam(value = "message") String message) {
		log.info("processing message: {}", message);
		SystemMessage systemMessage = new SystemMessage(SystemPrompts.BACKEND_COPILOT);
		UserMessage userMessage = new UserMessage(message);

		Flux<String> response = chatClient.prompt(new Prompt(List.of(systemMessage, userMessage))).tools(developerTools).stream()
				.content();
		return response;
	}

	/**
	   curl -N --get  --data-urlencode "message=What is the current weather in hyderabad" \
	   "http://localhost:8080/api/v1/chat"
	 */
}
