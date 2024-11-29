package config;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

public class AppiumConfig {
    
    public static AppiumDriver<AndroidElement> driver;
    public int width = 0;
    public int height = 0;

    static String type_dc;

    public AppiumConfig() {
        type_dc = System.getProperty("type_dc", "Pix2");
    }

    @BeforeMethod(alwaysRun = true)
    public void setup(){
        DesiredCapabilities desiredCapabilitiesPix2 = new DesiredCapabilities();
        desiredCapabilitiesPix2.setCapability("platformName", "Android");
        desiredCapabilitiesPix2.setCapability("deviceName", "Pix2");
        desiredCapabilitiesPix2.setCapability("platformVersion", "8.0");
        desiredCapabilitiesPix2.setCapability("appPackage", "com.sheygam.contactapp");
        desiredCapabilitiesPix2.setCapability("appActivity", ".SplashActivity");
        String urlPix2 = "http://localhost:4723/wd/hub";

        DesiredCapabilities desiredCapabilitiesA35 = new DesiredCapabilities();
        desiredCapabilitiesA35.setCapability("platformName", "Android");
        desiredCapabilitiesA35.setCapability("deviceName", "A35");
        desiredCapabilitiesA35.setCapability("platformVersion", "13");
        desiredCapabilitiesA35.setCapability("appPackage", "com.sheygam.contactapp");
        desiredCapabilitiesA35.setCapability("appActivity", ".SplashActivity");
        String urlA35 = "http://localhost:4723/wd/hub";

        desiredCapabilitiesPix2.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
        try {
            if (type_dc.equals("Pix2")) {
                driver = new AppiumDriver<>(new URL(urlPix2), desiredCapabilitiesPix2);
            }
            else if(type_dc.equals("A35")) {
                driver = new AppiumDriver<>(new URL(urlA35), desiredCapabilitiesA35);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        height = driver.manage().window().getSize().getHeight();
        width = driver.manage().window().getSize().getWidth();
        System.out.println(width + " x " + height);

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        //driver.quit();
    }


}
