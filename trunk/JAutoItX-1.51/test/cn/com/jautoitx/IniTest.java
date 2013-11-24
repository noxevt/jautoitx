package cn.com.jautoitx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class IniTest extends BaseTest {
	@Test
	public void iniDelete() throws IOException {
		FileUtils.copyFile(new File("test/iniDelete.tmp"), new File(
				"test/iniDelete.ini"));

		Ini.delete(new File("test/iniDelete.ini").getAbsolutePath(),
				"SectionName2", "Key3");
		assertFileContentEquals("test/iniDelete_1.ini", "test/iniDelete.ini");

		Ini.delete(new File("test/iniDelete.ini").getAbsolutePath(),
				"SectionName", "Key2");
		assertFileContentEquals("test/iniDelete_2.ini", "test/iniDelete.ini");

		// delete not exists selection
		Ini.delete(new File("test/iniDelete.ini").getAbsolutePath(),
				"NotExistsSection", "Key");
		assertFileContentEquals("test/iniDelete_2.ini", "test/iniDelete.ini");

		// delete not exists key
		Ini.delete(new File("test/iniDelete.ini").getAbsolutePath(),
				"SectionName", "NotExistsKey");
		assertFileContentEquals("test/iniDelete_2.ini", "test/iniDelete.ini");
	}

	@Test
	public void iniRead() throws IOException {
		Assert.assertEquals("Value3",
				Ini.read("test/iniDelete.tmp", "SectionName2", "Key3"));
		Assert.assertEquals("Value",
				Ini.read("test/iniDelete.tmp", "SectionName", "Key"));
		Assert.assertEquals("ERROR",
				Ini.read("test/iniDelete.tmp", "NotExistsSection", "Key"));
		Assert.assertEquals("ERROR",
				Ini.read("test/iniDelete.tmp", "SectionName", "NotExistsKey"));
	}

	@Test
	public void iniWrite() throws IOException {
		FileUtils.copyFile(new File("test/iniDelete.tmp"), new File(
				"test/iniWrite.ini"));

		Ini.write("test/iniWrite.ini", "SectionName2", "Key5", "Value5");
		Assert.assertEquals("Value5",
				Ini.read("test/iniWrite.ini", "SectionName2", "Key5"));
		assertFileContentEquals("test/iniWrite.ini", "test/iniWrite_1.ini");

		Ini.write("test/iniWrite.ini", "SectionName3", "Key6", "Value6");
		Assert.assertEquals("Value6",
				Ini.read("test/iniWrite.ini", "SectionName3", "Key6"));
		assertFileContentEquals("test/iniWrite.ini", "test/iniWrite_2.ini");
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
}
