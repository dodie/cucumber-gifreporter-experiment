package hu.advancedweb.gifassembler;

import cucumber.api.Scenario;
import cucumber.api.java.After;

public class ScenarioHook {
	
	@After
	public void embedScreenshot(Scenario scenario) {
		if (scenario.isFailed()) {
			try {
				byte[] animation = ScreenshotRunListener.gifAssembler.generate();
				scenario.embed(animation, "image/gif");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ScreenshotRunListener.gifAssembler.clearFrames();
	}
	
}