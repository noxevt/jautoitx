package cn.com.jautoitdll;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * AutoIt is a simple tool that can simulate key presses, mouse movements and
 * window commands (maximize, minimize, wait for, etc.) in order to automate any
 * windows based task (or even windowed DOS tasks).
 * 
 * AutoItDLL is a pure DLL version of AutoIt. It allows you to perform the
 * following types of functions:
 * 
 * 1. Sending keystrokes and mouse clicks (all characters, keyboard layout
 * independent).
 * 
 * 2. Window functions (e.g. minimizing, hiding, restoring, waiting for,
 * activating (even under Win98/NT2000)).
 * 
 * 3. INI file reading and writing. 4. Simple clipboard (text) commands.
 * 
 * 5. Shutdown and reboot commands (including a forced reboot that works under
 * both 9x and NT).
 * 
 * 6.Blocking input under Windows NT4 SP6 and 2000/XP.
 * 
 * @author zhengbo.wang
 */
public final class AutoItDLL {
	/* log4j's config file path */
	public static String LOG4J_CONF_PATH = "conf/log4j.properties";

	/* Buffer size used in clipGet method */
	public static int CLIP_GET_BUF_SIZE = 16385;

	/* Buffer size used in iniRead method */
	public static int INI_READ_BUF_SIZE = 10240;

	/* Buffer size used in winGetActiveTitle method */
	public static int WIN_GET_ACTIVE_TITLE_BUF_SIZE = 16385;

	/*
	 * The default value for detectHiddenText method
	 */
	public static final boolean DEFAULT_DETECT_HIDDEN_TEXT = false;

	/*
	 * The default amount of time in milliseconds that AutoIt pauses between
	 * each simulated keypress
	 */
	public static final int DEFAULT_KEY_DELAY = 20;

	/*
	 * The default value for setStoreCapslockMode method
	 */
	public static final boolean DEFAULT_STORE_CAPSLOCK_MODE = true;

	/*
	 * Default window text, if you don't wish to specify window text, you must
	 * use a blank string instead, i.e. ""
	 */
	public static final String DEFAULT_WIN_TEXT = "";

	/*
	 * The default amount of time in milliseconds that AutoIt pauses after
	 * performing a WinWait-type function
	 */
	public static final int DEFAULT_WIN_DELAY = 500;

	/*
	 * The return value when the specified window is exists/active/close/not
	 * active.
	 */
	public static final int SUCCESS_WIN_WAIT = 0;

	private static final Logger logger = Logger.getLogger(AutoItDLL.class);
	private static AutoItDLLLibrary autoItDLL;

	/* AutoItDLLLibrary中方法成功时的返回值 */
	private static final int SUCCESS_RETURN_VALUE = 1;

	static {
		// 初始化AutoItDLL
		initAutoItDLL();
	}

	/**
	 * 初始化AutoItDLL。
	 */
	private static void initAutoItDLL() {
		// 初始化log4j
		try {
			final File file = new File(LOG4J_CONF_PATH);
			if (file.exists() && file.canRead()) {
				System.setProperty("log4j.debug", "true");
				PropertyConfigurator.configure(file.getAbsolutePath());
			}
		} catch (Exception e) {
			logger.error("Unable to initialize log4j.", e);
		}

		// 初始化AutoItDLL
		try {
			loadNativeLibrary();

			if (logger.isDebugEnabled()) {
				logger.debug("AutoItDLL initialized.");
			}
		} catch (Throwable e) {
			logger.error(
					"Unable to initialize " + AutoItDLL.class.getSimpleName(),
					e);
		}
	}

