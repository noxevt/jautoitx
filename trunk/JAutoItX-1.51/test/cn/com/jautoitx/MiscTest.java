package cn.com.jautoitx;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.com.jautoitx.Misc.ShutdownMode;

public class MiscTest extends BaseTest {
	@Test
	public void blockInput() {
		Misc.blockInput(true);
		sleep(2000);

		Misc.blockInput(false);
		sleep(2000);
	}

	@Ignore
	@Test
	public void shutdown() {
		try {
			Misc.shutdown();
			Assert.fail();
		} catch (IllegalArgumentException e) {

		}

		Misc.shutdown(ShutdownMode.SHUTDOWN);
	}

	@Test
	public void sleep() {
		long start = System.currentTimeMillis();
		Misc.sleep(1000);
		long end = System.currentTimeMillis();
		Assert.assertTrue((end - start) >= 1000);

		start = System.currentTimeMillis();
		Misc.sleep(1500);
		end = System.currentTimeMillis();
		Assert.assertTrue((end - start) >= 1500);
	}
}
