# 🤖 Backend Copilot AI

An AI-powered engineering assistant built with **Spring Boot 4** and **Spring AI 2**, running fully locally via **Ollama**. Ask it backend development questions, get streaming responses, and watch it call real-world tools — like live weather data and system time — automatically when needed.

---

## ✨ Features

- **Conversational AI** — powered by Llama 3.2 (or any Ollama-compatible model) with a focused backend engineering persona
- **Streaming responses** — answers stream token-by-token via reactive `Flux<String>` for a real-time feel
- **Intelligent tool calling** — the model decides when to invoke tools; no hardcoded routing
  - 🌤️ **Live weather** — fetches current conditions for any city via WeatherAPI
  - 🕐 **Current date/time** — returns the locale-aware system time
- **Fully local inference** — no OpenAI API key required; Ollama runs the model on your machine
- **Spring AI abstraction** — clean, portable AI client code decoupled from the underlying model

---

## 🏗️ Architecture

```
Client (curl / browser)
        │  GET /api/v1/chat?message=...
        ▼
CopilotController          ← REST layer, builds Prompt, streams response
        │
ChatClient (Spring AI)     ← Sends prompt + tools to Ollama
        │
  ┌─────┴──────┐
  │  Ollama    │           ← Local LLM inference (llama3.2 by default)
  └─────┬──────┘
        │ tool call (if needed)
        ▼
DeveloperTools             ← @Tool methods registered with the ChatClient
  ├── getCurrentWeather()  ← delegates to WeatherClient → WeatherAPI
  └── getCurrentTime()     ← returns LocalDateTime in the server's timezone
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.7 |
| AI Integration | Spring AI 2.0.0 |
| LLM Runtime | Ollama (llama3.2) |
| Reactive Streams | Project Reactor (`Flux`) |
| HTTP Client | Spring `RestClient` |
| Boilerplate Reduction | Lombok |
| Build Tool | Maven (with Maven Wrapper) |
| External API | WeatherAPI (`api.weatherapi.com`) |

---

## 📁 Project Structure

```
src/main/java/com/danish/backendcopilot/
├── BackendCopilotApplication.java   # Entry point; enables @ConfigurationProperties
├── config/
│   └── SpringAiConfiguration.java  # ChatClient bean wired with ChatModel
├── controller/
│   └── CopilotController.java      # GET /api/v1/chat — streams AI response
├── prompt/
│   └── SystemPrompts.java          # System prompt defining the AI's persona
├── service/
│   ├── WeatherClient.java          # HTTP client for WeatherAPI; uses records for DTOs
│   └── WeatherProperties.java      # @ConfigurationProperties for weather.* config
└── tool/
    └── DeveloperTools.java         # @Tool methods exposed to the LLM
```

---

## ⚙️ Prerequisites

- **Java 17+**
- **Maven** (or use the included `./mvnw` wrapper)
- **[Ollama](https://ollama.com)** installed and running locally
- A free **[WeatherAPI](https://www.weatherapi.com)** key

---

## 🚀 Getting Started

### 1. Start Ollama and pull the model

```bash
ollama serve
ollama pull llama3.2
ollama pull nomic-embed-text   # embedding model (used by Spring AI autoconfigure)
```

### 2. Set your environment variables

```bash
export WEATHER_API_KEY=your_weatherapi_key_here

# Optional overrides (defaults shown)
export OLLAMA_BASE_URL=http://localhost:11434
export OLLAMA_CHAT_MODEL=llama3.2
export OLLAMA_EMBEDDING_MODEL=nomic-embed-text
```

### 3. Build and run

```bash
./mvnw spring-boot:run
```

The application starts on **`http://localhost:8080`**.

---

## 📡 API Usage

### `GET /api/v1/chat`

Sends a message to the AI and streams the response.

| Parameter | Type | Required | Description |
|---|---|---|---|
| `message` | `string` | ✅ | Your prompt or question |

**Example — backend question:**

```bash
curl --get \
  --data-urlencode "message=Explain the difference between optimistic and pessimistic locking in JPA" \
  "http://localhost:8080/api/v1/chat"
```

**Example — triggers the weather tool:**

```bash
curl -N --get \
  --data-urlencode "message=What is the current weather in Hyderabad?" \
  "http://localhost:8080/api/v1/chat"
```

The `-N` flag disables buffering so streamed tokens appear immediately in your terminal.

**Example — triggers the time tool:**

```bash
curl --get \
  --data-urlencode "message=What time is it right now?" \
  "http://localhost:8080/api/v1/chat"
```

---

## 🔧 Configuration Reference

All settings live in `application.properties` and can be overridden with environment variables.

```properties
# Ollama connection
spring.ai.ollama.base-url=${OLLAMA_BASE_URL:http://localhost:11434}
spring.ai.ollama.chat.model=${OLLAMA_CHAT_MODEL:llama3.2}
spring.ai.ollama.embedding.model=${OLLAMA_EMBEDDING_MODEL:nomic-embed-text}

# WeatherAPI
weather.api-key=${WEATHER_API_KEY}
weather.api-base-url=https://api.weatherapi.com
weather.api-path=/v1/current.json
```

---

## 🧑‍💻 AI Persona

The assistant is given this system prompt at the start of every conversation:

> *"You are Backend Copilot AI, an AI-powered engineering assistant specialized in backend development. Provide accurate, practical, and well-structured answers about Java, Spring Boot, distributed systems, databases, messaging systems, APIs, cloud-native development, DevOps fundamentals, and software architecture. Use available tools only when external or real-time information is needed. Prefer concise explanations, code examples where helpful, and industry best practices."*

---

## 🔑 Getting a WeatherAPI Key

1. Sign up for free at [weatherapi.com](https://www.weatherapi.com)
2. Copy your API key from the dashboard
3. Set it as the `WEATHER_API_KEY` environment variable before starting the app

