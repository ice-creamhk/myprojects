package operator;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.util.ArrayList;

public class MovXY {
	

	public void exe(String cmdBody) throws AWTException  {
		// TODO Auto-generated method stub
		//坐标改变量
		String[] n=cmdBody.split(",");
		double sumx=Double.parseDouble(n[0]);
		double sumy=Double.parseDouble(n[1]);
		//获取屏幕的尺寸
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		double height = dimension.getHeight();
		double width = dimension.getWidth();
		//获取当前鼠标的坐标
        Point point = java.awt.MouseInfo.getPointerInfo().getLocation();
        System.out.println("Location:x=" + point.x + ", y=" + point.y);
        
        sumx+=point.x;
        sumy+=point.y;
        if (sumx>width) {
			sumx=width;
		}
        if(sumx<0) {
        	sumx=0;
        }
        if(sumy>height) {
        	sumy=height;
        }
        if(sumy<0) {
        	sumy=0;
        }
        //进行移动
		Robot robot=new Robot();
		robot.mouseMove((int)(sumx),(int)(sumy));

	}
	
}
