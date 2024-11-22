package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;

public class DatePickerScreen extends BaseScreen {
    public DatePickerScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(id = "com.sheygam.contactapp:id/dateBtn")
    AndroidElement btnChangeDate;

    @FindBy(id = "android:id/prev")
    AndroidElement btnPrevMonth;
    @FindBy(id = "android:id/next")
    AndroidElement btnNextMonth;
    @FindBy(id = "android:id/button1")
    AndroidElement btnOk;
    @FindBy(id = "android:id/date_picker_header_year")
    AndroidElement btnPickerYear;



    public void typeDate(String date) {
        clickWait(btnChangeDate,2);
        clickWait(btnPrevMonth, 2); // из-за бага

        String[] arrayDate = date.split(" ");
        LocalDate localDate = LocalDate.now();
        if (localDate.getYear() != Integer.parseInt(arrayDate[2])) {
            btnPickerYear.click();
            driver.findElement(By.xpath("//*[@text='"+arrayDate[2]+"']")).click();


        }




        /* AndroidElement pickerDate = driver.findElement(By
                .xpath("//*[@content-desc='"+date+"']"));
        clickWait(pickerDate, 2);
        */
    }
}
