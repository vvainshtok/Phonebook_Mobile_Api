package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

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
        //clickWait(btnPrevMonth, 2); // because of the bug

        String[] arrayDate = date.split(" ");
        LocalDate localDate = LocalDate.now();
        if (localDate.getYear() != Integer.parseInt(arrayDate[2])) {
            btnPickerYear.click();
            driver.findElement(By.xpath("//*[@text='"+arrayDate[2]+"']")).click();
        }
        int monthsDiff = getMonthsDiff(arrayDate[1],localDate.getMonth().toString());
        //  "+1" and "-1" in "for" cycles added because of the bug
        if (monthsDiff > 1)
            for (int i = 0; i < monthsDiff-1; i++) clickWait(btnNextMonth, 2);
        else if (monthsDiff < 1)
            for (int i = 0; i < -monthsDiff+1; i++) clickWait(btnPrevMonth, 2);
        else pause(3);

        AndroidElement pickerDate = driver.findElement(By.xpath("//*[@content-desc='"+date+"']"));
        clickWait(pickerDate, 4);
        clickWait(btnOk, 3);
    }

    public int getMonthsDiff(String month1, String month2) {
        return (Month.valueOf(month1.toUpperCase()).getValue()
                - Month.valueOf(month2.toUpperCase()).getValue());
    }

    public boolean validateDateSetByDatePicker(String longDate) {
        String shortDate = LocalDate.parse(longDate, DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                .format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        WebElement dateOnScreen = driver.findElement(By.xpath("//*[@text='"
                + shortDate + "']"));
        return dateOnScreen.isDisplayed();
    }
}
