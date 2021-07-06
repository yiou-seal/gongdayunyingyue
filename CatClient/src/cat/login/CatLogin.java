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
					// ������½����
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
		setTitle("�������\n");		//��¼�����������
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(350, 250, 450, 300); 		//�ƶ�������������С
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon(
								"images\\\u767B\u9646\u754C\u9762.jpg").getImage(), 0,
						0, getWidth(), getHeight(), null);
			}
		};		//��������ͼ������
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));  //�������ı߽磬Border������������ܵı߽磨��������ڲ�����EmptyBorder��һ���հ׵ı߽�
		setContentPane(contentPane);
		contentPane.setLayout(null);	//ȡ����Ĭ�ϵ�border���ַ�ʽ

		textField = new JTextField();
		textField.setBounds(128, 153, 104, 21);			//�ı����λ������
		textField.setOpaque(false);
		contentPane.add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setForeground(Color.BLACK);
		passwordField.setEchoChar('*');		//�ַ���������
		passwordField.setOpaque(false);
		passwordField.setBounds(128, 189, 104, 21);
		contentPane.add(passwordField);

		//����Ϊ��¼��ť�����ð�ť�Ķ���
		final JButton btnNewButton = new JButton();
		btnNewButton.setIcon(new ImageIcon("images\\\u767B\u9646.jpg"));
		btnNewButton.setBounds(246, 227, 50, 25);
		getRootPane().setDefaultButton(btnNewButton);
		contentPane.add(btnNewButton);

		final JButton btnNewButton_1 = new JButton();
		btnNewButton_1.setIcon(new ImageIcon("images\\\u6CE8\u518C.jpg"));
		btnNewButton_1.setBounds(317, 227, 50, 25);
		contentPane.add(btnNewButton_1);

		// ��¼��������½���ʾ��Ϣ  lblNewLabel���������ü�����
		final JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBounds(60, 220, 151, 21);
		lblNewLabel.setForeground(Color.red);
		getContentPane().add(lblNewLabel);

		// ������½��ť
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Properties userPro = new Properties();
				File file = new File("Users.properties"); //����properties�����ļ�
				CatUtil.loadPro(userPro, file);	//���Դ�.properties�����ļ���Ӧ���ļ��������У����������б�Properties�����
				//��key=value�ļ�ֵ�Ե���ʽ��userPro�����д����˺ź�����  CatUtil��һ���Զ�����   �ڴ˴���ȡ����userPro��file�ļ���Ϣ

				String u_name = textField.getText();		//�ı����ȡ�û���

				if (file.length() != 0) {

					if (userPro.containsKey(u_name)) {			//������ݿ����Ƿ�����û���
						String u_pwd = new String(passwordField.getPassword());		//������ȡ����
						if (u_pwd.equals(userPro.getProperty(u_name))) {			//����û�����Ӧ�������Ƿ���ȣ�userPro.getProperty��ȡ������Ϣ

							try {
								Socket client = new Socket("localhost", 8520);

								btnNewButton.setEnabled(false);
								CatChatroom frame = new CatChatroom(u_name, client);		//���������ң������û�����client�ͻ�����Ϣ��Ŀ��IP���˿ںţ�
								frame.setVisible(true);// ��ʾ�������
								setVisible(false);// ���ص���½����

							} catch (UnknownHostException e1) {
								errorTip("The connection with the server is interrupted, please login again");
							} catch (IOException e1) {
								errorTip("The connection with the server is interrupted, please login again");
							}

						} else {
							lblNewLabel.setText("���������������");
							textField.setText("");		//����û����ı���������
							passwordField.setText("");
							textField.requestFocus();		//�����̽���ת�Ƶ��û����ı���
						}
					} else {
						lblNewLabel.setText("�������ǳƲ����ڣ�");
						textField.setText("");
						passwordField.setText("");
						textField.requestFocus();
					}
				} else {		//�����ǳ�����յ����
					lblNewLabel.setText("�������ǳƲ����ڣ�");
					textField.setText("");
					passwordField.setText("");
					textField.requestFocus();
				}
			}
		});

		//ע�ᰴť����
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton_1.setEnabled(false);
				CatResign frame = new CatResign();
				frame.setVisible(true);// ��ʾע�����
				setVisible(false);// ���ص���½����
			}
		});
	}

	protected void errorTip(String str) {		//�쳣����  ʵ����Ϊ�������� ��������δ����ʱ���пͻ��˵�¼��ᴥ�����浯��
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(contentPane, str, "Error Message",
				JOptionPane.ERROR_MESSAGE);
		textField.setText("");
		passwordField.setText("");
		textField.requestFocus();
	}
}