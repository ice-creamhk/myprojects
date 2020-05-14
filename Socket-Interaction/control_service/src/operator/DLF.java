package operator;

import java.io.File;
import java.util.ArrayList;
import socket.FileDownLoadSocketThread;;

public class DLF extends BaseOperator {

	@Override
	public ArrayList<String> exe(String cmdBody) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> msgBackList=new ArrayList<>();
		File file=new File(cmdBody);
		long file_length= file.length();
		
		FileDownLoadSocketThread  fileDownLoadSocketThread=new FileDownLoadSocketThread(file,0);
		fileDownLoadSocketThread.start();
		msgBackList.add(String.valueOf(fileDownLoadSocketThread.new_port));
		msgBackList.add(String.valueOf(file_length));
		System.out.println("下载文件的长度是："+file_length);
		return msgBackList;
	}
		
}
