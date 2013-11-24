package cn.com.jautoitx;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for #cn.com.jautoitx.AutoItX.
 * 
 * @author zhengbo.wang
 */
public class AutoItXTest extends BaseTest {
	@Test
	public void version() {
		Assert.assertEquals("1.51.0.0", AutoItX.version());
	}
}
