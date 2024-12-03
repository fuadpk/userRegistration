package com.mykare.task.service;

import com.mykare.task.dto.UserDTO;
import com.mykare.task.enums.Gender;
import com.mykare.task.exception.UserAlreadyExists;
import com.mykare.task.exception.UserNotFoundException;
import com.mykare.task.model.User;
import com.mykare.task.repository.UserRepository;
import com.mykare.task.util.DTOMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setUuid(UUID.randomUUID());
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setGender(Gender.MALE);
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("password123");

        user = new User();
        user.setUuid(userDTO.getUuid());
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setGender(Gender.MALE);
        user.setEmail("john.doe@example.com");
        user.setPassword("encodedPassword123");
    }

    @Test
    void testCreateUserSuccessfully() {
        try (MockedStatic<DTOMapperUtil> mockedStatic = mockStatic(DTOMapperUtil.class)) {
            when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword123");
            mockedStatic.when(() -> DTOMapperUtil.mapUserDTOToUser(userDTO)).thenReturn(user);
            when(userRepository.save(any(User.class))).thenReturn(user);
            mockedStatic.when(() -> DTOMapperUtil.mapUserToUserDTO(user)).thenReturn(userDTO);
            UserDTO createdUser = userService.create(userDTO);
            assertEquals(userDTO.getEmail(), createdUser.getEmail());
            verify(userRepository, times(1)).save(any(User.class));
        }
    }


    @Test
    void testCreateUserAlreadyExists() {
        try (MockedStatic<DTOMapperUtil> mockedStatic = mockStatic(DTOMapperUtil.class)) {
            when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
            UserAlreadyExists exception = assertThrows(UserAlreadyExists.class, () -> userService.create(userDTO));
            assertEquals("User already exists with email:john.doe@example.com", exception.getMessage());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void testReadUserSuccessfully() {
        UUID uuid = userDTO.getUuid();
        try (MockedStatic<DTOMapperUtil> mockedStatic = mockStatic(DTOMapperUtil.class)) {
            when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
            mockedStatic.when(() -> DTOMapperUtil.mapUserToUserDTO(user)).thenReturn(userDTO);
            UserDTO foundUser = userService.read(uuid);
            assertEquals(userDTO.getEmail(), foundUser.getEmail());
            verify(userRepository, times(1)).findByUuid(uuid);
        }
    }

    @Test
    void testReadUserNotFound() {
        UUID uuid = UUID.randomUUID();
        try (MockedStatic<DTOMapperUtil> mockedStatic = mockStatic(DTOMapperUtil.class)) {
            when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());
            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.read(uuid));
            assertEquals("User not found with UUID:" + uuid, exception.getMessage());
        }
    }

    @Test
    void testUpdateUserSuccessfully() {
        UUID uuid = userDTO.getUuid();
        try (MockedStatic<DTOMapperUtil> mockedStatic = mockStatic(DTOMapperUtil.class)) {
            when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
            when(DTOMapperUtil.updateUserFromDTO(user, userDTO)).thenReturn(user);
            when(userRepository.save(user)).thenReturn(user);
            mockedStatic.when(() -> DTOMapperUtil.mapUserToUserDTO(user)).thenReturn(userDTO);
            UserDTO updatedUser = userService.update(uuid, userDTO);
            assertEquals(userDTO.getEmail(), updatedUser.getEmail());
            verify(userRepository, times(1)).save(user);
        }
    }

    @Test
    void testUpdateUserNotFound() {
        UUID uuid = UUID.randomUUID();
        try (MockedStatic<DTOMapperUtil> mockedStatic = mockStatic(DTOMapperUtil.class)) {
            when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());
            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.update(uuid, userDTO));
            assertEquals("User not found with UUID:" + uuid, exception.getMessage());
        }
    }

    @Test
    void testDeleteUserSuccessfully() {
        UUID uuid = userDTO.getUuid();
        try (MockedStatic<DTOMapperUtil> mockedStatic = mockStatic(DTOMapperUtil.class)) {
            when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
            userService.delete(uuid);
            verify(userRepository, times(1)).delete(user);
        }
    }

    @Test
    void testDeleteUserNotFound() {
        UUID uuid = UUID.randomUUID();
        try (MockedStatic<DTOMapperUtil> mockedStatic = mockStatic(DTOMapperUtil.class)) {
            when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());
            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.delete(uuid));
            assertEquals("User not found with UUID:" + uuid, exception.getMessage());
        }
    }

    @Test
    void testGetAllUsersSuccessfully() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<User> userPage = mock(Page.class);
        try (MockedStatic<DTOMapperUtil> mockedStatic = mockStatic(DTOMapperUtil.class)) {
            when(userRepository.findAll(pageRequest)).thenReturn(userPage);
            when(userPage.getContent()).thenReturn(List.of(user));
            mockedStatic.when(() -> DTOMapperUtil.mapUserToUserDTO(user)).thenReturn(userDTO);
            var users = userService.getAll(0, 10);
            assertNotNull(users);
            assertEquals(1, users.size());
            verify(userRepository, times(1)).findAll(pageRequest);
        }
    }

    @Test
    void testFindUserByEmailSuccessfully() {
        String email = "john.doe@example.com";
        try (MockedStatic<DTOMapperUtil> mockedStatic = mockStatic(DTOMapperUtil.class)) {
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
            mockedStatic.when(() -> DTOMapperUtil.mapUserToUserDTO(user)).thenReturn(userDTO);
            UserDTO foundUser = userService.findUserByEmail(email);
            assertEquals(userDTO.getEmail(), foundUser.getEmail());
            verify(userRepository, times(1)).findByEmail(email);
        }
    }

    @Test
    void testFindUserByEmailNotFound() {
        String email = "john.doe@example.com";
        try (MockedStatic<DTOMapperUtil> mockedStatic = mockStatic(DTOMapperUtil.class)) {
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findUserByEmail(email));
            assertEquals("User not found with email:john.doe@example.com", exception.getMessage());
        }
    }
}
