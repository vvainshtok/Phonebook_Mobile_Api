package dto;

import lombok.*;

import java.io.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class UserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 6543456L;

    private String username;
    private String password;

    public static void serializableUserDto(UserDto userDto, String fileName) {
        try {
            ObjectOutputStream outputStream =
                    new ObjectOutputStream(new FileOutputStream("src/main/resources/serializable_file/" +fileName));
            outputStream.writeObject(userDto);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserDto deserializableUserDto(String fileName) {
        try (ObjectInputStream inputStream =
                     new ObjectInputStream(new FileInputStream ("src/main/resources/serializable_file/" +fileName))){
            return (UserDto) inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
