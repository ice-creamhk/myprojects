package operator;

import java.io.IOException;
import java.util.ArrayList;

public class AllCmdOrd {
	
	int all_num = 0;
	DIR dir = new DIR();
	OPEN open=new OPEN();
	DIRPAN dirpan=new DIRPAN();
	DIRbefore diRbefore=new DIRbefore();
	String sb="";
	public ArrayList<String> exe(String new_cmd,String str) throws Exception {
		ArrayList<String> ls_list=new ArrayList<>();
		
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
		
		return ls_list;
	}
}
