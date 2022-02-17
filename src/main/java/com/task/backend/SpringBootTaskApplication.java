package com.task.backend;

import com.task.backend.controllers.FileUploadController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
@ComponentScan({"com.task.backend", "controller"})
public class SpringBootTaskApplication {

    public static void main(String[] args) {
        new File(FileUploadController.uploadDirectory).mkdir();
        SpringApplication.run(SpringBootTaskApplication.class, args);
    }

}
