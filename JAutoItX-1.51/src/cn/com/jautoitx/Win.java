package cn.com.jautoitx;

public class Win extends AutoItX {
	/*
	 * The return value if the the timeout waiting period was not exceeded in
	 * wait/waitActive/waitClose/waitNotActive methods.
	 */
	private static final int SUCCESS_WIN_WAIT = 0;

	/**
	 * This function will activate a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void activate(final String title) {
		activate(title, null);
	}

	/**
	 * This function will activate a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void activate(final String title, final String text) {
		invoke("WinActivate", title, defaultString(text));
	}

	/**
	 * Checks if a given window is currently active.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns true if the given window was active, otherwise it returns
	 *         false.
	 * @see #exists(String, String)
	 */
	public static boolean active(final String title) {
		return active(title, null);
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
	 * @see #exists(String, String)
	 */
	public static boolean active(final String title, final String text) {
		return invokeAndGetBoolean("IfWinActive", title, defaultString(text));
	}

	/**
	 * This function will close a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #activate(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void close(final String title) {
		close(title, null);
	}

	/**
	 * This function will close a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #activate(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void close(final String title, final String text) {
		invoke("WinClose", title, defaultString(text));
	}

	/**
	 * Checks if a given window current exists (in any state).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns true if the given window exists (regardless of its
	 *         state), otherwise it returns false.
	 * @see #active(String, String)
	 */
	public static boolean exists(final String title) {
		return exists(title, null);
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
	 * @see #active(String, String)
	 */
	public static boolean exists(final String title, final String text) {
		return invokeAndGetBoolean("IfWinExist", title, defaultString(text));
	}

	/**
	 * This function will return the title of the current active window.
	 * 
	 * Remarks: A maximum of 16384 characters of text can be received; ensure
	 * that the text buffer you pass is at least this large.
	 * 
	 * @return Returns the title of the current active window.
	 * @see #setTitle(String, String, String)
	 */
	public static String getActiveTitle() {
		return invokeAndGetString("WinGetActiveTitle");
	}

	/**
	 * This function will hide a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void hide(final String title) {
		hide(title, null);
	}

	/**
	 * This function will hide a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void hide(final String title, final String text) {
		invoke("WinHide", title, defaultString(text));
	}

	/**
	 * This function will forceably close a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void kill(final String title) {
		kill(title, null);
	}

	/**
	 * This function will forceably close a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void kill(final String title, final String text) {
		invoke("WinKill", title, defaultString(text));
	}

	/**
	 * This function will maximize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void maximize(final String title) {
		maximize(title, null);
	}

	/**
	 * This function will maximize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void maximize(final String title, final String text) {
		invoke("WinMaximize", title, defaultString(text));
	}

	/**
	 * This function will minimize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void minimize(final String title) {
		minimize(title, null);
	}

	/**
	 * This function will minimize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void minimize(final String title, final String text) {
		invoke("WinMinimize", title, defaultString(text));
	}

	/**
	 * This function will minimize all windows.
	 * 
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void minimizeAll() {
		invoke("WinMinimizeAll");
	}

	/**
	 * This function will undo a previous AUTOIT_WinMinimizeAll call.
	 * 
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #restore(String, String)
	 * @see #show(String, String)
	 */
	public static void minimizeAllUndo() {
		invoke("WinMinimizeAllUndo");
	}

	/**
	 * Use this function to move/resize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param x
	 *            X coordinate (to coordinate)
	 * @param y
	 *            Y coordinate (to coordinate)
	 */
	public static void move(final String title, final int x, final int y) {
		move(title, null, x, y, -1, -1);
	}

	/**
	 * Use this function to move/resize a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param x
	 *            X coordinate (to coordinate)
	 * @param y
	 *            Y coordinate (to coordinate)
	 * @param width
	 *            New width of the window (use -1 to leave the size alone)
	 * @param height
	 *            New height of the window (use -1 to leave the size alone)
	 */
	public static void move(final String title, final int x, final int y,
			final int width, final int height) {
		move(title, null, x, y, width, height);
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
	 *            Y coordinate (to coordinate)
	 */
	public static void move(final String title, final String text, final int x,
			final int y) {
		move(title, text, x, y, -1, -1);
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
	 *            Y coordinate (to coordinate)
	 * @param width
	 *            New width of the window (use -1 to leave the size alone)
	 * @param height
	 *            New height of the window (use -1 to leave the size alone)
	 */
	public static void move(final String title, final String text, final int x,
			final int y, final int width, final int height) {
		invoke("WinMove", title, defaultString(text), x, y, width, height);
	}

	/**
	 * This function will restore a window from a minimized state.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #show(String, String)
	 */
	public static void restore(final String title) {
		restore(title, null);
	}

	/**
	 * This function will restore a window from a minimized state.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #show(String, String)
	 */
	public static void restore(final String title, final String text) {
		invoke("WinRestore", title, defaultString(text));
	}

	/**
	 * Changes the title of a specified window.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param newTitle
	 *            A string pointer to the text of the new window title.
	 * @see #getActiveTitle()
	 */
	public static void setTitle(final String title, final String newTitle) {
		setTitle(title, null, newTitle);
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
	 * @see #getActiveTitle()
	 */
	public static void setTitle(final String title, final String text,
			final String newTitle) {
		invoke("WinSetTitle", title, defaultString(text), newTitle);
	}

	/**
	 * This function will show a specified window previously hidden.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 */
	public static void show(final String title) {
		show(title, null);
	}

	/**
	 * This function will show a specified window previously hidden.
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #activate(String, String)
	 * @see #close(String, String)
	 * @see #hide(String, String)
	 * @see #kill(String, String)
	 * @see #maximize(String, String)
	 * @see #minimize(String, String)
	 * @see #minimizeAll()
	 * @see #minimizeAllUndo()
	 * @see #restore(String, String)
	 */
	public static void show(final String title, final String text) {
		invoke("WinShow", title, defaultString(text));
	}

	/**
	 * This function will pause until the specified window exists (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #waitActive(String, String, int)
	 * @see #waitClose(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean wait(final String title) {
		return wait(title, null);
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
	 * @see #waitActive(String, String, int)
	 * @see #waitClose(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean wait(final String title, final int timeout) {
		return wait(title, null, timeout);
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
	 * @see #waitActive(String, String, int)
	 * @see #waitClose(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean wait(final String title, final String text) {
		return wait(title, defaultString(text), 0);
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
	 * @see #waitActive(String, String, int)
	 * @see #waitClose(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean wait(final String title, final String text,
			final int timeout) {
		return invokeAndGetInteger("WinWait", title, defaultString(text),
				timeout).intValue() == SUCCESS_WIN_WAIT;
	}

	/**
	 * This function will pause until the specified window is active (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #wait(String, String, int)
	 * @see #waitClose(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean waitActive(final String title) {
		return waitActive(title, null);
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
	 * @see #wait(String, String, int)
	 * @see #waitClose(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean waitActive(final String title, final int timeout) {
		return waitActive(title, null, timeout);
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
	 * @see #wait(String, String, int)
	 * @see #waitClose(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean waitActive(final String title, final String text) {
		return waitActive(title, defaultString(text), 0);
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
	 * @see #wait(String, String, int)
	 * @see #waitClose(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean waitActive(final String title, final String text,
			final int timeout) {
		return invokeAndGetInteger("WinWaitActive", title, defaultString(text),
				timeout).intValue() == SUCCESS_WIN_WAIT;
	}

	/**
	 * This function will pause until the specified window doesn't exist (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #wait(String, String, int)
	 * @see #waitActive(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean waitClose(final String title) {
		return waitClose(title, null);
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
	 * @see #wait(String, String, int)
	 * @see #waitActive(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean waitClose(final String title, final int timeout) {
		return waitClose(title, null, timeout);
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
	 * @see #wait(String, String, int)
	 * @see #waitActive(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean waitClose(final String title, final String text) {
		return waitClose(title, defaultString(text), 0);
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
	 * @see #wait(String, String, int)
	 * @see #waitActive(String, String, int)
	 * @see #waitNotActive(String, String, int)
	 */
	public static boolean waitClose(final String title, final String text,
			final int timeout) {
		return invokeAndGetInteger("WinWaitClose", title, defaultString(text),
				timeout).intValue() == SUCCESS_WIN_WAIT;
	}

	/**
	 * This function will pause until the specified window is not active (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @return Returns false if the the nTimeout waiting period was exceeded,
	 *         otherwise it returns true.
	 * @see #wait(String, String, int)
	 * @see #waitActive(String, String, int)
	 * @see #waitClose(String, String, int)
	 */
	public static boolean waitNotActive(final String title) {
		return waitNotActive(title, null);
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
	 * @see #wait(String, String, int)
	 * @see #waitActive(String, String, int)
	 * @see #waitClose(String, String, int)
	 */
	public static boolean waitNotActive(final String title, final int timeout) {
		return waitNotActive(title, null, timeout);
	}

	/**
	 * This function will pause until the specified window is not active (or the
	 * function times out).
	 * 
	 * @param title
	 *            A string pointer to the title of the window to match.
	 * @param text
	 *            A string pointer to the text of the window to match.
	 * @see #wait(String, String, int)
	 * @see #waitActive(String, String, int)
	 * @see #waitClose(String, String, int)
	 */
	public static boolean waitNotActive(final String title, final String text) {
		return waitNotActive(title, defaultString(text), 0);
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
	 * @see #wait(String, String, int)
	 * @see #waitActive(String, String, int)
	 * @see #waitClose(String, String, int)
	 */
	public static boolean waitNotActive(final String title, final String text,
			final int timeout) {
		return invokeAndGetInteger("WinWaitNotActive", title,
				defaultString(text), timeout).intValue() == SUCCESS_WIN_WAIT;
	}
}
