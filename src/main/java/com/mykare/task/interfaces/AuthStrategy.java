package com.mykare.task.interfaces;

import com.mykare.task.dto.LoginRequestDTO;

public interface AuthStrategy {
    String signIn(LoginRequestDTO loginRequestDTO);
}
