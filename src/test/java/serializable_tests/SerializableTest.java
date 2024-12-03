package serializable_tests;

import dto.UserDto;
import dto.UserSer;
import org.testng.annotations.Test;

import java.io.Serializable;

import static dto.UserDto.*;
import static dto.UserSer.*;

public class SerializableTest {

    @Test
    public void serialTest() {

        //new UserDto();
        UserDto user = UserDto.builder()
                .username("Bob")
                .password("Family")
                .build();
        serializableUserDto(user,"user1.ser");


        System.out.println(deserializableUserDto("user1.ser").toString());

    }

    @Test
    public void serialTest2() {


        UserSer userSer = UserSer.builder()
                .username("Bob")
                .password("Family")
                .build();
        anInt = 100;
        serializableUserSer(userSer,"user1.ser");
        anInt = 200;


        System.out.println(deserializableUserSer("user1.ser").toString());
        System.out.println(anInt);

    }

    @Test
    public void serialTest3() {


        UserSer userSer = UserSer.builder()
                .username("Bob")
                .password("Family")
                .build();

        serializableUserSer(userSer,"user1.ser");

        System.out.println(deserializableUserSer("user1.ser").toString());


    }

}
