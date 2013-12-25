package cn.com.jautoitx;

import java.nio.CharBuffer;

import com.sun.jna.Native;

public class Reg extends AutoItX {
	public static int REG_ENUM_KEY_BUF_SIZE = 8 * 1024;
	public static int REG_ENUM_VAL_BUF_SIZE = 8 * 1024;
	public static int REG_READ_BUF_SIZE = 8 * 1024;

	public static final int REG_ENUM_KEY_SUCCESS = 0;
	public static final int REG_ENUM_KEY_UNABLE_TO_OPEN_REQUESTED_KEY = 1;
	public static final int REG_ENUM_KEY_UNABLE_TO_RETRIEVE_REQUESTED_SUBKEY = -1;
	public static final int REG_ENUM_VAL_SUCCESS = 0;
	public static final int REG_ENUM_VAL_UNABLE_TO_OPEN_REQUESTED_KEY = 1;
	public static final int REG_ENUM_VAL_UNABLE_TO_RETRIEVE_REQUESTED_VALUE_NAME = -1;
	public static final int REG_READ_SUCCESS = 0;
	public static final int REG_READ_UNABLE_TO_OPEN_REQUESTED_KEY = 1;
	public static final int REG_READ_UNABLE_TO_OPEN_REQUESTED_VALUE = -1;
	public static final int REG_READ_VALUE_TYPE_NOT_SUPPORTED = -2;

	/**
	 * Deletes a key from the registry.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * Deleting from the registry is potentially dangerous--please exercise
	 * caution!
	 * 
	 * It is possible to access remote registries by using a keyname in the form
	 * "\\computername\keyname". To use this feature you must have the correct
	 * access rights on NT/2000/XP/2003, or if you are using a 9x based OS the
	 * remote PC must have the remote regsitry service installed first (See
	 * Microsoft Knowledge Base Article - 141460).
	 * 
	 * @param keyName
	 *            The registry key to delete.
	 * @return Return true if success, return false if the key dose not exist or
	 *         if error deleting key.
	 */
	public static boolean deleteKey(final String keyName) {
		return deleteKey_(keyName) == RegDeleteResult.SUCCESS;
	}

	/**
	 * Deletes a key from the registry.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * Deleting from the registry is potentially dangerous--please exercise
	 * caution!
	 * 
	 * It is possible to access remote registries by using a keyname in the form
	 * "\\computername\keyname". To use this feature you must have the correct
	 * access rights on NT/2000/XP/2003, or if you are using a 9x based OS the
	 * remote PC must have the remote regsitry service installed first (See
	 * Microsoft Knowledge Base Article - 141460).
	 * 
	 * @param keyName
	 *            The registry key to delete.
	 * @return Return 1 if success, return 0 if the key dose not exist, return 2
	 *         if error deleting key.
	 */
	public static RegDeleteResult deleteKey_(final String keyName) {
		int result = autoItX
				.AU3_RegDeleteKey(stringToWString(defaultString(keyName)));
		RegDeleteResult regDeleteResult = RegDeleteResult.FAILED;
		if (result == RegDeleteResult.SUCCESS.getValue()) {
			regDeleteResult = RegDeleteResult.SUCCESS;
		} else if (result == RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS.getValue()) {
			regDeleteResult = RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS;
		}

		return regDeleteResult;
	}

	/**
	 * Deletes a value from the registry.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * To access the (Default) value use "" (a blank string) for the valuename.
	 * 
	 * Deleting from the registry is potentially dangerous--please exercise
	 * caution!
	 * 
	 * It is possible to access remote registries by using a keyname in the form
	 * "\\computername\keyname". To use this feature you must have the correct
	 * access rights on NT/2000/XP/2003, or if you are using a 9x based OS the
	 * remote PC must have the remote regsitry service installed first (See
	 * Microsoft Knowledge Base Article - 141460).
	 * 
	 * @param keyName
	 *            The registry key to write to.
	 * @param valueName
	 *            The value name to delete.
	 * @return Return true if success, return false if the key/value does not
	 *         exist or if error deleting key/value.
	 */
	public static boolean deleteVal(final String keyName, final String valueName) {
		return deleteVal_(keyName, valueName) == RegDeleteResult.SUCCESS;
	}

