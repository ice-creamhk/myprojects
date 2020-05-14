package operator;

import java.io.IOException;

public class Slp_ord {

	public void exe(String cmdBody) throws NumberFormatException, InterruptedException, IOException {
		Thread.sleep(Integer.valueOf(cmdBody));
		int num=Integer.valueOf(cmdBody);
		Runtime.getRuntime().exec("Shutdown -s -t "+num/1000);
	}
}
