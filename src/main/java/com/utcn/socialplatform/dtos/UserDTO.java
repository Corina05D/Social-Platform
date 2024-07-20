package com.utcn.socialplatform.dtos;

import com.utcn.socialplatform.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
}
