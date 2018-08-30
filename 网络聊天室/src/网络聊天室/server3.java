package ����������;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;

public class server3 extends Thread {
	static List<Socket> list = new ArrayList<>();

	public static void main(String[] args) {
		ResultSet result;
		JFrame jf = new JFrame();
		JPanel jp = new JPanel();
		JTextArea jta = new JTextArea();
		JScrollPane js1 = new JScrollPane(jta);
		JLabel jla = new JLabel("��ǰ��  ������");
		jla.setBounds(0, 550, 600, 25);
		JButton jb = new JButton("����");
		jb.setBounds(0, 575, 600, 25);
		jta.setBackground(Color.GRAY);
		jta.setForeground(Color.RED);
		js1.setBounds(0, 0, 600, 550);
		jta.append("���������40000�˿ڼ���\n");
		jf.add(jp);
		jp.add(js1);
		jp.add(jla);
		jp.add(jb);
		jp.setLayout(null);
		jp.setBounds(0, 0, 600, 600);
		jf.setSize(600, 640);
		jf.setLocation(600, 200);
		jf.setVisible(true);
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				System.exit(0);
			}
		});
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(40000);
			while (true) {
				Socket s = ss.accept();
				list.add(s);
				jdbcbean.getconnection();
				result = jdbcbean.cha("select name from test.pass where IP='" + s.getInetAddress() + "'");
				jdbcbean.updata("DELETE FROM test.pass");
				result.last();
				int n = result.getRow();
				result.beforeFirst();
				ArrayList<String> user = new ArrayList<String>();
				while (result.next()) {
					user.add(result.getString("name"));
				}
				String user1[] = new String[user.size()];
				for (int i = 0; i < user.size(); i++) {
					user1[i] = user.get(i);
				}
				new server3(s).start();
				jla.setText("��ǰ��" + list.size() + "������");
				jta.append("��һ���ͻ�������.....����IP��ַ��" + s.getInetAddress() + ".....�ǳ�Ϊ:" + user1[0] + "\n");
			}
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (SQLException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}

	}

	Socket s;

	public server3(Socket s) {
		this.s = s;
	}

	public void run() {
		InputStream is = null;
		OutputStream os = null;
		String all;
		try {
			while (true) {
				is = s.getInputStream();
				os = s.getOutputStream();
				byte b[] = new byte[256];
				int len = is.read(b);
				all = new String(b, 0, len) + "\n";
				sendToAll(all);
			}
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}

	public void sendToAll(String all) throws IOException {
		Object p = new Object();
		synchronized (p) {
			for (Socket s : list) {
				Socket s1 = null;
				if (s != s1) {
					PrintStream ps = new PrintStream(s.getOutputStream());
					ps.print(all);
				}
			}
		}

	}
}
