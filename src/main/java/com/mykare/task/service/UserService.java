package com.mykare.task.service;

import com.mykare.task.constants.ExceptionMessages;
import com.mykare.task.interfaces.generic.GenericCrudService;
import com.mykare.task.dto.UserDTO;
import com.mykare.task.exception.UserNotFoundException;
import com.mykare.task.exception.UserAlreadyExists;
import com.mykare.task.interfaces.generic.GenericExtendedCrudService;
import com.mykare.task.model.User;
import com.mykare.task.repository.UserRepository;
import com.mykare.task.util.DTOMapperUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements GenericCrudService<UserDTO>, GenericExtendedCrudService<UserDTO> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExists(ExceptionMessages.USER_ALREADY_EXISTS_WITH_EMAIL + userDTO.getEmail());
        }
        User user = DTOMapperUtil.mapUserDTOToUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);
        return DTOMapperUtil.mapUserToUserDTO(savedUser);
    }

    @Override
    @Cacheable(value = "users", key = "#uuid")
    public UserDTO read(UUID uuid) {
        User user = userRepository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_WITH_UUID + uuid));
        return DTOMapperUtil.mapUserToUserDTO(user);
    }

    @Override
    @CachePut(value = "users", key = "#uuid")
    public UserDTO update(UUID uuid, UserDTO userDTO) {
        User existingUser = userRepository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_WITH_UUID + uuid));
        User modifiedUser = DTOMapperUtil.updateUserFromDTO(existingUser, userDTO);
        userRepository.save(modifiedUser);
        return DTOMapperUtil.mapUserToUserDTO(modifiedUser);
    }

    @Override
    @CacheEvict(value = "users", key = "#uuid")
    public void delete(UUID uuid) {
        User user = userRepository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_WITH_UUID + uuid));
        userRepository.delete(user);
    }

    @Override
    public List<UserDTO> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageRequest);
        return userPage.getContent().stream()
                .map(DTOMapperUtil::mapUserToUserDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "users", key = "#email")
    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_WITH_EMAIL + email));
        return DTOMapperUtil.mapUserToUserDTO(user);
    }
}
