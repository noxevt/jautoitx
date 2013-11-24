package cn.com.jautoitx;

import cn.com.jautoitx.Opt.CoordMode;
import cn.com.jautoitx.Win32.User32Ext;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;

public class Mouse extends AutoItX {
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
		invoke("LeftClick", convertMouseCoordXToRelative(x),
				convertMouseCoordYToRelative(y));
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
		invoke("LeftClickDrag", convertMouseCoordXToRelative(x1),
				convertMouseCoordYToRelative(y1),
				convertMouseCoordXToRelative(x2),
				convertMouseCoordYToRelative(y2));
	}

	/**
	 * Gets the X coordinate of the mouse pointer.
	 * 
	 * Remarks: The X and Y co-ordinates are relative to the currently active
	 * window. Run the full version of AutoIt in reveal mode to determine the
	 * required co-ordinates of a window.
	 * 
	 * @return The X coordinate of the mouse pointer.
	 * @see #move(int, int)
	 * @see #getPosY()
	 */
	public static int getPosX() {
		return convertMouseCoordXToAbsolute(invokeAndGetInteger("MouseGetPosX"));
	}

	/**
	 * Gets the Y coordinate of the mouse pointer.
	 * 
	 * Remarks: The X and Y co-ordinates are relative to the currently active
	 * window. Run the full version of AutoIt in reveal mode to determine the
	 * required co-ordinates of a window.
	 * 
	 * @return The Y coordinate of the mouse pointer.
	 * @see #move(int, int)
	 * @see #getPosX()
	 */
	public static int getPosY() {
		return convertMouseCoordYToAbsolute(invokeAndGetInteger("MouseGetPosY"));
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
	 * @see #getPosX()
	 * @see #getPosY()
	 */
	public static void move(final int x, final int y) {
		invoke("MouseMove", convertMouseCoordXToRelative(x),
				convertMouseCoordYToRelative(y));
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
		invoke("RightClick", convertMouseCoordXToRelative(x),
				convertMouseCoordYToRelative(y));
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
		invoke("RightClickDrag", convertMouseCoordXToRelative(x1),
				convertMouseCoordYToRelative(y1),
				convertMouseCoordXToRelative(x2),
				convertMouseCoordYToRelative(y2));
	}

	/**
	 * Convert relative X coordinate of the mouse pointer to absolute if current
	 * mouse coord mode is 'absolute screen coordinates'.
	 * 
	 * @param x
	 *            The X coordinate of the mouse pointer.
	 */
	private static int convertMouseCoordXToAbsolute(int x) {
		if (Opt.mouseCoordMode == CoordMode.RELATIVE_TO_ACTIVE_WINDOW) {
			return x;
		}
		int absoluteX = x;
		HWND hWnd = getActiveWindow();
		if (hWnd != null) {
			absoluteX += getWindowRect(hWnd).left;
		}
		return absoluteX;
	}

	/**
	 * Convert relative X coordinate of the mouse pointer to relative if current
	 * mouse coord mode is 'absolute screen coordinates'.
	 * 
	 * @param x
	 *            The X coordinate of the mouse pointer.
	 */
	private static int convertMouseCoordXToRelative(int x) {
		if (Opt.mouseCoordMode == CoordMode.RELATIVE_TO_ACTIVE_WINDOW) {
			return x;
		}
		int absoluteX = x;
		HWND hWnd = getActiveWindow();
		if (hWnd != null) {
			absoluteX -= getWindowRect(hWnd).left;
		}
		return absoluteX;
	}

	/**
	 * Convert relative Y coordinate of the mouse pointer to absolute if current
	 * mouse coord mode is 'absolute screen coordinates'.
	 * 
	 * @param y
	 *            The Y coordinate of the mouse pointer.
	 */
	private static int convertMouseCoordYToAbsolute(int y) {
		if (Opt.mouseCoordMode == CoordMode.RELATIVE_TO_ACTIVE_WINDOW) {
			return y;
		}
		int absoluteY = y;
		HWND hWnd = getActiveWindow();
		if (hWnd != null) {
			absoluteY += getWindowRect(hWnd).top;
		}
		return absoluteY;
	}

	/**
	 * Convert relative Y coordinate of the mouse pointer to relative if current
	 * mouse coord mode is 'absolute screen coordinates'.
	 * 
	 * @param y
	 *            The Y coordinate of the mouse pointer.
	 */
	private static int convertMouseCoordYToRelative(int y) {
		if (Opt.mouseCoordMode == CoordMode.RELATIVE_TO_ACTIVE_WINDOW) {
			return y;
		}
		int absoluteY = y;
		HWND hWnd = getActiveWindow();
		if (hWnd != null) {
			absoluteY -= getWindowRect(hWnd).top;
		}
		return absoluteY;
	}

	private static RECT getWindowRect(HWND hWnd) {
		RECT rect = new RECT();
		User32Ext.INSTANCE.GetWindowRect(hWnd, rect);
		return rect;
	}
}
