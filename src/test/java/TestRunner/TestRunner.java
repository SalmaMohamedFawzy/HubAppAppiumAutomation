package TestRunner;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
        features = "src/test/java/resources", // Path to your feature files
        glue = {"Steps", "Hooks"}  ,            // Path to your step definitions
        plugin = {
                "pretty",
                "html:target/HtmlBasicReport.html",
                "json:target/cucumber.json" ,
                "junit:target/cukes.xml",
                "rerun:target/rerun.txt"
        },
        tags = "@test"
)
public class TestRunner extends AbstractTestNGCucumberTests {
}