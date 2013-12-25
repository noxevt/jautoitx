package cn.com.jautoitx;

public class Misc extends AutoItX {
	private static final String CD_TRAY_STATUS_OPEN = "open";
	private static final String CD_TRAY_STATUS_CLOSED = "closed";

	/**
	 * Disable/enable the mouse and keyboard.
	 * 
	 * If BlockInput is enabled, the Alt keypress cannot be sent!<br>
	 * The table below shows how BlockInput behavior depends on Windows'
	 * version; however, pressing <i>Ctrl+Alt+Del</i> on any platform will
	 * re-enable input due to a Windows API feature.<br>
	 * <br>
	 * <table width="100%" border="1">
	 * <tr>
	 * <td><b><i>Operating System</b></i></td>
	 * <td><b><i>"BlockInput" Results</b></i></td>
	 * </tr>
	 * <tr>
	 * <td>Windows 95</td>
	 * <td>No effect.</td>
	 * </tr>
	 * <tr>
	 * <td>Windows 98/Me</td>
	 * <td>User input is blocked but AutoIt is also unable to simulate input.</td>
	 * </tr>
	 * <tr>
	 * <td>Windows NT 4 (Without ServicePack 6)</td>
	 * <td>No effect.</td>
	 * </tr>
	 * <tr>
	 * <td>Windows NT 4 (With ServicePack 6)</td>
	 * <td>User input is blocked and AutoIt can simulate most input.</td>
	 * </tr>
	 * <tr>
	 * <td>Windows 2000/XP</td>
	 * <td>User input is blocked and AutoIt can simulate most input.</td>
	 * </tr>
	 * </table>
	 * <br>
	 * Note that functions such as WinMove() will still work on Windows 98/Me
	 * when BlockInput is enabled.
	 * 
	 * @param disable
	 *            true = Disable user input<br>
	 *            false = Enable user input
	 */
	public static void blockInput(final boolean disable) {
		autoItX.AU3_BlockInput(disable ? 1 : 0);
	}

	/**
	 * Opens or closes the CD tray.
	 * 
	 * CDTray works as expected with virtual CD drives such as DAEMON Tools.
	 * CDTray does not work on non-local/mapped CD drives; CDTray must be run
	 * from the computer whose drive it affects. CDTray("X:", "close") tends to
	 * return 0 even on laptop-style cd trays that can only be closed manually.
	 * 
	 * @param drive
	 *            The drive letter of the CD tray to control, in the format D:,
	 *            E:, etc.
	 * @param open
	 *            Specifies if you want the CD tray to be open or closed.
	 * @return Returns true if success, returns false if drive is locked via CD
	 *         burning software or if the drive letter is not a CD drive.
	 */
	public static boolean cdTray(final String drive, final boolean open) {
		return cdTray(drive, open ? CD_TRAY_STATUS_OPEN : CD_TRAY_STATUS_CLOSED);
	}

	/**
	 * Opens or closes the CD tray.
	 * 
	 * CDTray works as expected with virtual CD drives such as DAEMON Tools.
	 * CDTray does not work on non-local/mapped CD drives; CDTray must be run
	 * from the computer whose drive it affects. CDTray("X:", "close") tends to
	 * return 0 even on laptop-style cd trays that can only be closed manually.
	 * 
	 * @param drive
	 *            The drive letter of the CD tray to control, in the format D:,
	 *            E:, etc.
	 * @param status
	 *            Specifies if you want the CD tray to be open or closed: "open"
	 *            or "closed".
	 * @return Returns true if success, returns false if drive is locked via CD
	 *         burning software or if the drive letter is not a CD drive.
	 */
	public static boolean cdTray(final String drive, final String status) {
		return autoItX.AU3_CDTray(stringToWString(defaultString(drive)),
				stringToWString(status)) == SUCCESS_RETURN_VALUE;
	}

	/**
	 * Checks if the current user has administrator privileges.
	 * 
	 * @return Return 1 if the current user has administrator privileges, return
	 *         0 if user lacks admin privileges.
	 */
	public static boolean isAdmin() {
		return autoItX.AU3_IsAdmin() == SUCCESS_RETURN_VALUE;
	}

	/**
	 * Pause script execution.
	 * 
	 * Maximum sleep time is 2147483647 milliseconds (24 days).
	 * 
	 * @param milliSeconds
	 *            Amount of time to pause (in milliseconds).
	 */
	public static void sleep(final int milliSeconds) {
		autoItX.AU3_Sleep(milliSeconds);
	}
}
