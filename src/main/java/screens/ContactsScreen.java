package screens;

import config.AppiumConfig;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;

public class ContactsScreen extends BaseScreen {

    public ContactsScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(xpath="/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/android.widget.TextView\n")
    AndroidElement txtContactList;

    public boolean validatePage() {
        return(textInElementPresent(txtContactList, "Contact list", 5));

    }


}
