package operator;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Mouse_Clik {
	public void exe(String cmdBody) throws AWTException, InterruptedException  {
		// TODO Auto-generated method stub
		
    	Robot robot=new Robot();
    	if(cmdBody.equals("left")) {
    		robot.mousePress(KeyEvent.BUTTON1_MASK);
    		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
    	}
    	else if(cmdBody.equals("right")) {
    		robot.mousePress(KeyEvent.BUTTON3_MASK);
    		robot.mouseRelease(KeyEvent.BUTTON3_MASK);
		}
    	else if(cmdBody.equals("left_press")) {
    		robot.mousePress(KeyEvent.BUTTON1_MASK);
		}
    	else if(cmdBody.equals("left_release")) {
    		robot.keyRelease(KeyEvent.BUTTON1_MASK);
		}
    	else if(cmdBody.equals("right_press")) {
    		robot.mousePress(KeyEvent.BUTTON3_MASK);
    	}
    	else if(cmdBody.equals("right_release")) {
    		robot.keyRelease(KeyEvent.BUTTON3_MASK);
		}
    	else {
			
		}
	}
}
