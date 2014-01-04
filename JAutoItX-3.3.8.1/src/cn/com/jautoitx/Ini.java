package cn.com.jautoitx;

import java.nio.CharBuffer;

import com.sun.jna.Native;

public class Ini extends AutoItX {
	public static final int DEFAULT_INI_READ_BUF_SIZE = 10 * 1024; // 10kb
	public static int INI_READ_BUF_SIZE = DEFAULT_INI_READ_BUF_SIZE;

	/**
	 * Deletes a value from a standard format .ini file.
	 * 
	 * @param fileName
	 *            The filename of the .ini file.
	 * @param section
	 *            The section name in the .ini file.
	 * @return Return true if success, return false if section is not found or
	 *         if INI file is read-only.
	 */
	public static boolean delete(final String fileName, final String section) {
		return delete(fileName, section, null);
	}

	/**
	 * Deletes a value from a standard format .ini file.
	 * 
	 * @param fileName
	 *            The filename of the .ini file.
	 * @param section
	 *            The section name in the .ini file.
	 * @param key
	 *            The key name in the in the .ini file. If no key name is given
	 *            the entire section is deleted.
	 * @return Return true if success, return false if section/key is not found
	 *         or if INI file is read-only.
	 */
	public static boolean delete(final String fileName, final String section,
			final String key) {
		return getAutoItX().AU3_IniDelete(
				stringToWString(defaultString(fileName)),
				stringToWString(defaultString(section)),
				stringToWString(defaultString(key))) == SUCCESS_RETURN_VALUE;
	}

	/**
	 * Reads a value from a standard format .ini file.
	 * 
	 * @param fileName
	 *            The filename of the .ini file.
	 * @param section
	 *            The section name in the .ini file.
	 * @param key
	 *            The key name in the in the .ini file.
	 * @return Return the requested key value if success, return "" if requested
	 *         key not found.
	 */
	public static String read(final String fileName, final String section,
			final String key) {
		return read(fileName, section, key, null);
	}

	/**
	 * Reads a value from a standard format .ini file.
	 * 
	 * @param fileName
	 *            The filename of the .ini file.
	 * @param section
	 *            The section name in the .ini file.
	 * @param key
	 *            The key name in the in the .ini file.
	 * @param defaultValue
	 *            The default value to return if the requested key is not found.
	 * @return Return the requested key value if success, return the default
	 *         string(return "" if default string is null) if requested key not
	 *         found.
	 */
	public static String read(final String fileName, final String section,
			final String key, final String defaultValue) {
		int bufSize = INI_READ_BUF_SIZE;
		if (bufSize <= 0) {
			bufSize = DEFAULT_INI_READ_BUF_SIZE;
		}
		final CharBuffer value = CharBuffer.allocate(bufSize);
		getAutoItX().AU3_IniRead(stringToWString(defaultString(fileName)),
				stringToWString(defaultString(section)),
				stringToWString(defaultString(key)),
				stringToWString(defaultString(defaultValue)), value, bufSize);

		return Native.toString(value.array());
	}

	/**
	 * Writes a value to a standard format .ini file.
	 * 
	 * A standard ini file looks like:<br>
	 * [SectionName]<br>
	 * Key=Value<br>
	 * If file does not exist, it is created. Keys and/or sections are added to
	 * the end and are not sorted in any way.
	 * 
	 * @param fileName
	 *            The filename of the .ini file.
	 * @param section
	 *            The section name in the .ini file.
	 * @param key
	 *            The key name in the in the .ini file.
	 * @param value
	 *            The value to write/change.
	 * @return Return 1 if success, return 0 if file is read-only.
	 */
	public static boolean write(final String fileName, final String section,
			final String key, final String value) {
		return getAutoItX().AU3_IniWrite(
				stringToWString(defaultString(fileName)),
				stringToWString(defaultString(section)),
				stringToWString(defaultString(key)),
				stringToWString(defaultString(value))) == SUCCESS_RETURN_VALUE;
	}
}