	/**
	 * Deletes a value from the registry.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * To access the (Default) value use "" (a blank string) for the valuename.
	 * 
	 * Deleting from the registry is potentially dangerous--please exercise
	 * caution!
	 * 
	 * It is possible to access remote registries by using a keyname in the form
	 * "\\computername\keyname". To use this feature you must have the correct
	 * access rights on NT/2000/XP/2003, or if you are using a 9x based OS the
	 * remote PC must have the remote regsitry service installed first (See
	 * Microsoft Knowledge Base Article - 141460).
	 * 
	 * @param keyName
	 *            The registry key to write to.
	 * @param valueName
	 *            The value name to delete.
	 * @return Return 1 if success, return 0 if the key/value does not exist,
	 *         return 2 if error deleting key/value.
	 */
	public static RegDeleteResult deleteVal_(final String keyName,
			final String valueName) {
		int result = autoItX.AU3_RegDeleteVal(
				stringToWString(defaultString(keyName)),
				stringToWString(defaultString(valueName)));
		RegDeleteResult regDeleteResult = RegDeleteResult.FAILED;
		if (result == RegDeleteResult.SUCCESS.getValue()) {
			regDeleteResult = RegDeleteResult.SUCCESS;
		} else if (result == RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS.getValue()) {
			regDeleteResult = RegDeleteResult.KEY_OR_VALUE_NOT_EXISTS;
		}

		return regDeleteResult;
	}

	/**
	 * Reads the name of a subkey according to it's instance.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * @param keyName
	 *            The registry key to read.
	 * @param instance
	 *            The 1-based key instance to retrieve.
	 * @return Return the requested subkey name if success, return null if
	 *         unable to open requested key or if unable to retrieve requested
	 *         subkey (key instance out of range).
	 */
	public static String enumKey(final String keyName, final int instance) {
		return enumKey_(keyName, instance).getSubKeyName();
	}

	/**
	 * Reads the name of a subkey according to it's instance.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * @param keyName
	 *            The registry key to read.
	 * @param instance
	 *            The 1-based key instance to retrieve.
	 * @return Return the requested subkey name if success, return "" and sets
	 *         the @error flag: 1 if unable to open requested key, -1 if unable
	 *         to retrieve requested subkey (key instance out of range) if
	 *         failed.
	 */
	public static RegEnumKeyResult enumKey_(final String keyName,
			final int instance) {
		final int bufSize = REG_ENUM_KEY_BUF_SIZE;
		final CharBuffer result = CharBuffer.allocate(bufSize);
		autoItX.AU3_RegEnumKey(stringToWString(defaultString(keyName)),
				instance, result, bufSize);

		RegEnumKeyResult enumKeyResult = null;
		if (hasError()) {
			int error = error();
			if (error == RegEnumKeyStatus.UNABLE_TO_OPEN_REQUESTED_KEY
					.getStatus()) {
				enumKeyResult = new RegEnumKeyResult(
						RegEnumKeyStatus.UNABLE_TO_OPEN_REQUESTED_KEY, null);
			} else if (error == RegEnumKeyStatus.UNABLE_TO_RETRIEVE_REQUESTED_SUBKEY
					.getStatus()) {
				enumKeyResult = new RegEnumKeyResult(
						RegEnumKeyStatus.UNABLE_TO_RETRIEVE_REQUESTED_SUBKEY,
						null);
			}
		} else {
			enumKeyResult = new RegEnumKeyResult(RegEnumKeyStatus.SUCCESS,
					Native.toString(result.array()));
		}

		return enumKeyResult;
	}

