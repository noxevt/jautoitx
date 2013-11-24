package cn.com.jautoitx;

import org.junit.Assert;
import org.junit.Test;

public class KeyboardTest extends BaseTest {
	@Test
	public void send() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		Clip.clipPut("hi");
		Assert.assertEquals("hi", Clip.clipGet());
		Keyboard.send("123456789^a^c");
		Assert.assertEquals("123456789", Clip.clipGet());

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// kill notepad
		Win.kill(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}
}
