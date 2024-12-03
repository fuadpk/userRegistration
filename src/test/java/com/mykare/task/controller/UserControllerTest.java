package com.mykare.task.controller;

import com.mykare.task.dto.UserDTO;
import com.mykare.task.enums.Gender;
import com.mykare.task.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userDTO = new UserDTO();
        userDTO.setUuid(UUID.randomUUID());
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setGender(Gender.MALE);
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("password123");
    }

    @Test
    void testCreateUserSuccessfully() throws Exception {
        when(userService.create(any(UserDTO.class))).thenReturn(userDTO);
        mockMvc.perform(post("/api/users/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"gender\":\"MALE\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
        verify(userService, times(1)).create(any(UserDTO.class));
    }

    @Test
    void testReadUserSuccessfully() throws Exception {
        UUID uuid = userDTO.getUuid();
        when(userService.read(uuid)).thenReturn(userDTO);
        mockMvc.perform(get("/api/users/get/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
        verify(userService, times(1)).read(uuid);
    }

    @Test
    void testUpdateUserSuccessfully() throws Exception {
        UUID uuid = userDTO.getUuid();
        when(userService.update(eq(uuid), any(UserDTO.class))).thenReturn(userDTO);
        mockMvc.perform(put("/api/users/update/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"gender\":\"MALE\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
        verify(userService, times(1)).update(eq(uuid), any(UserDTO.class));
    }

    @Test
    void testDeleteUserSuccessfully() throws Exception {
        UUID uuid = userDTO.getUuid();
        doNothing().when(userService).delete(uuid);
        mockMvc.perform(delete("/api/users/delete/{uuid}", uuid))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).delete(uuid);
    }

    @Test
    void testGetAllUsersSuccessfully() throws Exception {
        when(userService.getAll(0, 10)).thenReturn(List.of(userDTO));
        mockMvc.perform(get("/api/users/getAll")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
        verify(userService, times(1)).getAll(0, 10);
    }
}
