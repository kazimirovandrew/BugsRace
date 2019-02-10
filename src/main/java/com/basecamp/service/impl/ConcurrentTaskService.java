package com.basecamp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConcurrentTaskService {

    private static final int THREAD_COUNT = 5;

    private static final int CONCURRENT_MAP_SIZE = 100;

    private static final int FINISH = 50;

    private Map<String, String> uuidMap = new ConcurrentHashMap<>(CONCURRENT_MAP_SIZE);

    private Map<String, Integer> raceResults = new ConcurrentHashMap<>(THREAD_COUNT);

    private ExecutorService executorService =
            Executors.newFixedThreadPool(THREAD_COUNT);

    public Future<String> handleProductIdByExecutor(String productId) {
        Callable<String> task = () -> {
            try {
                return ConcurrentTaskService.this.updateProductId(productId);
            } catch (InterruptedException e) {
                throw new IllegalStateException("Task interrupted.", e);
            }
        };

        return executorService.submit(task);
    }

    public void handleProductIdByThread(String productId) {
        new Thread(() -> {
            try {
                ConcurrentTaskService.this.updateProductId(productId);
            } catch (InterruptedException e) {
                throw new IllegalStateException("Task interrupted.", e);
            }
        }).run();
    }

    public void stopExecutorService() {
        try {
            log.info("Attempt to shutdown executor.");
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            log.warn("Stop executor interrupted.");
        }
        finally {
            if (!executorService.isTerminated()) {
                log.error("Cancel non-finished tasks.");
            }
            executorService.shutdownNow();

            log.info("Executor shutdown finished.");
        }
    }

    private String updateProductId(String productId) throws InterruptedException {
        String keyId = "key" + productId;

        if (uuidMap.containsKey(keyId)) {
            uuidMap.remove(keyId);
        } else {
            uuidMap.put(keyId, productId);
        }

        TimeUnit.MILLISECONDS.sleep(
                (long) (Math.random() * 100));

        return keyId;
    }

    public Future<String> handleBugByExecutor() {

        Callable<String> task = () -> ConcurrentTaskService.this.findPosition();

        return executorService.submit(task);

    }

    private String findPosition() {
        String bugName = Thread.currentThread().getName();

        if(!raceResults.containsKey(bugName)){
            raceResults.put(bugName, 0);
        }

        int lastNumberOfSteps = raceResults.get(bugName);
        int newNumberOfSteps = lastNumberOfSteps + (new Random().nextInt(10) + 1);

        StringBuilder stringBuilder = new StringBuilder();

        if(newNumberOfSteps < FINISH){
            raceResults.put(bugName, newNumberOfSteps);

            stringBuilder.append(bugName + " |");

            for(int step = 0; step < FINISH; step++){
                if(step == newNumberOfSteps){
                    stringBuilder.append("Ж");
                }
                else{
                    stringBuilder.append("_");
                }
            }

            stringBuilder.append("|");

            return stringBuilder.toString();
        }
        else{
            return bugName + " wins!!!";
        }
    }
}
