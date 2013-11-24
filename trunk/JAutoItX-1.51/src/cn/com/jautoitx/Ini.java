package cn.com.jautoitx;

public class Ini extends AutoItX {
	/**
	 * Deletes a specified value from a standard .INI file.
	 * 
	 * Remarks: An INI file is taken to be a file of the following format:
	 * [SectionName]
	 * 
	 * Value=Result
	 * 
	 * The full path of the INI file must be given, however, it is acceptable to
	 * use something similar to ".\myfile.ini" to indicate a file in the current
	 * working directory.
	 * 
	 * @param file
	 *            A string pointer to the filename of the .INI file.
	 * @param section
	 *            A string pointer to the required section of the .INI file.
	 * @param value
	 *            A string pointer to the value to delete.
	 * @see #read(String, String, String)
	 * @see #read(String, String, String, int)
	 * @see #write(String, String, String, String)
	 */
	public static void delete(final String file, final String section,
			final String value) {
		invoke("IniDelete", file, section, value);
	}

	/**
	 * Reads a specified value from a standard .INI file.
	 * 
	 * Remarks: An INI file is taken to be a file of the following format:
	 * [SectionName]
	 * 
	 * Value=Result
	 * 
	 * The full path of the INI file must be given, however, it is acceptable to
	 * use something similar to ".\myfile.ini" to indicate a file in the current
	 * working directory.
	 * 
	 * @param file
	 *            A string pointer to the filename of the .INI file.
	 * @param section
	 *            A string pointer to the required section of the .INI file.
	 * @param value
	 *            A string pointer to the value to read.
	 * @return Returns the value read from from the .INI file.
	 * @see #delete(String, String, String)
	 * @see #write(String, String, String, String)
	 */
	public static String read(final String file, final String section,
			final String value) {
		return invokeAndGetString("IniRead", file, section, value);
	}

	/**
	 * Write a specified value to a standard .INI file.
	 * 
	 * Remarks: An INI file is taken to be a file of the following format:
	 * [SectionName]
	 * 
	 * Value=Result
	 * 
	 * The full path of the INI file must be given, however, it is acceptable to
	 * use something similar to ".\myfile.ini" to indicate a file in the current
	 * working directory.
	 * 
	 * @param file
	 *            A string pointer to the filename of the .INI file.
	 * @param section
	 *            A string pointer to the required section of the .INI file.
	 * @param value
	 *            A string pointer to the value to write to.
	 * @param result
	 *            A string pointer to result to write.
	 * @see #delete(String, String, String)
	 * @see #read(String, String, String)
	 * @see #read(String, String, String, int)
	 */
	public static void write(final String file, final String section,
			final String value, final String result) {
		invoke("IniWrite", file, section, value, result);
	}
}
