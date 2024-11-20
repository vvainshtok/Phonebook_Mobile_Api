package screens;

import dto.ContactDtoLombok;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class ContactsScreen extends BaseScreen {

    public ContactsScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(xpath="/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/android.widget.TextView")
    AndroidElement headerContactsScreen;

    @FindBy(id = "com.sheygam.contactapp:id/add_contact_btn")
    AndroidElement btnAddNewContact;

    @FindBy(xpath = "/hierarchy/android.widget.Toast")
    AndroidElement popUpMessage;

    @FindBy(xpath = "//android.widget.ImageView[@content-desc='More options']")
    AndroidElement btnMenu;

    @FindBy(xpath = "//*[@resource-id='com.sheygam.contactapp:id/rowName']")
    AndroidElement firstElementContactList;

    @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.TextView[1]")
    AndroidElement element1ContactList;
    @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.TextView[1]")
    AndroidElement element2ContactList;
    @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[3]/android.widget.TextView[1]")
    AndroidElement element3ContactList;
    @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[4]/android.widget.TextView[1]")
    AndroidElement element4ContactList;
    @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[5]/android.widget.TextView[1]")
    AndroidElement element5ContactList;

    @FindBy(id = "android:id/button1")
    AndroidElement popUpBtnYes;

   @FindBy(xpath = "//*[@text='Logout']")
    AndroidElement btnLogout;
    @FindBy(xpath = "//*[@text='Date picker']")
    AndroidElement btnDatePicker;


    public boolean validateHeader() {
        return(textInElementPresent(headerContactsScreen, "Contact list", 5));
    }

    public void clickBtnAddNewContact () {
            clickWait(btnAddNewContact, 5);
    }

    public boolean validatePopUpMessage(String text) {
        return textInElementPresent(popUpMessage, text, 5);
    }

    public void clickBtnMenu() {
        clickWait(btnMenu, 3);
    }

    public void deleteContact() {
        pause(2);
        int xLeftUpCorner = firstElementContactList.getLocation().getX();
        int yLeftUpCorner = firstElementContactList.getLocation().getY();
        int heightElement = firstElementContactList.getSize().height;
        int widthElement = firstElementContactList.getSize().width;
        System.out.println("x --> " + xLeftUpCorner);
        System.out.println("y --> " + yLeftUpCorner);
        System.out.println("height --> " + heightElement);
        System.out.println("width --> " + widthElement);
        TouchAction<?> touchAction = new TouchAction(driver);
        touchAction.longPress(PointOption.point(xLeftUpCorner + widthElement/6, yLeftUpCorner + heightElement/2))
                .moveTo(PointOption.point(xLeftUpCorner + widthElement/6*5, yLeftUpCorner + heightElement/2))
                .release()
                .perform();
    }

    public void clickBtnYes() {
        popUpBtnYes.click();
    }

    public String[] get5ElementsNames() {
        String[] names = new String[5];
        pause(1);
        names[0] = element1ContactList.getText();
        names[1] = element2ContactList.getText();
        names[2] = element3ContactList.getText();
        names[3] = element4ContactList.getText();
        names[4] = element5ContactList.getText();
        return names;
    }

    public void goToEditScreen() {
        pause(2);
        int xLeftUpCorner = firstElementContactList.getLocation().getX();
        int yLeftUpCorner = firstElementContactList.getLocation().getY();
        int heightElement = firstElementContactList.getSize().height;
        int widthElement = firstElementContactList.getSize().width;
        TouchAction<?> touchAction = new TouchAction(driver);
        touchAction.longPress(PointOption.point(xLeftUpCorner + 5*widthElement/6, yLeftUpCorner + heightElement/2))
                .moveTo(PointOption.point(xLeftUpCorner + widthElement/6, yLeftUpCorner + heightElement/2))
                .release()
                .perform();
    }

    public boolean validateUIListContact(ContactDtoLombok contact) {
        String nameFamily = contact.getName() + " " + contact.getLastName();
        List<AndroidElement> listContactsOnScreen = new ArrayList<>();
        System.out.println("List size --> " + listContactsOnScreen.size());
        boolean flagEqualsNameFamily = false;
        boolean flagEndOfList = false;
        listContactsOnScreen = driver.findElements
                (By.xpath("//*[@resource-id='com.sheygam.contactapp:id/rowName']"));

        while(!flagEndOfList) {
            AndroidElement lastElementInListPrev = listContactsOnScreen.get(listContactsOnScreen.size()-1);
            //lastElementInList.getLocation();
            for (AndroidElement e : listContactsOnScreen) {
                System.out.println(e.getText());
                if (e.getText().equals(nameFamily)) {
                    flagEqualsNameFamily = true;
                    flagEndOfList = true;
                    break;
                }
            }
            scrollUp();
            listContactsOnScreen = driver.findElements
                    (By.xpath("//*[@resource-id='com.sheygam.contactapp:id/rowName']"));
            if (lastElementInListPrev.getLocation()
                    .equals( listContactsOnScreen.get(listContactsOnScreen.size()-1).getLocation())) {
                flagEndOfList = true;
            }
        }
        return flagEqualsNameFamily;
    }

    private void scrollUp() {
        int height = driver.manage().window().getSize().getHeight();
        int width = driver.manage().window().getSize().getWidth();
        TouchAction<?> touchAction = new TouchAction<>(driver);
        touchAction.longPress(PointOption.point(10, height*7/8))
                .moveTo(PointOption.point(10, height/8))
                .release()
                .perform();
        }

    public void logout() {
        clickWait(btnMenu, 3);
        clickWait(btnLogout, 3);
    }
}
