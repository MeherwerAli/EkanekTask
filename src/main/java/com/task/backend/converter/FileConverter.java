package com.task.backend.converter;

import com.task.backend.model.FileModel;
import com.task.backend.model.User;
import com.task.backend.payload.response.FileDTO;
import com.task.backend.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class FileConverter {

    @Autowired
    private static UserRepository userRepository;
    private FileConverter() {
        throw new IllegalStateException("TicketConverter class");
    }

    public static FileModel toDAO(FileDTO fileDTO) throws NotFoundException {
        FileModel fileDAO = new FileModel();


        fileDAO.setId(fileDTO.getId());
        User user = null;
        if(userRepository.findById(fileDTO.getUserId()).isPresent()){
            user = userRepository.findById(fileDTO.getUserId()).get();
            fileDAO.setUser(user);
        }
        fileDAO.setName(fileDTO.getName());
        fileDAO.setCreatedOn(fileDTO.getCreatedOn());
        fileDAO.setUpdatedOn(fileDTO.getUpdateOn());
        return fileDAO;
    }

    public static FileDTO toDTO(FileModel fileEntity) {
        FileDTO fileDTO = new FileDTO();

        fileDTO.setId(fileDTO.getId());
        fileDTO.setUserId((int) fileEntity.getUser().getId());
        fileDTO.setUserName(fileEntity.getUser().getFirstName()+" "+fileEntity.getUser().getLastName());
        fileDTO.setName(fileDTO.getName());
        fileDTO.setCreatedOn(fileDTO.getCreatedOn());
        fileDTO.setUpdateOn(fileDTO.getUpdateOn());
        return fileDTO;
    }

    public static List<FileModel> fileDAOList(List<FileDTO> fileDTOs) {
        List<FileModel> fileDAOs = new ArrayList<>();
        fileDTOs.forEach(
                dto -> {
                    try {
                        fileDAOs.add(toDAO(dto)
                        );
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );
        return fileDAOs;
    }

    public static List<FileDTO> fileDTOList(List<FileModel> fileDAOs) {
        List<FileDTO> fileDTOs = new ArrayList<>();
        fileDAOs.forEach(
                dao -> fileDTOs.add(toDTO(dao)
                )
        );
        return fileDTOs;
    }
}
