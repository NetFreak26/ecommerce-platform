package com.platform.ecommerce.security.payloads;

import com.platform.ecommerce.users.models.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserInfoResponse {
    private Long userId;
    private String username;
    private String email;
}
