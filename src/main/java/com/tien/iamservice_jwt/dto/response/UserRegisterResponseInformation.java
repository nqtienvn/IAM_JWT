package com.tien.iamservice_jwt.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterResponseInformation {
    Long id;
    String lastName;
    String firstName;
    String email;
    String password;
    String profilePicture;
    String phoneNumber;
}
