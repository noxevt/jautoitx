package cn.com.jautoitx;

import org.junit.Assert;
import org.junit.Test;

public class ClipTest extends BaseTest {
	@Test
	public void get() {
		Clip.put("Hello");
		Assert.assertEquals("Hello", Clip.get());

		Clip.put("World");
		Assert.assertEquals("World", Clip.get());

		Clip.put("One world, one dream.");
		Assert.assertEquals("One world, one dream.", Clip.get());

		Clip.put("ͬһ�����磬ͬһ�����롣");
		Assert.assertEquals("ͬһ�����磬ͬһ�����롣", Clip.get());

		Clip.put("");
		Assert.assertNull(Clip.get());
	}

	@Test
	public void put() {
		Clip.put("Hello");
		Assert.assertEquals("Hello", Clip.get());

		Clip.put("World");
		Assert.assertEquals("World", Clip.get());

		Clip.put("One world, one dream.");
		Assert.assertEquals("One world, one dream.", Clip.get());

		Clip.put("ͬһ�����磬ͬһ�����롣");
		Assert.assertEquals("ͬһ�����磬ͬһ�����롣", Clip.get());
	}
}
