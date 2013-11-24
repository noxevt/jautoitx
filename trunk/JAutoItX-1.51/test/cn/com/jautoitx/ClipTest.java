package cn.com.jautoitx;

import org.junit.Assert;
import org.junit.Test;

public class ClipTest extends BaseTest {
	@Test
	public void clipGet() {
		Clip.clipPut("Hello");
		Assert.assertEquals("Hello", Clip.clipGet());

		Clip.clipPut("World");
		Assert.assertEquals("World", Clip.clipGet());

		Clip.clipPut("One world, one dream.");
		Assert.assertEquals("One world, one dream.", Clip.clipGet());

		Clip.clipPut("ͬһ�����磬ͬһ�����롣");
		Assert.assertEquals("ͬһ�����磬ͬһ�����롣", Clip.clipGet());
	}

	@Test
	public void clipPut() {
		Clip.clipPut("Hello");
		Assert.assertEquals("Hello", Clip.clipGet());

		Clip.clipPut("World");
		Assert.assertEquals("World", Clip.clipGet());

		Clip.clipPut("One world, one dream.");
		Assert.assertEquals("One world, one dream.", Clip.clipGet());

		Clip.clipPut("ͬһ�����磬ͬһ�����롣");
		Assert.assertEquals("ͬһ�����磬ͬһ�����롣", Clip.clipGet());
	}
}
