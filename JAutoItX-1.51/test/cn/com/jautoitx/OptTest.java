package cn.com.jautoitx;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;

import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.LongHolder;

import cn.com.jautoitx.Keyboard.Keys;
import cn.com.jautoitx.Opt.CoordMode;
import cn.com.jautoitx.Opt.TitleMatchMode;

public class OptTest extends BaseTest {
	@Test
	public void detectHiddenText() {
		final Frame frame1 = createTestFrame("JAutoItX - detectHiddenText",
				"Hello", true);
		frame1.setVisible(true);
		final Frame frame2 = createTestFrame("JAutoItX - not detectHiddenText",
				"World", false);
		frame2.setVisible(true);

		try {
			Assert.assertTrue(Win.exists("JAutoItX - detectHiddenText", ""));
			Assert.assertFalse(Win.exists("JAutoItX - detectHiddenText",
					"Hello"));
			Assert.assertTrue(Win.exists("JAutoItX - not detectHiddenText", ""));
			Assert.assertTrue(Win.exists("JAutoItX - not detectHiddenText",
					"World"));

			Opt.detectHiddenText(true);
			Assert.assertTrue(Win.exists("JAutoItX - detectHiddenText", ""));
			Assert.assertTrue(Win
					.exists("JAutoItX - detectHiddenText", "Hello"));
			Assert.assertTrue(Win.exists("JAutoItX - not detectHiddenText", ""));
			Assert.assertTrue(Win.exists("JAutoItX - not detectHiddenText",
					"World"));

			Opt.detectHiddenText(false);
			Assert.assertTrue(Win.exists("JAutoItX - detectHiddenText", ""));
			Assert.assertFalse(Win.exists("JAutoItX - detectHiddenText",
					"Hello"));
			Assert.assertTrue(Win.exists("JAutoItX - not detectHiddenText", ""));
			Assert.assertTrue(Win.exists("JAutoItX - not detectHiddenText",
					"World"));

			Opt.detectHiddenText(true);
			Assert.assertTrue(Win.exists("JAutoItX - detectHiddenText", ""));
			Assert.assertTrue(Win
					.exists("JAutoItX - detectHiddenText", "Hello"));
			Assert.assertTrue(Win.exists("JAutoItX - not detectHiddenText", ""));
			Assert.assertTrue(Win.exists("JAutoItX - not detectHiddenText",
					"World"));

			Opt.detectHiddenText(Opt.DEFAULT_DETECT_HIDDEN_TEXT);
			Assert.assertTrue(Win.exists("JAutoItX - detectHiddenText", ""));
			Assert.assertFalse(Win.exists("JAutoItX - detectHiddenText",
					"Hello"));
			Assert.assertTrue(Win.exists("JAutoItX - not detectHiddenText", ""));
			Assert.assertTrue(Win.exists("JAutoItX - not detectHiddenText",
					"World"));
		} finally {
			frame1.setVisible(false);
			frame2.setVisible(false);
		}
	}

	@Test
	public void setCapslockState() {
		final boolean isCapslockOn = Win32.isCapslockOn();

		Opt.setCapslockState(true);
		sleep(1000);
		Assert.assertTrue(Win32.isCapslockOn());

		Opt.setCapslockState(false);
		sleep(1000);
		Assert.assertFalse(Win32.isCapslockOn());

		Opt.setCapslockState(true);
		sleep(1000);
		Assert.assertTrue(Win32.isCapslockOn());

		Opt.setCapslockState(true);
		Assert.assertTrue(Win32.isCapslockOn());

		Opt.setCapslockState(false);
		Assert.assertFalse(Win32.isCapslockOn());

		Opt.setCapslockState(false);
		Assert.assertFalse(Win32.isCapslockOn());

		Opt.setCapslockState(isCapslockOn);
		Assert.assertEquals(isCapslockOn, Win32.isCapslockOn());
	}

