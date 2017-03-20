/*****************
 Mailchat Server
 By JIN Lei
 13120017 
 
 ***************/
package mailchat;

import java.awt.geom.Area;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.SelectableChannel;
import java.net.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.*;

import javax.swing.JPopupMenu.Separator;

import org.omg.CORBA.PRIVATE_MEMBER;

public class server {
	boolean start = false;
	ServerSocket ssocket = null;
	Socket s = null;
	String dec=null;
	int count=0;
	int allpeople=0;
	Date time=new Date();
	public server() {
		// TODO Auto-generated constructor stub
	}
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy.MM.dd' at 'HH:mm:ss ");
	List <withClient> clientList=new ArrayList<withClient>();

	public static void main(String[] args) {
		new server().start();

	}

	public void start() {
		try {
			ssocket = new ServerSocket(8888);
			start = true;
		} catch (BindException e) {
			System.out.println("port used");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {

			while (start) {
				Socket s = ssocket.accept();
				withClient c = new withClient(s);
System.out.println("a client connected!"+s.getInetAddress() +"   "+s.getPort());
				new Thread(c).start();
				clientList.add(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ssocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	file sFile=new file();
	

	
	class withClient implements Runnable {
		private Socket s = null;
		private int port;
		private DataInputStream fromclient = null;
		private boolean isconnected = false;
		private DataOutputStream toAllclient=null;
		private String email=null;
		private String name=null;
		private String [] sepst=null;
		
		private void insertname(List<withClient> nlist) throws IOException  {
			String str="";
			for(int i=0;i<nlist.size();i++)
			{
				str=str.concat(nlist.get(i).name+" ");
			}
	System.out.println(str+"in the insertlist");
			sFile.creatTxtFile("test");
		    sFile.writeTxtFile(str);
			
		}
		

		public withClient(Socket s) {//reverive messages
			this.s = s;
			port=s.getPort();
			try {
				fromclient = new DataInputStream(s.getInputStream());
				isconnected = true;
				toAllclient=new DataOutputStream(s.getOutputStream());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void sendmessage(String str,String clname){//send message to all the clients
			try {
				
				toAllclient.writeUTF("msg"+sdf.format(time)+'\n'+clname+" say to all: "+'\n'+str);
				toAllclient.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void getout (String clname){//send message to all the clients
			try {
				
				toAllclient.writeUTF("msg"+sdf.format(time)+'\n'+clname+" left the chatroom "+'\n');
				toAllclient.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public int getIndex(int port){//get the index in the clientlist by comparing the port
			for(int i=0;i<clientList.size();i++)
				if(clientList.get(i).port==port)
				 {
					System.out.print("assa");
					return i;	
				 }
			
			System.out.println("suc");
			return 0;
		}
		
		public String getonename(int port){//get the index in the clientlist by comparing the port
			for(int i=0;i<clientList.size();i++)
				if(clientList.get(i).port==port)
				 {
					return clientList.get(i).name;	
				 }
			return null;
		}
		
		
		public void someonein(String n,String m){//send message to all the clients
			try {

				toAllclient.writeUTF("sin"+sdf.format(time)+'\n'+n+" joins the chat"+'\n');
				toAllclient.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void sendnumbers(int all){//send how many people in the room
			try {
				
				toAllclient.writeUTF("num"+all);
				toAllclient.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		private String getnames(List <withClient> a){//get all names
			String names="";
			for(int j=0;j<a.size();j++)
			{
				names=names.concat(clientList.get(j).name+'\n');
			}
			return names;
			
			
		}
		
			public void sendnames(withClient a) throws IOException{
			
			toAllclient.writeUTF("nam"+a.getnames(clientList));
			toAllclient.flush();
		}
			
		
			public void pritalk(String fromname,String toname,String str)
			{//private talk
				boolean check=false;
				int i=0;
				
				for( i=0;i<clientList.size();i++)
					if(clientList.get(i).name.equals(toname))
						{check=true;break;}
				else  check=false;
					
					if(check==true)
					 {
						try {
							withClient c= clientList.get(i);
							c.toAllclient.writeUTF("msg"+sdf.format(time)+'\n'+fromname+" say to you: "+'\n'+str);
							c.toAllclient.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
						for(int j=0;j<clientList.size();j++)
							if(clientList.get(j).name.equals(fromname))
								try{
									withClient d= clientList.get(j);
									d.toAllclient.writeUTF("msg"+sdf.format(time)+'\n'+"you say to"+ toname+": "+'\n'+str);
									d.toAllclient.flush();
								}catch (Exception e) {	
								}
	
					 }
					else{
						for(count=0;count<clientList.size();count++){
						withClient c= clientList.get(count);
						c.sendmessage(str,getonename(s.getPort()));}
						
					}
				
			}

		
		private String[] sepstring(String str){
			String [] ss=str.split(" ");
			return ss;
		}
		
		
		
		@Override
		public void run() {
			try {
				while (isconnected) {
					String str;
					try {
						
						str = fromclient.readUTF();//get the message from client
System.out.println(str);

						if (str.equals("$@#$%QWER!@#")) {
							sendnumbers(clientList.size());
							sendnames(this);
							insertname(clientList);
							continue;
						}
						
						
						if (str.equals("!#%&(@$^QwEr123")) {//log out
							
							for(count=0;count<clientList.size();count++){
								withClient c= clientList.get(count);
								c.getout(getonename(s.getPort()));;	
								
							}
							clientList.remove(getIndex(s.getPort()));
							sendnumbers(clientList.size());
							insertname(clientList);
							
							continue;
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
System.out.println("disconnected");
						break;
					}
					
					
					if(str.length()>10&&str.substring(0, 11).equals("!@#$%^&qwer"))
					{
						sepst=sepstring(str);//seperate the input string
						this.name=sepst[1];
						this.email=sepst[2];
						for(count=0;count<clientList.size();count++){
							withClient c= clientList.get(count);
							c.someonein(this.name,this.email);
						}
						
							continue;
					}
					
					
					if(str.length()>2&&str.substring(0, 1).equals("@")){
						String d=null;
						String q="";
						d=sepstring(str.substring(1))[0];
						for(int i=1;i<sepstring(str.substring(1)).length;i++)
						q=q.concat(sepstring(str.substring(1))[i]+" ");
//	System.out.println(d+"in sepr");
						pritalk(this.name,d,q);
						continue;
					}

					for(count=0;count<clientList.size();count++){
						withClient c= clientList.get(count);
						c.sendmessage(str,getonename(s.getPort()));
						
					}

				}
				
			} finally {
				try {
					fromclient.close();
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
