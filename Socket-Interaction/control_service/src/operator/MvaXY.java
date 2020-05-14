package operator;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;

public class MvaXY {
	
	public void exe(String cmdBody) throws AWTException  {
		// TODO Auto-generated method stub
		//绝对值坐标
		String[] n=cmdBody.split(",");
		double sumx=Double.parseDouble(n[0]);
		double sumy=Double.parseDouble(n[1]);
		//获取屏幕的尺寸
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		double height = dimension.getHeight();
		double width = dimension.getWidth();
        
        //进行移动
    	Robot robot=new Robot();
        if(sumx<=1 && sumy<=1) {
    		robot.mouseMove((int)(height*sumx),(int)(width*sumy));
        }
        else {
            robot.mouseMove((int)(sumx),(int)(sumy));
		}	

	}
	
}
