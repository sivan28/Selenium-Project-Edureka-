package automation.selenium.base;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import automation.selenium.utilities.ExcelReader;

public class Page extends ExcelReader {

	public static WebDriver driver;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static Logger log = Logger.getLogger("devpinoyLogger");

	@BeforeClass
	public void setUp() throws IOException {

		fis = new FileInputStream(
				"C:\\Users\\Siva\\eclipse-workspace\\NewTours\\src\\test\\resources\\properties\\config.properties");
		config.load(fis);
		log.debug("config file loaded!");

		fis = new FileInputStream(
				"C:\\Users\\Siva\\eclipse-workspace\\NewTours\\src\\test\\resources\\properties\\OR.properties");
		OR.load(fis);
		log.debug("OR file loaded!");

		if (config.getProperty("browser").equals("chrome")) {

			System.setProperty("webdriver.chrome.driver",
					"C:\\Users\\Siva\\eclipse-workspace\\NewTours\\src\\test\\resources\\executables\\chromedriver.exe");

			driver = new ChromeDriver();
			log.debug("chrome launched!");
		} else if (config.getProperty("browser").equals("firefox")) {

			System.setProperty("webdriver.gecko.driver",
					"C:\\Users\\Siva\\eclipse-workspace\\NewTours\\src\\test\\resources\\executables\\geckodriver.exe");

			driver = new FirefoxDriver();
		}

		driver.get(config.getProperty("baseURL"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterClass
	public void tearDown() {

		if (driver != null) {
			driver.quit();
		}
	}

	@AfterSuite
	public static void pdfMail() {
		sendPDFReportByGMail("FROM@gmail.com", "password", "TO@gmail.com", "PDF Report", "");
	}

	private static void sendPDFReportByGMail(String from, String pass, String to, String subject, String body) {
		Properties props = System.getProperties();

		String host = "smtp.gmail.com";

		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {

			// Set from address

			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set subject

			message.setSubject(subject);
			message.setText(body);

			BodyPart objMessageBodyPart = new MimeBodyPart();
			objMessageBodyPart.setText("Please Find The Attached Report File!");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(objMessageBodyPart);
			objMessageBodyPart = new MimeBodyPart();

			// Set path to the pdf report file

			String filename = System.getProperty("user.dir") + "\\Default test.pdf";

			// Create data source to attach the file in mail

			DataSource source = new FileDataSource(filename);
			objMessageBodyPart.setDataHandler(new DataHandler(source));
			objMessageBodyPart.setFileName(filename);

			multipart.addBodyPart(objMessageBodyPart);
			message.setContent(multipart);

			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		}

		catch (AddressException ae) {

			ae.printStackTrace();

		}

		catch (MessagingException me) {

			me.printStackTrace();

		}

	}

	@DataProvider(name = "testData")
	public static Object[][] getTestData() throws IOException {
		Object[][] data = ExcelReader.getDataFromExcel();
		return data;
	}

}
