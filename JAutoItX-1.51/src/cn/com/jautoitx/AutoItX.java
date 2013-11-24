package cn.com.jautoitx;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.ComThread;
import com.jacob.com.LibraryLoader;
import com.jacob.com.Variant;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * AutoIt is a simple tool that can simulate key presses, mouse movements and
 * window commands (maximize, minimize, wait for, etc.) in order to automate any
 * windows based task (or even windowed DOS tasks).
 * 
 * AutoItX 1.51 is an ActiveX control version of AutoIt. It allows you to
 * perform the following types of functions:
 * 
 * 1. Sending keystrokes and mouse clicks (all characters, keyboard layout
 * independent).
 * 
 * 2. Window functions (e.g. minimizing, hiding, restoring, waiting for,
 * activating (even under Win98/NT2000)).
 * 
 * 3. Sleep (for those with WSH 1.0 :) )
 * 
 * 4. INI file reading and writing. 4. Simple clipboard (text) commands.
 * 
 * 5. Simple clipboard (text) commands.
 * 
 * 6. Shutdown and reboot commands (including a forced reboot that works under
 * both 9x and NT).
 * 
 * 7. Blocking input under Windows NT4 SP6 and 2000.
 * 
 * @author zhengbo.wang
 */
public class AutoItX {
	/* log4j's config file path */
	public static String LOG4J_CONF_PATH = "conf/log4j.properties";

	/* Buffer size used in clipGet method */
	public static final int CLIP_GET_BUF_SIZE = 16385;

	/* Buffer size used in iniRead method */
	public static final int INI_READ_BUF_SIZE = 16385;

	/* Buffer size used in winGetActiveTitle method */
	public static final int WIN_GET_ACTIVE_TITLE_BUF_SIZE = 16385;

	/* The return value used for AutoItXControl component when success */
	private static final int SUCCESS_RETURN_VALUE = 1;

	/* AutoItX library name */
	private static final String AUTOITX_DLL_LIB_NAME = "AutoItX";

	/* AutoItX library path */
	private static final String DLL_LIB_RESOURCE_PATH = "/cn/com/jautoitx/lib/";

	private static final Logger logger = Logger.getLogger(AutoItX.class);
	private static AutoItXLibrary autoItX;
	private static final String AUTOITX_PROGRAM_ID = "AutoItX.Control";
	private static ActiveXComponent autoItXControl = null;
	private static Map<Thread, Boolean> threads = new WeakHashMap<Thread, Boolean>();

	/* AutoItX's version */
	private static String autoItXVersion = null;

	static {
		// Initialize AutoItX
		initAutoItX();
	}

	/**
	 * Initialize AutoItX.
	 */
	private static void initAutoItX() {
		// Initialize log4j
		try {
			final File file = new File(LOG4J_CONF_PATH);
			if (file.exists() && file.canRead()) {
				System.setProperty("log4j.debug", "true");
				PropertyConfigurator.configure(file.getAbsolutePath());
			}
		} catch (Exception e) {
			logger.error("Unable to initialize log4j.", e);
		}

		// Initialize AutoItX
		try {
			loadNativeLibrary();

			if (logger.isDebugEnabled()) {
				logger.debug(String
						.format("AutoItX %s initialized.", version()));
			}
		} catch (Throwable e) {
			logger.error(
					"Unable to initialize " + AutoItX.class.getSimpleName(), e);
		}
	}

