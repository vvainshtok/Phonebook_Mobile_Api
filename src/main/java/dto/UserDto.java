package dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    private String username;
    private String password;

}
