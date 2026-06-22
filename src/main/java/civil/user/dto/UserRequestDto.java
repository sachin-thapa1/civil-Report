package civil.user.entity;

import lombok.Data;

@Data
public class UserRequestDto {

    private String fullName;
    private String email;
    private String password;
}