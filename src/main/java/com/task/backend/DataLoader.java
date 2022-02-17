package com.task.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class DataLoader implements CommandLineRunner {


    @Override
    public void run(String... args) throws RuntimeException {
        loadData();
    }

    private void loadData() {
    }
}
