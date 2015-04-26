package hu.advancedweb.gifassembler.step;

import hu.advancedweb.gifassembler.FeatureTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ExampleSteps {

	private static final String URL = "http://localhost";
	WebDriver driver = FeatureTest.driver;

	@Given("^I am on the blog main page$")
	public void i_am_on_the_blog_main_page() throws Throwable {
		driver.navigate().to(URL);
	}

	@When("^I open the menu$")
	public void i_open_the_menu() throws Throwable {
		driver.findElement(By.cssSelector("#mobile-nav-toggle > a")).click();
		Thread.sleep(500L);
	}

	@When("^I click on the Archive link$")
	public void i_click_on_the_Archive_link() throws Throwable {
		driver.findElement(By.cssSelector(".navbar-nav li:nth-child(2) a")).click();
	}

	@Then("^I should see unicorns$")
	public void i_should_see_unicorns() throws Throwable {
		driver.findElement(By.cssSelector("#unicorn"));
	}

}
