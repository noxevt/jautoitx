package cn.com.jautoitx;

import org.junit.Assert;
import org.junit.Test;

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

		// minimize notepad
		Win.minimize(NOTEPAD_TITLE);
		Assert.assertFalse(Win.waitActive(NOTEPAD_TITLE, 2));

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Win.waitActive(NOTEPAD_TITLE);
				// AutoItX.close();
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Win.waitActive(NOTEPAD_TITLE);
				// AutoItX.close();
			}
		});
		thread.start();
		Win.activate(NOTEPAD_TITLE);
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
				// AutoItX.close();
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
				// AutoItX.close();
			}
		});
		thread.start();
		Win.close(NOTEPAD_TITLE);
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
				// AutoItX.close();
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
				// AutoItX.close();
			}
		});
		thread.start();
		Win.minimize(NOTEPAD_TITLE);
		sleep(2000);
		Assert.assertFalse(thread.isAlive());

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}
}
