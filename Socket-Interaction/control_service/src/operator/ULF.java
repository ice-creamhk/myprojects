package operator;

import java.io.File;
import java.util.ArrayList;

import socket.FileDownLoadSocketThread;
import socket.fileUpLoadSocketThread;

public class ULF extends BaseOperator{

	@Override
	public ArrayList<String> exe(String cmdBody) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> msgBackList=new ArrayList<>();
		File file=new File(cmdBody);
		long file_length= file.length();
		int filemode=-1;
		FileNameUtils fileNameUtils=new FileNameUtils();
		File newfile=fileNameUtils.checkFile(cmdBody);
		
		fileUpLoadSocketThread  fileUpLoadSocketThread=new fileUpLoadSocketThread(newfile,filemode);
		fileUpLoadSocketThread.start();
		msgBackList.add(String.valueOf(fileUpLoadSocketThread.new_port));
		msgBackList.add(String.valueOf(file_length));
		System.out.println("上传文件的长度是："+file_length);
		return msgBackList;
		
	}

}
