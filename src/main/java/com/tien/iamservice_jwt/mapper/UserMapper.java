package com.tien.iamservice_jwt.mapper;

import com.tien.iamservice_jwt.dto.request.UserRegisterRequest;
import com.tien.iamservice_jwt.dto.response.UserRegisterResponseInformation;
import com.tien.iamservice_jwt.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterRequest userRegisterRequest);
    UserRegisterResponseInformation toUserRegisterResponseInformation(User user);
}
