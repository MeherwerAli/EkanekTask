package com.task.backend.controllers;

import com.task.backend.model.FileModel;
import com.task.backend.payload.response.FileDTO;
import com.task.backend.service.FileService;
import javassist.tools.reflect.CannotCreateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    public static String uploadDirectory = System.getProperty("user.dir")+"/uploads";

    @Autowired
    FileService fileService;


    @PostMapping("/uploadFiles")
    public ResponseEntity<String> uploadFiles(HttpServletRequest request, @RequestBody MultipartFile[] files) throws Exception, IOException {


        return ResponseEntity.ok()
                .body(fileService.uploadFile(request, files));
    }

    @GetMapping("/getUserFile")
    public ResponseEntity<List<FileDTO>> getUserFiles(HttpServletRequest request) throws CannotCreateException {

        List<FileDTO> response = fileService.findFilesByUser(request);
        return ResponseEntity.ok()
                .body(response);
    }

}
