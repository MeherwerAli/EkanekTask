package com.task.backend.service;

import com.task.backend.payload.response.FileDTO;
import javassist.NotFoundException;
import javassist.tools.reflect.CannotCreateException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileDTO> findAll() throws NotFoundException;

    public List<FileDTO> findFilesByUser(HttpServletRequest request);

    String uploadFile(HttpServletRequest request, MultipartFile[] files) throws CannotCreateException, IOException, Exception;
}
