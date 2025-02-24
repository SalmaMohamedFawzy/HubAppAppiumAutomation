package Hooks;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.net.MalformedURLException;
import java.net.URL;

public class Hooks {
    public static AndroidDriver driver;

    @BeforeClass
    public void SetUp() throws MalformedURLException {
        System.out.println("Hooks class is running");

        DesiredCapabilities caps=new DesiredCapabilities();
        caps.setCapability("platformName","Android");
        caps.setCapability("appium:platformVersion","10");
        caps.setCapability("appium:deviceName","EDA51K");
       /* caps.setCapability("appium:ignoreHiddenApiPolicyError", true);
        caps.setCapability("appium:noReset", true);  // i think tis helps bypass first qr code scan
*/
        caps.setCapability("appium:app","C:\\Users\\snagy\\Downloads\\HubApp.apk");
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);

    }
    @AfterClass
    public void tearDown(){
        if (driver != null) {
            driver.quit();
            System.out.println("Driver quit in hooks: " + driver);
            driver = null; // Reset driver to null after quitting
        }
    }
}
