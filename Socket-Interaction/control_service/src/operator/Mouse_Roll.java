package operator;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;

public class Mouse_Roll {
	
	public void exe(String cmdBody) throws AWTException  {
		// TODO Auto-generated method stub
		int num=Integer.parseInt(cmdBody);
    	Robot robot=new Robot();
    	robot.mouseWheel(num);   	
    	
	}
}
