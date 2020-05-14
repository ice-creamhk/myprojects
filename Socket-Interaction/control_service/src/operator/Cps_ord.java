package operator;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class Cps_ord {
	public void exe(String cmdBody) throws InterruptedException {
		Thread.sleep(5000);
		 Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();//获取剪切板
		    Transferable tText = new StringSelection(cmdBody);//cmdBody为String字符串，需要拷贝进剪贴板的内容
		    clip.setContents(tText, null); //设置剪切板内容
	}
}
