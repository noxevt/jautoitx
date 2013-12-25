package cn.com.jautoitx;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.omg.CORBA.BooleanHolder;
import org.omg.CORBA.IntHolder;

import cn.com.jautoitx.Process.ProcPriority;
import cn.com.jautoitx.Process.RunShowFlag;
import cn.com.jautoitx.Process.RunWaitResult;
import cn.com.jautoitx.Process.ShutdownCode;

public class ProcessTest extends BaseTest {
	private static final String ADMINISTRATOR_NAME = "Administrator";
	// TODO:
	private static final String ADMINISTRATOR_PASSWORD = "w1z2b3";
	private static final String ADMINISTRATOR_ERROR_PASSWORD = "~AdminiPassword~";

	private static Sigar sigar = null;

	@BeforeClass
	public static void beforeClass() {
		sigar = new Sigar();
	}

	@Test
	public void close() {
		int pid = runNotepad();

		// close process by name
		Process.close(NOTEPAD);
		sleep(500);
		Assert.assertNull(Process.exists(NOTEPAD));

		pid = runNotepad();

		// close process by pid
		Process.close(pid);
		sleep(500);
		Assert.assertNull(Process.exists(NOTEPAD));
	}

	@Test
	public void exists() {
		Assert.assertNull(Process.exists("notepad.exe"));

		// run notepad
		int pid = Process.run(NOTEPAD);
		Assert.assertTrue(pid > 0);
		Assert.assertEquals(pid, Process.exists("notepad.exe").intValue());
		Assert.assertTrue(Process.exists(pid));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertNull(Process.exists("notepad.exe"));
		Assert.assertFalse(Process.exists(pid));

		Assert.assertNull(Process.exists("System Idle Process"));
		Assert.assertFalse(Process.exists(0));
	}

	@Test
	public void getPriority() {
		// run notepad
		int pid = runNotepad();

		for (ProcPriority priority : ProcPriority.values()) {
			Assert.assertTrue(Process.setPriority(pid, priority));
			Assert.assertEquals(priority, Process.getPriority(pid));
		}

		// close notepad
		closeNotepad(pid);

		Assert.assertNull(Process.getPriority(pid));
	}

