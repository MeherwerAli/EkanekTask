package com.task.backend.service.impl;

import com.task.backend.converter.FileConverter;
import com.task.backend.model.FileModel;
import com.task.backend.model.User;
import com.task.backend.payload.response.FileDTO;
import com.task.backend.repository.FileRepository;
import com.task.backend.repository.UserRepository;
import com.task.backend.security.jwt.JwtUtils;
import com.task.backend.service.FileService;
import com.task.backend.util.AppConstants;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;


    @Override
    public List<FileDTO> findAll() throws NotFoundException {
        try {
            return FileConverter.fileDTOList(
                    new ArrayList<>((Collection<? extends FileModel>) fileRepository.findAll()));
        } catch (Exception ex) {
            logger.error(AppConstants.ERROR_STRING +"%s", ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        }
    }

    @Override
    public String uploadFile(HttpServletRequest request, MultipartFile[] files) throws Exception, IOException {
        String uploadDirectory = System.getProperty("user.dir")+"/uploads";
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwt = null;
//      Getting token from Header
        try {
            if (requestTokenHeader != null) {
                if (requestTokenHeader.startsWith("Bearer "))
                    jwt = requestTokenHeader.substring(7);
                else
                    jwt = requestTokenHeader;
            }
            String userName = jwtUtils.getUserNameFromJwtToken(jwt);
            User user = userRepository.findByUserName(userName).get();
            StringBuilder fileNames = new StringBuilder();
            for (MultipartFile file : files) {
                Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
                FileModel fileModel = new FileModel();
                fileModel.setCreatedOn(new Date());
                fileModel.setName(file.getOriginalFilename());
                fileModel.setUser(user);
                fileModel.setUpdatedOn(new Date());
                fileModel = fileRepository.save(fileModel);
                Files.write(fileNameAndPath, file.getBytes());

                //UploadFile Mechanism
                fileNames.append(file.getOriginalFilename());
                fileNames.append(", ");

                Files.write(fileNameAndPath, file.getBytes());
            }
        }catch (IOException e) {
                e.printStackTrace();
                throw new IOException(e.getMessage());
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
        return AppConstants.UPLOAD_SUCCESSFUL;
    }

    @Override
    public List<FileDTO> findFilesByUser(HttpServletRequest request){
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwt = null;
//      Getting token from Header
        if (requestTokenHeader != null) {
            if (requestTokenHeader.startsWith("Bearer "))
                jwt = requestTokenHeader.substring(7);
            else
                jwt = requestTokenHeader;
        }
        String userName = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUserName(userName).get();

        return FileConverter.fileDTOList(
                new ArrayList<>((Collection<? extends FileModel>) fileRepository.findByUser_Id((int) user.getId())));
    }
}
