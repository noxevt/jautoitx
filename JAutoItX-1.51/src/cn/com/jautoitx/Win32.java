package cn.com.jautoitx;

import java.awt.event.KeyEvent;
import java.io.File;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.VerRsrc.VS_FIXEDFILEINFO;
import com.sun.jna.platform.win32.Version;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APIOptions;

public class Win32 {
	public static final User32Ext user32 = User32Ext.INSTANCE;
	public static final Version version = Version.INSTANCE;

	public static final int INVALID_CONTROL_ID = -1;

	/**
	 * Retrieves version information for the specified file.
	 * 
	 * @param filename
	 *            The name of the file to get version information.
	 * @return Return version information for the specified file if success,
	 *         return null if failed.
	 * @see http://msdn.microsoft.com/en-us/library/ms647005(v=vs.85).aspx
	 * @see http://msdn.microsoft.com/en-us/library/ms647003(v=vs.85).aspx
	 * @see http://msdn.microsoft.com/en-us/library/ms647464(v=vs.85).aspx
	 * @see http://stackoverflow.com/questions/6918022/get-version-info-for-exe
	 */
	public static String getFileVersion(String filename) {
		return getFileVersion((filename == null) ? null : new File(filename));
	}

	/**
	 * Retrieves version information for the specified file.
	 * 
	 * @param file
	 *            The file to get version information.
	 * @return Return version information for the specified file if success,
	 *         return null if failed.
	 * @see http://msdn.microsoft.com/en-us/library/ms647005(v=vs.85).aspx
	 * @see http://msdn.microsoft.com/en-us/library/ms647003(v=vs.85).aspx
	 * @see http://msdn.microsoft.com/en-us/library/ms647464(v=vs.85).aspx
	 * @see http://stackoverflow.com/questions/6918022/get-version-info-for-exe
	 */
	public static String getFileVersion(File file) {
		String fileVersion = null;
		if ((file != null) && file.exists()) {
			String filePath = file.getAbsolutePath();

			// A pointer to a variable that the function sets to zero
			IntByReference dwHandle = new IntByReference(0);

			// Determines whether the operating system can retrieve version
			// information for a specified file. If version information is
			// available, GetFileVersionInfoSize returns the size, in bytes, of
			// that information. If the function fails, the return value is
			// zero.
			int versionLength = version.GetFileVersionInfoSize(filePath,
					dwHandle);
			if (versionLength > 0) {
				// Pointer to a buffer that receives the file-version
				// information
				Pointer lpData = new Memory(versionLength);

				// Retrieves version information for the specified file
				if (version.GetFileVersionInfo(filePath, 0, versionLength,
						lpData)) {
					PointerByReference lplpBuffer = new PointerByReference();

					if (version.VerQueryValue(lpData, "\\", lplpBuffer,
							new IntByReference())) {
						VS_FIXEDFILEINFO lplpBufStructure = new VS_FIXEDFILEINFO(
								lplpBuffer.getValue());
						lplpBufStructure.read();

						int v1 = (lplpBufStructure.dwFileVersionMS).intValue() >> 16;
						int v2 = (lplpBufStructure.dwFileVersionMS).intValue() & 0xffff;
						int v3 = (lplpBufStructure.dwFileVersionLS).intValue() >> 16;
						int v4 = (lplpBufStructure.dwFileVersionLS).intValue() & 0xffff;
						fileVersion = String.format("%d.%d.%d.%d", v1, v2, v3,
								v4);
					}
				}
			}
		}
		return fileVersion;
	}

	/**
	 * Check whether the capslock is on or not.
	 * 
	 * @return Return true if the capslock is on, otherwise return false.
	 */
	public static boolean isCapslockOn() {
		return (user32.GetKeyState(KeyEvent.VK_CAPS_LOCK) & 0xffff) != 0;
	}

	public static interface User32Ext extends User32 {
		User32Ext INSTANCE = (User32Ext) Native.loadLibrary("User32",
				User32Ext.class, W32APIOptions.DEFAULT_OPTIONS);

		/**
		 * Retrieves the window handle to the active window attached to the
		 * calling thread's message queue.
		 * 
		 * @return The return value is the handle to the active window attached
		 *         to the calling thread's message queue. Otherwise, the return
		 *         value is NULL.
		 */
		public HWND GetActiveWindow();

		/**
		 * Retrieves the status of the specified virtual key. The status
		 * specifies whether the key is up, down, or toggled (on,
		 * off¡ªalternating each time the key is pressed).
		 * 
		 * @param key
		 *            virtual key. If the desired virtual key is a letter or
		 *            digit (A through Z, a through z, or 0 through 9), nVirtKey
		 *            must be set to the ASCII value of that character. For
		 *            other keys, it must be a virtual-key code. If a
		 *            non-English keyboard layout is used, virtual keys with
		 *            values in the range ASCII A through Z and 0 through 9 are
		 *            used to specify most of the character keys. For example,
		 *            for the German keyboard layout, the virtual key of value
		 *            ASCII O (0x4F) refers to the "o" key, whereas VK_OEM_1
		 *            refers to the "o with umlaut" key.
		 * @return The return value specifies the status of the specified
		 *         virtual key, as follows:<br/>
		 * 
		 *         If the high-order bit is 1, the key is down; otherwise, it is
		 *         up.<br/>
		 * 
		 *         If the low-order bit is 1, the key is toggled. A key, such as
		 *         the CAPS LOCK key, is toggled if it is turned on. The key is
		 *         off and untoggled if the low-order bit is 0. A toggle key's
		 *         indicator light (if any) on the keyboard will be on when the
		 *         key is toggled, and off when the key is untoggled.
		 * @see <a href=
		 *      "http://msdn.microsoft.com/en-us/library/windows/desktop/ms646301(v=vs.85).aspx"
		 *      >GetKeyState function (Windows)</a>
		 */
		public short GetKeyState(int key);
	}
}
