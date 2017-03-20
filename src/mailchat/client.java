/*****************
 Mailchat Client
 By JIN Lei
 13120017 
 
 ***************/
package mailchat;

//
//chatwith different people 
//close a client and decline the number in the clientlist
//show numbers dynamically
//show ssomeone in
import java.awt.*;
import java.awt.JobAttributes.DialogType;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.swing.*;
import javax.xml.stream.events.Namespace;

import java.awt.event.*;

public class client extends JFrame {
	public static void main(String[] args) {
		//new client("a","b");
	}

	JPanel panel1 = new JPanel();// up
	JPanel panel2 = new JPanel();// down
	JPanel panel3 = new JPanel(); // mid
	JPanel panel4 = new JPanel(); // left
	JTextArea typeplace = new JTextArea(5, 56);

	JTextArea showplace = new JTextArea(20, 68);
	JTextArea allpeople = new JTextArea(3, 2);// amount of people
	JTextArea nameshow = new JTextArea(27, 20);// people's name
	JTextArea room = new JTextArea(5, 5);

	String[] xialalist_size = { "15", "25", "30" };
	JComboBox xialabox_size = new JComboBox(xialalist_size);
	String[] xialalist_font = { "宋体", "黑体", "楷书" };
	JComboBox xialabox_font = new JComboBox(xialalist_font);
	String[] xialalist_color = { "黑色", "蓝色", "红色" };
	JComboBox xialabox_color = new JComboBox(xialalist_color);
	String[] names = { "All People" };
	//JComboBox xialabox_name = new JComboBox(names);

	JScrollPane jsp1 = new JScrollPane(showplace);
	JScrollPane jsp2 = new JScrollPane(typeplace);
	JScrollPane jsp3 = new JScrollPane(nameshow);
	JScrollPane jsp4 = new JScrollPane(room);
	JButton sendbutton = new JButton("SEND");
	JButton cleanbutton = new JButton("CLEAN");

	Socket csocket;
	DataOutputStream fromType = null;
	DataOutputStream endchat = null;
	String mail = null;
	String name = null;
	String[] allnamesforchoose = null;
	Date time = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd' at \n'HH:mm:ss ");

