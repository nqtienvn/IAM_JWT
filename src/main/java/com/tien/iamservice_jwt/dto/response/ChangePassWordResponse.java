package com.tien.iamservice_jwt.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChangePassWordResponse {
    String unvalidateRefreshToken;
    String message;
}
