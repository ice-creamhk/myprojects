package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class fileUpLoadSocketThread extends Thread{
	
	 private ServerSocket serverSocket;
	 private long filePos=0;
	 private File file;
	 public int new_port;
	 Socket socket;  
	 private DataInputStream dis;
	 private FileOutputStream fos;
	 String filename;
	 
	public fileUpLoadSocketThread(File file,long filePos) {
		 try {
			 serverSocket = new ServerSocket(0);//动态分配可用端口
			 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
			 }
			 new_port=serverSocket.getLocalPort();
			 System.out.println("新的端口号是："+new_port);
			 this.file=file;
			 this.filePos=filePos;
			 
			 File aimfile = new File("C:\\pcdownload");
			 if(!aimfile.exists()) {
				 boolean result=aimfile.mkdirs();
		            System.out.println("创建文件夹情况"+result);
			 }
			 String data=file.toString();
			 System.out.println("data是"+data);
			 String[] value=data.split("\\\\");
			 System.out.println("数组是"+value[0]);
			 filename="C:\\pcdownload\\"+value[value.length-1];
			 System.out.println("文件名是"+filename);
			
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		File pfile=new File(filename);
		
		 try {
			 if(!pfile.exists()) {
				 pfile.createNewFile();
			 }
			 	socket = serverSocket.accept();
			 	System.out.println("成功连接，开始下载"+file.toString());
			 	
	            dis = new DataInputStream(socket.getInputStream());
//	            String fileName = dis.readUTF();
//	            System.out.println("wenjianming"+fileName);
//	            long fileLength = dis.readLong();
//	            File pfile = new File(filename);
	            System.out.println("======== 接收地址" + pfile.toString());

	            if(!pfile.exists()){
	                pfile.createNewFile();
	            }
	            fos = new FileOutputStream(pfile);
	            byte[] bytes = new byte[1024*8];
	            int length = 0;
	            System.out.println("======== 开始接收文件");
	            long progress = 0;
	            while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
	                fos.write(bytes, 0, length);
	                fos.flush();
	                progress += length;
	                System.out.print("| " + (100 * progress / pfile.length()) + "% |");
	            }
	            System.out.println("======== 文件接收成功 File Name：" + filename);

	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (fos != null)
	                    fos.close();
	                if (dis != null)
	                    dis.close();
	                socket.close();
	            } catch (Exception e) {
	            }
	        }
		
		super.run();
	}
		
	
}
