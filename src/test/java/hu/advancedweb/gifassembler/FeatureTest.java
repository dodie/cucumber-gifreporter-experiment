package hu.advancedweb.gifassembler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "html:target/cucumber", "json:target/cucumber.json" },
        features = {"src/test/resources/feature/"})
public class FeatureTest {

	public static WebDriver driver = new FirefoxDriver();
	
	@BeforeClass
	public static void resize() {
		driver.manage().window().setSize(new Dimension(600, 600));
	}
	
	@AfterClass
	public static void close() {
		driver.close();
	}
	
}
