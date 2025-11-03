package com.sendika.bookstore.web;

import com.sendika.bookstore.service.ThreadDemoService;
import com.sendika.bookstore.web.dto.ThreadDemoResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes the thread demo so it can be exercised from the browser or Postman.
 */
@RestController
@RequestMapping("/api/thread-demo")
public class ThreadDemoController {

    private final ThreadDemoService threadDemoService;

    public ThreadDemoController(ThreadDemoService threadDemoService) {
        this.threadDemoService = threadDemoService;
    }

    @GetMapping
    public ResponseEntity<ThreadDemoResult> runDemo() {
        return ResponseEntity.ok(threadDemoService.runDemo());
    }
}
