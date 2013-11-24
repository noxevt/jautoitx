package cn.com.jautoitx;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.W32APIOptions;

public class WinTest extends BaseTest {
	@Test
	public void activate() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		// minimize notepad
		Win.minimize(NOTEPAD_TITLE);
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));

		// activate notepad
		Win.activate(NOTEPAD_TITLE);
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		// hide notepad
		Win.hide(NOTEPAD_TITLE);
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));

		// show notepad
		Win.show(NOTEPAD_TITLE);
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void active() {
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		// minimize notepad
		Win.minimize(NOTEPAD_TITLE);
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));

		// activate notepad
		Win.activate(NOTEPAD_TITLE);
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));
	}

	@Test
	public void close() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// close another notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void exists() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// minimize notepad
		Win.minimize(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// hide notepad
		Win.hide(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// show notepad
		Win.show(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void getActiveTitle() {
		Assert.assertTrue(!NOTEPAD_TITLE.equals(Win.getActiveTitle()));

		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(NOTEPAD_TITLE.equals(Win.getActiveTitle()));

		// minimize notepad
		Win.minimize(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(!NOTEPAD_TITLE.equals(Win.getActiveTitle()));

		// activate
		Win.activate(NOTEPAD_TITLE);
		Assert.assertTrue(NOTEPAD_TITLE.equals(Win.getActiveTitle()));

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void hide() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// hide notepad
		Win.hide(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// show notepad
		Win.show(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void kill() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		// Write 'hello' to notepad
		Keyboard.send("123");

		// close notepad, but this can not close it because it's content is not
		// saved
		Win.close(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// kill notepad
		Win.kill(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void maximize() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// maximize notepad
		Win.maximize(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		sleep(1000);

		// restore notepad
		Win.restore(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		sleep(1000);

		// hide notepad
		Win.hide(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		Win.maximize(NOTEPAD_TITLE);
		sleep(1000);

		// show notepad
		Win.show(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		sleep(1000);

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void minimize() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// minimize notepad
		Win.minimize(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		sleep(1000);

		// restore notepad
		Win.restore(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		sleep(1000);

		// hide notepad
		Win.hide(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		Win.minimize(NOTEPAD_TITLE);
		sleep(1000);

		// show notepad
		Win.show(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		sleep(1000);

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void minimizeAll() {
		Win.minimizeAll();
		sleep(1000);
		Win.minimizeAllUndo();
	}

	@Test
	public void minimizeAllUndo() {
		Win.minimizeAll();
		sleep(1000);
		Win.minimizeAllUndo();
	}

	@Test
	public void move() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// move notepad
		Win.move(NOTEPAD_TITLE, 100, 50, 200, 100);
		sleep(500);

		// move notepad
		Win.move(NOTEPAD_TITLE, 120, 70, 400, 200);
		sleep(500);

		// move notepad
		Win.move(NOTEPAD_TITLE, 120, 70, -1, 300);
		sleep(500);

		// move notepad
		Win.move(NOTEPAD_TITLE, 120, 70, 500, -1);
		sleep(500);

		// move notepad
		Win.move(NOTEPAD_TITLE, 220, 170);
		sleep(500);

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void restore() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// minimize Minimiz
		Win.minimize(NOTEPAD_TITLE);
		sleep(500);

		// restore notepad
		Win.restore(NOTEPAD_TITLE);
		sleep(500);

		// maximize notepad
		Win.maximize(NOTEPAD_TITLE);
		sleep(500);

		// restore notepad
		Win.restore(NOTEPAD_TITLE);
		sleep(500);

		// hide notepad
		Win.hide(NOTEPAD_TITLE);
		sleep(500);

		// restore notepad
		Win.restore(NOTEPAD_TITLE);
		sleep(500);

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void setTitle() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.exists("New " + NOTEPAD_TITLE));

		// set notepad's title
		Win.setTitle(NOTEPAD_TITLE, "New " + NOTEPAD_TITLE);
		Assert.assertEquals("New " + NOTEPAD_TITLE, Win.getActiveTitle());
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists("New " + NOTEPAD_TITLE));

		// close notepad
		Win.close("New " + NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.exists("New " + NOTEPAD_TITLE));

		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.exists("New " + NOTEPAD_TITLE));

		// set notepad's title
		Win.setTitle(NOTEPAD_TITLE, "New " + NOTEPAD_TITLE);
		Assert.assertEquals("New " + NOTEPAD_TITLE, Win.getActiveTitle());
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists("New " + NOTEPAD_TITLE));

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists("New " + NOTEPAD_TITLE));

		// close notepad
		Win.close("New " + NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.exists("New " + NOTEPAD_TITLE));
	}

	@Test
	public void show() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// hide notepad
		Win.hide(NOTEPAD_TITLE);
		sleep(500);

		// show notepad
		Win.show(NOTEPAD_TITLE);
		sleep(500);

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void winWait() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.wait(NOTEPAD_TITLE, 2));

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Win.wait(NOTEPAD_TITLE);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Win.wait(NOTEPAD_TITLE);
			}
		});
		thread.start();
		runNotepad();
		sleep(2000);
		Assert.assertFalse(thread.isAlive());

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void waitActive() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		HWND hWnd = AutoItX.getActiveWindow();

		// minimize notepad
		Win.minimize(NOTEPAD_TITLE);
		Assert.assertFalse(Win.waitActive(NOTEPAD_TITLE, 2));

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Win.waitActive(NOTEPAD_TITLE);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Win.wait(NOTEPAD_TITLE);
			}
		});
		thread.start();
		activateWindow(hWnd);
		sleep(2000);
		Assert.assertFalse(thread.isAlive());

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@Test
	public void winWaitClose() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Win.waitClose(NOTEPAD_TITLE);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Win.waitClose(NOTEPAD_TITLE);
			}
		});
		thread.start();
		closeWindow(AutoItX.getActiveWindow());
		sleep(2000);
		Assert.assertFalse(thread.isAlive());
	}

	@Test
	public void waitNotActive() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// activate notepad
		Win.activate(NOTEPAD_TITLE);
		Assert.assertFalse(Win.waitNotActive(NOTEPAD_TITLE, 2));

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Win.waitNotActive(NOTEPAD_TITLE);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Win.waitNotActive(NOTEPAD_TITLE);
			}
		});
		thread.start();
		HWND hWnd = AutoItX.getActiveWindow();
		minimizeWindow(hWnd);
		sleep(2000);
		Assert.assertFalse(thread.isAlive());

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	private void activateWindow(HWND hWnd) {
		if (hWnd != null) {
			Assert.assertTrue(User32Ext.INSTANCE.SetForegroundWindow(hWnd));
		}
	}

	private void closeWindow(HWND hWnd) {
		if (hWnd != null) {
			final int WM_SYSCOMMAND = 0x0112;
			final int SC_CLOSE = 0xF060;
			User32Ext.INSTANCE.SendMessage(hWnd, WM_SYSCOMMAND, SC_CLOSE, 0);
		}
	}

	private void minimizeWindow(HWND hWnd) {
		if (hWnd != null) {
			final int WM_SYSCOMMAND = 0x0112;
			final int SC_MINIMIZE = 0xF020;
			User32Ext.INSTANCE.SendMessage(hWnd, WM_SYSCOMMAND, SC_MINIMIZE, 0);
		}
	}

	private static interface User32Ext extends User32 {
		public static User32Ext INSTANCE = (User32Ext) Native.loadLibrary(
				"User32", User32Ext.class, W32APIOptions.DEFAULT_OPTIONS);

		public int SendMessage(HWND hWnd, int msg, int wParam, int lParam);

		/**
		 * Activates a window. The window must be attached to the calling
		 * thread's message queue.
		 * 
		 * @param hWnd
		 *            A handle to the top-level window to be activated.
		 * @return If the function succeeds, the return value is the handle to
		 *         the window that was previously active. If the function fails,
		 *         the return value is null.
		 */
		public HWND SetActiveWindow(HWND hWnd);
	}
}
