package operator;

import java.io.IOException;

public class Cmd_ord  {
	
	
	public void exe(String cmdBody) throws IOException {
		Runtime.getRuntime().exec(cmdBody);
	}
}
