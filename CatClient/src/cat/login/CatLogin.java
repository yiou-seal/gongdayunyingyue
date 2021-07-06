package cat.login;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import cat.client.CatChatroom;
import cat.function.CatBean;
import cat.function.ClientBean;
import cat.util.CatUtil;

public class CatLogin extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	public static HashMap<String, ClientBean> onlines;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// 启动登陆界面
					CatLogin frame = new CatLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CatLogin() {
		setTitle("登入程序\n");		//登录界面标题设置
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(350, 250, 450, 300); 		//移动组件并调整其大小
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon(
								"images\\\u767B\u9646\u754C\u9762.jpg").getImage(), 0,
						0, getWidth(), getHeight(), null);
			}
		};		//包含背景图的设置
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));  //设置面板的边界，Border描述了面板四周的边界（属于面板内部），EmptyBorder是一个空白的边界
		setContentPane(contentPane);
		contentPane.setLayout(null);	//取消了默认的border布局方式

		textField = new JTextField();
		textField.setBounds(128, 153, 104, 21);			//文本框的位置设置
		textField.setOpaque(false);
		contentPane.add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setForeground(Color.BLACK);
		passwordField.setEchoChar('*');		//字符回显设置
		passwordField.setOpaque(false);
		passwordField.setBounds(128, 189, 104, 21);
		contentPane.add(passwordField);

		//下面为登录按钮和设置按钮的定义
		final JButton btnNewButton = new JButton();
		btnNewButton.setIcon(new ImageIcon("images\\\u767B\u9646.jpg"));
		btnNewButton.setBounds(246, 227, 50, 25);
		getRootPane().setDefaultButton(btnNewButton);
		contentPane.add(btnNewButton);

		final JButton btnNewButton_1 = new JButton();
		btnNewButton_1.setIcon(new ImageIcon("images\\\u6CE8\u518C.jpg"));
		btnNewButton_1.setBounds(317, 227, 50, 25);
		contentPane.add(btnNewButton_1);

		// 登录界面的左下角提示信息  lblNewLabel的内容设置见下面
		final JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBounds(60, 220, 151, 21);
		lblNewLabel.setForeground(Color.red);
		getContentPane().add(lblNewLabel);

		// 监听登陆按钮
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Properties userPro = new Properties();
				File file = new File("Users.properties"); //生成properties属性文件
				CatUtil.loadPro(userPro, file);	//可以从.properties属性文件对应的文件输入流中，加载属性列表到Properties类对象
				//以key=value的键值对的形式（userPro）进行储存账号和密码  CatUtil是一个自定义类   在此处获取加载userPro和file文件信息

				String u_name = textField.getText();		//文本框获取用户名

				if (file.length() != 0) {

					if (userPro.containsKey(u_name)) {			//检测数据库中是否包含用户名
						String u_pwd = new String(passwordField.getPassword());		//密码框获取密码
						if (u_pwd.equals(userPro.getProperty(u_name))) {			//检测用户名对应的密码是否相等，userPro.getProperty获取属性信息

							try {
								Socket client = new Socket("localhost", 8520);

								btnNewButton.setEnabled(false);
								CatChatroom frame = new CatChatroom(u_name, client);		//建立聊天室，包含用户名、client客户端信息（目的IP、端口号）
								frame.setVisible(true);// 显示聊天界面
								setVisible(false);// 隐藏掉登陆界面

							} catch (UnknownHostException e1) {
								errorTip("The connection with the server is interrupted, please login again");
							} catch (IOException e1) {
								errorTip("The connection with the server is interrupted, please login again");
							}

						} else {
							lblNewLabel.setText("您输入的密码有误！");
							textField.setText("");		//清空用户名文本框和密码框
							passwordField.setText("");
							textField.requestFocus();		//将键盘焦点转移到用户名文本框
						}
					} else {
						lblNewLabel.setText("您输入昵称不存在！");
						textField.setText("");
						passwordField.setText("");
						textField.requestFocus();
					}
				} else {		//代表昵称输入空的情况
					lblNewLabel.setText("您输入昵称不存在！");
					textField.setText("");
					passwordField.setText("");
					textField.requestFocus();
				}
			}
		});

		//注册按钮监听
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton_1.setEnabled(false);
				CatResign frame = new CatResign();
				frame.setVisible(true);// 显示注册界面
				setVisible(false);// 隐藏掉登陆界面
			}
		});
	}

	protected void errorTip(String str) {		//异常处理  实际上为弹窗设置 当服务器未开启时进行客户端登录便会触发警告弹窗
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(contentPane, str, "Error Message",
				JOptionPane.ERROR_MESSAGE);
		textField.setText("");
		passwordField.setText("");
		textField.requestFocus();
	}
}