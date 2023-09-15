package libs;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class FirstTest {
    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("deviceName","AndroidTestDevice");
        capabilities.setCapability("platformVersion","9");
        capabilities.setCapability("automationName","UiAutomator2");
        capabilities.setCapability("appPackage","com.example.android.notepad");
        capabilities.setCapability("appActivity",".NotesList");
        capabilities.setCapability("app","/Users/elenakandaurova/Downloads/notes.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }
    @After
    public void tearDown(){
        driver.quit();
    }
    @Test
    public void createOneNoteAndDelete() {
        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@content-desc='New note']"),
                "Cannot find '//android.widget.TextView[@content-desc='New note']'",
                10
        );
        waitForElementAndSendKeys(
                By.id("com.example.android.notepad:id/note"),
                "My first note",
                "Cannot find note field",
                10
        );
        waitForElementAndClick(
                By.id("com.example.android.notepad:id/menu_save"),
                "Not found Save icon",
                5
        );
        webElementPresentWait(
                By.id("android:id/text1"),
                "List item not found",
                5
        );
        waitElementNotPresent(
                By.id("com.example.android.notepad:id/menu_save"),
                "Save button still present",
                10
        );
        waitForElementAndClick(
                By.id("android:id/text1"), //click by saved item on the list
                "List item not found",
                5
        );
        waitForElementAndClick(
                By.id("com.example.android.notepad:id/menu_delete"), //click by trash icon
                "Trash icon not found",
                5
        );
        waitElementNotPresent(
                By.id("com.example.android.notepad:id/menu_delete"),
                "Trash icon still present",
                10
        );
    }
    @Test
    public void compareNoteTitle(){
        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@content-desc='New note']"),
                "Cannot find '//android.widget.TextView[@content-desc='New note']'",
                10
        );
        waitForElementAndSendKeys(
                By.id("com.example.android.notepad:id/note"),
                "My first note",
                "Cannot find note field",
                10
        );
        waitForElementAndClick(
                By.id("com.example.android.notepad:id/menu_save"),
                "Not found Save icon",
                5
        );
        WebElement noteTitle = webElementPresentWait(
                By.id("android:id/text1"),
                "List item not found",
                5
        );
        String title = noteTitle.getAttribute("text");
        Assert.assertEquals(
                "Its not our title",
                "My first note",
                title
        );

    }
    @Test
    public void clearElement(){
        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@content-desc='New note']"),
                "Cannot find '//android.widget.TextView[@content-desc='New note']'",
                10
        );
        waitForElementAndSendKeys(
                By.id("com.example.android.notepad:id/note"),
                "My first note",
                "Cannot find note field",
                10
        );
        waitElementAndClear(
                By.id("com.example.android.notepad:id/note"),
                "Cannot find notes text",
                10
                );
    }
    @Test
    public void compareName(){
        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@content-desc='New note']"),
                "Cannot find '//android.widget.TextView[@content-desc='New note']'",
                10
        );
        waitForElementAndSendKeys(
                By.id("com.example.android.notepad:id/note"),
                "My first note",
                "Cannot find note field",
                10
        );
        assertElementHasText(
                By.id("com.example.android.notepad:id/note"),
                "Cannot find content field",
                "My first note"
        );
    }
    @Test
    public void compareWord(){
        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@content-desc='New note']"),
                "Cannot find '//android.widget.TextView[@content-desc='New note']'",
                10
        );
        waitForElementAndSendKeys(
                By.id("com.example.android.notepad:id/note"),
                "My first note",
                "Cannot find note field",
                10
        );
        assertElementContainsWord(
                By.id("com.example.android.notepad:id/note"),
                "Cannot find word",
                "My",
                10
        );
    }
    private WebElement webElementPresentWait(By by, String error_message, long timeOut){
        WebDriverWait wait = new WebDriverWait(driver, timeOut);
        wait.withMessage(error_message +"\n");
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }
    private WebElement webElementPresentWait(By by, String error_message) {
        return webElementPresentWait(by, error_message, 5);
    }
    private WebElement waitForElementAndClick(By by, String error_message, long timeOut){
        WebElement element = webElementPresentWait(by, error_message, timeOut);
        element.click();
        return element;
    }
    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeOut){
        WebElement element = webElementPresentWait(by, error_message, timeOut);
        element.sendKeys(value);
        return element;
    }
    private boolean waitElementNotPresent(By by, String error_message, long timeOut){
        WebDriverWait wait = new WebDriverWait(driver, timeOut);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }
    private WebElement waitElementAndClear(By by, String error_message, long timeOut){
        WebElement element = webElementPresentWait(by, error_message, timeOut);
        element.clear();
        return element;
    }
    private void assertElementHasText(By by, String error_message, String value){
        WebElement noteContent = webElementPresentWait(by, error_message);
        String content = noteContent.getAttribute("text");
        Assert.assertEquals(error_message, value, content );
    }
    private void assertElementContainsWord(By by, String error_message, String value, long timeOut){
        WebElement titleContent = webElementPresentWait(by, error_message, 10);
        String word = titleContent.getAttribute("text");
        Assert.assertTrue("Not found word in content", word.contains(value));
    }
}

