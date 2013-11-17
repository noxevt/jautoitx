package cn.com.jautoitdll;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.omg.CORBA.LongHolder;

import cn.com.jautoitdll.AutoItDLL.CoordMode;
import cn.com.jautoitdll.AutoItDLL.Keys;
import cn.com.jautoitdll.AutoItDLL.ShutdownMode;
import cn.com.jautoitdll.AutoItDLL.TitleMatchMode;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Test case for #cn.com.jautoitdll.AutoItDLL.
 * 
 * @author zhengbo.wang
 */
public class AutoItDLLTest {
	private static final boolean isZhUserLanguage = "zh"
			.equalsIgnoreCase(System.getProperty("user.language"));
	private static final String NOTEPAD_TITLE = isZhUserLanguage ? "无标题 - 记事本"
			: "Untitled - Notepad";
	private static final String NOTEPAD_TITLE_START = isZhUserLanguage ? "无标题"
			: "Untitled";
	private static final String NOTEPAD_TITLE_ANY = isZhUserLanguage ? "题 - 记"
			: "titled - Note";
	private static final String NOTEPAD_TITLE_END = isZhUserLanguage ? "记事本"
			: "Notepad";
	private long currentTimeMillis = 0;

	@Before
	public void setUp() {
		while (AutoItDLL.ifWinExist(NOTEPAD_TITLE)) {
			AutoItDLL.winClose(NOTEPAD_TITLE);
		}
		currentTimeMillis = System.currentTimeMillis();
	}

	@Test
	public void blockInput() {
		AutoItDLL.blockInput(true);
		sleep(2000);

		AutoItDLL.blockInput(false);
		sleep(2000);
	}

	@Test
	public void clipGet() {
		AutoItDLL.clipPut("Hello");
		Assert.assertEquals("Hello", AutoItDLL.clipGet());

		AutoItDLL.clipPut("World");
		Assert.assertEquals("World", AutoItDLL.clipGet());

		AutoItDLL.clipPut("One world, one dream.");
		Assert.assertEquals("One world, one dream.", AutoItDLL.clipGet());

		AutoItDLL.clipPut("同一个世界，同一个梦想。");
		Assert.assertEquals("同一个世界，同一个梦想。", AutoItDLL.clipGet());
	}

	@Test
	public void clipPut() {
		AutoItDLL.clipPut("Hello");
		Assert.assertEquals("Hello", AutoItDLL.clipGet());

		AutoItDLL.clipPut("World");
		Assert.assertEquals("World", AutoItDLL.clipGet());

		AutoItDLL.clipPut("One world, one dream.");
		Assert.assertEquals("One world, one dream.", AutoItDLL.clipGet());

		AutoItDLL.clipPut("同一个世界，同一个梦想。");
		Assert.assertEquals("同一个世界，同一个梦想。", AutoItDLL.clipGet());
	}

	@Test
	public void detectHiddenText() {
		final Frame frame1 = createTestFrame("JAutoItDLL - detectHiddenText",
				"Hello", true);
		frame1.setVisible(true);
		final Frame frame2 = createTestFrame(
				"JAutoItDLL - not detectHiddenText", "World", false);
		frame2.setVisible(true);

		try {
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - detectHiddenText", ""));
			Assert.assertFalse(AutoItDLL.ifWinExist(
					"JAutoItDLL - detectHiddenText", "Hello"));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - not detectHiddenText", ""));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - not detectHiddenText", "World"));

