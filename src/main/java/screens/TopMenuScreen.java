package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;

public class TopMenuScreen extends BaseScreen{
    public TopMenuScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(xpath = "//*[@text='Logout']")
    AndroidElement menuItemLogout;

    public void clickLogout() {
        menuItemLogout.click();
    }
}
