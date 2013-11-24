package cn.com.jautoitx;

import java.awt.Frame;

import org.junit.Assert;
import org.junit.Test;

import cn.com.jautoitx.Opt.CoordMode;

public class MouseTest extends BaseTest {
	@Test
	public void leftClick() {
		String title = "leftClick - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			Mouse.leftClick(100, 200);
			int x = Mouse.getPosX();
			int y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 200) || (y == 199));

			Mouse.leftClick(120, 230);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));

			// test relative mouse coord mode
			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.leftClick(100, 200);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 200) || (y == 199));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 220) || (y == 219));

			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.leftClick(120, 230);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 250) || (y == 249));
		} finally {
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			Win.waitNotActive(title, 3);
		}
	}

	@Test
	public void leftClickDrag() {
		String title = "leftClickDrag - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			Mouse.leftClickDrag(100, 200, 110, 210);
			int x = Mouse.getPosX();
			int y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 210) || (y == 209));

			Mouse.leftClickDrag(120, 230, 130, 240);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 240) || (y == 239));

			// test relative mouse coord mode
			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.leftClickDrag(100, 200, 110, 210);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 210) || (y == 209));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));

			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.leftClickDrag(120, 230, 130, 240);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 240) || (y == 239));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 140) || (x == 139));
			Assert.assertTrue("y is " + y, (y == 260) || (y == 259));
		} finally {
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			Win.waitNotActive(title, 3);
		}
	}

	@Test
	public void mouseGetPosX() {
		String title = "mouseGetPosX - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			Mouse.move(10, 50);
			int x = Mouse.getPosX();
			int y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));

			Mouse.move(100, 500);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));

			// test relative mouse coord mode
			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.move(10, 50);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 20) || (x == 19));
			Assert.assertTrue("y is " + y, (y == 70) || (y == 69));

			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.move(100, 500);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 520) || (y == 519));
		} finally {
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			Win.waitNotActive(title, 3);
		}
	}

	@Test
	public void mouseGetPosY() {
		String title = "mouseGetPosY - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			Mouse.move(10, 50);
			int x = Mouse.getPosX();
			int y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));

			Mouse.move(100, 500);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));

			// test relative mouse coord mode
			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.move(10, 50);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 20) || (x == 19));
			Assert.assertTrue("y is " + y, (y == 70) || (y == 69));

			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.move(100, 500);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 520) || (y == 519));
		} finally {
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			Win.waitNotActive(title, 3);
		}
	}

	@Test
	public void mouseMove() {
		String title = "mouseMove - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			Mouse.move(10, 50);
			int x = Mouse.getPosX();
			int y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));

			Mouse.move(100, 500);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));

			// test relative mouse coord mode
			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.move(10, 50);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 10) || (x == 9));
			Assert.assertTrue("y is " + y, (y == 50) || (y == 49));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 20) || (x == 19));
			Assert.assertTrue("y is " + y, (y == 70) || (y == 69));

			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.move(100, 500);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 500) || (y == 499));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 520) || (y == 519));
		} finally {
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			Win.waitNotActive(title, 3);
		}
	}

	@Test
	public void rightClick() {
		String title = "rightClick - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			Mouse.rightClick(100, 200);
			int x = Mouse.getPosX();
			int y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 200) || (y == 199));

			Mouse.rightClick(120, 230);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));

			// test relative mouse coord mode
			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.rightClick(100, 200);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 100) || (x == 99));
			Assert.assertTrue("y is " + y, (y == 200) || (y == 199));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 220) || (y == 219));

			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.rightClick(120, 230);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 250) || (y == 249));
		} finally {
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			Win.waitNotActive(title, 3);
		}
	}

	@Test
	public void rightClickDrag() {
		String title = "rightClickDrag - " + currentTimeMillis;
		Frame frame = createFrame(title, 10, 20);

		try {
			Mouse.rightClickDrag(100, 200, 110, 210);
			int x = Mouse.getPosX();
			int y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 210) || (y == 209));

			Mouse.rightClickDrag(120, 230, 130, 240);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 240) || (y == 239));

			// test relative mouse coord mode
			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.rightClickDrag(100, 200, 110, 210);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 110) || (x == 109));
			Assert.assertTrue("y is " + y, (y == 210) || (y == 209));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 120) || (x == 119));
			Assert.assertTrue("y is " + y, (y == 230) || (y == 229));

			Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW);
			Mouse.rightClickDrag(120, 230, 130, 240);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 130) || (x == 129));
			Assert.assertTrue("y is " + y, (y == 240) || (y == 239));
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);
			x = Mouse.getPosX();
			y = Mouse.getPosY();
			Assert.assertTrue("x is " + x, (x == 140) || (x == 139));
			Assert.assertTrue("y is " + y, (y == 260) || (y == 259));
		} finally {
			Opt.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES);

			frame.setVisible(false);
			Win.waitNotActive(title, 3);
		}
	}
}