	/**
	 * Unpacking and loading the library into the Java Virtual Machine.
	 * 
	 * @throws IOException
	 */
	private static void loadNativeLibrary() throws IOException {
		System.setProperty("com.jacob.autogc", "true");

		File tmpFile = File.createTempFile("JAutoItX", ".tmp");
		tmpFile.deleteOnExit();

		// Copy all dll library to temporary-file directory
		File autoItXLibFile = copyDllLibToDir(tmpFile.getParentFile(),
				AUTOITX_DLL_LIB_NAME);
		File jacobLibFile = copyDllLibToDir(tmpFile.getParentFile(),
				LibraryLoader.getPreferredDLLName());
		autoItXVersion = Win32.getFileVersion(autoItXLibFile);

		// Register AutoItX.Control
		System.load(autoItXLibFile.getPath());
		autoItX = (AutoItXLibrary) Native.loadLibrary(autoItXLibFile.getName(),
				AutoItXLibrary.class);
		Native.setProtected(true);
		autoItX.DllRegisterServer();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				autoItX.DllUnregisterServer();
			}
		});

		// Ask LibraryLoader to load the dll for us based on the path we set
		System.setProperty(LibraryLoader.JACOB_DLL_PATH,
				jacobLibFile.getAbsolutePath());
		LibraryLoader.loadJacobLibrary();
	}

	private static File copyDllLibToDir(File dir, String dllLibName)
			throws IOException {
		// Get what the system "thinks" the library name should be.
		String libNativeName = System.mapLibraryName(dllLibName);

		// Create the temp file for this instance of the library.
		// Slice up the library name.
		int i = libNativeName.lastIndexOf('.');
		String libNativePrefix = libNativeName.substring(0, i) + '_';
		String libNativeSuffix = libNativeName.substring(i);
		File libFile = File.createTempFile(libNativePrefix, libNativeSuffix,
				dir);
		libFile.deleteOnExit();

		// This may return null in some circumstances.
		InputStream libInputStream = AutoItX.class
				.getResourceAsStream(DLL_LIB_RESOURCE_PATH.toLowerCase()
						+ libNativeName);
		if (libInputStream == null) {
			throw new IOException("Unable to locate the native library.");
		}

		// Copy dll library to the temp file.
		OutputStream libOutputStream = new FileOutputStream(libFile);
		byte[] buffer = new byte[4 * 1024];
		int size;
		while ((size = libInputStream.read(buffer)) != -1) {
			libOutputStream.write(buffer, 0, size);
		}

		// Close output and input stream
		closeStream(libOutputStream);
		closeStream(libInputStream);

		return libFile;
	}

	/**
	 * Get AutoItX's version.
	 * 
	 * @return Returns AutoItX's version.
	 */
	public static String version() {
		return autoItXVersion;
	}

	protected static HWND getActiveWindow() {
		HWND hWnd = Win32.user32.GetActiveWindow();
		if (hWnd == null) {
			hWnd = Win32.user32.GetForegroundWindow();
		}
		return hWnd;
	}

	protected static String defaultString(String text) {
		return (text == null) ? "" : text;
	}

	protected static Variant invoke(String name, Object... args) {
		init();
		Variant[] variantArgs = new Variant[args.length];
		for (int i = 0; i < args.length; i++) {
			variantArgs[i] = new Variant(args[i]);
		}
		return autoItXControl.invoke(name, variantArgs);
	}

	protected static String invokeAndGetString(String name, Object... args) {
		Variant result = invoke(name, args);
		return (result == null) ? null : result.getString();
	}

	protected static Integer invokeAndGetInteger(String name, Object... args) {
		Variant result = invoke(name, args);
		return (result == null) ? null : result.getInt();
	}

	protected static boolean invokeAndGetBoolean(String name, Object... args) {
		Integer result = invokeAndGetInteger(name, args);
		return (result == null) ? false
				: (result.intValue() == AutoItX.SUCCESS_RETURN_VALUE);
	}

	private static void closeStream(final Closeable closable) {
		if (closable != null) {
			try {
				closable.close();
			} catch (Exception e) {
				// Ignore exception
			}
		}
	}

	private static void init() {
		Thread currentThread = Thread.currentThread();
		synchronized (threads) {
			if (!threads.containsKey(currentThread)) {
				ComThread.InitSTA();

				if (autoItXControl == null) {
					try {
						autoItXControl = new ActiveXComponent(
								AUTOITX_PROGRAM_ID);
					} catch (ComFailException e) {
						throw new RuntimeException(String.format(
								"Create ActiveXComponent for %s failed.",
								AUTOITX_PROGRAM_ID), e);
					}
				}

				threads.put(currentThread, true);
			}
		}
	}

	private static interface AutoItXLibrary extends Library {
		/**
		 * Register AutoItX.Control.
		 */
		public void DllRegisterServer();

		/**
		 * Unregister AutoItX.Control.
		 */
		public void DllUnregisterServer();
	}
}