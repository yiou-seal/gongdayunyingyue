package cat.function;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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


public class Info extends JFrame{

    private JPanel contentPane;
    public JTextField textField1;
    public JTextField textField2;
    public JTextField textField3;
    public JTextField textField4;
    private JPasswordField passwordField;
    public static HashMap<String, ClientBean> onlines;
    CatChatroom   owner;
    public String username;
    public int userId;
    public String sex;
    public String password;
    public String telenumber;
    public String email;
    public String accountstate;

    public void setUsername(String username){
        this.username=username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUserId(int userId){
        this.userId=userId;
    }

    public int getUserId(){
        return this.userId;
    }

    public void setSex(String sex){
        this.sex=sex;
    }

    public String getSex(){
        return this.sex;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public String getPassword(){
        return this.password;
    }

    public void setTelenumber(String telenumber){
        this.telenumber=telenumber;
    }

    public String getTelenumber(){
        return this.telenumber;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setAccountstate(String accountstate){
        this.accountstate=accountstate;
    }

    public String getAccountstate(){
        return this.accountstate;
    }

    public Info(CatChatroom   owner) {
        this.owner=owner;       //�������ҵĴ�����
        setTitle("�û���Ϣ\n");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(350, 250, 450, 320);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        final JLabel lblNewLabel = new JLabel();
        lblNewLabel.setBounds(90, 50, 151, 21);
        lblNewLabel.setForeground(Color.red);
        lblNewLabel.setText("�Ա�");
        getContentPane().add(lblNewLabel);

        final JLabel lblNewLabel1 = new JLabel();
        lblNewLabel1.setBounds(90, 80, 151, 21);
        lblNewLabel1.setForeground(Color.red);
        lblNewLabel1.setText("����");
        getContentPane().add(lblNewLabel1);

        final JLabel lblNewLabel2 = new JLabel();
        lblNewLabel2.setBounds(90, 110, 151, 21);
        lblNewLabel2.setForeground(Color.red);
        lblNewLabel2.setText("�绰");
        getContentPane().add(lblNewLabel2);


        textField1 = new JTextField();
        textField1.setBounds(128, 50, 200, 20);
        textField1.setOpaque(false);
        contentPane.add(textField1);
        textField1.setColumns(10);
        textField1.setEditable(false);

        textField2 = new JTextField();
        textField2.setBounds(128, 80, 200, 20);
        textField2.setOpaque(false);
        contentPane.add(textField2);
        textField2.setColumns(10);
        textField2.setEditable(false);

        textField3 = new JTextField();
        textField3.setBounds(128, 110, 200, 20);
        textField3.setOpaque(false);
        contentPane.add(textField3);
        textField3.setColumns(10);
        textField3.setEditable(false);

//        textField1.setText(getSex());			//ÿ����Ϣ��ѯǰ����
//        textField2.setText(getTelenumber());
//        textField3.setText(getEmail());


        final JButton btnNewButton = new JButton("�޸�");
        btnNewButton.setBounds(100, 180, 80, 40);
        getRootPane().setDefaultButton(btnNewButton);
        contentPane.add(btnNewButton);

        final JButton btnNewButton_1 = new JButton("����");
        btnNewButton_1.setBounds(250, 180, 80, 40);
        contentPane.add(btnNewButton_1);


        btnNewButton.addActionListener(new ActionListener() {       //�޸İ�ťʵ��
            public void actionPerformed(ActionEvent e) {
                btnNewButton.setEnabled(false);
                textField1.setEditable(true);
                textField2.setEditable(true);
                textField3.setEditable(true);
//                textField4.setEditable(true);
//                textField1.setText("hhh");
                textField1.requestFocus();
                btnNewButton_1.setEnabled(true);
            }
        });

    //���水ťʵ��
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                btnNewButton_1.setEnabled(false);
                try {
                    Socket client = new Socket("localhost", 8520);
                    btnNewButton_1.setEnabled(false);
                    CatBean clientBean = new CatBean();
                    clientBean.setType(11);		//�����������������Ϣ���ͣ��޸���Ϣ��
                    clientBean.setName(owner.name);	//����������
                    String time = CatUtil.getTimer();
                    clientBean.setTimer(time);
                    HashSet<String> set = new HashSet<String>();
                    set.add(owner.name);
                    clientBean.setClients(set);  //��Ϣ��Դ

                    sex=textField1.getText();       //���ı����ȡ
                    email=textField2.getText();
                    telenumber=textField3.getText();

                    String information=username+"$"+userId+"$"+sex+"$"+password+"$"+telenumber+"$"+email+"$"+accountstate;
                    clientBean.setInfo(information);		//���ݴ��

                    owner.sendMessage(clientBean);		//���͸�������

                    btnNewButton.setEnabled(true);          //�޸İ�ť�ָ�
                    textField1.setEditable(false);
                    textField2.setEditable(false);
                    textField3.setEditable(false);
                    setVisible(false);// ���ص���½����
                } catch (UnknownHostException e1) {
                    errorTip("error");
                } catch (IOException e1) {
                    errorTip("error");
                }
//                setVisible(false);// ���ص���½����
            }
        });


//        addWindowListener(new WindowAdapter() {		//�ر��޸���Ϣ����
//            @Override
//            public void windowClosing(WindowEvent e){
//                setVisible(false);// ���ؽ���
//            }
//        });
    }



    protected void errorTip(String str) {
        // TODO Auto-generated method stub
        JOptionPane.showMessageDialog(contentPane, str, "Error Message",
                JOptionPane.ERROR_MESSAGE);
        textField1.setText("");
//        passwordField.setText("");
        textField1.requestFocus();
    }
}

