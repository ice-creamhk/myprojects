package operator;

import java.lang.reflect.Executable;

public class SetDelayed {
		public void exe(String cmdBody) throws NumberFormatException, InterruptedException {
			Thread.sleep(Integer.valueOf(cmdBody));
			
		}
}
