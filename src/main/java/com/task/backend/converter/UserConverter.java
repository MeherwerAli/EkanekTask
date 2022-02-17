package com.task.backend.converter;

import com.task.backend.model.User;
import com.task.backend.payload.response.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {
    private UserConverter() {
        throw new IllegalStateException("TicketConverter class");
    }

    public static User toDAO(UserDTO userDTO) {
        User userDAO = new User();

        userDAO.setId(userDTO.getId());
        userDAO.setUserName(userDTO.getUsername());
        userDAO.setEmail(userDTO.getEmail());
        userDAO.setFirstName(userDTO.getFirstName());
        userDAO.setLastName(userDTO.getLastName());

        return userDAO;
    }

    public static UserDTO toDTO(User userDAO) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(userDAO.getId());
        userDTO.setUsername(userDAO.getUserName());
        userDTO.setEmail(userDAO.getEmail());
        userDTO.setFirstName(userDAO.getFirstName());
        userDTO.setLastName(userDAO.getLastName());

        return userDTO;
    }

    public static List<User> toDAOList(List<UserDTO> userDTOs) {
        List<User> userDAOs = new ArrayList<>();
        userDTOs.forEach(
                dto -> userDAOs.add(toDAO(dto)
                )
        );
        return userDAOs;
    }

    public static List<UserDTO> toDTOList(List<User> userDAOs) {
        List<UserDTO> userDTOs = new ArrayList<>();
        userDAOs.forEach(
                dao -> userDTOs.add(toDTO(dao)
                )
        );
        return userDTOs;
    }
}
