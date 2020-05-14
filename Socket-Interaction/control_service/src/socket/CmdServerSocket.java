package socket;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import operator.AllCmdOrd;
import operator.Cmd_ord;
import operator.Cps_ord;
import operator.DIR;
import operator.DIRPAN;
import operator.DIRbefore;
import operator.DLF;
import operator.KEY;
import operator.Mouse_Clik;
import operator.Mouse_Roll;
import operator.MovXY;
import operator.MvaXY;
import operator.OPEN;
import operator.SetDelayed;
import operator.Slp_ord;
import operator.ULF;

public class CmdServerSocket {

	int port = 8019;// 自定义一个端口，端口号尽可能挑选一些不被其他服务占用的端口，祥见http://blog.csdn.net/hsj521li/article/details/7678880
	static int connect_count = 0;// 连接次数统计
	BufferedReader bufferedReader;
	ArrayList<String> msgBackList = new ArrayList<String>();
	ArrayList<String> ls_list= new ArrayList<String>();
	public Socket now_socket = new Socket();
	int all_num = 0;
	DIR dir = new DIR();
	OPEN open=new OPEN();
	DIRPAN dirpan=new DIRPAN();
	DIRbefore diRbefore=new DIRbefore();
	String sb="";
	
	
	public CmdServerSocket() {
		// TODO Auto-generated constructor stub
	}

	public CmdServerSocket(int port) {
		super();
		this.port = port;
	}

	String readSocketMsg(Socket socket) throws Exception {
		// ArrayList<String> msgList = new ArrayList<>();
		String msg_str;
		InputStreamReader isr = new InputStreamReader(socket.getInputStream(), "UTF-8");
		bufferedReader = new BufferedReader(isr);
		msg_str = bufferedReader.readLine();		
		System.out.println("获得命令：" + msg_str);
		return msg_str;
	}

	// 获得解析后的数据
	public void processCmd(String msg_str, Socket socket) throws Exception {
		all_num = 0;
		String new_cmd="";
		String str="";
		String cmd ="";
			cmd = msg_str.substring(0, msg_str.indexOf(":"));// 命令类型
			str = msg_str.substring(cmd.length() + 1);// 文件地址
			System.out.println("准备解析命令："+cmd);
			System.out.println("获得地址："+str);
			 
		     new_cmd=cmd.replaceAll(" +","");
			if(new_cmd.equals("dir")) {
			sb=str;
			ls_list= dir.exeDir(str);
		}	
		else if(new_cmd.equals("opn")) {
			open.exeopen(str);
			ls_list.add("success open!");
		}
		else if(new_cmd.equals("...")) {	
			ls_list=dirpan.exe(str);
		}
		else if(new_cmd.equals("..")){
			ls_list=diRbefore.exe(sb);
			sb=diRbefore.strsb;
		}
		else if(new_cmd.equals("key")) {
			KEY key=new KEY();
			ls_list=key.exe(str);
		}
		else if (new_cmd.equals("mov")) {
			MovXY movXY=new MovXY();
			movXY.exe(str);
			ls_list.add("success mov!");
		}
		else if (new_cmd.equals("clk")) {
			Mouse_Clik mouse_click=new Mouse_Clik();
			mouse_click.exe(str);
			ls_list.add("success clk!");
		}
		else if (new_cmd.equals("rol")) {
			Mouse_Roll mouse_Roll=new Mouse_Roll();
			mouse_Roll.exe(str);
			ls_list.add("success roll!");
		}
		else if (new_cmd.equals("cmd")) {
			Cmd_ord cmd_ord=new Cmd_ord();
			cmd_ord.exe(str);
			ls_list.add("success cmd!");
		}
		else if (new_cmd.equals("slp")) {
			Slp_ord slp_ord=new Slp_ord();
			slp_ord.exe(str);
			ls_list.add("success slp!");
		}
		else if (new_cmd.equals("cps")) {
			Cps_ord cps_ord=new Cps_ord();
			cps_ord.exe(str);
			ls_list.add("success cps!");
		}
		else if (new_cmd.equals("mva")) {
			MvaXY mvaXY=new MvaXY();
			mvaXY.exe(str);
			ls_list.add("success mva!");
		}
		else if (new_cmd.equals("dyd")) {
			SetDelayed setDelayed=new SetDelayed();
			setDelayed.exe(str);
			ls_list.add("success setDelayed!");
		}
		else if (new_cmd.equals("dlf")) {
			DLF dlf=new DLF();
			ls_list=dlf.exe(str);
		}
		else if (new_cmd.equals("ulf")) {
			ULF ulf=new ULF();
			ls_list=ulf.exe(str);
		}
		else  if (new_cmd.equals("con")) {
			ls_list.add("连接成功！");
		}
		
		
		else {
			dir.exeDir("错误");
		}
		System.out.println("当前的文件夹："+sb);	
		System.out.println("已经解析命令");
		all_num += ls_list.size();
		String num_str=String.valueOf(all_num+1);

		
		msgBackList.add(num_str);
		msgBackList.add("ok");
		
		msgBackList.add(str);
		for(int i=0;i<ls_list.size();i++) {
			msgBackList.add(ls_list.get(i));
		}
		ls_list.clear();
		System.out.println("解析文件夹总" + all_num + "个");
	}

	// 将解析后的数据传回
	public void readback(String msg_str, Socket socket) throws IOException {
		BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
		OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
	 
		for (int j = 0; j < msgBackList.size(); j++) {
			System.out.println(msgBackList.get(j).toString());
			writer.write(msgBackList.get(j).toString() + "\n");// 未真正写入的输出流，仅仅在内存中
		}
		msgBackList.clear();
		writer.flush();// 写入输出流，真正将数据传输出去
		writer.close();
	}

	public void work() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			while (true) {// 无限循环，使之能结束当前socket服务后，准备下一次socket服务
				System.out.println("Waiting client to connect.....");
				Socket socket = serverSocket.accept();// 阻塞式，直到有客户端连接进来，才会继续往下执行，否则一直停留在此代码
				now_socket = socket;
				InetAddress inetAddress = socket.getInetAddress();
				System.out.println("Client connected from: " + socket.getRemoteSocketAddress().toString());// 打印客户端的域名和IP地址
				connect_count++;

				// ArrayList<String> msgList = new ArrayList<>();
				String msg_str="";
				try {
					ArrayList<String> list=new ArrayList<>();
					msg_str = readSocketMsg(socket);// 读入客户端命令
					try {
						processCmd(msg_str, socket);// 解析命令，产生数据				
			
						try {
							readback(msg_str, socket);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							msgBackList.add(e.toString());
							System.out.println("e抛出错误:" + e.toString());
						}
					} catch (Exception e3) {
						// TODO: handle exception
						msgBackList.add(e3.toString());
						readback(msg_str, socket);
						System.out.println("e3抛出错误:" + e3.toString());
					}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					msgBackList.add(e1.toString());
					readback(msg_str, socket);
					System.out.println("e1抛出错误:" + e1.toString());
				}
					socket.close();
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			System.out.println("e2抛出错误:"+e2.toString());
			e2.printStackTrace();
		}

		// socket.close();
		System.out.println("当前Socket服务结束");
	}

}