	/**
	 * Reads the name of a value according to it's instance.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * @param keyName
	 *            The registry key to read.
	 * @param instance
	 *            The 1-based value instance to retrieve.
	 * @return Return the requested value name if success, return null if unable
	 *         to open requested key or if unable to retrieve requested value
	 *         name (value instance out of range) if failed.
	 */
	public static String enumVal(final String keyName, final int instance) {
		return enumVal_(keyName, instance).getSubKeyName();
	}

	/**
	 * Reads the name of a value according to it's instance.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * @param keyName
	 *            The registry key to read.
	 * @param instance
	 *            The 1-based value instance to retrieve.
	 * @return Return the requested value name if success, return "" and sets
	 *         the @error flag: 1 if unable to open requested key, -1 if unable
	 *         to retrieve requested value name (value instance out of range) if
	 *         failed.
	 */
	public static RegEnumValResult enumVal_(final String keyName,
			final int instance) {
		final int bufSize = REG_ENUM_VAL_BUF_SIZE;
		final CharBuffer result = CharBuffer.allocate(bufSize);
		autoItX.AU3_RegEnumVal(stringToWString(defaultString(keyName)),
				instance, result, bufSize);

		RegEnumValResult enumValResult = null;
		if (hasError()) {
			int error = error();
			if (error == RegEnumValStatus.UNABLE_TO_OPEN_REQUESTED_KEY
					.getStatus()) {
				enumValResult = new RegEnumValResult(
						RegEnumValStatus.UNABLE_TO_OPEN_REQUESTED_KEY, null);
			} else if (error == RegEnumValStatus.UNABLE_TO_RETRIEVE_REQUESTED_VALUE_NAME
					.getStatus()) {
				enumValResult = new RegEnumValResult(
						RegEnumValStatus.UNABLE_TO_RETRIEVE_REQUESTED_VALUE_NAME,
						null);
			}
		} else {
			enumValResult = new RegEnumValResult(RegEnumValStatus.SUCCESS,
					Native.toString(result.array()));
		}

		return enumValResult;
	}

	/**
	 * Reads a value from the registry.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * AutoIt supports registry keys of type REG_BINARY, REG_SZ, REG_MULTI_SZ,
	 * REG_EXPAND_SZ, and REG_DWORD.
	 * 
	 * To access the (Default) value use "" (a blank string) for the valuename.
	 * 
	 * When reading a REG_BINARY key the result is a string of hex characters,
	 * e.g. the REG_BINARY value of 01,a9,ff,77 will be read as the string
	 * "01A9FF77".
	 * 
	 * When reading a REG_MULTI_SZ key the multiple entries are seperated by a
	 * linefeed character.
	 * 
	 * It is possible to access remote registries by using a keyname in the form
	 * "\\computername\keyname". To use this feature you must have the correct
	 * access rights on NT/2000/XP/2003, or if you are using a 9x based OS the
	 * remote PC must have the remote regsitry service installed first (See
	 * Microsoft Knowledge Base Article - 141460).
	 * 
	 * @param keyName
	 *            The registry key to read.
	 * @param valueName
	 *            The value to read.
	 * @return Return the requested registry value value if success, return null
	 *         if unable to open requested or if unable to open requested value
	 *         or if value type not supported if failed.
	 */
	public static String read(final String keyName, final String valueName) {
		return read_(keyName, valueName).getValue();
	}

