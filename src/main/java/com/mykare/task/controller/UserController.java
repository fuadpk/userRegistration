package com.mykare.task.controller;

import com.mykare.task.constants.AppConstants;
import com.mykare.task.dto.UserDTO;
import com.mykare.task.interfaces.generic.GenericCrudController;
import com.mykare.task.interfaces.generic.GenericExtendedCrudController;
import com.mykare.task.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController implements GenericCrudController<UserDTO>, GenericExtendedCrudController<UserDTO> {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO post(@Valid @RequestBody UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @Override
    @GetMapping("/get/{uuid}")
    public UserDTO get(@PathVariable UUID uuid) {
        return userService.read(uuid);
    }

    @Override
    @PutMapping("/update/{uuid}")
    public UserDTO update(@PathVariable UUID uuid, @Valid @RequestBody UserDTO userDTO) {
        return userService.update(uuid, userDTO);
    }

    @Override
    @DeleteMapping("/delete/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID uuid) {
        userService.delete(uuid);
    }

    @Override
    @GetMapping("/getAll")
    public List<UserDTO> getAll(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SIZE) int size)
    {
        return userService.getAll(page, size);
    }
}
