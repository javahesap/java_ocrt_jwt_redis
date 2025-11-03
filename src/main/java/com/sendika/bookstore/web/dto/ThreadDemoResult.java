package com.sendika.bookstore.web.dto;

import java.util.List;

/**
 * DTO that groups the different thread demo outputs so the controller can return a
 * structured JSON response.
 */
public class ThreadDemoResult {

    private final List<String> basicThreadMessages;
    private final List<String> executorServiceMessages;
    private final List<String> completableFutureMessages;
    private final String explanation;

    public ThreadDemoResult(
        List<String> basicThreadMessages,
        List<String> executorServiceMessages,
        List<String> completableFutureMessages,
        String explanation
    ) {
        this.basicThreadMessages = basicThreadMessages;
        this.executorServiceMessages = executorServiceMessages;
        this.completableFutureMessages = completableFutureMessages;
        this.explanation = explanation;
    }

    public List<String> getBasicThreadMessages() {
        return basicThreadMessages;
    }

    public List<String> getExecutorServiceMessages() {
        return executorServiceMessages;
    }

    public List<String> getCompletableFutureMessages() {
        return completableFutureMessages;
    }

    public String getExplanation() {
        return explanation;
    }
}
