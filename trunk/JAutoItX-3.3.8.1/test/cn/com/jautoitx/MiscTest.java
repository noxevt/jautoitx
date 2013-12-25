package cn.com.jautoitx;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import cn.com.jautoitx.Keyboard.Keys;

public class MiscTest extends BaseTest {
	@Test
	public void blockInput() {
		try {
			// block user input
			Misc.blockInput(true);

			// run notepad
			int pid = runNotepad();

			// wait notepad active
			Assert.assertTrue(Win.waitActive(NOTEPAD_TITLE, 5));
			sleep(200);

			// send F5 to notepad
			Keyboard.send(Keys.F5);
			sleep(1000);

			// close notepad
			Process.close(pid);
		} finally {
			// unblock user input
			Misc.blockInput(false);
		}
	}

	@Test
	public void cdTray() {
		Assert.assertFalse(Misc.cdTray(null, true));
		Assert.assertFalse(Misc.cdTray(null, false));

		Assert.assertFalse(Misc.cdTray("", true));
		Assert.assertFalse(Misc.cdTray("", false));

		Assert.assertFalse(Misc.cdTray("AAAA", true));
		Assert.assertFalse(Misc.cdTray("AAAA", false));

		Assert.assertFalse(Misc.cdTray("¹âÇý", true));
		Assert.assertFalse(Misc.cdTray("¹âÇý", false));

		Assert.assertFalse(Misc.cdTray("C:", true));
		Assert.assertFalse(Misc.cdTray("C:", false));

		// TODO:
		Assert.assertTrue(Misc.cdTray("M:", true));
		Assert.assertTrue(Misc.cdTray("M:", false));
	}

	@Test
	public void isAdmin() {
		String userHome = System.getProperty("user.home");
		assertNotBlank(userHome);
		if (StringUtils.endsWithIgnoreCase(userHome, "\\Administrator")) {
			Assert.assertTrue(Misc.isAdmin());
		} else {
			Assert.assertFalse(Misc.isAdmin());
		}
	}

	@Test
	public void sleep() {
		for (int i = 1; i <= 3; i++) {
			long startTime = System.currentTimeMillis();
			Misc.sleep(i * 1000);
			long endTime = System.currentTimeMillis();
			Assert.assertTrue("Sleeped time is " + (endTime - startTime)
					+ " milliseconds.", (endTime - startTime) >= i * 1000);
		}
	}
}
