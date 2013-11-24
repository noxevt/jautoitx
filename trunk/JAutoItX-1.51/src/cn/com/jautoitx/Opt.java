package cn.com.jautoitx;

public class Opt extends AutoItX {
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
	 * The default amount of time in milliseconds that AutoIt pauses after
	 * performing a WinWait-type function
	 */
	public static final int DEFAULT_WIN_DELAY = 500;

	/* The default way the coords are used in the mouse functions */
	public static final CoordMode DEFAULT_MOUSE_COORD_MODE = CoordMode.ABSOLUTE_SCREEN_COORDINATES;

	/* The way the coords are used in the mouse functions */
	protected static CoordMode mouseCoordMode = DEFAULT_MOUSE_COORD_MODE;

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
		invoke("DetectHiddenText", toggle ? "on" : "off");
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
		invoke("SetCapslockState", toggle ? "on" : "off");
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
		invoke("SetKeyDelay", (delay < 0) ? DEFAULT_KEY_DELAY : delay);
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
		invoke("SetStoreCapslockMode", toggle ? "on" : "off");
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
		invoke("SetTitleMatchMode", mode.getValue());
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
		invoke("SetWinDelay", (delay < 0) ? DEFAULT_WIN_DELAY : delay);
	}

	/**
	 * Sets the way coords are used in the mouse functions.
	 * 
	 * @param mouseCoordMode
	 *            The way coords are used in the mouse functions, default is
	 *            absolute screen coordinates.
	 */
	public static void setMouseCoordMode(final CoordMode mouseCoordMode) {
		if (mouseCoordMode == null) {
			throw new IllegalArgumentException(
					"mouseCoordMode can not be null.");
		}
		Opt.mouseCoordMode = mouseCoordMode;
	}

	/**
	 * The way coords are used in the mouse functions, either absolute coords or
	 * coords relative to the current active window.
	 * 
	 * @author zhengbo.wang
	 */
	public static enum CoordMode {
		/* relative coords to the active window */
		RELATIVE_TO_ACTIVE_WINDOW(0),

		/* absolute screen coordinates (default) */
		ABSOLUTE_SCREEN_COORDINATES(1);

		private final int coordMode;

		private CoordMode(final int coordMode) {
			this.coordMode = coordMode;
		}

		public int getCoordMode() {
			return coordMode;
		}

		@Override
		public String toString() {
			switch (this) {
			case RELATIVE_TO_ACTIVE_WINDOW:
				return "relative coords to the active window";
			case ABSOLUTE_SCREEN_COORDINATES:
				return "absolute screen coordinates";
			default:
				return "Unknown coord mode";
			}
		}
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
}