	@Test
	public void run() throws SigarException, IOException {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in default mode
		int pid = Process.run(NOTEPAD);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in hide mode
		pid = Process.run(NOTEPAD, RunShowFlag.HIDE);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in minimize mode
		pid = Process.run(NOTEPAD, RunShowFlag.MINIMIZE);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertTrue(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in maximize mode
		pid = Process.run(NOTEPAD, "C:/", RunShowFlag.MAXIMIZE);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertTrue(Win.maximized(NOTEPAD_TITLE));
		Assert.assertEquals("C:", sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in normal mode
		final String workingDir = (new File(".")).getCanonicalPath();
		pid = Process.run(NOTEPAD, workingDir, RunShowFlag.NORMAL);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertEquals(workingDir, sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in normal mode
		Assert.assertNull(Process.run(NOTEPAD + "xxx", RunShowFlag.NORMAL));
	}

	@Test
	public void runAsSet() throws UnknownHostException {
		String domain = InetAddress.getLocalHost().getHostName();

		// run notepad
		Integer pid = runNotepad();
		// close notepad
		closeNotepad(pid);

		// run notepad as error admin account
		Assert.assertTrue(Process.runAsSet(ADMINISTRATOR_NAME, domain,
				ADMINISTRATOR_ERROR_PASSWORD));
		pid = Process.run(NOTEPAD);
		Assert.assertNull(pid);

		// run notepad as admin
		Assert.assertTrue(Process.runAsSet(ADMINISTRATOR_NAME, domain,
				ADMINISTRATOR_PASSWORD));
		pid = runNotepad();
		// close notepad
		closeNotepad(pid);

		// run notepad as error admin account
		Assert.assertTrue(Process.runAsSet(ADMINISTRATOR_NAME, domain,
				ADMINISTRATOR_ERROR_PASSWORD));
		pid = Process.run(NOTEPAD);
		Assert.assertNull(pid);

		pid = Process.run(NOTEPAD);
		Assert.assertNull(pid);

		// Reset user's permissions
		Assert.assertTrue(Process.runAsSet());
		// run notepad
		pid = runNotepad();
		// close notepad
		closeNotepad(pid);

		// run notepad as error admin account
		Assert.assertTrue(Process.runAsSet(ADMINISTRATOR_NAME, domain,
				ADMINISTRATOR_ERROR_PASSWORD));
		pid = Process.run(NOTEPAD);
		Assert.assertNull(pid);

		// Reset user's permissions
		Assert.assertTrue(Process.runAsSet(null, null, null));
		// run notepad
		pid = runNotepad();
		// close notepad
		closeNotepad(pid);

		// run notepad as error admin account
		Assert.assertTrue(Process.runAsSet(ADMINISTRATOR_NAME, domain,
				ADMINISTRATOR_ERROR_PASSWORD));
		pid = Process.run(NOTEPAD);
		Assert.assertNull(pid);

		// Reset user's permissions
		Assert.assertTrue(Process.runAsSet("", "", ""));
		// run notepad
		pid = runNotepad();
		// close notepad
		closeNotepad(pid);

		// run notepad as admin account
		Assert.assertTrue(Process.runAsSet(ADMINISTRATOR_NAME, domain,
				ADMINISTRATOR_PASSWORD));
		// run notepad
		pid = runNotepad();
		// close notepad
		closeNotepad(pid);

		// Reset user's permissions
		Assert.assertTrue(Process.runAsSet());
		// run notepad
		pid = runNotepad();
		// close notepad
		closeNotepad(pid);

		// run notepad as error admin account
		Assert.assertTrue(Process.runAsSet(ADMINISTRATOR_NAME, domain,
				ADMINISTRATOR_ERROR_PASSWORD));
		RunWaitResult result = Process.runWait(NOTEPAD);
		Assert.assertTrue(result.hasError());
		Assert.assertEquals(0, result.getExitCode());

		Assert.assertTrue(Process.runAsSet());
		final ObjectHolder objHolder = new ObjectHolder();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				objHolder.value = Process.runWait(NOTEPAD);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		Assert.assertTrue(Win.close(NOTEPAD_TITLE));
		sleep(2000);
		Assert.assertFalse(thread.isAlive());
		result = (RunWaitResult) objHolder.value;
		Assert.assertNotNull(result);
		Assert.assertFalse(result.hasError());
		Assert.assertEquals(0, result.getExitCode());
	}

	@Test
	public void runWait() throws SigarException, IOException {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in default mode
		final IntHolder exitCodeHolder = new IntHolder(Integer.MIN_VALUE);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				exitCodeHolder.value = Process.runWait(NOTEPAD).getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in hide mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				exitCodeHolder.value = Process.runWait(NOTEPAD,
						RunShowFlag.HIDE).getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in minimize mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				exitCodeHolder.value = Process.runWait(NOTEPAD,
						RunShowFlag.MINIMIZE).getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertTrue(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in maximize mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				exitCodeHolder.value = Process.runWait(NOTEPAD, "C:/",
						RunShowFlag.MAXIMIZE).getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertTrue(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		int pid = Win.getProcess(NOTEPAD_TITLE);
		Assert.assertTrue(pid > 0);
		Assert.assertEquals("C:", sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in normal mode
		final String workingDir = (new File(".")).getCanonicalPath();
		exitCodeHolder.value = Integer.MIN_VALUE;
		final BooleanHolder errorHolder = new BooleanHolder(true);
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				RunWaitResult result = Process.runWait(NOTEPAD, workingDir,
						RunShowFlag.NORMAL);
				exitCodeHolder.value = result.getExitCode();
				errorHolder.value = result.hasError();
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		pid = Win.getProcess(NOTEPAD_TITLE);
		Assert.assertTrue(pid > 0);
		Assert.assertEquals(workingDir, sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(2000);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);
		Assert.assertFalse(errorHolder.value);

		// run notepad in normal mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		errorHolder.value = false;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				RunWaitResult result = Process.runWait(NOTEPAD + "xxx",
						RunShowFlag.NORMAL);
				exitCodeHolder.value = result.getExitCode();
				errorHolder.value = result.hasError();
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertFalse(thread.isAlive());
		Assert.assertTrue(errorHolder.value);
	}

	@Test
	public void setPriority() {
		// run notepad
		int pid = runNotepad();

		for (ProcPriority priority : ProcPriority.values()) {
			Assert.assertTrue(Process.setPriority(pid, priority));
			Assert.assertEquals(priority, Process.getPriority(pid));
		}

		// close notepad
		closeNotepad(pid);

		Assert.assertFalse(Process.setPriority(pid, ProcPriority.NORMAL));
	}

	@Ignore
	@Test
	public void shutdown() {
		Assert.assertTrue(Process.shutdown(ShutdownCode.LOGOFF));
	}

	@Test
	public void testWait() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Process.wait(NOTEPAD);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());

		// run notepad
		int pid = runNotepad();
		Assert.assertFalse(thread.isAlive());

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		final IntHolder timeHolder = new IntHolder();
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				Process.wait(NOTEPAD, 2);
				long end = System.currentTimeMillis();
				timeHolder.value = (int) (end - start);
			}
		});
		thread.start();
		sleep(3000);
		Assert.assertFalse(thread.isAlive());
		Assert.assertTrue("Assert timeHolder.value >= 2000, but actual is "
				+ timeHolder.value + ".", timeHolder.value >= 2000);
		Assert.assertTrue("Assert timeHolder.value <= 3000, but actual is "
				+ timeHolder.value + ".", timeHolder.value <= 3000);
	}

	@Test
	public void waitClose() {
		// run notepad
		int pid = Process.run(NOTEPAD);
		Assert.assertTrue(pid > 0);
		sleep(500);
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		final int notepadPid = pid;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Process.waitClose(notepadPid);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(thread.isAlive());

		// run notepad
		pid = Process.run(NOTEPAD);
		Assert.assertTrue(pid > 0);
		sleep(500);
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Process.waitClose(NOTEPAD);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(thread.isAlive());

		// run notepad
		pid = Process.run(NOTEPAD);
		Assert.assertTrue(pid > 0);
		sleep(500);
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));
		long start = System.currentTimeMillis();
		Process.waitClose(pid, 2);
		long end = System.currentTimeMillis();
		Assert.assertTrue((end - start) >= 2000);
		Assert.assertTrue((end - start) <= 3000);
		Assert.assertTrue(Process.exists(pid));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		start = System.currentTimeMillis();
		Process.waitClose(pid, 2);
		end = System.currentTimeMillis();
		Assert.assertTrue((end - start) >= 0);
		Assert.assertTrue((end - start) <= 500);
	}
}
