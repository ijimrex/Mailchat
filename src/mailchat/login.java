/*****************
 Mailchat Login
 By JIN Lei
 13120017 
 
 ***************/
package mailchat;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.omg.CORBA.PRIVATE_MEMBER;

public class login extends JFrame {
	JPanel panel1 = new JPanel();
	JLabel tx1 = new JLabel();// email
	JLabel tx2 = new JLabel();// name
	JTextField email = new JTextField();// email
	JTextField name = new JTextField();
	JButton logbutton = new JButton();

	public static void main(String[] args) {
		new login();
	}

	private String[] sepstring(String str) {
		String[] ss = str.split(" ");
		return ss;
	}

	private boolean checkemail(String s) {// chech weather the emaeladdress is
											// right or not
		boolean flag = false;
		String[] m = s.split("@");
		if (m.length < 2) {
			flag = true;
		}
		return flag;
	}

	boolean n = false;

	private void namecheck(String s2, String s1) throws IOException {
		n = false;
		file Fromclientlist = new file();
		Fromclientlist.creatTxtFile("test");
		String[] clist = sepstring(Fromclientlist.readDate());

		for (int j = 0; j < clist.length; j++) {
			System.out.println(clist[j]);
			if (clist[j].equals(s1)) {
				n = true;
				break;
			}
		}

		if (n) {
			JOptionPane.showMessageDialog(null, "The name has been used" + '\n'
					+ "please use another name", "mailchat",
					JOptionPane.ERROR_MESSAGE);
		} else {
			new client(s2, s1);
			setVisible(false);
		}

	}

	public login() {
		this.add(panel1);
		panel1.setLayout(null);
		this.setSize(500, 360);
		this.setLocation(500, 200);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("MailChat login");
		panel1.add(tx1);
		tx1.setText("E-Mail");
		tx1.setFont(new Font("TimesNewRoman", 0, 20));
		tx1.setBounds(70, 100, 100, 30);
		panel1.add(email);
		// panel1.setBackground(arg0);
		email.setBounds(170, 100, 200, 30);
		panel1.add(tx2);
		tx2.setText("Name");
		tx2.setFont(new Font("TimesNewRoman", 0, 20));
		tx2.setBounds(70, 150, 100, 30);
		panel1.add(name);
		name.setBounds(170, 150, 200, 30);
		logbutton.setFont(new Font("ºÚÌå", 0, 20));

		panel1.add(logbutton);
		logbutton.setBounds(170, 230, 200, 40);
		logbutton.setText("Log In");
		String path = "bg12.jpg";
		ImageIcon background = new ImageIcon(path);
		JLabel label = new JLabel(background);

		label.setBounds(0, 0, this.getWidth(), this.getHeight());
		panel1.add(label);
		logbutton.addActionListener(new logbuttonListener());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private class logbuttonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String s1 = name.getText().trim();
			String s2 = email.getText().trim();
			System.out.println(s1 + '\n' + s2);
			if (s1.equals(""))
				JOptionPane.showMessageDialog(null, "please inout the name",
						"mailchat", JOptionPane.ERROR_MESSAGE);
			else if (checkemail(s2)) {
				JOptionPane.showMessageDialog(null,
						"E-Mail address is not right", "mailchat",
						JOptionPane.ERROR_MESSAGE);

			} else {
				try {
					namecheck(s2, s1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}

	}

}
