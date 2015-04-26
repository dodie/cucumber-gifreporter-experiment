package hu.advancedweb.gifassembler;


import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class ScreenshotRunListener extends RunListener {
	
	protected static GifAssembler gifAssembler = new GifAssembler();
		
	@Override
	public void testFinished(Description description) throws Exception {
		String details = description.getMethodName();
		byte[] screenshot = ((TakesScreenshot) FeatureTest.driver)
				.getScreenshotAs(OutputType.BYTES);
		gifAssembler.addFrame(details, screenshot);
	}
	
}