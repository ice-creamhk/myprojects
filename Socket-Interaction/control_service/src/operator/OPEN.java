package operator;

import java.awt.Desktop;
import java.io.File;

public class OPEN {
	
	public  OPEN() {
		
	}
	public void exeopen(String cmdBody) {
		Desktop desk=Desktop.getDesktop();  
		try  
		{  
		    File file=new File(cmdBody);//创建一个java文件系统  
		    desk.open(file); //调用open（File f）方法打开文件   
		}catch(Exception e)  
		{  
		    System.out.println(e.toString());  
		}  
	}
}
