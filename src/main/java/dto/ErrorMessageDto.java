package dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class ErrorMessageDto {

    private String timestamp; // ($date-time)
    private int status; // ($int32)
    private String error;
    private Object message;
    private String path;
}
