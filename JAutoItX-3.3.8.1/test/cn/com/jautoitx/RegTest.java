package cn.com.jautoitx;

import org.junit.Assert;
import org.junit.Test;

import cn.com.jautoitx.Reg.RegDeleteResult;
import cn.com.jautoitx.Reg.RegEnumKeyResult;
import cn.com.jautoitx.Reg.RegEnumKeyStatus;
import cn.com.jautoitx.Reg.RegEnumValResult;
import cn.com.jautoitx.Reg.RegEnumValStatus;
import cn.com.jautoitx.Reg.RegReadResult;
import cn.com.jautoitx.Reg.RegReadStatus;
import cn.com.jautoitx.Reg.RegType;

public class RegTest extends BaseTest {
	@Test
	public void deleteKey() {
		Assert.assertFalse(Reg.deleteKey(null));
		Assert.assertEquals(RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS,
				Reg.deleteKey_(null));
		Assert.assertFalse(Reg.deleteKey(""));
		Assert.assertEquals(RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS,
				Reg.deleteKey_(""));
		Assert.assertFalse(Reg
				.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
						+ currentTimeMillis));
		Assert.assertEquals(
				RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS,
				Reg.deleteKey_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
						+ currentTimeMillis));

		// create a key in the registry
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
				+ currentTimeMillis, "name", RegType.REG_SZ, "JAutoItX"));
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
				+ currentTimeMillis));
		Assert.assertFalse(Reg
				.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
						+ currentTimeMillis));

		// create a key in the registry
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
				+ currentTimeMillis, "name", RegType.REG_SZ, "JAutoItX"));
		Assert.assertEquals(
				RegDeleteResult.SUCCESS,
				Reg.deleteKey_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
						+ currentTimeMillis));
		Assert.assertEquals(
				RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS,
				Reg.deleteKey_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
						+ currentTimeMillis));
	}

	@Test
	public void regDeleteVal() {
		Assert.assertFalse(Reg.deleteVal(null, null));
		Assert.assertEquals(RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS,
				Reg.deleteVal_(null, null));
		Assert.assertFalse(Reg.deleteVal("", ""));
		Assert.assertEquals(RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS,
				Reg.deleteVal_("", ""));
		Assert.assertFalse(Reg.deleteVal(
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX" + currentTimeMillis,
				"JAutoItX"));
		Assert.assertEquals(
				RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS,
				Reg.deleteVal_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
						+ currentTimeMillis, "JAutoItX"));

		// create a key in the registry
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
				+ currentTimeMillis, "name", RegType.REG_SZ, "JAutoItX"));
		Assert.assertFalse(Reg.deleteVal(
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX" + currentTimeMillis,
				"JAutoItX"));
		Assert.assertTrue(Reg.deleteVal("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
				+ currentTimeMillis, "name"));
		Assert.assertFalse(Reg.deleteVal(
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX" + currentTimeMillis,
				"JAutoItX"));

		// create a key in the registry
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
				+ currentTimeMillis, "name", RegType.REG_SZ, "JAutoItX"));
		Assert.assertEquals(
				RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS,
				Reg.deleteVal_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
						+ currentTimeMillis, "JAutoItX"));
		Assert.assertEquals(
				RegDeleteResult.SUCCESS,
				Reg.deleteVal_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
						+ currentTimeMillis, "name"));
		Assert.assertEquals(
				RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS,
				Reg.deleteVal_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoItX"
						+ currentTimeMillis, "JAutoItX"));
	}

	@Test
	public void regEnumKey() {
		Assert.assertNull(Reg.enumKey(
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt\\AutoItX"
						+ currentTimeMillis, 1));
		RegEnumKeyResult regEnumKeyResult = Reg.enumKey_(
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt\\AutoItX"
						+ currentTimeMillis, 1);
		Assert.assertEquals(RegEnumKeyStatus.UNABLE_TO_OPEN_REQUESTED_KEY,
				regEnumKeyResult.getStatus());
		Assert.assertNull(regEnumKeyResult.getSubKeyName());

		// create a key in the registry
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis,
				"name", RegType.REG_SZ, "JAutoItX"));
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_version" + currentTimeMillis,
				"version", RegType.REG_SZ, "3.8.8.1"));
		Assert.assertEquals(
				"AutoItX_name" + currentTimeMillis,
				Reg.enumKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
						+ currentTimeMillis, 1));
		Assert.assertEquals(
				"AutoItX_version" + currentTimeMillis,
				Reg.enumKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
						+ currentTimeMillis, 2));
		Assert.assertNull(Reg.enumKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, 0));
		Assert.assertNull(Reg.enumKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, 3));

		// delete reg key
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis));
		Assert.assertNull(Reg.enumKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, 1));
		Assert.assertNull(Reg.enumKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, 2));

		// create a key in the registry
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis,
				"name", RegType.REG_SZ, "JAutoItX"));
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_version" + currentTimeMillis,
				"version", RegType.REG_SZ, "3.8.8.1"));
		regEnumKeyResult = Reg.enumKey_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, 1);
		Assert.assertEquals(RegEnumKeyStatus.SUCCESS,
				regEnumKeyResult.getStatus());
		Assert.assertEquals("AutoItX_name" + currentTimeMillis,
				regEnumKeyResult.getSubKeyName());
		regEnumKeyResult = Reg.enumKey_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, 2);
		Assert.assertEquals(RegEnumKeyStatus.SUCCESS,
				regEnumKeyResult.getStatus());
		Assert.assertEquals("AutoItX_version" + currentTimeMillis,
				regEnumKeyResult.getSubKeyName());
		regEnumKeyResult = Reg.enumKey_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, 0);
		Assert.assertEquals(
				RegEnumKeyStatus.UNABLE_TO_RETRIEVE_REQUESTED_SUBKEY,
				regEnumKeyResult.getStatus());
		Assert.assertNull(regEnumKeyResult.getSubKeyName());
		regEnumKeyResult = Reg.enumKey_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, 3);
		Assert.assertEquals(
				RegEnumKeyStatus.UNABLE_TO_RETRIEVE_REQUESTED_SUBKEY,
				regEnumKeyResult.getStatus());
		Assert.assertNull(regEnumKeyResult.getSubKeyName());

		// delete reg key
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis));
		regEnumKeyResult = Reg.enumKey_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, 1);
		Assert.assertEquals(RegEnumKeyStatus.UNABLE_TO_OPEN_REQUESTED_KEY,
				regEnumKeyResult.getStatus());
		Assert.assertNull(regEnumKeyResult.getSubKeyName());
		regEnumKeyResult = Reg.enumKey_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, 2);
		Assert.assertEquals(RegEnumKeyStatus.UNABLE_TO_OPEN_REQUESTED_KEY,
				regEnumKeyResult.getStatus());
		Assert.assertNull(regEnumKeyResult.getSubKeyName());
	}

	@Test
	public void regEnumVal() {
		Assert.assertNull(Reg.enumVal(
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt\\AutoItX"
						+ currentTimeMillis, 1));
		RegEnumValResult regEnumValResult = Reg.enumVal_(
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt\\AutoItX"
						+ currentTimeMillis, 1);
		Assert.assertEquals(RegEnumValStatus.UNABLE_TO_OPEN_REQUESTED_KEY,
				regEnumValResult.getStatus());
		Assert.assertNull(regEnumValResult.getSubKeyName());

		// create a key in the registry
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis,
				"name", RegType.REG_SZ, "JAutoItX"));
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis,
				"name2", RegType.REG_SZ, "JAutoItX2"));
		Assert.assertEquals(
				"name",
				Reg.enumVal("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
						+ currentTimeMillis + "\\AutoItX_name"
						+ currentTimeMillis, 1));
		Assert.assertEquals(
				"name2",
				Reg.enumVal("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
						+ currentTimeMillis + "\\AutoItX_name"
						+ currentTimeMillis, 2));
		Assert.assertNull(Reg.enumVal("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis, 0));
		Assert.assertNull(Reg.enumVal("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis, 3));

		// delete reg key
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis));
		Assert.assertNull(Reg.enumVal("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis, 1));
		Assert.assertNull(Reg.enumVal("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis, 2));

		// create a key in the registry
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis,
				"name", RegType.REG_SZ, "JAutoItX"));
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis,
				"name2", RegType.REG_SZ, "JAutoItX2"));
		regEnumValResult = Reg.enumVal_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis, 1);
		Assert.assertEquals(RegEnumValStatus.SUCCESS,
				regEnumValResult.getStatus());
		Assert.assertEquals("name", regEnumValResult.getSubKeyName());
		regEnumValResult = Reg.enumVal_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis, 2);
		Assert.assertEquals(RegEnumValStatus.SUCCESS,
				regEnumValResult.getStatus());
		Assert.assertEquals("name2", regEnumValResult.getSubKeyName());
		regEnumValResult = Reg.enumVal_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis, 0);
		Assert.assertEquals(
				RegEnumValStatus.UNABLE_TO_RETRIEVE_REQUESTED_VALUE_NAME,
				regEnumValResult.getStatus());
		Assert.assertNull(regEnumValResult.getSubKeyName());
		regEnumValResult = Reg.enumVal_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis, 3);
		Assert.assertEquals(
				RegEnumValStatus.UNABLE_TO_RETRIEVE_REQUESTED_VALUE_NAME,
				regEnumValResult.getStatus());
		Assert.assertNull(regEnumValResult.getSubKeyName());

		// delete reg key
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis));
		regEnumValResult = Reg.enumVal_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis, 1);
		Assert.assertEquals(RegEnumValStatus.UNABLE_TO_OPEN_REQUESTED_KEY,
				regEnumValResult.getStatus());
		Assert.assertNull(regEnumValResult.getSubKeyName());
		regEnumValResult = Reg.enumVal_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis + "\\AutoItX_name" + currentTimeMillis, 2);
		Assert.assertEquals(RegEnumValStatus.UNABLE_TO_OPEN_REQUESTED_KEY,
				regEnumValResult.getStatus());
		Assert.assertNull(regEnumValResult.getSubKeyName());
	}

	@Test
	public void regRead() {
		Assert.assertNull(Reg
				.read("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt\\AutoItX"
						+ currentTimeMillis, "AutoItX_name" + currentTimeMillis));
		RegReadResult regReadResult = Reg.read_(
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt\\AutoItX"
						+ currentTimeMillis, "name");
		Assert.assertEquals(RegReadStatus.UNABLE_TO_OPEN_REQUESTED_KEY,
				regReadResult.getStatus());
		Assert.assertNull(regReadResult.getValue());

		// create a key in the registry
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "name", RegType.REG_SZ, "JAutoItX"));
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "version", RegType.REG_SZ, "3.8.8.1"));
		Assert.assertEquals(
				"JAutoItX",
				Reg.read("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
						+ currentTimeMillis, "name"));
		Assert.assertEquals(
				"3.8.8.1",
				Reg.read("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
						+ currentTimeMillis, "version"));
		Assert.assertNull(Reg.read("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "company"));

		// delete reg key
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis));
		Assert.assertNull(Reg.read("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "name"));
		Assert.assertNull(Reg.read("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "version"));

		// create a key in the registry
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "name", RegType.REG_SZ, "JAutoItX"));
		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "version", RegType.REG_SZ, "3.8.8.1"));
		regReadResult = Reg.read_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "name");
		Assert.assertEquals(RegReadStatus.SUCCESS, regReadResult.getStatus());
		Assert.assertEquals("JAutoItX", regReadResult.getValue());
		regReadResult = Reg.read_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "version");
		Assert.assertEquals(RegReadStatus.SUCCESS, regReadResult.getStatus());
		Assert.assertEquals("3.8.8.1", regReadResult.getValue());
		regReadResult = Reg.read_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "company");
		Assert.assertEquals(RegReadStatus.UNABLE_TO_OPEN_REQUESTED_VALUE,
				regReadResult.getStatus());
		Assert.assertNull(regReadResult.getValue());

		// delete reg key
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis));
		regReadResult = Reg.read_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "name");
		Assert.assertEquals(RegReadStatus.UNABLE_TO_OPEN_REQUESTED_KEY,
				regReadResult.getStatus());
		Assert.assertNull(regReadResult.getValue());
		regReadResult = Reg.read_("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "version");
		Assert.assertEquals(RegReadStatus.UNABLE_TO_OPEN_REQUESTED_KEY,
				regReadResult.getStatus());
		Assert.assertNull(regReadResult.getValue());
	}

	@Test
	public void regWrite() {
		Assert.assertFalse(Reg.write("xxxHKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "name", RegType.REG_SZ, "JAutoIt"));
		Assert.assertFalse(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, null, null, null));
		Assert.assertFalse(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "version", null, "3.8.8.1"));

		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "name", RegType.REG_SZ, "JAutoIt"));
		Assert.assertEquals(
				"JAutoIt",
				Reg.read("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
						+ currentTimeMillis, "name"));
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis));

		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "TestKey", RegType.REG_SZ, "line1\nline2"));
		Assert.assertEquals(
				"line1\nline2",
				Reg.read("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
						+ currentTimeMillis, "TestKey"));
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis));

		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "TestKey", RegType.REG_MULTI_SZ,
				"line1\nline2\n"));
		Assert.assertEquals(
				"line1\nline2\n",
				Reg.read("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
						+ currentTimeMillis, "TestKey"));
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis));

		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "TestKey", RegType.REG_MULTI_SZ,
				"line1\n\nline2\n"));
		Assert.assertEquals(
				"line1\n\nline2\n",
				Reg.read("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
						+ currentTimeMillis, "TestKey"));
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis));

		Assert.assertTrue(Reg.write("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis, "TestKey", RegType.REG_SZ,
				"line1\n\nline2\n"));
		Assert.assertEquals(
				"line1\n\nline2\n",
				Reg.read("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
						+ currentTimeMillis, "TestKey"));
		Assert.assertTrue(Reg.deleteKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\AutoIt"
				+ currentTimeMillis));
	}
}
