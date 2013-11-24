package cn.com.jautoitx;

import java.awt.Frame;

import org.junit.Assert;
import org.junit.Before;

public abstract class BaseTest {
	protected static final boolean isZhUserLanguage = "zh"
			.equalsIgnoreCase(System.getProperty("user.language"));
	protected static final String NOTEPAD_TITLE = isZhUserLanguage ? "无标题 - 记事本"
			: "Untitled - Notepad";
	protected static final String NOTEPAD_TITLE_START = isZhUserLanguage ? "无标题"
			: "Untitled";
	protected static final String NOTEPAD_TITLE_ANY = isZhUserLanguage ? "题 - 记"
			: "titled - Note";
	protected static final String NOTEPAD_TITLE_END = isZhUserLanguage ? "记事本"
			: "Notepad";
	protected long currentTimeMillis = 0;

	@Before
	public void setUp() {
		while (Win.exists(NOTEPAD_TITLE)) {
			Win.close(NOTEPAD_TITLE);
		}
		currentTimeMillis = System.currentTimeMillis();
	}

	/**
	 * Run notepad.
	 */
	protected void runNotepad() {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder("notepad.exe");
			processBuilder.start();
			Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 30));
			Win.activate(NOTEPAD_TITLE);
			Assert.assertTrue(Win.active(NOTEPAD_TITLE));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Run notepad.
	 */
	protected void runNotepad2() {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder("notepad.exe");
			processBuilder.start();
			sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	protected void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Assert.fail();
		}
	}

	protected Frame createFrame(String title, int x, int y) {
		Frame frame = new Frame(title);
		frame.setBounds(x, y, 400, 300);
		frame.setVisible(true);
		Assert.assertTrue(Win.wait(title, 60));
		Win.activate(title);
		Assert.assertTrue(Win.active(title));

		return frame;
	}
}
