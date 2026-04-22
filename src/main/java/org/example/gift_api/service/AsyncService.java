package org.example.gift_api.service;

import org.example.gift_api.model.entity.ChildView;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    @Async
    public CompletableFuture<Void> processChildAsync(ChildView child) {
        System.out.println("START " + child.getId() + " " + Thread.currentThread().getName());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        System.out.println("END " + child.getId() + " " + Thread.currentThread().getName());

        return CompletableFuture.completedFuture(null);
    }
}
