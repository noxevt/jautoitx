package cn.com.jautoitx;

public class Clip extends AutoItX {
	/**
	 * Allows you to get text from the clipboard.
	 * 
	 * Remarks: A maximum of 16384 characters of text can be received; ensure
	 * that the text buffer you pass is at least this large.
	 * 
	 * @return Returns the text from the clipboard.
	 * @see #clipPut(String)
	 */
	public static String clipGet() {
		return invokeAndGetString("ClipGet");
	}

	/**
	 * Allows you to send text to the clipboard.
	 * 
	 * Remarks: A maximum of 16384 characters of text can be sent.
	 * 
	 * @param text
	 *            A pointer to a string buffer that contains the text you want
	 *            to send.
	 * @see #clipGet()
	 */
	public static void clipPut(final String text) {
		invoke("ClipPut", text);
	}
}