			AutoItDLL.detectHiddenText(true);
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - detectHiddenText", ""));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - detectHiddenText", "Hello"));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - not detectHiddenText", ""));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - not detectHiddenText", "World"));

			AutoItDLL.detectHiddenText(false);
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - detectHiddenText", ""));
			Assert.assertFalse(AutoItDLL.ifWinExist(
					"JAutoItDLL - detectHiddenText", "Hello"));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - not detectHiddenText", ""));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - not detectHiddenText", "World"));

			AutoItDLL.detectHiddenText(true);
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - detectHiddenText", ""));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - detectHiddenText", "Hello"));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - not detectHiddenText", ""));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - not detectHiddenText", "World"));

			AutoItDLL.detectHiddenText(AutoItDLL.DEFAULT_DETECT_HIDDEN_TEXT);
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - detectHiddenText", ""));
			Assert.assertFalse(AutoItDLL.ifWinExist(
					"JAutoItDLL - detectHiddenText", "Hello"));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - not detectHiddenText", ""));
			Assert.assertTrue(AutoItDLL.ifWinExist(
					"JAutoItDLL - not detectHiddenText", "World"));
		} finally {
			frame1.setVisible(false);
			frame2.setVisible(false);
		}
	}

	/**
	 * Create frame to test AutoItDLL.detectHiddenText() method.
	 * 
	 * @param title
	 *            frame title
	 * @param buttonText
	 *            button text
	 * @param hideButton
	 *            whether or not hidden the button
	 * @return
	 */
	private Frame createTestFrame(final String title, final String buttonText,
			final boolean hideButton) {
		Frame frame = new Frame(title);

		final Button button = new Button(buttonText);
		if (hideButton) {
			button.setVisible(false);
		}
		frame.add(button, BorderLayout.CENTER);
		frame.setSize(400, 300);

		return frame;
	}

	@Test
	public void ifWinActive() {
		Assert.assertFalse(AutoItDLL.ifWinActive(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinActive(NOTEPAD_TITLE));

		// minimize notepad
		AutoItDLL.winMinimize(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinActive(NOTEPAD_TITLE));

		// activate notepad
		AutoItDLL.winActivate(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinActive(NOTEPAD_TITLE));

		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinActive(NOTEPAD_TITLE));
	}

	@Test
	public void ifWinExist() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// minimize notepad
		AutoItDLL.winMinimize(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// hide notepad
		AutoItDLL.winHide(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// show notepad
		AutoItDLL.winShow(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void iniDelete() throws IOException {
		FileUtils.copyFile(new File("test/iniDelete.tmp"), new File(
				"test/iniDelete.ini"));

		AutoItDLL.iniDelete(new File("test/iniDelete.ini").getAbsolutePath(),
				"SectionName2", "Key3");
		assertFileContentEquals("test/iniDelete_1.ini", "test/iniDelete.ini");

		AutoItDLL.iniDelete(new File("test/iniDelete.ini").getAbsolutePath(),
				"SectionName", "Key2");
		assertFileContentEquals("test/iniDelete_2.ini", "test/iniDelete.ini");

		// delete not exists selection
		AutoItDLL.iniDelete(new File("test/iniDelete.ini").getAbsolutePath(),
				"NotExistsSection", "Key");
		assertFileContentEquals("test/iniDelete_2.ini", "test/iniDelete.ini");

		// delete not exists key
		AutoItDLL.iniDelete(new File("test/iniDelete.ini").getAbsolutePath(),
				"SectionName", "NotExistsKey");
		assertFileContentEquals("test/iniDelete_2.ini", "test/iniDelete.ini");
	}

	@Test
	public void iniRead() throws IOException {
		Assert.assertEquals("Value3",
				AutoItDLL.iniRead("test/iniDelete.tmp", "SectionName2", "Key3"));
		Assert.assertEquals("Value",
				AutoItDLL.iniRead("test/iniDelete.tmp", "SectionName", "Key"));
		Assert.assertEquals("ERROR", AutoItDLL.iniRead("test/iniDelete.tmp",
				"NotExistsSection", "Key"));
		Assert.assertEquals("ERROR", AutoItDLL.iniRead("test/iniDelete.tmp",
				"SectionName", "NotExistsKey"));
	}

	@Test
	public void iniWrite() throws IOException {
		FileUtils.copyFile(new File("test/iniDelete.tmp"), new File(
				"test/iniWrite.ini"));

		AutoItDLL.iniWrite("test/iniWrite.ini", "SectionName2", "Key5",
				"Value5");
		Assert.assertEquals("Value5",
				AutoItDLL.iniRead("test/iniWrite.ini", "SectionName2", "Key5"));
		assertFileContentEquals("test/iniWrite.ini", "test/iniWrite_1.ini");

		AutoItDLL.iniWrite("test/iniWrite.ini", "SectionName3", "Key6",
				"Value6");
		Assert.assertEquals("Value6",
				AutoItDLL.iniRead("test/iniWrite.ini", "SectionName3", "Key6"));
		assertFileContentEquals("test/iniWrite.ini", "test/iniWrite_2.ini");
	}

	@Test
	public void leftClick() {
		String title = "leftClick - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			AutoItDLL.leftClick(100, 200);
			int x = AutoItDLL.mouseGetPosX();
			int y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 200) || (y == 199));

			AutoItDLL.leftClick(120, 230);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));

			// test relative mouse coord mode
			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.leftClick(100, 200);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 200) || (y == 199));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 220) || (y == 219));

			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.leftClick(120, 230);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 250) || (y == 249));
		} finally {
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			AutoItDLL.winWaitNotActive(title, 3);
		}
	}

	@Test
	public void leftClickDrag() {
		String title = "leftClickDrag - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			AutoItDLL.leftClickDrag(100, 200, 110, 210);
			int x = AutoItDLL.mouseGetPosX();
			int y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 210) || (y == 209));

			AutoItDLL.leftClickDrag(120, 230, 130, 240);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 240) || (y == 239));

			// test relative mouse coord mode
			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.leftClickDrag(100, 200, 110, 210);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 210) || (y == 209));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));

			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.leftClickDrag(120, 230, 130, 240);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 240) || (y == 239));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 140) || (x == 139));
			Assert.assertTrue("y is " + y, (y == 260) || (y == 259));
		} finally {
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			AutoItDLL.winWaitNotActive(title, 3);
		}
	}

	@Test
	public void mouseGetPosX() {
		String title = "mouseGetPosX - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			AutoItDLL.mouseMove(10, 50);
			int x = AutoItDLL.mouseGetPosX();
			int y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));

			AutoItDLL.mouseMove(100, 500);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));

			// test relative mouse coord mode
			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.mouseMove(10, 50);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 20) || (x == 19));
			Assert.assertTrue("y is " + y, (y == 70) || (y == 69));

			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.mouseMove(100, 500);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 520) || (y == 519));
		} finally {
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			AutoItDLL.winWaitNotActive(title, 3);
		}
	}

	@Test
	public void mouseGetPosY() {
		String title = "mouseGetPosY - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			AutoItDLL.mouseMove(10, 50);
			int x = AutoItDLL.mouseGetPosX();
			int y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));

			AutoItDLL.mouseMove(100, 500);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));

			// test relative mouse coord mode
			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.mouseMove(10, 50);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 20) || (x == 19));
			Assert.assertTrue("y is " + y, (y == 70) || (y == 69));

			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.mouseMove(100, 500);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 520) || (y == 519));
		} finally {
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			AutoItDLL.winWaitNotActive(title, 3);
		}
	}

	@Test
	public void mouseMove() {
		String title = "mouseMove - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			AutoItDLL.mouseMove(10, 50);
			int x = AutoItDLL.mouseGetPosX();
			int y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));

			AutoItDLL.mouseMove(100, 500);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));

			// test relative mouse coord mode
			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.mouseMove(10, 50);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 20) || (x == 19));
			Assert.assertTrue("y is " + y, (y == 70) || (y == 69));

			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.mouseMove(100, 500);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 520) || (y == 519));
		} finally {
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			AutoItDLL.winWaitNotActive(title, 3);
		}
	}

	@Test
	public void rightClick() {
		String title = "rightClick - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			AutoItDLL.rightClick(100, 200);
			int x = AutoItDLL.mouseGetPosX();
			int y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 200) || (y == 199));

			AutoItDLL.rightClick(120, 230);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));

			// test relative mouse coord mode
			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.rightClick(100, 200);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 200) || (y == 199));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 220) || (y == 219));

			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.rightClick(120, 230);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 250) || (y == 249));
		} finally {
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			AutoItDLL.winWaitNotActive(title, 3);
		}
	}

	@Test
	public void rightClickDrag() {
		String title = "rightClickDrag - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			AutoItDLL.rightClickDrag(100, 200, 110, 210);
			int x = AutoItDLL.mouseGetPosX();
			int y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 210) || (y == 209));

			AutoItDLL.rightClickDrag(120, 230, 130, 240);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 240) || (y == 239));

			// test relative mouse coord mode
			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.rightClickDrag(100, 200, 110, 210);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 210) || (y == 209));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));

			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.rightClickDrag(120, 230, 130, 240);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 240) || (y == 239));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 140) || (x == 139));
			Assert.assertTrue("y is " + y, (y == 260) || (y == 259));
		} finally {
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			AutoItDLL.winWaitNotActive(title, 3);
		}
	}

	@Test
	public void send() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertTrue(AutoItDLL.ifWinActive(NOTEPAD_TITLE));

		AutoItDLL.clipPut("hi");
		Assert.assertEquals("hi", AutoItDLL.clipGet());
		AutoItDLL.send("123456789^a^c");
		Assert.assertEquals("123456789", AutoItDLL.clipGet());

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// kill notepad
		AutoItDLL.winKill(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void setCapslockState() {
		final boolean isCapslockOn = isCapslockOn();

		AutoItDLL.setCapslockState(true);
		sleep(1000);
		Assert.assertTrue(isCapslockOn());

		AutoItDLL.setCapslockState(false);
		sleep(1000);
		Assert.assertFalse(isCapslockOn());

		AutoItDLL.setCapslockState(true);
		sleep(1000);
		Assert.assertTrue(isCapslockOn());

		AutoItDLL.setCapslockState(true);
		Assert.assertTrue(isCapslockOn());

		AutoItDLL.setCapslockState(false);
		Assert.assertFalse(isCapslockOn());

		AutoItDLL.setCapslockState(false);
		Assert.assertFalse(isCapslockOn());

		AutoItDLL.setCapslockState(isCapslockOn);
		Assert.assertEquals(isCapslockOn, isCapslockOn());
	}

	@Test
	public void setKeyDelay() {
		try {
			AutoItDLL.winMinimizeAll();

			// delay 0.1 second
			AutoItDLL.setKeyDelay(100);
			sleep(1000);
			final LongHolder time = new LongHolder(0);
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					long start = System.currentTimeMillis();
					AutoItDLL.send("AB");
					long end = System.currentTimeMillis();
					time.value = end - start;
				}
			});
			thread.start();
			sleep(3000);
			Assert.assertTrue(
					"Expected greater than or equals to 100, but actusl is "
							+ time.value, time.value >= 100);
			Assert.assertFalse(thread.isAlive());

			// delay 0.5 second
			AutoItDLL.setKeyDelay(500);
			sleep(1000);
			time.value = 0;
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					long start = System.currentTimeMillis();
					AutoItDLL.send("XY");
					long end = System.currentTimeMillis();
					time.value = end - start;
				}
			});
			thread.start();
			sleep(8000);
			Assert.assertTrue(
					"Expected greater than or equals to 500, but actusl is "
							+ time.value, time.value >= 500);
			Assert.assertFalse(thread.isAlive());

			// restore default key delay
			AutoItDLL.setKeyDelay(AutoItDLL.DEFAULT_KEY_DELAY);
		} finally {
			AutoItDLL.winMinimizeAllUndo();
		}
	}

	@Test
	public void setMouseCoordMode() {
		try {
			AutoItDLL.setMouseCoordMode(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("mouseCoordMode can not be null.",
					e.getMessage());
		} catch (Exception e) {
			Assert.fail();
		}

		String title = "setMouseCoordMode - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			Assert.assertEquals(CoordMode.ABSOLUTE_SCREEN_COORDINATES,
					AutoItDLL.DEFAULT_MOUSE_COORD_MODE);

			AutoItDLL.mouseMove(10, 50);
			int x = AutoItDLL.mouseGetPosX();
			int y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));

			AutoItDLL.mouseMove(100, 500);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));

			// test relative mouse coord mode
			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.mouseMove(10, 50);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 20) || (x == 19));
			Assert.assertTrue("y is " + y, (y == 70) || (y == 69));

			AutoItDLL.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			AutoItDLL.mouseMove(100, 500);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = AutoItDLL.mouseGetPosX();
			y = AutoItDLL.mouseGetPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 520) || (y == 519));
		} finally {
			AutoItDLL.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			AutoItDLL.winWaitNotActive(title, 3);
		}
	}

	@Test
	public void setStoreCapslockMode() {
		final boolean isCpaslockOn = isCapslockOn();

		AutoItDLL.setCapslockState(true);
		Assert.assertTrue(isCapslockOn());
		AutoItDLL.setStoreCapslockMode(true);
		AutoItDLL.send(Keys.CAPSLOCK);
		Assert.assertTrue(isCapslockOn());

		AutoItDLL.setStoreCapslockMode(false);
		AutoItDLL.send(Keys.CAPSLOCK);
		Assert.assertFalse(isCapslockOn());

		AutoItDLL.setCapslockState(false);
		Assert.assertFalse(isCapslockOn());
		AutoItDLL.setStoreCapslockMode(true);
		AutoItDLL.send(Keys.CAPSLOCK);
		Assert.assertFalse(isCapslockOn());

		AutoItDLL.setStoreCapslockMode(false);
		AutoItDLL.send(Keys.CAPSLOCK);
		Assert.assertTrue(isCapslockOn());

		// restore default capslock
		AutoItDLL.setCapslockState(isCpaslockOn);

		// restore default capslock mode
		AutoItDLL.setStoreCapslockMode(AutoItDLL.DEFAULT_STORE_CAPSLOCK_MODE);
	}

	@Test
	public void setTitleMatchMode() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// start mode
		AutoItDLL.setTitleMatchMode(TitleMatchMode.START_WITH);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE_START));
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE_ANY));
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE_END));

		// contains mode
		AutoItDLL.setTitleMatchMode(TitleMatchMode.ANY);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE_START));
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE_ANY));
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE_END));

		// restore default
		AutoItDLL.setTitleMatchMode(TitleMatchMode.START_WITH);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE_START));
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE_ANY));
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE_END));

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void setWinDelay() {
		// delay 5 seconds
		AutoItDLL.setWinDelay(5000);
		final LongHolder time = new LongHolder(0);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				AutoItDLL.winWait(NOTEPAD_TITLE);
				long end = System.currentTimeMillis();
				time.value = end - start;
			}
		});
		thread.start();
		runNotepad();
		sleep(6000);
		Assert.assertTrue(time.value >= 5000);
		Assert.assertFalse(thread.isAlive());
		AutoItDLL.winClose(NOTEPAD_TITLE);

		// delay 2 seconds
		AutoItDLL.setWinDelay(2000);
		time.value = 0;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				AutoItDLL.winWait(NOTEPAD_TITLE);
				long end = System.currentTimeMillis();
				time.value = end - start;
			}
		});
		thread.start();
		runNotepad();
		sleep(3000);
		Assert.assertTrue(time.value >= 2000);
		Assert.assertFalse(thread.isAlive());
		AutoItDLL.winClose(NOTEPAD_TITLE);

		// restore default win delay
		AutoItDLL.setWinDelay(AutoItDLL.DEFAULT_WIN_DELAY);
	}

	@Ignore
	@Test
	public void shutdown() {
		try {
			AutoItDLL.shutdown();
			Assert.fail();
		} catch (IllegalArgumentException e) {

		}

		AutoItDLL.shutdown(ShutdownMode.SHUTDOWN);
	}

	@Test
	public void sleep() {
		long start = System.currentTimeMillis();
		AutoItDLL.sleep(1000);
		long end = System.currentTimeMillis();
		Assert.assertTrue((end - start) >= 1000);

		start = System.currentTimeMillis();
		AutoItDLL.sleep(1500);
		end = System.currentTimeMillis();
		Assert.assertTrue((end - start) >= 1500);
	}

	@Test
	public void winActivate() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertTrue(AutoItDLL.ifWinActive(NOTEPAD_TITLE));

		// minimize notepad
		AutoItDLL.winMinimize(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinActive(NOTEPAD_TITLE));

		// activate notepad
		AutoItDLL.winActivate(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinActive(NOTEPAD_TITLE));

		// hide notepad
		AutoItDLL.winHide(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinActive(NOTEPAD_TITLE));

		// show notepad
		AutoItDLL.winShow(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinActive(NOTEPAD_TITLE));

		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinActive(NOTEPAD_TITLE));
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winClose() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// close another notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winGetActiveTitle() {
		Assert.assertTrue(!NOTEPAD_TITLE.equals(AutoItDLL.winGetActiveTitle()));

		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertTrue(NOTEPAD_TITLE.equals(AutoItDLL.winGetActiveTitle()));

		// minimize notepad
		AutoItDLL.winMinimize(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertTrue(!NOTEPAD_TITLE.equals(AutoItDLL.winGetActiveTitle()));

		// activate
		AutoItDLL.winActivate(NOTEPAD_TITLE);
		Assert.assertTrue(NOTEPAD_TITLE.equals(AutoItDLL.winGetActiveTitle()));

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winHide() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// hide notepad
		AutoItDLL.winHide(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// show notepad
		AutoItDLL.winShow(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winKill() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertTrue(AutoItDLL.ifWinActive(NOTEPAD_TITLE));

		// Write 'hello' to notepad
		AutoItDLL.send("123");

		// close notepad, but this can not close it because it's content is not
		// saved
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// kill notepad
		AutoItDLL.winKill(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winMaximize() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// maximize notepad
		AutoItDLL.winMaximize(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		sleep(1000);

		// restore notepad
		AutoItDLL.winRestore(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		sleep(1000);

		// hide notepad
		AutoItDLL.winHide(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		AutoItDLL.winMaximize(NOTEPAD_TITLE);
		sleep(1000);

		// show notepad
		AutoItDLL.winShow(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		sleep(1000);

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winMinimize() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// minimize notepad
		AutoItDLL.winMinimize(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		sleep(1000);

		// restore notepad
		AutoItDLL.winRestore(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		sleep(1000);

		// hide notepad
		AutoItDLL.winHide(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		AutoItDLL.winMinimize(NOTEPAD_TITLE);
		sleep(1000);

		// show notepad
		AutoItDLL.winShow(NOTEPAD_TITLE);
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		sleep(1000);

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winMinimizeAll() {
		AutoItDLL.winMinimizeAll();
		sleep(1000);
		AutoItDLL.winMinimizeAllUndo();
	}

	@Test
	public void winMinimizeAllUndo() {
		AutoItDLL.winMinimizeAll();
		sleep(1000);
		AutoItDLL.winMinimizeAllUndo();
	}

	@Test
	public void winMove() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// move notepad
		AutoItDLL.winMove(NOTEPAD_TITLE, 100, 50, 200, 100);
		sleep(500);

		// move notepad
		AutoItDLL.winMove(NOTEPAD_TITLE, 120, 70, 400, 200);
		sleep(500);

		// move notepad
		AutoItDLL.winMove(NOTEPAD_TITLE, 120, 70, -1, 300);
		sleep(500);

		// move notepad
		AutoItDLL.winMove(NOTEPAD_TITLE, 120, 70, 500, -1);
		sleep(500);

		// move notepad
		AutoItDLL.winMove(NOTEPAD_TITLE, 220, 170);
		sleep(500);

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winRestore() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// minimize Minimiz
		AutoItDLL.winMinimize(NOTEPAD_TITLE);
		sleep(500);

		// restore notepad
		AutoItDLL.winRestore(NOTEPAD_TITLE);
		sleep(500);

		// maximize notepad
		AutoItDLL.winMaximize(NOTEPAD_TITLE);
		sleep(500);

		// restore notepad
		AutoItDLL.winRestore(NOTEPAD_TITLE);
		sleep(500);

		// hide notepad
		AutoItDLL.winHide(NOTEPAD_TITLE);
		sleep(500);

		// restore notepad
		AutoItDLL.winRestore(NOTEPAD_TITLE);
		sleep(500);

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winSetTitle() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertFalse(AutoItDLL.ifWinExist("New " + NOTEPAD_TITLE));

		// set notepad's title
		AutoItDLL.winSetTitle(NOTEPAD_TITLE, "New " + NOTEPAD_TITLE);
		Assert.assertEquals("New " + NOTEPAD_TITLE,
				AutoItDLL.winGetActiveTitle());
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertTrue(AutoItDLL.ifWinExist("New " + NOTEPAD_TITLE));

		// close notepad
		AutoItDLL.winClose("New " + NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertFalse(AutoItDLL.ifWinExist("New " + NOTEPAD_TITLE));

		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertFalse(AutoItDLL.ifWinExist("New " + NOTEPAD_TITLE));

		// set notepad's title
		AutoItDLL.winSetTitle(NOTEPAD_TITLE, "New " + NOTEPAD_TITLE);
		Assert.assertEquals("New " + NOTEPAD_TITLE,
				AutoItDLL.winGetActiveTitle());
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertTrue(AutoItDLL.ifWinExist("New " + NOTEPAD_TITLE));

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertTrue(AutoItDLL.ifWinExist("New " + NOTEPAD_TITLE));

		// close notepad
		AutoItDLL.winClose("New " + NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertFalse(AutoItDLL.ifWinExist("New " + NOTEPAD_TITLE));
	}

	@Test
	public void winShow() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// hide notepad
		AutoItDLL.winHide(NOTEPAD_TITLE);
		sleep(500);

		// show notepad
		AutoItDLL.winShow(NOTEPAD_TITLE);
		sleep(500);

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winWait() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		Assert.assertFalse(AutoItDLL.winWait(NOTEPAD_TITLE, 2));

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				AutoItDLL.winWait(NOTEPAD_TITLE);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				AutoItDLL.winWait(NOTEPAD_TITLE);
			}
		});
		thread.start();
		runNotepad();
		sleep(2000);
		Assert.assertFalse(thread.isAlive());

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winWaitActive() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// minimize notepad
		AutoItDLL.winMinimize(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.winWaitActive(NOTEPAD_TITLE, 2));

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				AutoItDLL.winWaitActive(NOTEPAD_TITLE);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				AutoItDLL.winWait(NOTEPAD_TITLE);
			}
		});
		thread.start();
		AutoItDLL.winActivate(NOTEPAD_TITLE);
		sleep(2000);
		Assert.assertFalse(thread.isAlive());

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	@Test
	public void winWaitClose() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				AutoItDLL.winWaitClose(NOTEPAD_TITLE);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				AutoItDLL.winWaitClose(NOTEPAD_TITLE);
			}
		});
		thread.start();
		AutoItDLL.winClose(NOTEPAD_TITLE);
		sleep(2000);
		Assert.assertFalse(thread.isAlive());
	}

	@Test
	public void winWaitNotActive() {
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(AutoItDLL.ifWinExist(NOTEPAD_TITLE));

		// activate notepad
		AutoItDLL.winActivate(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.winWaitNotActive(NOTEPAD_TITLE, 2));

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				AutoItDLL.winWaitNotActive(NOTEPAD_TITLE);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				AutoItDLL.winWaitNotActive(NOTEPAD_TITLE);
			}
		});
		thread.start();
		AutoItDLL.winMinimize(NOTEPAD_TITLE);
		sleep(2000);
		Assert.assertFalse(thread.isAlive());

		// close notepad
		AutoItDLL.winClose(NOTEPAD_TITLE);
		Assert.assertFalse(AutoItDLL.ifWinExist(NOTEPAD_TITLE));
	}

	private void assertFileContentEquals(final String filePath1,
			final String filePath2) throws IOException {
		Assert.assertTrue((filePath1 != null)
				&& (filePath1.trim().length() > 0));
		Assert.assertTrue((filePath2 != null)
				&& (filePath2.trim().length() > 0));

		File file1 = new File(filePath1);
		File file2 = new File(filePath2);

		Assert.assertTrue(file1.exists());
		Assert.assertTrue(file1.isFile());
		Assert.assertTrue(file2.exists());
		Assert.assertTrue(file2.isFile());

		InputStream input1 = new FileInputStream(file1);
		InputStream input2 = new FileInputStream(file2);
		Assert.assertEquals(IOUtils.toString(input1), IOUtils.toString(input2));
		IOUtils.closeQuietly(input1);
		IOUtils.closeQuietly(input2);
	}

	private Frame createFrame(String title) {
		return createFrame(title, 0, 0);
	}

	private Frame createFrame(String title, int x, int y) {
		Frame frame = new Frame(title);
		frame.setBounds(x, y, 400, 300);
		frame.setVisible(true);
		Assert.assertTrue(AutoItDLL.winWait(title, 60));
		AutoItDLL.winActivate(title);
		Assert.assertTrue(AutoItDLL.ifWinActive(title));

		return frame;
	}

	/**
	 * Run notepad.
	 */
	private void runNotepad() {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder("notepad.exe");
			processBuilder.start();
			sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Assert.fail();
		}
	}

	/**
	 * Check whether the capslock is on or not.
	 * 
	 * @return Return true if the capslock is on, otherwise return false.
	 */
	private boolean isCapslockOn() {
		return (User32.INSTANCE.GetKeyState(KeyEvent.VK_CAPS_LOCK) & 0xffff) != 0;
	}

	private static interface User32 extends Library {
		public static User32 INSTANCE = (User32) Native.loadLibrary("User32",
				User32.class);

		public short GetKeyState(int key);
	}
}
