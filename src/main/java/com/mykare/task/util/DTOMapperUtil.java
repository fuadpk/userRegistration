package com.mykare.task.util;

import com.mykare.task.dto.UserDTO;
import com.mykare.task.model.User;
import org.apache.commons.lang3.StringUtils;

public class DTOMapperUtil {

    private DTOMapperUtil() {
    }

    public static UserDTO mapUserToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUuid(user.getUuid());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setGender(user.getGender());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }

    public static User mapUserDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setGender(userDTO.getGender());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    public static User updateUserFromDTO(User existingUser, UserDTO userDTO) {
        if (existingUser == null || userDTO == null) {
            return existingUser;
        }
        if (StringUtils.isNotBlank(userDTO.getFirstName())) {
            existingUser.setFirstName(userDTO.getFirstName());
        }
        if (StringUtils.isNotBlank(userDTO.getLastName())) {
            existingUser.setLastName(userDTO.getLastName());
        }
        if (StringUtils.isNotBlank(userDTO.getGender().toString())) {
            existingUser.setGender(userDTO.getGender());
        }
        if (StringUtils.isNotBlank(userDTO.getEmail())) {
            existingUser.setEmail(userDTO.getEmail());
        }
        return existingUser;
    }
}
