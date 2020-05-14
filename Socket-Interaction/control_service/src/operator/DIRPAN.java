package operator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DIRPAN {
		public  DIRPAN() {
			// TODO Auto-generated constructor stub
		}
		public ArrayList<String> exe(String cmdBody) throws IOException{
			ArrayList<String> msgBackList=new ArrayList<>();
			File file = new File(cmdBody);
			 File[] listFiles= File.listRoots();
			msgBackList.clear();
			for (File mfile : listFiles) {
				String fileName = mfile.getCanonicalPath();
				long lastModified = mfile.lastModified();// 获取文件修改时间
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 给时间格式，例如：2018-03-16 09:50:23
				String fileDate = dateFormat.format(new Date(lastModified));// 取得文件最后修改时间，并按格式转为字符串
				String fileSize = "0";
				String isDir = "2";
				msgBackList.add(fileName + ">" + fileDate + ">" + fileSize + ">" + isDir + ">");
			}
			return msgBackList;
		}
}
