package com.sendika.bookstore.service;

import com.sendika.bookstore.web.dto.ThreadDemoResult;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;

/**
 * Service that demonstrates different ways of working with threads in Java.
 * <p>
 * The goal of this service is to give hands-on examples that highlight the key
 * building blocks of the platform: creating raw threads, using an
 * {@link ExecutorService} and composing asynchronous logic via
 * {@link CompletableFuture}.
 */
@Service
public class ThreadDemoService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    /**
     * Runs all thread demos and aggregates their explanations into a response DTO.
     */
    public ThreadDemoResult runDemo() {
        List<String> basicThreadMessages = runBasicThreadExample();
        List<String> executorMessages = runExecutorServiceExample();
        List<String> completableMessages = runCompletableFutureExample();

        String explanation = "Bu örnek, aynı işi farklı yollarla nasıl paralel çalıştırabileceğimizi sade bir dille anlatır." 
            + "\n1. Thread sınıfı ile kendi işçimizi oluşturup başlatıyoruz." 
            + "\n2. ExecutorService ile hazır bir havuza işleri bırakıyoruz." 
            + "\n3. CompletableFuture ile işleri zincirleyip sonucu bekliyoruz.";

        return new ThreadDemoResult(basicThreadMessages, executorMessages, completableMessages, explanation);
    }

    /**
     * Demonstrates creating and joining raw {@link Thread} instances.
     */
    private List<String> runBasicThreadExample() {
        List<String> messages = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        messages.add("Önce üç tane Thread oluşturuyoruz, hepsi kısa bir işi yapıyor.");

        for (int i = 1; i <= 3; i++) {
            int workerId = i;
            Thread thread = new Thread(() -> {
                Instant start = Instant.now();
                simulateWorkload();
                Duration duration = Duration.between(start, Instant.now());
                synchronized (messages) {
                    messages.add("Thread " + workerId + " işini bitirdi. (" + duration.toMillis() + " ms sürdü)");
                }
            }, "basic-thread-" + workerId);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                messages.add(thread.getName() + " beklerken kesildi.");
            }
        }

        return messages;
    }

    /**
     * Demonstrates delegating work to an {@link ExecutorService} with {@link Future}s.
     */
    private List<String> runExecutorServiceExample() {
        List<String> messages = new ArrayList<>();
        List<Future<String>> futures = new ArrayList<>();

        messages.add("Şimdi aynı işleri ExecutorService havuzuna veriyoruz.");

        for (int i = 1; i <= 3; i++) {
            int workerId = i;
            futures.add(executorService.submit(() -> {
                Instant start = Instant.now();
                simulateWorkload();
                Duration duration = Duration.between(start, Instant.now());
                return "Havuzdaki görev " + workerId + " bitti. (" + duration.toMillis() + " ms sürdü)";
            }));
        }

        for (Future<String> future : futures) {
            try {
                messages.add(future.get());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                messages.add("Havuzdaki görev beklenirken kesinti oldu.");
            } catch (ExecutionException ex) {
                messages.add("Havuzdaki görev hata verdi: " + ex.getCause());
            }
        }

        return messages;
    }

    /**
     * Demonstrates composing asynchronous logic with {@link CompletableFuture}.
     */
    private List<String> runCompletableFutureExample() {
        List<String> messages = new ArrayList<>();

        messages.add("Son olarak CompletableFuture ile işleri zincirliyoruz.");

        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            int workerId = i;
            futures.add(CompletableFuture.supplyAsync(() -> {
                simulateWorkload();
                return workerId;
            }, executorService).thenApply(result -> "CompletableFuture görevi " + result + " bitti."));
        }

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
            allDone.join();
            futures.forEach(future -> messages.add(future.join()));
        } catch (Exception ex) {
            messages.add("CompletableFuture zincirinde sorun oldu: " + ex.getMessage());
        }

        return messages;
    }

    private void simulateWorkload() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextLong(75L, 150L));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @PreDestroy
    public void shutdownExecutor() {
        executorService.shutdownNow();
    }
}