	/**
	 * Reads a value from the registry.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * AutoIt supports registry keys of type REG_BINARY, REG_SZ, REG_MULTI_SZ,
	 * REG_EXPAND_SZ, and REG_DWORD.
	 * 
	 * To access the (Default) value use "" (a blank string) for the valuename.
	 * 
	 * When reading a REG_BINARY key the result is a string of hex characters,
	 * e.g. the REG_BINARY value of 01,a9,ff,77 will be read as the string
	 * "01A9FF77".
	 * 
	 * When reading a REG_MULTI_SZ key the multiple entries are seperated by a
	 * linefeed character.
	 * 
	 * It is possible to access remote registries by using a keyname in the form
	 * "\\computername\keyname". To use this feature you must have the correct
	 * access rights on NT/2000/XP/2003, or if you are using a 9x based OS the
	 * remote PC must have the remote regsitry service installed first (See
	 * Microsoft Knowledge Base Article - 141460).
	 * 
	 * @param keyName
	 *            The registry key to read.
	 * @param valueName
	 *            The value to read.
	 * @return Return the requested registry value value if success, return ""
	 *         and sets the oAutoIt.error flag: 1 if unable to open requested
	 *         key, -1 if unable to open requested value, -2 if value type not
	 *         supported if failed.
	 */
	public static RegReadResult read_(final String keyName,
			final String valueName) {
		final int bufSize = REG_READ_BUF_SIZE;
		final CharBuffer result = CharBuffer.allocate(bufSize);
		// Returns numeric 1 and sets the oAutoIt.error flag:
		// 1 if unable to open requested key
		// -1 if unable to open requested value
		// -2 if value type not supported
		autoItX.AU3_RegRead(stringToWString(defaultString(keyName)),
				stringToWString(defaultString(valueName)), result, bufSize);

		RegReadResult readResult = null;
		if (hasError()) {
			int error = error();
			if (error == RegReadStatus.UNABLE_TO_OPEN_REQUESTED_KEY.getStatus()) {
				readResult = new RegReadResult(
						RegReadStatus.UNABLE_TO_OPEN_REQUESTED_KEY, null);
			} else if (error == RegReadStatus.UNABLE_TO_OPEN_REQUESTED_VALUE
					.getStatus()) {
				readResult = new RegReadResult(
						RegReadStatus.UNABLE_TO_OPEN_REQUESTED_VALUE, null);
			} else if (error == RegReadStatus.VALUE_TYPE_NOT_SUPPORTED
					.getStatus()) {
				readResult = new RegReadResult(
						RegReadStatus.VALUE_TYPE_NOT_SUPPORTED, null);
			}
		} else {
			readResult = new RegReadResult(RegReadStatus.SUCCESS,
					Native.toString(result.array()));
		}

		return readResult;
	}

	/**
	 * Creates a key or value in the registry.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * AutoIt supports registry keys of type REG_BINARY, REG_SZ, REG_MULTI_SZ,
	 * REG_EXPAND_SZ, and REG_DWORD.
	 * 
	 * To access the (Default) value use "" (a blank string) for the valuename.
	 * 
	 * When writing a REG_BINARY key use a string of hex characters, e.g. the
	 * REG_BINARY value of 01,a9,ff,77 can be written by using the string
	 * "01A9FF77".
	 * 
	 * When writing a REG_MULTI_SZ key you must separate each value with
	 * 
	 * @LF. The value must NOT end with @LF and no "blank" entries are allowed
	 *      (see example).
	 * 
	 *      It is possible to access remote registries by using a keyname in the
	 *      form "\\computername\keyname". To use this feature you must have the
	 *      correct access rights on NT/2000/XP/2003, or if you are using a 9x
	 *      based OS the remote PC must have the remote regsitry service
	 *      installed first (See Microsoft Knowledge Base Article - 141460).
	 * 
	 * @param keyName
	 *            The registry key to write to. If no other parameters are
	 *            specified this key will simply be created.
	 * @param valueName
	 *            The valuename to write to.
	 * @param type
	 *            Type of key to write: "REG_SZ", "REG_MULTI_SZ",
	 *            "REG_EXPAND_SZ", "REG_DWORD", or "REG_BINARY".
	 * @param value
	 *            The value to write.
	 * @return Return 1 if success, return 0 if error writing registry key or
	 *         value.
	 */
	public static boolean write(final String keyName) {
		return write(keyName, null, null, null);
	}

