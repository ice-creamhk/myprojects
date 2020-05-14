package hk_server_app;

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

import socket.CmdServerSocket;

public class ServerSocketApp {
	public ServerSocketApp() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		Socket socket=new Socket();	
			new CmdServerSocket().work();
		
	}
}
