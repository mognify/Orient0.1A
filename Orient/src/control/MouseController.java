package control;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;

import org.opencv.core.Point;

public class MouseController {
	// reads instructions from MouseEngine
	//protected static Queue<Thread> q;
	
	protected static void next() {
		//q.remove().start();
	}
	
	protected static void click(int x1, int y1, int x2, int y2, int delayMS) throws AWTException, InterruptedException {
		Robot bot = new Robot(MouseInfo.getPointerInfo().getDevice());
		
		Thread.sleep(333);
	    bot.mouseMove(x1, y1);    
	    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    Thread.sleep(delayMS);
	    bot.mouseMove(x2, y2);    
	    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	    return;
	}

	public static void rightClick(String substring) throws HeadlessException, AWTException {
		//q.add(new Thread() {
		//public void run() {
			Robot bot = new Robot(MouseInfo.getPointerInfo().getDevice());
			int x = Integer.valueOf(substring.replace("-", " ").split(" ")[0]);
			int y = Integer.valueOf(substring.replace("-", " ").split(" ")[1]);
			int delayMS = Integer.valueOf(substring.replace("-", " ").split(" ")[2]);
			try {
				bot = new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    bot.mouseMove(x, y);    
		    bot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
		    try {
				Thread.sleep(delayMS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    bot.mouseMove(x, y);    
		    bot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
		//}
	//});
	return;
	}

	public static void click(Point p) throws AWTException, InterruptedException {
		click((int)p.x, (int)p.y, (int)p.x, (int)p.y, 333);
	}
}
