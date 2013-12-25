package cn.com.jautoitx;

import java.nio.CharBuffer;

import com.sun.jna.Native;

public class Clip extends AutoItX {
	public static int CLIP_GET_BUF_SIZE = 8 * 1024;

	/**
	 * Retrieves text from the clipboard.
	 * 
	 * Sets oAutoIt.error to 1 if clipboard is empty or contains a non-text
	 * entry.
	 * 
	 * @return Returns a string containing the text on the clipboard, returns
	 *         empty string if clipboard contains a non-text entry.
	 */
	public static String get() {
		final int bufSize = CLIP_GET_BUF_SIZE;
		final CharBuffer clip = CharBuffer.allocate(bufSize);
		autoItX.AU3_ClipGet(clip, bufSize);
		return Native.toString(clip.array());
	}

	/**
	 * Writes text to the clipboard.
	 * 
	 * Any existing clipboard contents are overwritten.
	 * 
	 * @param clip
	 *            The text to write to the clipboard.
	 */
	public static void put(final String clip) {
		autoItX.AU3_ClipPut(stringToWString(defaultString(clip)));
	}
}