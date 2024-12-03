package dto;

import lombok.*;

import java.io.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class UserSer implements Serializable {

    @Serial
    private static final long serialVersionUID = 6543457L;

    public static int anInt;

    private String username;
    transient private String password;

    final String email = "name@mail.com";

    public static void serializableUserSer(UserSer userSer, String fileName) {
        try {
            ObjectOutputStream outputStream =
                    new ObjectOutputStream(new FileOutputStream("src/main/resources/serializable_file/" +fileName));
            outputStream.writeObject(userSer);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserSer deserializableUserSer(String fileName) {
        try (ObjectInputStream inputStream =
                     new ObjectInputStream(new FileInputStream ("src/main/resources/serializable_file/" +fileName))){
            return (UserSer) inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

