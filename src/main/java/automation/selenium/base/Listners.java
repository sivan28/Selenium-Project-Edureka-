package automation.selenium.base;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class Listners extends Page implements ITestListener  {

	public void onTestStart(ITestResult result) {
		System.out.println("Test started.."+ result.getTestName());
		
	}

	public void onTestSuccess(ITestResult result) {
		System.out.println(result.getTestName() + "Test case passed");
		
		TakesScreenshot screenshot = ((TakesScreenshot) driver);
		File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
		File destFile = new File("C://Users//Siva//Downloads//test.png");
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onTestFailure(ITestResult result) {
		System.out.println(result.getTestName() + "Test case failed");
		
			TakesScreenshot screenshot = ((TakesScreenshot) driver);
			File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
			File destFile = new File("C://Users//Siva//Downloads//test.png");
			try {
				FileUtils.copyFile(srcFile, destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void onTestSkipped(ITestResult result) {
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
	}

	public void onStart(ITestContext context) {
		
	}

	public void onFinish(ITestContext context) {
		
	}

}
