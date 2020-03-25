package com.epam.esm.service;

import com.epam.esm.dto.UserEntityDTO;

import java.util.List;

public interface UserService {

    UserEntityDTO getUserById(Long id);

    List<UserEntityDTO> getUsers(int pageNum, int pageSize);

    UserEntityDTO save(UserEntityDTO userEntityDTO);
}
