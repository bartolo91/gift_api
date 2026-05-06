package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataLoadService {

    private final ChildLoader childBatchLoader;
    private final PresentLoader presentBatchLoader;

    public void saveChildren() {
        childBatchLoader.loadChildren();
    }

    public void savePresents() {
        presentBatchLoader.loadPresents();
    }
}