	DataInputStream toShow = null;
	boolean isconnected = false;
	Thread getmessage = new Thread(new withServer());
	Thread refresh = new Thread() {// refresh
		public void run() {
			while (true) {
				try {
					sleep(1000);
					fromType.writeUTF("$@#$%QWER!@#");
					// System.out.print("threadrefresh");
					fromType.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	public client(String mail, String name) {
		this.setTitle("MailChat");
		this.setLocation(230, 100);
		this.setSize(1110, 700);
		this.setLayout(null);
		this.setVisible(true);
		this.setResizable(false);
		this.mail = mail;
		this.name = name;
		
	/*not used*/	
	xialabox_size.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				switch (xialabox_size.getSelectedIndex()) {
				case 0:showplace.setFont(new Font("宋体",0,15));					
					break;
				case 1:showplace.setFont(new Font("宋体",0,25));
					break;
				case 2:showplace.setFont(new Font("宋体",0,35));
				break;
				default:
					break;
				}
			}
		});
	/*not used*/
xialabox_color.addItemListener(new ItemListener() {
		
			@Override
		public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				switch (xialabox_color.getSelectedIndex()) {
				case 0:showplace.setForeground(Color.black);					
					break;
				case 1:showplace.setForeground(Color.blue);	
					break;
				case 2:showplace.setForeground(Color.red);	
				break;
				
				}
			}
		});
xialabox_font.addItemListener(new ItemListener() {
	
	@Override
public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		switch (xialabox_font.getSelectedIndex()) {
		case 0:showplace.setFont(new Font("宋体",0,20));				
			break;
		case 1:showplace.setFont(new Font("黑体",0,20));	
			break;
		case 2:showplace.setFont(new Font("楷体",0,20));	
		break;
		default:
			break;
		}
	}
});

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				String strend = new String("!#%&(@$^QwEr123");
				try {
					endchat = new DataOutputStream(csocket.getOutputStream());
					// System.out.println(strend);
					endchat.writeUTF(strend);
					endchat.flush();
				} catch (IOException e1) {
					System.exit(0);
				} finally {
					System.exit(0);
				}

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {

			}
		});

		initLayout();
		
		
		this.add(panel1);// showbox
		this.add(panel3); // lists
		this.add(panel2);// inputbox
		this.add(panel4);// left
		String path = "bg13.jpg";   
        ImageIcon background = new ImageIcon(path);  
        JLabel label = new JLabel(background); 
        label.setBounds(0, 0, this.getWidth(), this.getHeight());  
        this.add(label);
		connect();

		getmessage.start();
		refresh.start();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			fromType.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void showweather() {
		URL url;

		String link = "http://php.weather.sina.com.cn/xml.php?city=%CE%F7%B0%B2&password=DJOYnieT8234jlsK&day=0";
		try {

			url = new URL(link);
			weather parser = new weather(url);
			String[] nodes = { "city", "status1", "temperature1", "status2",
					"temperature2" };
			Map<String, String> map = parser.getValue(nodes);
			room.append("Werther Report:" + '\n' + map.get(nodes[0]) + '\n'
					+ " 今天白天：" + map.get(nodes[1]) + '\n' + " 最高温度："
					+ map.get(nodes[2]) + "℃" + '\n' + " 今天夜间："
					+ map.get(nodes[3]) + '\n' + " 最低温度：" + map.get(nodes[4])
					+ "℃ ");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private void initLayout() {
		// panel1.setBackground(Color.black);
		// panel2.setBackground(Color.blue);
		// panel3.setBackground(Color.red);
		// panel4.setBackground(Color.green);
		this.setBackground(getBackground());
		panel1.setBounds(400, 0, 700, 500);
		panel2.setBounds(400, 527, 700, 150);
		panel3.setBounds(400, 500, 700, 25);
		panel4.setBounds(0, 52, 400, 600);
		panel3.setLayout(null);
		panel2.setLayout(null);
		panel4.setLayout(null);
		jsp1.setBounds(0,0,700,500);

		//panel3.add(xialabox_size);// xiala
		//xialabox_size.setBounds(13, 0, 100, 25);
		panel3.add(xialabox_font);
		xialabox_font.setBounds(13, 0, 100, 25);
		panel3.add(xialabox_color);
		panel3.add(sendbutton);
		sendbutton.setBounds(500, 0, 88, 25);
		sendbutton.addActionListener(new sendbuttonListener());
		panel3.add(cleanbutton);
		cleanbutton.setBounds(600, 0, 88, 25);
		cleanbutton.addActionListener(new cleanbuttonListener());
		xialabox_color.setBounds(143, 0, 100, 25);
		panel4.add(jsp4);// roomso
		jsp4.setBounds(5, 8, 140, 620);
		room.setEditable(false);
		room.setFont(new Font("黑体",0,15));
		panel4.add(allpeople);// leftaddallpeople
		allpeople.setEditable(false);
		allpeople.setBounds(150, 10, 230, 30);
		allpeople.setFont(new Font("黑体",0,17));

		panel4.add(jsp3);// leftaddnameshow
		nameshow.setEditable(false);
		nameshow.setFont(new Font("黑体",0,17));
	
		jsp3.setBounds(150, 50, 230, 580);

		showplace.setEditable(false);
		showplace.setBounds(0, 0, 700, 500);
		showplace.setFont(new Font("宋体",0,20));;
		room.setText("Hello!" + '\n' + name + '\n' + '\n'
				+ "You log in this \n room in:" + '\n' +'\n'+ sdf.format(time) + '\n'
				+ '\n');
	try{showweather();}catch(Exception e){e.printStackTrace();}
	finally{
		System.out.println("The Internet Environment does no support the Weather Service");
	}

		panel1.add(jsp1); // text&show
		panel2.add(jsp2);
	
        panel1.setOpaque(false);
        panel2.setOpaque(false);
        panel3.setOpaque(false);
        panel4.setOpaque(false);
    	
		
		jsp2.setBounds(5, 0, 685, 130);
		
	}

	public void connect() {
		try {
			csocket = new Socket("127.0.0.1", 8888);
			fromType = new DataOutputStream(csocket.getOutputStream());
			System.out.println("ok connected");
			isconnected = true;
			fromType.writeUTF("!@#$%^&qwer" + " " + name + " " + mail);
			fromType.flush();
			toShow = new DataInputStream(csocket.getInputStream());
		} catch (UnknownHostException e) {
			System.out.println("Server not found!");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// send messages
	private class sendbuttonListener implements ActionListener {
		public void actionPerformed(ActionEvent e1) {
			String s = typeplace.getText().trim();
			// showplace.setText(s);
			typeplace.setText("");

			try {
				fromType.writeUTF(s);
				fromType.flush();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}

	}

	private class cleanbuttonListener implements ActionListener {
		public void actionPerformed(ActionEvent e1) {

			showplace.setText("");

		}

	}

	private String[] sepstring(String str) {

		String[] ss = str.split("\n");
		// System.out.printf("sepstring(%s): %s\n", str, Arrays.toString(ss));
		return ss;
	}

	public class withServer implements Runnable {
		private String substr = null;
		private String[] namelistpre = null;
		private String namelist = "";

		@Override
		public void run() {
			try {
				while (isconnected) {
					String str = toShow.readUTF();
					System.out.println(str);

					if (str.substring(0, 3).equals("sin")) {
						showplace.append(str.substring(3) + '\n');
					}
					if (str.substring(0, 3).equals("nam")) {
						namelistpre = sepstring(str.substring(3));
						allnamesforchoose = namelistpre;
						System.out.println(allnamesforchoose[0]);

						namelist = "These People Are Online" + '\n'+'\n'+'\n';
						for (int i = 0; i < namelistpre.length; i++)
							namelist = namelist.concat(namelistpre[i] + '\n');
						nameshow.setText(namelist);

					}

					if (str.substring(0, 3).equals("msg")) {
						showplace.append(str.substring(3) + '\n'+'\n');
					}
					if (str.substring(0, 3).equals("num")) {
						allpeople.setText("Num of people Online:  "
								+ str.substring(3));

					}
				}
			} catch (SocketException e) {
				System.out.println("chat over");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