	/**
	 * Creates a key or value in the registry.
	 * 
	 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
	 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
	 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
	 * 
	 * AutoIt supports registry keys of type REG_BINARY, REG_SZ, REG_MULTI_SZ,
	 * REG_EXPAND_SZ, and REG_DWORD.
	 * 
	 * To access the (Default) value use "" (a blank string) for the valuename.
	 * 
	 * When writing a REG_BINARY key use a string of hex characters, e.g. the
	 * REG_BINARY value of 01,a9,ff,77 can be written by using the string
	 * "01A9FF77".
	 * 
	 * When writing a REG_MULTI_SZ key you must separate each value with
	 * 
	 * @LF. The value must NOT end with @LF and no "blank" entries are allowed
	 *      (see example).
	 * 
	 *      It is possible to access remote registries by using a keyname in the
	 *      form "\\computername\keyname". To use this feature you must have the
	 *      correct access rights on NT/2000/XP/2003, or if you are using a 9x
	 *      based OS the remote PC must have the remote regsitry service
	 *      installed first (See Microsoft Knowledge Base Article - 141460).
	 * 
	 * @param keyName
	 *            The registry key to write to. If no other parameters are
	 *            specified this key will simply be created.
	 * @param valueName
	 *            The valuename to write to.
	 * @param type
	 *            Type of key to write: "REG_SZ", "REG_MULTI_SZ",
	 *            "REG_EXPAND_SZ", "REG_DWORD", or "REG_BINARY".
	 * @param value
	 *            The value to write.
	 * @return Return 1 if success, return 0 if error writing registry key or
	 *         value.
	 */
	public static boolean write(final String keyName, final String valueName,
			final RegType type, final String value) {
		return autoItX.AU3_RegWrite(
				stringToWString(defaultString(keyName)),
				stringToWString(defaultString(valueName)),
				stringToWString(defaultString((type == null) ? null : type
						.getType())), stringToWString(defaultString(value))) == SUCCESS_RETURN_VALUE;
	}

	/**
	 * The result for deletes a key or a value from the registry.
	 * 
	 * @author zhengbo.wang
	 */
	public static enum RegDeleteResult {
		/* delete key or value successfully */
		SUCCESS(SUCCESS_RETURN_VALUE),

		/* key or value does not exist */
		KEY_OR_VALUE_NOT_EXISTS(0),

		/* error deleting key or value */
		FAILED(2);

		private int value;

		private RegDeleteResult(final int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String toString() {
			String result = null;
			switch (this) {
			case SUCCESS:
				result = "success";
				break;
			case KEY_OR_VALUE_NOT_EXISTS:
				result = "key not exists";
				break;
			default:
				result = "failed";
			}
			return result;
		}
	}

	/**
	 * The status for reads the name of a subkey according to it's instance.
	 * 
	 * @author zhengbo.wang
	 */
	public static enum RegEnumKeyStatus {
		/* read successfully */
		SUCCESS(REG_ENUM_KEY_SUCCESS),

		/* unable to open requested key */
		UNABLE_TO_OPEN_REQUESTED_KEY(REG_ENUM_KEY_UNABLE_TO_OPEN_REQUESTED_KEY),

		/* unable to retrieve requested subkey (key instance out of range) */
		UNABLE_TO_RETRIEVE_REQUESTED_SUBKEY(
				REG_ENUM_KEY_UNABLE_TO_RETRIEVE_REQUESTED_SUBKEY);

		private int status = 0;

		private RegEnumKeyStatus(final int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}

		@Override
		public String toString() {
			String result = null;
			switch (this) {
			case SUCCESS:
				result = "success";
				break;
			case UNABLE_TO_OPEN_REQUESTED_KEY:
				result = "unable to open requested key";
				break;
			default:
				result = "unable to retrieve requested subkey (key instance out of range)";
			}
			return result;
		}
	}

	/**
	 * The result for reads the name of a subkey according to it's instance.
	 * 
	 * @author zhengbo.wang
	 */
	public static class RegEnumKeyResult {
		private RegEnumKeyStatus status = null;
		private String subKeyName = null;

