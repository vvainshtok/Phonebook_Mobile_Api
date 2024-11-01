package helper;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

//@Slf4j
public class PropertiesReader {

    public static String getProperty(String fileName, String key) {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream =
                    new FileInputStream("src/test/resources/"+fileName);
            properties.load(fileInputStream);
            return properties.getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