	/**
	 * Unpacking and loading the library into the Java Virtual Machine.
	 */
	private static void loadNativeLibrary() {
		String libName = "AutoItDLL";

		try {
			String libResourcePath = "/cn/com/jautoitdll/lib/";

			// Get what the system "thinks" the library name should be.
			String libNativeName = System.mapLibraryName(libName);

			// Slice up the library name.
			int i = libNativeName.lastIndexOf('.');
			String libNativePrefix = libNativeName.substring(0, i) + '_';
			String libNativeSuffix = libNativeName.substring(i);

			// Create the temp file for this instance of the library.
			File libFile = File
					.createTempFile(libNativePrefix, libNativeSuffix);

			// Check and see if a copy of the native lib already exists.
			OutputStream libOutputStream = new FileOutputStream(libFile);
			byte[] buffer = new byte[4 * 1024];

			// This may return null in some circumstances.
			InputStream libInputStream = AutoItDLL.class
					.getResourceAsStream(libResourcePath.toLowerCase()
							+ libNativeName);

			if (libInputStream == null) {
				throw new IOException("Unable to locate the native library.");
			}

			int size;
			while ((size = libInputStream.read(buffer)) != -1) {
				libOutputStream.write(buffer, 0, size);
			}
			libOutputStream.close();
			libInputStream.close();

			libFile.deleteOnExit();

			System.load(libFile.getPath());

			Native.setProtected(true);
			autoItDLL = (AutoItDLLLibrary) Native.loadLibrary(
					libFile.getName(), AutoItDLLLibrary.class);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Will prevent any user input via the mouse or keyboard. See the Remarks
	 * section for limitations.
	 * 
	 * Remarks: This function will have different effects depending on the
	 * operating system used:.
	 * 
	 * <table border="1" cellspacing="1">
	 * <tr>
	 * <td width="38%"><b><i><font size="2" face="Arial">Operating
	 * System</font></i></b></td>
	 * <td width="62%"><b><i><font size="2" face="Arial">&quot;BlockInput&quot;
	 * Results</font></i></b></td>
	 * </tr>
	 * <tr>
	 * <td width="38%">&nbsp;</td>
	 * <td width="62%">&nbsp;</td>
	 * </tr>
	 * <tr>
	 * <td width="38%"><font size="2" face="Arial">Windows 95</font></td>
	 * <td width="62%"><font size="2" face="Arial">No effect.</font></td>
	 * </tr>
	 * <tr>
	 * <td width="38%"><font size="2" face="Arial">Windows 98</font></td>
	 * <td width="62%"><font size="2" face="Arial">User input is blocked but
	 * AutoIt is also unable to simulate input.</font></td>
	 * </tr>
	 * <tr>
	 * <td width="38%"><font size="2" face="Arial">Windows NT 4 (<i>Without</i>
	 * ServicePack 6)</font></td>
	 * <td width="62%"><font size="2" face="Arial">No effect.</font></td>
	 * </tr>
	 * <tr>
	 * <td width="38%"><font size="2" face="Arial">Windows NT 4 (<i>With</i>
	 * ServicePack 6)</font></td>
	 * <td width="62%"><font size="2" face="Arial">User input is blocked and
	 * AutoIt can simulate input.</font></td>
	 * </tr>
	 * <tr>
	 * <td width="38%"><font size="2" face="Arial">Windows 2000</font></td>
	 * <td width="62%"><font size="2" face="Arial">User input is blocked and
	 * AutoIt can simulate input.</font></td>
	 * </tr>
	 * </table>
	 * 
	 * @param toggle
	 *            This should be set to true to block input, and false to enable
	 *            input.
	 */
	public static void blockInput(final boolean toggle) {
		autoItDLL.AUTOIT_BlockInput(toggle ? 1 : 0);
	}

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
		final byte[] text = new byte[CLIP_GET_BUF_SIZE];
		autoItDLL.AUTOIT_ClipGet(text);
		return Native.toString(text);
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
		autoItDLL.AUTOIT_ClipPut(text);
	}

	/**
	 * Closes down AutoIt. This function is called automatically when the DLL is
	 * unloaded.
	 * 
	 * Remarks: This is called internally when the DLL unloads.
	 * 
	 * If you are using the static library version of AutoIt, call this function
	 * when you have finished using AutoIt functions.
	 */
	public static void close() {
		autoItDLL.AUTOIT_Close();
	}

	/**
	 * Some programs use hidden text on windows this can cause problems when
	 * trying to script them. This command allows you to tell AutoIt whether or
	 * not to detect this hidden text.
	 * 
	 * Remarks: When you use the "/reveal" mode of the full AutoIt product, you
	 * will see all text, including hidden text.
	 * 
	 * @param toggle
	 *            This should be set to true to enable hidden text detection,
	 *            and false to disable. (Default is false)
	 */
	public static void detectHiddenText(final boolean toggle) {
		autoItDLL.AUTOIT_DetectHiddenText(toggle ? 1 : 0);
	}

	/**
	 * Checks if a given window is currently active.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns true if the given window was active, otherwise it returns
	 *         false.
	 * @see #ifWinExist(String, String)
	 */
	public static boolean ifWinActive(final String title) {
		return ifWinActive(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * Checks if a given window is currently active.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @return Returns true if the given window was active, otherwise it returns
	 *         false.
	 * @see #ifWinExist(String, String)
	 */
	public static boolean ifWinActive(final String title, final String text) {
		final int value = autoItDLL.AUTOIT_IfWinActive(title, text);
		return value == SUCCESS_RETURN_VALUE;
	}

	/**
	 * Checks if a given window current exists (in any state).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns true if the given window exists (regardless of its
	 *         state), otherwise it returns false.
	 * @see #ifWinActive(String, String)
	 */
	public static boolean ifWinExist(final String title) {
		return ifWinExist(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * Checks if a given window current exists (in any state).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @return Returns true if the given window exists (regardless of its
	 *         state), otherwise it returns false.
	 * @see #ifWinActive(String, String)
	 */
	public static boolean ifWinExist(final String title, final String text) {
		final int value = autoItDLL.AUTOIT_IfWinExist(title, text);
		return value == SUCCESS_RETURN_VALUE;
	}

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
	 * @see #iniRead(String, String, String)
	 * @see #iniRead(String, String, String, int)
	 * @see #iniWrite(String, String, String, String)
	 */
	public static void iniDelete(final String file, final String section,
			final String value) {
		autoItDLL.AUTOIT_IniDelete(file, section, value);
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
	 * @see #iniDelete(String, String, String)
	 * @see #iniWrite(String, String, String, String)
	 */
	public static String iniRead(final String file, final String section,
			final String value) {
		return iniRead(file, section, value, INI_READ_BUF_SIZE);
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
	 * @param bufSize
	 *            If bufSize less than or equals to 0, the default buffer size
	 *            <code>INI_READ_BUF_SIZE</code> for iniRead will be used.
	 * @return Returns the value read from from the .INI file.
	 * @see #iniDelete(String, String, String)
	 * @see #iniWrite(String, String, String, String)
	 */
	public static String iniRead(final String file, final String section,
			final String value, final int bufSize) {
		final byte[] result = new byte[(bufSize > 0) ? bufSize
				: INI_READ_BUF_SIZE];
		autoItDLL.AUTOIT_IniRead(file, section, value, result);
		return Native.toString(result);
	}

	/**
	 * Resets AutoIt to defaults (window delays, key delays, etc.). This
	 * function is called automatically when the DLL is loaded.
	 * 
	 * Remarks: This is called internally when the DLL loads.
	 * 
	 * If you are using the static library version of AutoIt, call this function
	 * before using any AutoIt functions -- ideally call it as soon as your
	 * program starts (and has input focus) for best results (helps the
	 * AUTOIT_WinActivate command).
	 */
	public static void init() {
		autoItDLL.AUTOIT_Init();
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
	 * @see #iniDelete(String, String, String)
	 * @see #iniRead(String, String, String)
	 * @see #iniRead(String, String, String, int)
	 */
	public static void iniWrite(final String file, final String section,
			final String value, final String result) {
		autoItDLL.AUTOIT_IniWrite(file, section, value, result);
	}

	/**
	 * Simulates a mouse left-click at a given coordinate.
	 * 
	 * Remarks: The X and Y co-ordinates are relative to the currently active
	 * window. Run the full version of AutoIt in reveal mode to determine the
	 * required co-ordinates of a window. To perform a double-click, simply run
	 * the command twice :)
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @see #leftClickDrag(int, int, int, int)
	 * @see #rightClick(int, int)
	 * @see #rightClickDrag(int, int, int, int)
	 */
	public static void leftClick(final int x, final int y) {
		autoItDLL.AUTOIT_LeftClick(x, y);
	}

	/**
	 * Simulates a mouse left-click-drag operation.
	 * 
	 * Remarks: The X and Y co-ordinates are relative to the currently active
	 * window. Run the full version of AutoIt in reveal mode to determine the
	 * required co-ordinates of a window.
	 * 
	 * @param x1
	 *            X1 coordinate (from coordinate)
	 * @param y1
	 *            Y1 coordinate (from coordinate)
	 * @param x2
	 *            X2 coordinate (to coordinate)
	 * @param y2
	 *            Y2 coordinate (to coordinate)
	 * @see #leftClick(int, int)
	 * @see #leftClickDrag(int, int, int, int)
	 * @see #rightClick(int, int)
	 * @see #rightClickDrag(int, int, int, int)
	 */
	public static void leftClickDrag(final int x1, final int y1, final int x2,
			final int y2) {
		autoItDLL.AUTOIT_LeftClickDrag(x1, y1, x2, y2);
	}

	/**
	 * Gets the X coordinate of the mouse pointer.
	 * 
	 * Remarks: The X and Y co-ordinates are relative to the currently active
	 * window. Run the full version of AutoIt in reveal mode to determine the
	 * required co-ordinates of a window.
	 * 
	 * @return The X coordinate of the mouse pointer.
	 * @see #mouseMove(int, int)
	 * @see #mouseGetPosY()
	 */
	public static int mouseGetPosX() {
		return autoItDLL.AUTOIT_MouseGetPosX();
	}

	/**
	 * Gets the Y coordinate of the mouse pointer.
	 * 
	 * Remarks: The X and Y co-ordinates are relative to the currently active
	 * window. Run the full version of AutoIt in reveal mode to determine the
	 * required co-ordinates of a window.
	 * 
	 * @return The Y coordinate of the mouse pointer.
	 * @see #mouseMove(int, int)
	 * @see #mouseGetPosX()
	 */
	public static int mouseGetPosY() {
		return autoItDLL.AUTOIT_MouseGetPosY();
	}

	/**
	 * Moves the mouse pointer to the specified coordinates.
	 * 
	 * Remarks: The X and Y co-ordinates are relative to the currently active
	 * window. Run the full version of AutoIt in reveal mode to determine the
	 * required co-ordinates of a window.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @see #mouseGetPosX()
	 * @see #mouseGetPosY()
	 */
	public static void mouseMove(final int x, final int y) {
		autoItDLL.AUTOIT_MouseMove(x, y);
	}

	/**
	 * Simulates a mouse right-click at a given coordinate.
	 * 
	 * Remarks: The X and Y co-ordinates are relative to the currently active
	 * window. Run the full version of AutoIt in reveal mode to determine the
	 * required co-ordinates of a window. To perform a double-click, simply run
	 * the command twice :)
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @see #leftClick(int, int)
	 * @see #leftClickDrag(int, int, int, int)
	 * @see #rightClickDrag(int, int, int, int)
	 */
	public static void rightClick(final int x, final int y) {
		autoItDLL.AUTOIT_RightClick(x, y);
	}

	/**
	 * Simulates a mouse right-click-drag operation.
	 * 
	 * Remarks: The X and Y co-ordinates are relative to the currently active
	 * window. Run the full version of AutoIt in reveal mode to determine the
	 * required co-ordinates of a window.
	 * 
	 * @param x1
	 *            X1 coordinate (from coordinate)
	 * @param y1
	 *            Y1 coordinate (from coordinate)
	 * @param x2
	 *            X2 coordinate (to coordinate)
	 * @param y2
	 *            Y2 coordinate (to coordinate)
	 * @see #leftClick(int, int)
	 * @see #leftClickDrag(int, int, int, int)
	 * @see #rightClick(int, int)
	 */
	public static void rightClickDrag(final int x1, final int y1, final int x2,
			final int y2) {
		autoItDLL.AUTOIT_RightClickDrag(x1, y1, x2, y2);
	}

	/**
	 * Sends keystrokes to the currently active window.
	 * 
	 * Remarks: See here for the syntax of the send command.
	 * 
	 * <h3><font face="Arial">Send Command Syntax</font></h3>
	 * <p>
	 * <font face="Arial" size="2">The &quot;Send&quot; command syntax is
	 * similar to that of ScriptIt and the Visual Basic &quot;SendKeys&quot;
	 * command. Characters are sent as written with the exception of the
	 * following characters:<br>
	 * <br>
	 * <b>'!'</b><br>
	 * This tells AutoIt to send an ALT keystroke, therefore <b>Send &quot;This
	 * is text!a&quot;</b> would send the keys &quot;This is text&quot; and then
	 * press &quot;ALT+a&quot;.&nbsp;<br>
	 * <br>
	 * N.B. Some programs are very choosy about capital letters and ALT keys,
	 * i.e. &quot;!A&quot; is different to &quot;!a&quot;. The first says
	 * ALT+SHIFT+A, the second is ALT+a. If in doubt, use lowercase!<br>
	 * <br>
	 * <b>'+'</b><br>
	 * This tells AutoIt to send a SHIFT keystroke, therefore <b>Send
	 * &quot;Hell+o&quot;</b> would send the text &quot;HellO&quot;. <b>Send
	 * &quot;!+a&quot;</b> would send &quot;ALT+SHIFT+a&quot;.<br>
	 * <br>
	 * <b>'^'<br>
	 * </b>This tells AutoIt to send a CONTROL keystroke, therefore <b>Send
	 * &quot;^!a&quot;</b> would send &quot;CTRL+ALT+a&quot;.<br>
	 * <br>
	 * N.B. Some programs are very choosy about capital letters and CTRL keys,
	 * i.e. &quot;^A&quot; is different to &quot;^a&quot;. The first says
	 * CTRL+SHIFT+A, the second is CTRL+a. If in doubt, use lowercase!<br>
	 * <br>
	 * <b>'#'</b><br>
	 * The hash is used as a key delimiter to make a line easier to read.&nbsp;
	 * i.e. <b> Send &quot;H#e#l#l#o&quot;</b> is the same as <b>Send
	 * &quot;Hello&quot;</b>.<br>
	 * <br>
	 * <br>
	 * Certain special keys can be sent and should be enclosed in braces:<br>
	 * <br>
	 * <b><u>N.B.&nbsp; Windows does not allow the simulation of the
	 * &quot;CTRL-ALT-DEL&quot; combination!</u></b></font>
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td width="50%" align="center"><b><font size="2" face="Arial">Send
	 * Command</font></b></td>
	 * <td width="50%">
	 * <p align="center">
	 * <b><font size="2" face="Arial">Resulting Keypress</font></b></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{!}</font></td>
	 * <td width="50%"><font face="Arial" size="2">!</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{#}</font></td>
	 * <td width="50%"><font face="Arial" size="2">#</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{+}</font></td>
	 * <td width="50%"><font face="Arial" size="2">+</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{^}</font></td>
	 * <td width="50%"><font face="Arial" size="2">^</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{{}</font></td>
	 * <td width="50%"><font face="Arial" size="2">{</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{}}</font></td>
	 * <td width="50%"><font face="Arial" size="2">}</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{SPACE}</font>
	 * </td>
	 * <td width="50%"><font face="Arial" size="2">SPACE</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{ENTER}</font>
	 * </td>
	 * <td width="50%"><font face="Arial" size="2">ENTER</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{ALT}</font></td>
	 * <td width="50%"><font face="Arial" size="2">ALT</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{BACKSPACE} or
	 * {BS}</font></td>
	 * <td width="50%"><font face="Arial" size="2">BACKSPACE</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{DELETE} or
	 * {DEL}</font></td>
	 * <td width="50%"><font face="Arial" size="2">DELETE</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{UP}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Cursor up</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{DOWN}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Cursor down</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{LEFT}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Cursor left</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{RIGHT}</font>
	 * </td>
	 * <td width="50%"><font face="Arial" size="2">Cursor right</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{HOME}</font></td>
	 * <td width="50%"><font face="Arial" size="2">HOME</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{END}</font></td>
	 * <td width="50%"><font face="Arial" size="2">END</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{ESCAPE} or
	 * {ESC}</font></td>
	 * <td width="50%"><font face="Arial" size="2">ESCAPE</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{INSERT} or
	 * {INS}</font></td>
	 * <td width="50%"><font face="Arial" size="2">INS</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{PGUP}</font></td>
	 * <td width="50%"><font face="Arial" size="2">PGUP</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{PGDN}</font></td>
	 * <td width="50%"><font face="Arial" size="2">PGDN</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{F1} -
	 * {F12}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Function keys</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{TAB}</font></td>
	 * <td width="50%"><font face="Arial" size="2">TAB</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{PRINTSCREEN}</font></td>
	 * <td width="50%"><font face="Arial" size="2">PRINTSCR</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{LWIN}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Left Windows key</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{RWIN}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Right Windows key</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{NUMLOCK}</font></td>
	 * <td width="50%"><font face="Arial" size="2">NUMLOCK</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{CTRLBREAK}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Ctrl+break</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{PAUSE}</font>
	 * </td>
	 * <td width="50%"><font face="Arial" size="2">PAUSE</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{CAPSLOCK}</font></td>
	 * <td width="50%"><font face="Arial" size="2">CAPSLOCK</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{NUMPAD0} -
	 * {NUMPAD 9}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Numpad digits</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{NUMPADMULT}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Numpad Multiply</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{NUMPADADD}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Numpad Add</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{NUMPADSUB}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Numpad Subtract</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{NUMPADDIV}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Numpad Divide</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{NUMPADDOT}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Numpad period</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{APPSKEY}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Windows App key</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{ALTDOWN}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Holds the ALT key down until
	 * {ALTUP} is sent</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{SHIFTDOWN}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Holds the SHIFT key down
	 * until {SHIFTUP} is sent</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{CTRLDOWN}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Holds the CTRL key down until
	 * {CTRLUP} is sent</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{LWINDOWN}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Holds the left Windows key
	 * down until {LWINUP} is sent</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial"
	 * size="2">{RWINDOWN}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Holds the right Windows key
	 * down until {RWINUP} is sent</font></td>
	 * </tr>
	 * <tr>
	 * <td width="50%" align="center"><font face="Arial" size="2">{ASC
	 * nnnn}</font></td>
	 * <td width="50%"><font face="Arial" size="2">Send the ALT+nnnn key
	 * combination</font></td>
	 * </tr>
	 * </table>
	 * <p>
	 * <font face="Arial" size="2"><br>
	 * <i>To send the ASCII value A (same as pressing ALT+65 on the numeric
	 * keypad)<br>
	 * </i>&nbsp;&nbsp;&nbsp; Send &quot;{ASC 65}&quot;<br>
	 * <br>
	 * <i>Single keys can also be repeated, e.g.<br>
	 * </i>&nbsp;&nbsp;&nbsp; Send &quot;{DEL 4}&quot;&nbsp;&nbsp;&nbsp; Presses
	 * the DEL key 4 times<br>
	 * &nbsp;&nbsp;&nbsp; Send &quot;{S
	 * 30}&quot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Sends 30 'S' characters<br>
	 * &nbsp;&nbsp;&nbsp; Send &quot;+{TAB 4}&quot;&nbsp; Presses SHIFT+TAB 4
	 * times<br>
	 * </font>
	 * </p>
	 * 
	 * @param line
	 *            A string pointer to the set of keystrokes to send.
	 */
	public static void send(final String line) {
		autoItDLL.AUTOIT_Send(line);
	}

	/**
	 * This command will correctly set the state of the CAPSLOCK key to either
	 * on or off.
	 * 
	 * @param toggle
	 *            This should be set to true to set capslock to ON, and false to
	 *            set capslock to OFF.
	 * @see #setStoreCapslockMode(boolean)
	 */
	public static void setCapslockState(final boolean toggle) {
		autoItDLL.AUTOIT_SetCapslockState(toggle ? 1 : 0);
	}

	/**
	 * This functions will alter the amount of time that AutoIt pauses between
	 * each simulated keypress.
	 * 
	 * @param delay
	 *            The value in milliseconds pause between each simulated
	 *            keypress. (Default 20ms).
	 */
	public static void setKeyDelay(final int delay) {
		autoItDLL.AUTOIT_SetKeyDelay((delay < 0) ? DEFAULT_KEY_DELAY : delay);
	}

	/**
	 * By default, at the start of a "Send" command AutoIt will store the state
	 * of the CAPSLOCK key; at the end of the "Send" command this status will be
	 * restored. Use this command to turn off this behaviour.
	 * 
	 * @param toggle
	 *            This should be set to true to store/restore the capslock at
	 *            the start/end of a send command, and false to ignore capslock
	 *            state. (Default is true).
	 * @see #setCapslockState(boolean)
	 */
	public static void setStoreCapslockMode(final boolean toggle) {
		autoItDLL.AUTOIT_SetStoreCapslockMode(toggle ? 1 : 0);
	}

	/**
	 * This function will alter the way that AutoIt matches window titles in
	 * functions such as AUTOIT_WinWait, AUTOIT_IfWinActive, etc.
	 * 
	 * Remarks:
	 * 
	 * mode TitleMatchMode.START_WITH:
	 * 
	 * In the szTitle string you specify the start of a window title to match.
	 * i.e. for the notepad.exe window (Untitled - Notepad), valid matches would
	 * be: "Untitled", "Untitled -", "Unt" and "Untitled - Notepad".
	 * 
	 * mode TitleMatchMode.ANY:
	 * 
	 * In the szTitle string you can specify ANY substring of the window title
	 * you want to match. Again for the notepad.exe window valid matches would
	 * be: "Untitled", "Untitled - Notepad", "Notepad", "No".
	 * 
	 * @param mode
	 *            The mode of title matching to use -- see the remarks.
	 */
	public static void setTitleMatchMode(final TitleMatchMode mode) {
		autoItDLL.AUTOIT_SetTitleMatchMode(mode.getValue());
	}

	/**
	 * This functions will alter the amount of time that AutoIt pauses after
	 * performing a WinWait-type function.
	 * 
	 * @param delay
	 *            The value in milliseconds pause after a WinWait-related
	 *            function. (Default 500ms).
	 */
	public static void setWinDelay(final int delay) {
		autoItDLL.AUTOIT_SetWinDelay((delay < 0) ? DEFAULT_WIN_DELAY : delay);
	}

	/**
	 * This function can perform various types of shutdown on all Windows
	 * operating systems.
	 * 
	 * Remarks: The value of nFlag can be a combination of the flags below:
	 * 
	 * @param shutdownModes
	 *            A combination of the modes given in the remarks section.
	 */
	public static void shutdown(final ShutdownMode... shutdownModes) {
		if (shutdownModes.length == 0) {
			throw new IllegalArgumentException(
					"Parameter shutdownModes is requried.");
		}
		int modes = 0;
		for (ShutdownMode shutdownMode : shutdownModes) {
			modes += shutdownMode.getValue();
		}
		autoItDLL.AUTOIT_Shutdown(modes);
	}

	/**
	 * This function simply pauses for an amount of time.
	 * 
	 * @param milliseconds
	 *            The amount of time to sleep in milliseconds. (Note: 1000
	 *            milliseconds = 1 second)
	 */
	public static void sleep(final int milliseconds) {
		autoItDLL.AUTOIT_Sleep(milliseconds);
	}

	/**
	 * This function will activate a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winActivate(final String title) {
		winActivate(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will activate a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winActivate(final String title, final String text) {
		autoItDLL.AUTOIT_WinActivate(title, text);
	}

	/**
	 * This function will close a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winClose(final String title) {
		winClose(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will close a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winClose(final String title, final String text) {
		autoItDLL.AUTOIT_WinClose(title, text);
	}

	/**
	 * This function will return the title of the current active window.
	 * 
	 * Remarks: A maximum of 16384 characters of text can be received; ensure
	 * that the text buffer you pass is at least this large.
	 * 
	 * @return Returns the title of the current active window.
	 * @see #winSetTitle(String, String, String)
	 */
	public static String winGetActiveTitle() {
		final byte[] title = new byte[WIN_GET_ACTIVE_TITLE_BUF_SIZE];
		autoItDLL.AUTOIT_WinGetActiveTitle(title);
		return Native.toString(title);
	}

	/**
	 * This function will hide a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winHide(final String title) {
		winHide(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will hide a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winHide(final String title, final String text) {
		autoItDLL.AUTOIT_WinHide(title, text);
	}

	/**
	 * This function will forceably close a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winKill(final String title) {
		winKill(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will forceably close a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winKill(final String title, final String text) {
		autoItDLL.AUTOIT_WinKill(title, text);
	}

	/**
	 * This function will maximize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winMaximize(final String title) {
		winMaximize(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will maximize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winMaximize(final String title, final String text) {
		autoItDLL.AUTOIT_WinMaximize(title, text);
	}

	/**
	 * This function will minimize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winMinimize(final String title) {
		winMinimize(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will minimize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winMinimize(final String title, final String text) {
		autoItDLL.AUTOIT_WinMinimize(title, text);
	}

	/**
	 * This function will minimize all windows.
	 * 
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winMinimizeAll() {
		autoItDLL.AUTOIT_WinMinimizeAll();
	}

	/**
	 * This function will undo a previous AUTOIT_WinMinimizeAll call.
	 * 
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winRestore(String, String)
	 * @see #winShow(String, String)
	 */
	public static void winMinimizeAllUndo() {
		autoItDLL.AUTOIT_WinMinimizeAllUndo();
	}

	/**
	 * Use this function to move/resize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param x
	 *            X coordinate (to coordinate)
	 * @param y
	 *            Y1 coordinate (to coordinate)
	 */
	public static void winMove(final String title, final int x, final int y) {
		winMove(title, DEFAULT_WIN_TEXT, x, y, -1, -1);
	}

	/**
	 * Use this function to move/resize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param x
	 *            X coordinate (to coordinate)
	 * @param y
	 *            Y1 coordinate (to coordinate)
	 * @param width
	 *            New width of the window (use -1 to leave the size alone)
	 * @param height
	 *            New height of the window (use -1 to leave the size alone)
	 */
	public static void winMove(final String title, final int x, final int y,
			final int width, final int height) {
		winMove(title, DEFAULT_WIN_TEXT, x, y, width, height);
	}

	/**
	 * Use this function to move/resize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @param x
	 *            X coordinate (to coordinate)
	 * @param y
	 *            Y1 coordinate (to coordinate)
	 */
	public static void winMove(final String title, final String text,
			final int x, final int y) {
		winMove(title, text, x, y, -1, -1);
	}

	/**
	 * Use this function to move/resize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @param x
	 *            X coordinate (to coordinate)
	 * @param y
	 *            Y1 coordinate (to coordinate)
	 * @param width
	 *            New width of the window (use -1 to leave the size alone)
	 * @param height
	 *            New height of the window (use -1 to leave the size alone)
	 */
	public static void winMove(final String title, final String text,
			final int x, final int y, final int width, final int height) {
		autoItDLL.AUTOIT_WinMove(title, text, x, y, width, height);
	}

	/**
	 * This function will restore a window from a minimized state.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winShow(String, String)
	 */
	public static void winRestore(final String title) {
		winRestore(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will restore a window from a minimized state.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winShow(String, String)
	 */
	public static void winRestore(final String title, final String text) {
		autoItDLL.AUTOIT_WinRestore(title, text);
	}

	/**
	 * Changes the title of a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param newTitle
	 *            A string pointer to the text of the new window title.
	 * @see #winGetActiveTitle()
	 */
	public static void winSetTitle(final String title, final String newTitle) {
		winSetTitle(title, DEFAULT_WIN_TEXT, newTitle);
	}

	/**
	 * Changes the title of a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @param newTitle
	 *            A string pointer to the text of the new window title.
	 * @see #winGetActiveTitle()
	 */
	public static void winSetTitle(final String title, final String text,
			final String newTitle) {
		autoItDLL.AUTOIT_WinSetTitle(title, text, newTitle);
	}

	/**
	 * This function will show a specified window previously hidden.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 */
	public static void winShow(final String title) {
		winShow(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will show a specified window previously hidden.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #winActivate(String, String)
	 * @see #winClose(String, String)
	 * @see #winHide(String, String)
	 * @see #winKill(String, String)
	 * @see #winMaximize(String, String)
	 * @see #winMinimize(String, String)
	 * @see #winMinimizeAll()
	 * @see #winMinimizeAllUndo()
	 * @see #winRestore(String, String)
	 */
	public static void winShow(final String title, final String text) {
		autoItDLL.AUTOIT_WinShow(title, text);
	}

	/**
	 * This function will pause until the specified window exists (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWait(final String title) {
		return winWait(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will pause until the specified window exists (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param timeout
	 *            Specifies the number of seconds to wait for. Use the value 0
	 *            to wait indefinitely.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWait(final String title, final int timeout) {
		return winWait(title, DEFAULT_WIN_TEXT, timeout);
	}

	/**
	 * This function will pause until the specified window exists (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWait(final String title, final String text) {
		return winWait(title, text, 0);
	}

	/**
	 * This function will pause until the specified window exists (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @param timeout
	 *            Specifies the number of seconds to wait for. Use the value 0
	 *            to wait indefinitely.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWait(final String title, final String text,
			final int timeout) {
		final int value = autoItDLL.AUTOIT_WinWait(title, text, timeout);
		return value == SUCCESS_WIN_WAIT;
	}

	/**
	 * This function will pause until the specified window is active (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWait(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWaitActive(final String title) {
		return winWaitActive(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will pause until the specified window is active (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param timeout
	 *            Specifies the number of seconds to wait for. Use the value 0
	 *            to wait indefinitely.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWait(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWaitActive(final String title, final int timeout) {
		return winWaitActive(title, DEFAULT_WIN_TEXT, timeout);
	}

	/**
	 * This function will pause until the specified window is active (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWait(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWaitActive(final String title, final String text) {
		return winWaitActive(title, text, 0);
	}

	/**
	 * This function will pause until the specified window is active (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @param timeout
	 *            Specifies the number of seconds to wait for. Use the value 0
	 *            to wait indefinitely.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWait(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWaitActive(final String title, final String text,
			final int timeout) {
		final int value = autoItDLL.AUTOIT_WinWaitActive(title, text, timeout);
		return value == SUCCESS_WIN_WAIT;
	}

	/**
	 * This function will pause until the specified window doesn't exist (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWait(String, String, int)
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWaitClose(final String title) {
		return winWaitClose(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will pause until the specified window doesn't exist (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param timeout
	 *            Specifies the number of seconds to wait for. Use the value 0
	 *            to wait indefinitely.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWait(String, String, int)
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWaitClose(final String title, final int timeout) {
		return winWaitClose(title, DEFAULT_WIN_TEXT, timeout);
	}

	/**
	 * This function will pause until the specified window doesn't exist (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWait(String, String, int)
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWaitClose(final String title, final String text) {
		return winWaitClose(title, text, 0);
	}

	/**
	 * This function will pause until the specified window doesn't exist (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @param timeout
	 *            Specifies the number of seconds to wait for. Use the value 0
	 *            to wait indefinitely.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns false.
	 * @see #winWait(String, String, int)
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitNotActive(String, String, int)
	 */
	public static boolean winWaitClose(final String title, final String text,
			final int timeout) {
		final int value = autoItDLL.AUTOIT_WinWaitClose(title, text, timeout);
		return value == SUCCESS_WIN_WAIT;
	}

	/**
	 * This function will pause until the specified window is not active (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWait(String, String, int)
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 */
	public static boolean winWaitNotActive(final String title) {
		return winWaitNotActive(title, DEFAULT_WIN_TEXT);
	}

	/**
	 * This function will pause until the specified window is not active (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param timeout
	 *            Specifies the number of seconds to wait for. Use the value 0
	 *            to wait indefinitely.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWait(String, String, int)
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 */
	public static boolean winWaitNotActive(final String title, final int timeout) {
		return winWaitNotActive(title, DEFAULT_WIN_TEXT, timeout);
	}

	/**
	 * This function will pause until the specified window is not active (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #winWait(String, String, int)
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 */
	public static boolean winWaitNotActive(final String title, final String text) {
		return winWaitNotActive(title, text, 0);
	}

	/**
	 * This function will pause until the specified window is not active (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @param timeout
	 *            Specifies the number of seconds to wait for. Use the value 0
	 *            to wait indefinitely.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #winWait(String, String, int)
	 * @see #winWaitActive(String, String, int)
	 * @see #winWaitClose(String, String, int)
	 */
	public static boolean winWaitNotActive(final String title,
			final String text, final int timeout) {
		final int value = autoItDLL.AUTOIT_WinWaitNotActive(title, text,
				timeout);
		return value == SUCCESS_WIN_WAIT;
	}

	/**
	 * Special keys.
	 * 
	 * Certain special keys can be sent and should be enclosed in braces.
	 * 
	 * To send the ASCII value A (same as pressing ALT+65 on the numeric
	 * keypad): Send "{ASC 65}"
	 * 
	 * Single keys can also be repeated, e.g:
	 * 
	 * Send "{DEL 4}" Presses the DEL key 4 times
	 * 
	 * Send "{S 30}"
	 * 
	 * Sends 30 'S' characters
	 * 
	 * Send "+{TAB 4}" Presses SHIFT+TAB 4 times
	 * 
	 * @author zhengbo.wang
	 */
	public static interface Keys {
		// {!}
		// {!}
		// {#}
		// {+}
		// {^}
		// {{}
		// {}}

		public static final String SPACE = "{SPACE}";
		public static final String ENTER = "{ENTER}";
		public static final String ALT = "{ALT}";
		public static final String BACKSPACE = "{BACKSPACE}";
		public static final String BS = "{BS}";
		public static final String DELETE = "{DELETE}";
		public static final String DEL = "{DEL}";
		/* Cursor up */
		public static final String UP = "{UP}";
		/* Cursor down */
		public static final String DOWN = "{DOWN}";
		/* Cursor left */
		public static final String LEFT = "{LEFT}";
		/* Cursor right */
		public static final String RIGHT = "{RIGHT}";
		public static final String HOME = "{HOME}";
		public static final String END = "{END}";
		public static final String ESCAPE = "{ESCAPE}";
		public static final String ESC = "{ESC}";
		public static final String INSERT = "{INSERT}";
		public static final String INS = "{INS}";
		public static final String PGUP = "{PGUP}";
		public static final String PGDN = "{PGDN}";
		/* Function keys */
		public static final String F1 = "{F1}";
		public static final String F2 = "{F2}";
		public static final String F3 = "{F3}";
		public static final String F4 = "{F4}";
		public static final String F5 = "{F5}";
		public static final String F6 = "{F6}";
		public static final String F7 = "{F7}";
		public static final String F8 = "{F8}";
		public static final String F9 = "{F9}";
		public static final String F10 = "{F10}";
		public static final String F11 = "{F11}";
		public static final String F12 = "{F12}";
		public static final String TAB = "{TAB}";
		public static final String PRINTSCREEN = "{PRINTSCREEN}";
		/* Left Windows key */
		public static final String LWIN = "{LWIN}";
		/* Right Windows key */
		public static final String RWIN = "{RWIN}";
		public static final String NUMLOCK = "{NUMLOCK}";
		/* Ctrl+break */
		public static final String CTRLBREAK = "{CTRLBREAK}";
		public static final String PAUSE = "{PAUSE}";
		public static final String CAPSLOCK = "{CAPSLOCK}";
		/* Numpad digits */
		public static final String NUMPAD_0 = "{NUMPAD0}";
		public static final String NUMPAD_1 = "{NUMPAD1}";
		public static final String NUMPAD_2 = "{NUMPAD2}";
		public static final String NUMPAD_3 = "{NUMPAD3}";
		public static final String NUMPAD_4 = "{NUMPAD4}";
		public static final String NUMPAD_5 = "{NUMPAD5}";
		public static final String NUMPAD_6 = "{NUMPAD6}";
		public static final String NUMPAD_7 = "{NUMPAD7}";
		public static final String NUMPAD_8 = "{NUMPAD8}";
		public static final String NUMPAD_9 = "{NUMPAD9}";
		/* Numpad Multiply */
		public static final String NUMPAD_MULT = "{NUMPADMULT}";
		/* Numpad Add */
		public static final String NUMPAD_ADD = "{NUMPADADD}";
		/* Numpad Subtract */
		public static final String NUMPAD_SUB = "{NUMPADSUB}";
		/* Numpad Divide */
		public static final String NUMPAD_DIV = "{NUMPADDIV}";
		/* Numpad period */
		public static final String NUMPAD_DOT = "{NUMPADDOT}";
		/* Windows App key */
		public static final String APPSKEY = "{APPSKEY}";
		/* Holds the ALT key down until {ALTUP} is sent */
		public static final String ALTDOWN = "{ALTDOWN}";
		/* Holds the SHIFT key down until {SHIFTUP} is sent */
		public static final String SHIFTDOWN = "{SHIFTDOWN}";
		/* Holds the CTRL key down until {CTRLUP} is sent */
		public static final String CTRLDOWN = "{CTRLDOWN}";
		/* Holds the left Windows key down until {LWINUP} is sent */
		public static final String LWINDOWN = "{LWINDOWN}";
		/* Holds the right Windows key down until {RWINUP} is sent */
		public static final String RWINDOWN = "{RWINDOWN}";
		/* Send the ALT+nnnn key combination */
		public static final String ASCII_0 = "{ASC 0}";
		public static final String ASCII_1 = "{ASC 1}";
		public static final String ASCII_2 = "{ASC 2}";
		public static final String ASCII_3 = "{ASC 3}";
		public static final String ASCII_4 = "{ASC 4}";
		public static final String ASCII_5 = "{ASC 5}";
		public static final String ASCII_6 = "{ASC 6}";
		public static final String ASCII_7 = "{ASC 7}";
		public static final String ASCII_8 = "{ASC 8}";
		public static final String ASCII_9 = "{ASC 9}";
		public static final String ASCII_10 = "{ASC 10}";
		public static final String ASCII_11 = "{ASC 11}";
		public static final String ASCII_12 = "{ASC 12}";
		public static final String ASCII_13 = "{ASC 13}";
		public static final String ASCII_14 = "{ASC 14}";
		public static final String ASCII_15 = "{ASC 15}";
		public static final String ASCII_16 = "{ASC 16}";
		public static final String ASCII_17 = "{ASC 17}";
		public static final String ASCII_18 = "{ASC 18}";
		public static final String ASCII_19 = "{ASC 19}";
		public static final String ASCII_20 = "{ASC 20}";
		public static final String ASCII_21 = "{ASC 21}";
		public static final String ASCII_22 = "{ASC 22}";
		public static final String ASCII_23 = "{ASC 23}";
		public static final String ASCII_24 = "{ASC 24}";
		public static final String ASCII_25 = "{ASC 25}";
		public static final String ASCII_26 = "{ASC 26}";
		public static final String ASCII_27 = "{ASC 27}";
		public static final String ASCII_28 = "{ASC 28}";
		public static final String ASCII_29 = "{ASC 29}";
		public static final String ASCII_30 = "{ASC 30}";
		public static final String ASCII_31 = "{ASC 31}";
		public static final String ASCII_32 = "{ASC 32}";
		public static final String ASCII_33 = "{ASC 33}";
		public static final String ASCII_34 = "{ASC 34}";
		public static final String ASCII_35 = "{ASC 35}";
		public static final String ASCII_36 = "{ASC 36}";
		public static final String ASCII_37 = "{ASC 37}";
		public static final String ASCII_38 = "{ASC 38}";
		public static final String ASCII_39 = "{ASC 39}";
		public static final String ASCII_40 = "{ASC 40}";
		public static final String ASCII_41 = "{ASC 41}";
		public static final String ASCII_42 = "{ASC 42}";
		public static final String ASCII_43 = "{ASC 43}";
		public static final String ASCII_44 = "{ASC 44}";
		public static final String ASCII_45 = "{ASC 45}";
		public static final String ASCII_46 = "{ASC 46}";
		public static final String ASCII_47 = "{ASC 47}";
		public static final String ASCII_48 = "{ASC 48}";
		public static final String ASCII_49 = "{ASC 49}";
		public static final String ASCII_50 = "{ASC 50}";
		public static final String ASCII_51 = "{ASC 51}";
		public static final String ASCII_52 = "{ASC 52}";
		public static final String ASCII_53 = "{ASC 53}";
		public static final String ASCII_54 = "{ASC 54}";
		public static final String ASCII_55 = "{ASC 55}";
		public static final String ASCII_56 = "{ASC 56}";
		public static final String ASCII_57 = "{ASC 57}";
		public static final String ASCII_58 = "{ASC 58}";
		public static final String ASCII_59 = "{ASC 59}";
		public static final String ASCII_60 = "{ASC 60}";
		public static final String ASCII_61 = "{ASC 61}";
		public static final String ASCII_62 = "{ASC 62}";
		public static final String ASCII_63 = "{ASC 63}";
		public static final String ASCII_64 = "{ASC 64}";
		public static final String ASCII_65 = "{ASC 65}";
		public static final String ASCII_66 = "{ASC 66}";
		public static final String ASCII_67 = "{ASC 67}";
		public static final String ASCII_68 = "{ASC 68}";
		public static final String ASCII_69 = "{ASC 69}";
		public static final String ASCII_70 = "{ASC 70}";
		public static final String ASCII_71 = "{ASC 71}";
		public static final String ASCII_72 = "{ASC 72}";
		public static final String ASCII_73 = "{ASC 73}";
		public static final String ASCII_74 = "{ASC 74}";
		public static final String ASCII_75 = "{ASC 75}";
		public static final String ASCII_76 = "{ASC 76}";
		public static final String ASCII_77 = "{ASC 77}";
		public static final String ASCII_78 = "{ASC 78}";
		public static final String ASCII_79 = "{ASC 79}";
		public static final String ASCII_80 = "{ASC 80}";
		public static final String ASCII_81 = "{ASC 81}";
		public static final String ASCII_82 = "{ASC 82}";
		public static final String ASCII_83 = "{ASC 83}";
		public static final String ASCII_84 = "{ASC 84}";
		public static final String ASCII_85 = "{ASC 85}";
		public static final String ASCII_86 = "{ASC 86}";
		public static final String ASCII_87 = "{ASC 87}";
		public static final String ASCII_88 = "{ASC 88}";
		public static final String ASCII_89 = "{ASC 89}";
		public static final String ASCII_90 = "{ASC 90}";
		public static final String ASCII_91 = "{ASC 91}";
		public static final String ASCII_92 = "{ASC 92}";
		public static final String ASCII_93 = "{ASC 93}";
		public static final String ASCII_94 = "{ASC 94}";
		public static final String ASCII_95 = "{ASC 95}";
		public static final String ASCII_96 = "{ASC 96}";
		public static final String ASCII_97 = "{ASC 97}";
		public static final String ASCII_98 = "{ASC 98}";
		public static final String ASCII_99 = "{ASC 99}";
		public static final String ASCII_100 = "{ASC 100}";
		public static final String ASCII_101 = "{ASC 101}";
		public static final String ASCII_102 = "{ASC 102}";
		public static final String ASCII_103 = "{ASC 103}";
		public static final String ASCII_104 = "{ASC 104}";
		public static final String ASCII_105 = "{ASC 105}";
		public static final String ASCII_106 = "{ASC 106}";
		public static final String ASCII_107 = "{ASC 107}";
		public static final String ASCII_108 = "{ASC 108}";
		public static final String ASCII_109 = "{ASC 109}";
		public static final String ASCII_110 = "{ASC 110}";
		public static final String ASCII_111 = "{ASC 111}";
		public static final String ASCII_112 = "{ASC 112}";
		public static final String ASCII_113 = "{ASC 113}";
		public static final String ASCII_114 = "{ASC 114}";
		public static final String ASCII_115 = "{ASC 115}";
		public static final String ASCII_116 = "{ASC 116}";
		public static final String ASCII_117 = "{ASC 117}";
		public static final String ASCII_118 = "{ASC 118}";
		public static final String ASCII_119 = "{ASC 119}";
		public static final String ASCII_120 = "{ASC 120}";
		public static final String ASCII_121 = "{ASC 121}";
		public static final String ASCII_122 = "{ASC 122}";
		public static final String ASCII_123 = "{ASC 123}";
		public static final String ASCII_124 = "{ASC 124}";
		public static final String ASCII_125 = "{ASC 125}";
		public static final String ASCII_126 = "{ASC 126}";
		public static final String ASCII_127 = "{ASC 127}";
		public static final String ASCII_128 = "{ASC 128}";
		public static final String ASCII_129 = "{ASC 129}";
		public static final String ASCII_130 = "{ASC 130}";
		public static final String ASCII_131 = "{ASC 131}";
		public static final String ASCII_132 = "{ASC 132}";
		public static final String ASCII_133 = "{ASC 133}";
		public static final String ASCII_134 = "{ASC 134}";
		public static final String ASCII_135 = "{ASC 135}";
		public static final String ASCII_136 = "{ASC 136}";
		public static final String ASCII_137 = "{ASC 137}";
		public static final String ASCII_138 = "{ASC 138}";
		public static final String ASCII_139 = "{ASC 139}";
		public static final String ASCII_140 = "{ASC 140}";
		public static final String ASCII_141 = "{ASC 141}";
		public static final String ASCII_142 = "{ASC 142}";
		public static final String ASCII_143 = "{ASC 143}";
		public static final String ASCII_144 = "{ASC 144}";
		public static final String ASCII_145 = "{ASC 145}";
		public static final String ASCII_146 = "{ASC 146}";
		public static final String ASCII_147 = "{ASC 147}";
		public static final String ASCII_148 = "{ASC 148}";
		public static final String ASCII_149 = "{ASC 149}";
		public static final String ASCII_150 = "{ASC 150}";
		public static final String ASCII_151 = "{ASC 151}";
		public static final String ASCII_152 = "{ASC 152}";
		public static final String ASCII_153 = "{ASC 153}";
		public static final String ASCII_154 = "{ASC 154}";
		public static final String ASCII_155 = "{ASC 155}";
		public static final String ASCII_156 = "{ASC 156}";
		public static final String ASCII_157 = "{ASC 157}";
		public static final String ASCII_158 = "{ASC 158}";
		public static final String ASCII_159 = "{ASC 159}";
		public static final String ASCII_160 = "{ASC 160}";
		public static final String ASCII_161 = "{ASC 161}";
		public static final String ASCII_162 = "{ASC 162}";
		public static final String ASCII_163 = "{ASC 163}";
		public static final String ASCII_164 = "{ASC 164}";
		public static final String ASCII_165 = "{ASC 165}";
		public static final String ASCII_166 = "{ASC 166}";
		public static final String ASCII_167 = "{ASC 167}";
		public static final String ASCII_168 = "{ASC 168}";
		public static final String ASCII_169 = "{ASC 169}";
		public static final String ASCII_170 = "{ASC 170}";
		public static final String ASCII_171 = "{ASC 171}";
		public static final String ASCII_172 = "{ASC 172}";
		public static final String ASCII_173 = "{ASC 173}";
		public static final String ASCII_174 = "{ASC 174}";
		public static final String ASCII_175 = "{ASC 175}";
		public static final String ASCII_176 = "{ASC 176}";
		public static final String ASCII_177 = "{ASC 177}";
		public static final String ASCII_178 = "{ASC 178}";
		public static final String ASCII_179 = "{ASC 179}";
		public static final String ASCII_180 = "{ASC 180}";
		public static final String ASCII_181 = "{ASC 181}";
		public static final String ASCII_182 = "{ASC 182}";
		public static final String ASCII_183 = "{ASC 183}";
		public static final String ASCII_184 = "{ASC 184}";
		public static final String ASCII_185 = "{ASC 185}";
		public static final String ASCII_186 = "{ASC 186}";
		public static final String ASCII_187 = "{ASC 187}";
		public static final String ASCII_188 = "{ASC 188}";
		public static final String ASCII_189 = "{ASC 189}";
		public static final String ASCII_190 = "{ASC 190}";
		public static final String ASCII_191 = "{ASC 191}";
		public static final String ASCII_192 = "{ASC 192}";
		public static final String ASCII_193 = "{ASC 193}";
		public static final String ASCII_194 = "{ASC 194}";
		public static final String ASCII_195 = "{ASC 195}";
		public static final String ASCII_196 = "{ASC 196}";
		public static final String ASCII_197 = "{ASC 197}";
		public static final String ASCII_198 = "{ASC 198}";
		public static final String ASCII_199 = "{ASC 199}";
		public static final String ASCII_200 = "{ASC 200}";
		public static final String ASCII_201 = "{ASC 201}";
		public static final String ASCII_202 = "{ASC 202}";
		public static final String ASCII_203 = "{ASC 203}";
		public static final String ASCII_204 = "{ASC 204}";
		public static final String ASCII_205 = "{ASC 205}";
		public static final String ASCII_206 = "{ASC 206}";
		public static final String ASCII_207 = "{ASC 207}";
		public static final String ASCII_208 = "{ASC 208}";
		public static final String ASCII_209 = "{ASC 209}";
		public static final String ASCII_210 = "{ASC 210}";
		public static final String ASCII_211 = "{ASC 211}";
		public static final String ASCII_212 = "{ASC 212}";
		public static final String ASCII_213 = "{ASC 213}";
		public static final String ASCII_214 = "{ASC 214}";
		public static final String ASCII_215 = "{ASC 215}";
		public static final String ASCII_216 = "{ASC 216}";
		public static final String ASCII_217 = "{ASC 217}";
		public static final String ASCII_218 = "{ASC 218}";
		public static final String ASCII_219 = "{ASC 219}";
		public static final String ASCII_220 = "{ASC 220}";
		public static final String ASCII_221 = "{ASC 221}";
		public static final String ASCII_222 = "{ASC 222}";
		public static final String ASCII_223 = "{ASC 223}";
		public static final String ASCII_224 = "{ASC 224}";
		public static final String ASCII_225 = "{ASC 225}";
		public static final String ASCII_226 = "{ASC 226}";
		public static final String ASCII_227 = "{ASC 227}";
		public static final String ASCII_228 = "{ASC 228}";
		public static final String ASCII_229 = "{ASC 229}";
		public static final String ASCII_230 = "{ASC 230}";
		public static final String ASCII_231 = "{ASC 231}";
		public static final String ASCII_232 = "{ASC 232}";
		public static final String ASCII_233 = "{ASC 233}";
		public static final String ASCII_234 = "{ASC 234}";
		public static final String ASCII_235 = "{ASC 235}";
		public static final String ASCII_236 = "{ASC 236}";
		public static final String ASCII_237 = "{ASC 237}";
		public static final String ASCII_238 = "{ASC 238}";
		public static final String ASCII_239 = "{ASC 239}";
		public static final String ASCII_240 = "{ASC 240}";
		public static final String ASCII_241 = "{ASC 241}";
		public static final String ASCII_242 = "{ASC 242}";
		public static final String ASCII_243 = "{ASC 243}";
		public static final String ASCII_244 = "{ASC 244}";
		public static final String ASCII_245 = "{ASC 245}";
		public static final String ASCII_246 = "{ASC 246}";
		public static final String ASCII_247 = "{ASC 247}";
		public static final String ASCII_248 = "{ASC 248}";
		public static final String ASCII_249 = "{ASC 249}";
		public static final String ASCII_250 = "{ASC 250}";
		public static final String ASCII_251 = "{ASC 251}";
		public static final String ASCII_252 = "{ASC 252}";
		public static final String ASCII_253 = "{ASC 253}";
		public static final String ASCII_254 = "{ASC 254}";
		public static final String ASCII_255 = "{ASC 255}";
		public static final String[] ASCII = new String[] { ASCII_0, ASCII_1,
				ASCII_2, ASCII_3, ASCII_4, ASCII_5, ASCII_6, ASCII_7, ASCII_8,
				ASCII_9, ASCII_10, ASCII_11, ASCII_12, ASCII_13, ASCII_14,
				ASCII_15, ASCII_16, ASCII_17, ASCII_18, ASCII_19, ASCII_20,
				ASCII_21, ASCII_22, ASCII_23, ASCII_24, ASCII_25, ASCII_26,
				ASCII_27, ASCII_28, ASCII_29, ASCII_30, ASCII_31, ASCII_32,
				ASCII_33, ASCII_34, ASCII_35, ASCII_36, ASCII_37, ASCII_38,
				ASCII_39, ASCII_40, ASCII_41, ASCII_42, ASCII_43, ASCII_44,
				ASCII_45, ASCII_46, ASCII_47, ASCII_48, ASCII_49, ASCII_50,
				ASCII_51, ASCII_52, ASCII_53, ASCII_54, ASCII_55, ASCII_56,
				ASCII_57, ASCII_58, ASCII_59, ASCII_60, ASCII_61, ASCII_62,
				ASCII_63, ASCII_64, ASCII_65, ASCII_66, ASCII_67, ASCII_68,
				ASCII_69, ASCII_70, ASCII_71, ASCII_72, ASCII_73, ASCII_74,
				ASCII_75, ASCII_76, ASCII_77, ASCII_78, ASCII_79, ASCII_80,
				ASCII_81, ASCII_82, ASCII_83, ASCII_84, ASCII_85, ASCII_86,
				ASCII_87, ASCII_88, ASCII_89, ASCII_90, ASCII_91, ASCII_92,
				ASCII_93, ASCII_94, ASCII_95, ASCII_96, ASCII_97, ASCII_98,
				ASCII_99, ASCII_100, ASCII_101, ASCII_102, ASCII_103,
				ASCII_104, ASCII_105, ASCII_106, ASCII_107, ASCII_108,
				ASCII_109, ASCII_110, ASCII_111, ASCII_112, ASCII_113,
				ASCII_114, ASCII_115, ASCII_116, ASCII_117, ASCII_118,
				ASCII_119, ASCII_120, ASCII_121, ASCII_122, ASCII_123,
				ASCII_124, ASCII_125, ASCII_126, ASCII_127, ASCII_128,
				ASCII_129, ASCII_130, ASCII_131, ASCII_132, ASCII_133,
				ASCII_134, ASCII_135, ASCII_136, ASCII_137, ASCII_138,
				ASCII_139, ASCII_140, ASCII_141, ASCII_142, ASCII_143,
				ASCII_144, ASCII_145, ASCII_146, ASCII_147, ASCII_148,
				ASCII_149, ASCII_150, ASCII_151, ASCII_152, ASCII_153,
				ASCII_154, ASCII_155, ASCII_156, ASCII_157, ASCII_158,
				ASCII_159, ASCII_160, ASCII_161, ASCII_162, ASCII_163,
				ASCII_164, ASCII_165, ASCII_166, ASCII_167, ASCII_168,
				ASCII_169, ASCII_170, ASCII_171, ASCII_172, ASCII_173,
				ASCII_174, ASCII_175, ASCII_176, ASCII_177, ASCII_178,
				ASCII_179, ASCII_180, ASCII_181, ASCII_182, ASCII_183,
				ASCII_184, ASCII_185, ASCII_186, ASCII_187, ASCII_188,
				ASCII_189, ASCII_190, ASCII_191, ASCII_192, ASCII_193,
				ASCII_194, ASCII_195, ASCII_196, ASCII_197, ASCII_198,
				ASCII_199, ASCII_200, ASCII_201, ASCII_202, ASCII_203,
				ASCII_204, ASCII_205, ASCII_206, ASCII_207, ASCII_208,
				ASCII_209, ASCII_210, ASCII_211, ASCII_212, ASCII_213,
				ASCII_214, ASCII_215, ASCII_216, ASCII_217, ASCII_218,
				ASCII_219, ASCII_220, ASCII_221, ASCII_222, ASCII_223,
				ASCII_224, ASCII_225, ASCII_226, ASCII_227, ASCII_228,
				ASCII_229, ASCII_230, ASCII_231, ASCII_232, ASCII_233,
				ASCII_234, ASCII_235, ASCII_236, ASCII_237, ASCII_238,
				ASCII_239, ASCII_240, ASCII_241, ASCII_242, ASCII_243,
				ASCII_244, ASCII_245, ASCII_246, ASCII_247, ASCII_248,
				ASCII_249, ASCII_250, ASCII_251, ASCII_252, ASCII_253,
				ASCII_254, ASCII_255 };
	}

	/**
	 * The way that AutoIt matches window titles in functions such as
	 * AUTOIT_WinWait, AUTOIT_IfWinActive, etc, default is
	 * TitleMatchMode.START_WITH.
	 * 
	 * @author zhengbo.wang
	 */
	public static enum TitleMatchMode {
		/*
		 * In the szTitle string you specify the start of a window title to
		 * match. i.e. for the notepad.exe window (Untitled - Notepad), valid
		 * matches would be: "Untitled", "Untitled -", "Unt" and
		 * "Untitled - Notepad".
		 */
		START_WITH(1),

		/*
		 * In the szTitle string you can specify ANY substring of the window
		 * title you want to match. Again for the notepad.exe window valid
		 * matches would be: "Untitled", "Untitled - Notepad", "Notepad", "No".
		 */
		ANY(2);

		private final int value;

		private TitleMatchMode(final int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	/**
	 * Shutdown mode.
	 * 
	 * @author zhengbo.wang
	 */
	public static enum ShutdownMode {
		/* Log off the current user */
		LOG_OFF(0),

		/* Shutdown the workstation */
		SHUTDOWN(1),

		/* Reboot the workstation */
		REBOOT(2),

		/* Force closing of applications (may lose unsaved work) */
		FORCE_CLOSING_APPLICATIONS(4),

		/* Shutdown and power off (if supported) */
		SHUTDOWN_AND_POWER_OFF(8);

		private final int value;

		private ShutdownMode(final int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	private static interface AutoItDLLLibrary extends Library {
		/**
		 * Will prevent any user input via the mouse or keyboard. See the
		 * Remarks section for limitations.
		 * 
		 * Remarks: This function will have different effects depending on the
		 * operating system used:.
		 * 
		 * <table border="1" cellspacing="1">
		 * <tr>
		 * <td width="38%"><b><i><font size="2" face="Arial">Operating
		 * System</font></i></b></td>
		 * <td width="62%"><b><i><font size="2"
		 * face="Arial">&quot;BlockInput&quot; Results</font></i></b></td>
		 * </tr>
		 * <tr>
		 * <td width="38%">&nbsp;</td>
		 * <td width="62%">&nbsp;</td>
		 * </tr>
		 * <tr>
		 * <td width="38%"><font size="2" face="Arial">Windows 95</font></td>
		 * <td width="62%"><font size="2" face="Arial">No effect.</font></td>
		 * </tr>
		 * <tr>
		 * <td width="38%"><font size="2" face="Arial">Windows 98</font></td>
		 * <td width="62%"><font size="2" face="Arial">User input is blocked but
		 * AutoIt is also unable to simulate input.</font></td>
		 * </tr>
		 * <tr>
		 * <td width="38%"><font size="2" face="Arial">Windows NT 4
		 * (<i>Without</i> ServicePack 6)</font></td>
		 * <td width="62%"><font size="2" face="Arial">No effect.</font></td>
		 * </tr>
		 * <tr>
		 * <td width="38%"><font size="2" face="Arial">Windows NT 4 (<i>With</i>
		 * ServicePack 6)</font></td>
		 * <td width="62%"><font size="2" face="Arial">User input is blocked and
		 * AutoIt can simulate input.</font></td>
		 * </tr>
		 * <tr>
		 * <td width="38%"><font size="2" face="Arial">Windows 2000</font></td>
		 * <td width="62%"><font size="2" face="Arial">User input is blocked and
		 * AutoIt can simulate input.</font></td>
		 * </tr>
		 * </table>
		 * 
		 * @param toggle
		 *            This should be set to 1 to block input, and 0 to enable
		 *            input.
		 */
		public void AUTOIT_BlockInput(final int toggle);

		/**
		 * Allows you to get text from the clipboard.
		 * 
		 * Remarks: A maximum of 16384 characters of text can be received;
		 * ensure that the text buffer you pass is at least this large.
		 * 
		 * @param text
		 *            A pointer to a string buffer that will receive the text
		 *            from the clipboard.
		 * @see #AUTOIT_ClipPut(String)
		 */
		public void AUTOIT_ClipGet(final byte[] text);

		/**
		 * Allows you to send text to the clipboard.
		 * 
		 * Remarks: A maximum of 16384 characters of text can be sent.
		 * 
		 * @param text
		 *            A pointer to a string buffer that contains the text you
		 *            want to send.
		 * @see #AUTOIT_ClipGet(String)
		 */
		public void AUTOIT_ClipPut(final String text);

		/**
		 * Closes down AutoIt. This function is called automatically when the
		 * DLL is unloaded.
		 * 
		 * Remarks: This is called internally when the DLL unloads.
		 * 
		 * If you are using the static library version of AutoIt, call this
		 * function when you have finished using AutoIt functions.
		 */
		public void AUTOIT_Close();

		/**
		 * Some programs use hidden text on windows this can cause problems when
		 * trying to script them. This command allows you to tell AutoIt whether
		 * or not to detect this hidden text.
		 * 
		 * Remarks: When you use the "/reveal" mode of the full AutoIt product,
		 * you will see all text, including hidden text.
		 * 
		 * @param toggle
		 *            This should be set to 1 to enable hidden text detection,
		 *            and 0 to disable. (Default is 0)
		 */
		public void AUTOIT_DetectHiddenText(final int toggle);

		/**
		 * Checks if a given window is currently active.
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @return Returns 1 if the given window was active, otherwise it
		 *         returns 0.
		 * @see #AUTOIT_IfWinExist(String, String)
		 */
		public int AUTOIT_IfWinActive(final String title, final String text);

		/**
		 * Checks if a given window current exists (in any state).
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @return Returns 1 if the given window exists (regardless of its
		 *         state), otherwise it returns 0.
		 * @see #AUTOIT_IfWinActive(String, String)
		 */
		public int AUTOIT_IfWinExist(final String title, final String text);

		/**
		 * Deletes a specified value from a standard .INI file.
		 * 
		 * Remarks: An INI file is taken to be a file of the following format:
		 * [SectionName]
		 * 
		 * Value=Result
		 * 
		 * The full path of the INI file must be given, however, it is
		 * acceptable to use something similar to ".\myfile.ini" to indicate a
		 * file in the current working directory.
		 * 
		 * @param file
		 *            A string pointer to the filename of the .INI file.
		 * @param section
		 *            A string pointer to the required section of the .INI file.
		 * @param value
		 *            A string pointer to the value to delete.
		 * @see #AUTOIT_IniRead(String, String, String, String)
		 * @see #AUTOIT_IniWrite(String, String, String, String)
		 */
		public void AUTOIT_IniDelete(final String file, final String section,
				final String value);

		/**
		 * Reads a specified value from a standard .INI file.
		 * 
		 * Remarks: An INI file is taken to be a file of the following format:
		 * [SectionName]
		 * 
		 * Value=Result
		 * 
		 * The full path of the INI file must be given, however, it is
		 * acceptable to use something similar to ".\myfile.ini" to indicate a
		 * file in the current working directory.
		 * 
		 * @param file
		 *            A string pointer to the filename of the .INI file.
		 * @param section
		 *            A string pointer to the required section of the .INI file.
		 * @param value
		 *            A string pointer to the value to read.
		 * @param result
		 *            A string pointer to receive the result.
		 * @see #AUTOIT_IniDelete(String, String, String)
		 * @see #AUTOIT_IniWrite(String, String, String, String)
		 */
		public void AUTOIT_IniRead(final String file, final String section,
				final String value, final byte[] result);

		/**
		 * Resets AutoIt to defaults (window delays, key delays, etc.). This
		 * function is called automatically when the DLL is loaded.
		 * 
		 * Remarks: This is called internally when the DLL loads.
		 * 
		 * If you are using the static library version of AutoIt, call this
		 * function before using any AutoIt functions -- ideally call it as soon
		 * as your program starts (and has input focus) for best results (helps
		 * the AUTOIT_WinActivate command).
		 */
		public void AUTOIT_Init();

		/**
		 * Write a specified value to a standard .INI file.
		 * 
		 * Remarks: An INI file is taken to be a file of the following format:
		 * [SectionName]
		 * 
		 * Value=Result
		 * 
		 * The full path of the INI file must be given, however, it is
		 * acceptable to use something similar to ".\myfile.ini" to indicate a
		 * file in the current working directory.
		 * 
		 * @param file
		 *            A string pointer to the filename of the .INI file.
		 * @param section
		 *            A string pointer to the required section of the .INI file.
		 * @param value
		 *            A string pointer to the value to write to.
		 * @param result
		 *            A string pointer to result to write.
		 * @see #AUTOIT_IniDelete(String, String, String)
		 * @see #AUTOIT_IniRead(String, String, String, String)
		 */
		public void AUTOIT_IniWrite(final String file, final String section,
				final String value, final String result);

		/**
		 * Simulates a mouse left-click at a given coordinate.
		 * 
		 * Remarks: The X and Y co-ordinates are relative to the currently
		 * active window. Run the full version of AutoIt in reveal mode to
		 * determine the required co-ordinates of a window. To perform a
		 * double-click, simply run the command twice :)
		 * 
		 * @param x
		 *            X coordinate
		 * @param y
		 *            Y coordinate
		 * @see #AUTOIT_LeftClickDrag(int, int, int, int)
		 * @see #AUTOIT_RightClick(int, int)
		 * @see #AUTOIT_RightClickDrag(int, int, int, int)
		 */
		public void AUTOIT_LeftClick(final int x, final int y);

		/**
		 * Simulates a mouse left-click-drag operation.
		 * 
		 * Remarks: The X and Y co-ordinates are relative to the currently
		 * active window. Run the full version of AutoIt in reveal mode to
		 * determine the required co-ordinates of a window.
		 * 
		 * @param x1
		 *            X1 coordinate (from coordinate)
		 * @param y1
		 *            Y1 coordinate (from coordinate)
		 * @param x2
		 *            X2 coordinate (to coordinate)
		 * @param y2
		 *            Y2 coordinate (to coordinate)
		 * @see #AUTOIT_LeftClick(int, int)
		 * @see #AUTOIT_LeftClickDrag(int, int, int, int)
		 * @see #AUTOIT_RightClick(int, int)
		 * @see #AUTOIT_RightClickDrag(int, int, int, int)
		 */
		public void AUTOIT_LeftClickDrag(final int x1, final int y1,
				final int x2, final int y2);

		/**
		 * Gets the X coordinate of the mouse pointer.
		 * 
		 * Remarks: The X and Y co-ordinates are relative to the currently
		 * active window. Run the full version of AutoIt in reveal mode to
		 * determine the required co-ordinates of a window.
		 * 
		 * @return The X coordinate of the mouse pointer.
		 * @see #AUTOIT_MouseMove(int, int)
		 * @see #AUTOIT_MouseGetPosY()
		 */
		public int AUTOIT_MouseGetPosX();

		/**
		 * Gets the Y coordinate of the mouse pointer.
		 * 
		 * Remarks: The X and Y co-ordinates are relative to the currently
		 * active window. Run the full version of AutoIt in reveal mode to
		 * determine the required co-ordinates of a window.
		 * 
		 * @return The Y coordinate of the mouse pointer.
		 * @see #AUTOIT_MouseMove(int, int)
		 * @see #AUTOIT_MouseGetPosX()
		 */
		public int AUTOIT_MouseGetPosY();

		/**
		 * Moves the mouse pointer to the specified coordinates.
		 * 
		 * Remarks: The X and Y co-ordinates are relative to the currently
		 * active window. Run the full version of AutoIt in reveal mode to
		 * determine the required co-ordinates of a window.
		 * 
		 * @param x
		 *            X coordinate
		 * @param y
		 *            Y coordinate
		 * @see #AUTOIT_MouseGetPosX()
		 * @see #AUTOIT_MouseGetPosY()
		 */
		public void AUTOIT_MouseMove(final int x, final int y);

		/**
		 * Simulates a mouse right-click at a given coordinate.
		 * 
		 * Remarks: The X and Y co-ordinates are relative to the currently
		 * active window. Run the full version of AutoIt in reveal mode to
		 * determine the required co-ordinates of a window. To perform a
		 * double-click, simply run the command twice :)
		 * 
		 * @param x
		 *            X coordinate
		 * @param y
		 *            Y coordinate
		 * @see #AUTOIT_LeftClick(int, int)
		 * @see #AUTOIT_LeftClickDrag(int, int, int, int)
		 * @see #AUTOIT_RightClickDrag(int, int, int, int)
		 */
		public void AUTOIT_RightClick(final int x, final int y);

		/**
		 * Simulates a mouse right-click-drag operation.
		 * 
		 * Remarks: The X and Y co-ordinates are relative to the currently
		 * active window. Run the full version of AutoIt in reveal mode to
		 * determine the required co-ordinates of a window.
		 * 
		 * @param x1
		 *            X1 coordinate (from coordinate)
		 * @param y1
		 *            Y1 coordinate (from coordinate)
		 * @param x2
		 *            X2 coordinate (to coordinate)
		 * @param y2
		 *            Y2 coordinate (to coordinate)
		 * @see #AUTOIT_LeftClick(int, int)
		 * @see #AUTOIT_LeftClickDrag(int, int, int, int)
		 * @see #AUTOIT_RightClick(int, int)
		 */
		public void AUTOIT_RightClickDrag(final int x1, final int y1,
				final int x2, final int y2);

		/**
		 * Sends keystrokes to the currently active window.
		 * 
		 * Remarks: See here for the syntax of the send command.
		 * 
		 * <h3><font face="Arial">Send Command Syntax</font></h3>
		 * <p>
		 * <font face="Arial" size="2">The &quot;Send&quot; command syntax is
		 * similar to that of ScriptIt and the Visual Basic &quot;SendKeys&quot;
		 * command. Characters are sent as written with the exception of the
		 * following characters:<br>
		 * <br>
		 * <b>'!'</b><br>
		 * This tells AutoIt to send an ALT keystroke, therefore <b>Send
		 * &quot;This is text!a&quot;</b> would send the keys &quot;This is
		 * text&quot; and then press &quot;ALT+a&quot;.&nbsp;<br>
		 * <br>
		 * N.B. Some programs are very choosy about capital letters and ALT
		 * keys, i.e. &quot;!A&quot; is different to &quot;!a&quot;. The first
		 * says ALT+SHIFT+A, the second is ALT+a. If in doubt, use lowercase!<br>
		 * <br>
		 * <b>'+'</b><br>
		 * This tells AutoIt to send a SHIFT keystroke, therefore <b>Send
		 * &quot;Hell+o&quot;</b> would send the text &quot;HellO&quot;. <b>Send
		 * &quot;!+a&quot;</b> would send &quot;ALT+SHIFT+a&quot;.<br>
		 * <br>
		 * <b>'^'<br>
		 * </b>This tells AutoIt to send a CONTROL keystroke, therefore <b>Send
		 * &quot;^!a&quot;</b> would send &quot;CTRL+ALT+a&quot;.<br>
		 * <br>
		 * N.B. Some programs are very choosy about capital letters and CTRL
		 * keys, i.e. &quot;^A&quot; is different to &quot;^a&quot;. The first
		 * says CTRL+SHIFT+A, the second is CTRL+a. If in doubt, use lowercase!<br>
		 * <br>
		 * <b>'#'</b><br>
		 * The hash is used as a key delimiter to make a line easier to
		 * read.&nbsp; i.e. <b> Send &quot;H#e#l#l#o&quot;</b> is the same as
		 * <b>Send &quot;Hello&quot;</b>.<br>
		 * <br>
		 * <br>
		 * Certain special keys can be sent and should be enclosed in braces:<br>
		 * <br>
		 * <b><u>N.B.&nbsp; Windows does not allow the simulation of the
		 * &quot;CTRL-ALT-DEL&quot; combination!</u></b></font>
		 * </p>
		 * <table border="1">
		 * <tr>
		 * <td width="50%" align="center"><b><font size="2" face="Arial">Send
		 * Command</font></b></td>
		 * <td width="50%">
		 * <p align="center">
		 * <b><font size="2" face="Arial">Resulting Keypress</font></b></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{!}</font>
		 * </td>
		 * <td width="50%"><font face="Arial" size="2">!</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{#}</font>
		 * </td>
		 * <td width="50%"><font face="Arial" size="2">#</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{+}</font>
		 * </td>
		 * <td width="50%"><font face="Arial" size="2">+</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{^}</font>
		 * </td>
		 * <td width="50%"><font face="Arial" size="2">^</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{{}</font>
		 * </td>
		 * <td width="50%"><font face="Arial" size="2">{</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{}}</font>
		 * </td>
		 * <td width="50%"><font face="Arial" size="2">}</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{SPACE}</font></td>
		 * <td width="50%"><font face="Arial" size="2">SPACE</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{ENTER}</font></td>
		 * <td width="50%"><font face="Arial" size="2">ENTER</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{ALT}</font></td>
		 * <td width="50%"><font face="Arial" size="2">ALT</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{BACKSPACE} or {BS}</font></td>
		 * <td width="50%"><font face="Arial" size="2">BACKSPACE</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{DELETE}
		 * or {DEL}</font></td>
		 * <td width="50%"><font face="Arial" size="2">DELETE</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{UP}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Cursor up</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{DOWN}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Cursor down</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{LEFT}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Cursor left</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{RIGHT}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Cursor right</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{HOME}</font></td>
		 * <td width="50%"><font face="Arial" size="2">HOME</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{END}</font></td>
		 * <td width="50%"><font face="Arial" size="2">END</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{ESCAPE}
		 * or {ESC}</font></td>
		 * <td width="50%"><font face="Arial" size="2">ESCAPE</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{INSERT}
		 * or {INS}</font></td>
		 * <td width="50%"><font face="Arial" size="2">INS</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{PGUP}</font></td>
		 * <td width="50%"><font face="Arial" size="2">PGUP</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{PGDN}</font></td>
		 * <td width="50%"><font face="Arial" size="2">PGDN</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{F1} -
		 * {F12}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Function keys</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{TAB}</font></td>
		 * <td width="50%"><font face="Arial" size="2">TAB</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{PRINTSCREEN}</font></td>
		 * <td width="50%"><font face="Arial" size="2">PRINTSCR</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{LWIN}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Left Windows key</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{RWIN}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Right Windows key</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{NUMLOCK}</font></td>
		 * <td width="50%"><font face="Arial" size="2">NUMLOCK</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{CTRLBREAK}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Ctrl+break</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{PAUSE}</font></td>
		 * <td width="50%"><font face="Arial" size="2">PAUSE</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{CAPSLOCK}</font></td>
		 * <td width="50%"><font face="Arial" size="2">CAPSLOCK</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{NUMPAD0}
		 * - {NUMPAD 9}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Numpad digits</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{NUMPADMULT}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Numpad Multiply</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{NUMPADADD}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Numpad Add</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{NUMPADSUB}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Numpad Subtract</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{NUMPADDIV}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Numpad Divide</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{NUMPADDOT}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Numpad period</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{APPSKEY}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Windows App key</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{ALTDOWN}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Holds the ALT key down
		 * until {ALTUP} is sent</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{SHIFTDOWN}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Holds the SHIFT key down
		 * until {SHIFTUP} is sent</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{CTRLDOWN}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Holds the CTRL key down
		 * until {CTRLUP} is sent</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{LWINDOWN}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Holds the left Windows
		 * key down until {LWINUP} is sent</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial"
		 * size="2">{RWINDOWN}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Holds the right Windows
		 * key down until {RWINUP} is sent</font></td>
		 * </tr>
		 * <tr>
		 * <td width="50%" align="center"><font face="Arial" size="2">{ASC
		 * nnnn}</font></td>
		 * <td width="50%"><font face="Arial" size="2">Send the ALT+nnnn key
		 * combination</font></td>
		 * </tr>
		 * </table>
		 * <p>
		 * <font face="Arial" size="2"><br>
		 * <i>To send the ASCII value A (same as pressing ALT+65 on the numeric
		 * keypad)<br>
		 * </i>&nbsp;&nbsp;&nbsp; Send &quot;{ASC 65}&quot;<br>
		 * <br>
		 * <i>Single keys can also be repeated, e.g.<br>
		 * </i>&nbsp;&nbsp;&nbsp; Send &quot;{DEL 4}&quot;&nbsp;&nbsp;&nbsp;
		 * Presses the DEL key 4 times<br>
		 * &nbsp;&nbsp;&nbsp; Send &quot;{S
		 * 30}&quot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Sends 30 'S' characters
		 * <br>
		 * &nbsp;&nbsp;&nbsp; Send &quot;+{TAB 4}&quot;&nbsp; Presses SHIFT+TAB
		 * 4 times<br>
		 * </font>
		 * </p>
		 * 
		 * @param line
		 *            A string pointer to the set of keystrokes to send.
		 */
		public void AUTOIT_Send(final String line);

		/**
		 * This command will correctly set the state of the CAPSLOCK key to
		 * either on or off.
		 * 
		 * @param toggle
		 *            This should be set to 1 to set capslock to ON, and 0 to
		 *            set capslock to OFF.
		 * @see #AUTOIT_SetStoreCapslockMode(int)
		 */
		public void AUTOIT_SetCapslockState(final int toggle);

		/**
		 * This functions will alter the amount of time that AutoIt pauses
		 * between each simulated keypress.
		 * 
		 * @param delay
		 *            The value in milliseconds pause between each simulated
		 *            keypress. (Default 20ms).
		 */
		public void AUTOIT_SetKeyDelay(final int delay);

		/**
		 * By default, at the start of a "Send" command AutoIt will store the
		 * state of the CAPSLOCK key; at the end of the "Send" command this
		 * status will be restored. Use this command to turn off this behaviour.
		 * 
		 * @param toggle
		 *            This should be set to 1 to store/restore the capslock at
		 *            the start/end of a send command, and 0 to ignore capslock
		 *            state. (Default is 1).
		 * @see #AUTOIT_SetCapslockState(int)
		 */
		public void AUTOIT_SetStoreCapslockMode(final int toggle);

		/**
		 * This function will alter the way that AutoIt matches window titles in
		 * functions such as AUTOIT_WinWait, AUTOIT_IfWinActive, etc.
		 * 
		 * Remarks:
		 * 
		 * mode 1:
		 * 
		 * In the szTitle string you specify the start of a window title to
		 * match. i.e. for the notepad.exe window (Untitled - Notepad), valid
		 * matches would be: "Untitled", "Untitled -", "Unt" and
		 * "Untitled - Notepad".
		 * 
		 * mode 2:
		 * 
		 * In the szTitle string you can specify ANY substring of the window
		 * title you want to match. Again for the notepad.exe window valid
		 * matches would be: "Untitled", "Untitled - Notepad", "Notepad", "No".
		 * 
		 * @param mode
		 *            The mode of title matching to use -- see the remarks.
		 *            Valid modes are 1 and 2. (Default is 1).
		 * 
		 * @param mode
		 *            The mode of title matching to use -- see the remarks.
		 *            Valid modes are 1 and 2. (Default is 1).
		 */
		public void AUTOIT_SetTitleMatchMode(final int mode);

		/**
		 * This functions will alter the amount of time that AutoIt pauses after
		 * performing a WinWait-type function.
		 * 
		 * @param delay
		 *            The value in milliseconds pause after a WinWait-related
		 *            function. (Default 500ms).
		 */
		public void AUTOIT_SetWinDelay(final int delay);

		/**
		 * This function can perform various types of shutdown on all Windows
		 * operating systems.
		 * 
		 * Remarks: The value of nFlag can be a combination of the flags below:
		 * 
		 * <table border="1" width="46%">
		 * <tr>
		 * <td width="210%">
		 * <p align="center">
		 * <b><i>Function</i></b></td>
		 * <td width="16%" align="center"><b><i>Flag</i></b></td>
		 * </tr>
		 * <tr>
		 * <td width="210%">Log off the current user</td>
		 * <td width="16%" align="center">0</td>
		 * </tr>
		 * <tr>
		 * <td width="210%">Shutdown the workstation</td>
		 * <td width="16%" align="center">1</td>
		 * </tr>
		 * <tr>
		 * <td width="210%">Reboot the workstation</td>
		 * <td width="16%" align="center">2</td>
		 * </tr>
		 * <tr>
		 * <td width="210%">Force closing of applications (may lose unsaved
		 * work)</td>
		 * <td width="16%" align="center">4</td>
		 * </tr>
		 * <tr>
		 * <td width="210%">Shutdown and power off (if supported)</td>
		 * <td width="16%" align="center">8</td>
		 * </tr>
		 * </table>
		 * 
		 * @param modes
		 *            A combination of the modes given in the remarks section.
		 */
		public void AUTOIT_Shutdown(final int modes);

		/**
		 * This function simply pauses for an amount of time.
		 * 
		 * @param milliseconds
		 *            The amount of time to sleep in milliseconds. (Note: 1000
		 *            milliseconds = 1 second)
		 */
		public void AUTOIT_Sleep(final int milliseconds);

		/**
		 * This function will activate a specified window.
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @see #AUTOIT_WinClose(String, String)
		 * @see #AUTOIT_WinHide(String, String)
		 * @see #AUTOIT_WinKill(String, String)
		 * @see #AUTOIT_WinMaximize(String, String)
		 * @see #AUTOIT_WinMinimize(String, String)
		 * @see #AUTOIT_WinMinimizeAll()
		 * @see #AUTOIT_WinMinimizeAllUndo()
		 * @see #AUTOIT_WinRestore(String, String)
		 * @see #AUTOIT_WinShow(String, String)
		 */
		public void AUTOIT_WinActivate(final String title, final String text);

		/**
		 * This function will close a specified window.
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @see #AUTOIT_WinActivate(String, String)
		 * @see #AUTOIT_WinHide(String, String)
		 * @see #AUTOIT_WinKill(String, String)
		 * @see #AUTOIT_WinMaximize(String, String)
		 * @see #AUTOIT_WinMinimize(String, String)
		 * @see #AUTOIT_WinMinimizeAll()
		 * @see #AUTOIT_WinMinimizeAllUndo()
		 * @see #AUTOIT_WinRestore(String, String)
		 * @see #AUTOIT_WinShow(String, String)
		 */
		public void AUTOIT_WinClose(final String title, final String text);

		/**
		 * This function will return the title of the current active window.
		 * 
		 * Remarks: A maximum of 16384 characters of text can be received;
		 * ensure that the text buffer you pass is at least this large.
		 * 
		 * @param title
		 *            A string pointer to receive the window title..
		 * @see #AUTOIT_WinSetTitle(String, String, String)
		 */
		public void AUTOIT_WinGetActiveTitle(final byte[] title);

		/**
		 * This function will hide a specified window.
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @see #AUTOIT_WinActivate(String, String)
		 * @see #AUTOIT_WinClose(String, String)
		 * @see #AUTOIT_WinKill(String, String)
		 * @see #AUTOIT_WinMaximize(String, String)
		 * @see #AUTOIT_WinMinimize(String, String)
		 * @see #AUTOIT_WinMinimizeAll()
		 * @see #AUTOIT_WinMinimizeAllUndo()
		 * @see #AUTOIT_WinRestore(String, String)
		 * @see #AUTOIT_WinShow(String, String)
		 */
		public void AUTOIT_WinHide(final String title, final String text);

		/**
		 * This function will forceably close a specified window.
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @see #AUTOIT_WinActivate(String, String)
		 * @see #AUTOIT_WinClose(String, String)
		 * @see #AUTOIT_WinHide(String, String)
		 * @see #AUTOIT_WinMaximize(String, String)
		 * @see #AUTOIT_WinMinimize(String, String)
		 * @see #AUTOIT_WinMinimizeAll()
		 * @see #AUTOIT_WinMinimizeAllUndo()
		 * @see #AUTOIT_WinRestore(String, String)
		 * @see #AUTOIT_WinShow(String, String)
		 */
		public void AUTOIT_WinKill(final String title, final String text);

		/**
		 * This function will maximize a specified window.
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @see #AUTOIT_WinActivate(String, String)
		 * @see #AUTOIT_WinClose(String, String)
		 * @see #AUTOIT_WinHide(String, String)
		 * @see #AUTOIT_WinKill(String, String)
		 * @see #AUTOIT_WinMinimize(String, String)
		 * @see #AUTOIT_WinMinimizeAll()
		 * @see #AUTOIT_WinMinimizeAllUndo()
		 * @see #AUTOIT_WinRestore(String, String)
		 * @see #AUTOIT_WinShow(String, String)
		 */
		public void AUTOIT_WinMaximize(final String title, final String text);

		/**
		 * This function will minimize a specified window.
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @see #AUTOIT_WinActivate(String, String)
		 * @see #AUTOIT_WinClose(String, String)
		 * @see #AUTOIT_WinHide(String, String)
		 * @see #AUTOIT_WinKill(String, String)
		 * @see #AUTOIT_WinMaximize(String, String)
		 * @see #AUTOIT_WinMinimizeAll()
		 * @see #AUTOIT_WinMinimizeAllUndo()
		 * @see #AUTOIT_WinRestore(String, String)
		 * @see #AUTOIT_WinShow(String, String)
		 */
		public void AUTOIT_WinMinimize(final String title, final String text);

		/**
		 * This function will minimize all windows.
		 * 
		 * @see #AUTOIT_WinActivate(String, String)
		 * @see #AUTOIT_WinClose(String, String)
		 * @see #AUTOIT_WinHide(String, String)
		 * @see #AUTOIT_WinKill(String, String)
		 * @see #AUTOIT_WinMaximize(String, String)
		 * @see #AUTOIT_WinMinimize(String, String)
		 * @see #AUTOIT_WinMinimizeAllUndo()
		 * @see #AUTOIT_WinRestore(String, String)
		 * @see #AUTOIT_WinShow(String, String)
		 */
		public void AUTOIT_WinMinimizeAll();

		/**
		 * This function will undo a previous AUTOIT_WinMinimizeAll call.
		 * 
		 * @see #AUTOIT_WinActivate(String, String)
		 * @see #AUTOIT_WinClose(String, String)
		 * @see #AUTOIT_WinHide(String, String)
		 * @see #AUTOIT_WinKill(String, String)
		 * @see #AUTOIT_WinMaximize(String, String)
		 * @see #AUTOIT_WinMinimize(String, String)
		 * @see #AUTOIT_WinMinimizeAll()
		 * @see #AUTOIT_WinRestore(String, String)
		 * @see #AUTOIT_WinShow(String, String)
		 */
		public void AUTOIT_WinMinimizeAllUndo();

		/**
		 * Use this function to move/resize a specified window.
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @param x
		 *            X coordinate (to coordinate)
		 * @param y
		 *            Y1 coordinate (to coordinate)
		 * @param width
		 *            New width of the window (use -1 to leave the size alone)
		 * @param height
		 *            New height of the window (use -1 to leave the size alone)
		 */
		public void AUTOIT_WinMove(final String title, final String text,
				final int x, final int y, final int width, final int height);

		/**
		 * This function will restore a window from a minimized state.
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @see #AUTOIT_WinActivate(String, String)
		 * @see #AUTOIT_WinClose(String, String)
		 * @see #AUTOIT_WinHide(String, String)
		 * @see #AUTOIT_WinKill(String, String)
		 * @see #AUTOIT_WinMaximize(String, String)
		 * @see #AUTOIT_WinMinimize(String, String)
		 * @see #AUTOIT_WinMinimizeAll()
		 * @see #AUTOIT_WinMinimizeAllUndo()
		 * @see #AUTOIT_WinShow(String, String)
		 */
		public void AUTOIT_WinRestore(final String title, final String text);

		/**
		 * Changes the title of a specified window.
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @param newTitle
		 *            A string pointer to the text of the new window title.
		 * @see #AUTOIT_WinGetActiveTitle(String)
		 */
		public void AUTOIT_WinSetTitle(final String title, final String text,
				final String newTitle);

		/**
		 * This function will show a specified window previously hidden.
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @see #AUTOIT_WinActivate(String, String)
		 * @see #AUTOIT_WinClose(String, String)
		 * @see #AUTOIT_WinHide(String, String)
		 * @see #AUTOIT_WinKill(String, String)
		 * @see #AUTOIT_WinMaximize(String, String)
		 * @see #AUTOIT_WinMinimize(String, String)
		 * @see #AUTOIT_WinMinimizeAll()
		 * @see #AUTOIT_WinMinimizeAllUndo()
		 * @see #AUTOIT_WinRestore(String, String)
		 */
		public void AUTOIT_WinShow(final String title, final String text);

		/**
		 * This function will pause until the specified window exists (or the
		 * function times out).
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @param timeout
		 *            Specifies the number of seconds to wait for. Use the value
		 *            0 to wait indefinitely.
		 * @return Returns 1 if the the nTimeout waiting period was exceeded,
		 *         otherwise it returns 0.
		 * @see #AUTOIT_WinWaitActive(String, String, int)
		 * @see #AUTOIT_WinWaitClose(String, String, int)
		 * @see #AUTOIT_WinWaitNotActive(String, String, int)
		 */
		public int AUTOIT_WinWait(final String title, final String text,
				final int timeout);

		/**
		 * This function will pause until the specified window is active (or the
		 * function times out).
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @param timeout
		 *            Specifies the number of seconds to wait for. Use the value
		 *            0 to wait indefinitely.
		 * @return Returns 1 if the the nTimeout waiting period was exceeded,
		 *         otherwise it returns 0.
		 * @see #AUTOIT_WinWait(String, String, int)
		 * @see #AUTOIT_WinWaitClose(String, String, int)
		 * @see #AUTOIT_WinWaitNotActive(String, String, int)
		 */
		public int AUTOIT_WinWaitActive(final String title, final String text,
				final int timeout);

		/**
		 * This function will pause until the specified window doesn't exist (or
		 * the function times out).
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @param timeout
		 *            Specifies the number of seconds to wait for. Use the value
		 *            0 to wait indefinitely.
		 * @return Returns 1 if the the nTimeout waiting period was exceeded,
		 *         otherwise it returns 0.
		 * @see #AUTOIT_WinWait(String, String, int)
		 * @see #AUTOIT_WinWaitActive(String, String, int)
		 * @see #AUTOIT_WinWaitNotActive(String, String, int)
		 */
		public int AUTOIT_WinWaitClose(final String title, final String text,
				final int timeout);

		/**
		 * This function will pause until the specified window is not active (or
		 * the function times out).
		 * 
		 * @param title
		 *            A string pointer to the title of the window to match.
		 * @param text
		 *            A string pointer to the text of the window to match.
		 * @param timeout
		 *            Specifies the number of seconds to wait for. Use the value
		 *            0 to wait indefinitely.
		 * @return Returns 1 if the the nTimeout waiting period was exceeded,
		 *         otherwise it returns 0.
		 * @see #AUTOIT_WinWait(String, String, int)
		 * @see #AUTOIT_WinWaitActive(String, String, int)
		 * @see #AUTOIT_WinWaitClose(String, String, int)
		 */
		public int AUTOIT_WinWaitNotActive(final String title,
				final String text, final int timeout);
	}
}