	@Test
	public void setKeyDelay() {
		try {
			Win.minimizeAll();

			// delay 0.1 second
			Opt.setKeyDelay(100);
			sleep(1000);
			final LongHolder time = new LongHolder(0);
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					long start = System.currentTimeMillis();
					Keyboard.send("AB");
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
			Opt.setKeyDelay(500);
			sleep(1000);
			time.value = 0;
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					long start = System.currentTimeMillis();
					Keyboard.send("XY");
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
			Opt.setKeyDelay(Opt.DEFAULT_KEY_DELAY);
		} finally {
			Win.minimizeAllUndo();
		}
	}

	@Test
	public void setMouseCoordMode() {
		try {
			Opt.setMouseCoordMode(null);
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
					Opt.DEFAULT_MOUSE_COORD_MODE);

			Mouse.move(10, 50);
			int x = Mouse.getPosX();
			int y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));

			Mouse.move(100, 500);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));

			// test relative mouse coord mode
			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.move(10, 50);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 20) || (x == 19));
			Assert.assertTrue("y is " + y, (y == 70) || (y == 69));

			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.move(100, 500);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 520) || (y == 519));
		} finally {
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			Win.waitNotActive(title, 3);
		}
	}

	@Test
	public void setStoreCapslockMode() {
		final boolean isCpaslockOn = Win32.isCapslockOn();

		Opt.setCapslockState(true);
		Assert.assertTrue(Win32.isCapslockOn());
		Opt.setStoreCapslockMode(true);
		Keyboard.send(Keys.CAPSLOCK);
		Assert.assertTrue(Win32.isCapslockOn());

		Opt.setStoreCapslockMode(false);
		Keyboard.send(Keys.CAPSLOCK);
		Assert.assertFalse(Win32.isCapslockOn());

		Opt.setCapslockState(false);
		Assert.assertFalse(Win32.isCapslockOn());
		Opt.setStoreCapslockMode(true);
		Keyboard.send(Keys.CAPSLOCK);
		Assert.assertFalse(Win32.isCapslockOn());

		Opt.setStoreCapslockMode(false);
		Keyboard.send(Keys.CAPSLOCK);
		Assert.assertTrue(Win32.isCapslockOn());

		// restore default capslock
		Opt.setCapslockState(isCpaslockOn);

		// restore default capslock mode
		Opt.setStoreCapslockMode(Opt.DEFAULT_STORE_CAPSLOCK_MODE);
	}

	@Test
	public void setTitleMatchMode() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// start mode
		Opt.setTitleMatchMode(TitleMatchMode.START_WITH);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE_START));
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE_ANY));
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE_END));

		// contains mode
		Opt.setTitleMatchMode(TitleMatchMode.ANY);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE_START));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE_ANY));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE_END));

		// restore default
		Opt.setTitleMatchMode(TitleMatchMode.START_WITH);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE_START));
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE_ANY));
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE_END));

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void setWinDelay() {
		// delay 5 seconds
		Opt.setWinDelay(5000);
		final LongHolder time = new LongHolder(0);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				Win.wait(NOTEPAD_TITLE);
				long end = System.currentTimeMillis();
				time.value = end - start;
			}
		});
		thread.start();
		runNotepad();
		sleep(6000);
		Assert.assertTrue(time.value >= 5000);
		Assert.assertFalse(thread.isAlive());
		Win.close(NOTEPAD_TITLE);

		// delay 2 seconds
		Opt.setWinDelay(2000);
		time.value = 0;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				Win.wait(NOTEPAD_TITLE);
				long end = System.currentTimeMillis();
				time.value = end - start;
			}
		});
		thread.start();
		runNotepad2();
		sleep(3000);
		Assert.assertTrue(time.value >= 2000);
		Assert.assertFalse(thread.isAlive());
		Win.close(NOTEPAD_TITLE);

		// restore default win delay
		Opt.setWinDelay(Opt.DEFAULT_WIN_DELAY);
	}

	/**
	 * Create frame to test AutoItX.detectHiddenText() method.
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
}
