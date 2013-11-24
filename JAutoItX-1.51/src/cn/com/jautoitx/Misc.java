package cn.com.jautoitx;

public class Misc extends AutoItX {
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
		invoke("BlockInput", toggle ? "on" : "off");
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
		invoke("Shutdown", modes);
	}

	/**
	 * This function simply pauses for an amount of time.
	 * 
	 * @param milliseconds
	 *            The amount of time to sleep in milliseconds. (Note: 1000
	 *            milliseconds = 1 second)
	 */
	public static void sleep(final int milliseconds) {
		invoke("Sleep", milliseconds);
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
}
