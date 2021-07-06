package cat.function;


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
import java.util.HashSet;
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


public class addFriend extends JFrame{

    private JPanel contentPane;
    public JTextField textField1;
    public static String Friendname;
    public JTextField textField2;
    public JTextField textField3;
    public JTextField textField4;
    private JPasswordField passwordField;
    public static HashMap<String, ClientBean> onlines;
    CatChatroom   owner;


    public addFriend(CatChatroom   owner) {
        this.owner=owner;       //将聊天室的传进来
        setTitle("添加好友\n");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(350, 250, 450, 320);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        final JLabel lblNewLabel = new JLabel();
        lblNewLabel.setBounds(128, 30, 400, 21);
        lblNewLabel.setForeground(Color.red);
        lblNewLabel.setText("请输入您要添加的好友用户名：");
        getContentPane().add(lblNewLabel);


        textField1 = new JTextField();
        textField1.setBounds(128, 50, 200, 20);
        textField1.setOpaque(false);
        contentPane.add(textField1);
        textField1.setColumns(10);
        textField1.setEditable(true);


        final JButton btnNewButton = new JButton("确认添加");
        btnNewButton.setBounds(128, 180, 160, 40);
        getRootPane().setDefaultButton(btnNewButton);
        contentPane.add(btnNewButton);

        //添加按钮实现
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Socket client = new Socket("localhost", 8520);
                    CatBean clientBean = new CatBean();
                    clientBean.setType(12);		//设置向服务器发送消息类型
                    clientBean.setName(owner.name);	//发送者名字
                    String time = CatUtil.getTimer();
                    clientBean.setTimer(time);
                    HashSet<String> set = new HashSet<String>();
                    set.add(owner.name);
                    clientBean.setClients(set);  //消息来源

                    Friendname=textField1.getText();       //从文本框获取好友名字(即被添加的好友)

                    String information=owner.name+"$"+Friendname;
                    clientBean.setInfo(information);		//内容存放

                    owner.sendMessage(clientBean);		//发送给服务器

                    setVisible(false);// 隐藏掉登陆界面
                } catch (UnknownHostException e1) {
                    errorTip("error");
                } catch (IOException e1) {
                    errorTip("error");
                }
            }
        });
    }

    protected void errorTip(String str) {
        // TODO Auto-generated method stub
        JOptionPane.showMessageDialog(contentPane, str, "Error Message",
                JOptionPane.ERROR_MESSAGE);
        textField1.setText("");
        textField1.requestFocus();
    }
}

