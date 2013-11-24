package cn.com.jautoitx;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AutoItXTest.class, ClipTest.class, IniTest.class,
		KeyboardTest.class, MiscTest.class, MouseTest.class, OptTest.class,
		WinTest.class })
public class AllTests {

}
