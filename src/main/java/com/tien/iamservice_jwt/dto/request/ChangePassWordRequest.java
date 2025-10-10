package com.tien.iamservice_jwt.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePassWordRequest {
    String oldPassword;
    String newPassword;
    String confirmPassword;
}