		public RegEnumKeyResult(final RegEnumKeyStatus status,
				final String subKeyName) {
			this.status = status;
			this.subKeyName = subKeyName;
		}

		public RegEnumKeyStatus getStatus() {
			return status;
		}

		public String getSubKeyName() {
			return subKeyName;
		}
	}

	/**
	 * The status for reads the value of a value according to it's instance.
	 * 
	 * @author zhengbo.wang
	 */
	public static enum RegEnumValStatus {
		/* read successfully */
		SUCCESS(REG_ENUM_VAL_SUCCESS),

		/* unable to open requested key */
		UNABLE_TO_OPEN_REQUESTED_KEY(REG_ENUM_VAL_UNABLE_TO_OPEN_REQUESTED_KEY),

		/* unable to retrieve requested value name (value instance out of range) */
		UNABLE_TO_RETRIEVE_REQUESTED_VALUE_NAME(
				REG_ENUM_VAL_UNABLE_TO_RETRIEVE_REQUESTED_VALUE_NAME);

		private int status = 0;

		private RegEnumValStatus(final int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}

		@Override
		public String toString() {
			String result = null;
			switch (this) {
			case SUCCESS:
				result = "success";
				break;
			case UNABLE_TO_OPEN_REQUESTED_KEY:
				result = "unable to open requested key";
				break;
			default:
				result = "unable to retrieve requested value name (value instance out of range)";
			}
			return result;
		}
	}

	/**
	 * The result for reads the value of a subkey according to it's instance.
	 * 
	 * @author zhengbo.wang
	 */
	public static class RegEnumValResult {
		private RegEnumValStatus status = null;
		private String subKeyName = null;

		public RegEnumValResult(final RegEnumValStatus status,
				final String subKeyName) {
			this.status = status;
			this.subKeyName = subKeyName;
		}

		public RegEnumValStatus getStatus() {
			return status;
		}

		public String getSubKeyName() {
			return subKeyName;
		}
	}

	/**
	 * The status for reads a value from the registry.
	 * 
	 * @author zhengbo.wang
	 */
	public static enum RegReadStatus {
		/* read value successfully */
		SUCCESS(REG_READ_SUCCESS),

		/* unable to open requested key */
		UNABLE_TO_OPEN_REQUESTED_KEY(REG_READ_UNABLE_TO_OPEN_REQUESTED_KEY),

		/* unable to open requested value */
		UNABLE_TO_OPEN_REQUESTED_VALUE(REG_READ_UNABLE_TO_OPEN_REQUESTED_VALUE),

		/* value type not supported */
		VALUE_TYPE_NOT_SUPPORTED(REG_READ_VALUE_TYPE_NOT_SUPPORTED);

		private int status = 0;

		private RegReadStatus(final int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}

		@Override
		public String toString() {
			String result = null;
			switch (this) {
			case SUCCESS:
				result = "success";
				break;
			case UNABLE_TO_OPEN_REQUESTED_KEY:
				result = "unable to open requested key";
				break;
			case UNABLE_TO_OPEN_REQUESTED_VALUE:
				result = "unable to open requested value";
				break;
			default:
				result = "value type not supported";
			}
			return result;
		}
	}

	/**
	 * The result for reads a value from the registry.
	 * 
	 * @author zhengbo.wang
	 */
	public static class RegReadResult {
		private RegReadStatus status = null;
		private String value = null;

		public RegReadResult(final RegReadStatus status, final String value) {
			this.status = status;
			this.value = value;
		}

		public RegReadStatus getStatus() {
			return status;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Type of registry key.
	 * 
	 * @author zhengbo.wang
	 */
	public static enum RegType {
		REG_SZ("REG_SZ"),

		REG_MULTI_SZ("REG_MULTI_SZ"),

		REG_EXPAND_SZ("REG_EXPAND_SZ"),

		REG_DWORD("REG_DWORD"),

		REG_BINARY("REG_BINARY");

		private String type;

		private RegType(final String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		@Override
		public String toString() {
			return type;
		}
	}
}
