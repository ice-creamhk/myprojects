package socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

//文件下载服务线程
public class FileDownLoadSocketThread extends Thread {

	private ServerSocket serverSocket;
	private long filePos = 0;
	private File file;
	public int new_port;
	Socket socket;
	private FileInputStream fis;
	private DataOutputStream dos;

	public FileDownLoadSocketThread(File file, long filePos) {
		// TODO Auto-generated constructor stub
		try {
			serverSocket = new ServerSocket(0);// 动态分配可用端口
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new_port = serverSocket.getLocalPort();
		System.out.println("新的端口号是：" + new_port);
		this.file = file;
		this.filePos = filePos;

	}

	@Override
	public void run() {
		System.out.println("进入线程" + serverSocket.getLocalPort());
		try {
			socket = serverSocket.accept();
			System.out.println("成功连接，开始传输" + file.toString());
			fis = new FileInputStream(file);
			dos = new DataOutputStream(socket.getOutputStream());

			// 文件名和长度
			dos.writeUTF(file.getName());
			dos.flush();
			dos.writeLong(file.length());
			dos.flush();

			// 开始传输文件
			System.out.println("======== 开始传输文件 ========");
			byte[] bytes = new byte[1024];
			int length = 0;
			long progress = 0;
			while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
				dos.write(bytes, 0, length);
				dos.flush();
				progress += length;
				System.out.print("| " + (100 * progress / file.length()) + "% |");
			}
			System.out.println();
			System.out.println("======== 文件传输成功 ========");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (dos != null)
				try {
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
