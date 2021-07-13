package cat.server;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import cat.function.CatBean;
import cat.function.CatClientBean;
import cat.util.CatUtil;
import cat.forms.editCommentForm;
import database.databasesess;

public class ServerView extends JFrame
{
    private JPanel contentPane;
    public JTable table;
    private JButton buttonsendall;
    private JButton buttoncomment;
    private JButton buttonqiangzhixiaxian;
    private JPanel panel;
    private JPanel send_panel;
    public JTextArea textArea;
    private JPanel info_panel;
    public JList list;
    private JPanel online_panel;
    public MyCellRenderer myCellRenderer = null;
    public MyListmodel myListmodel = null;
    private JScrollPane scrollPane;
    private CatServer catServer = null;
    public static final char MSGENDCHAR = 0xff;

    private editCommentForm commentForm;
    private databasesess dbs;

    public void setCatServer(CatServer catServer)
    {
        this.catServer = catServer;
    }

    public ServerView()
    {
        init();
    }

    public void init()
    {
        Font font = new Font("宋体", 22, 16);
        setTitle("服务器端");
        setFont(font);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(310, 100, 789, 538);
        setResizable(false);
        contentPane = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
//				g.drawImage(new ImageIcon("images\\fuwu1.jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
            }

        };
        contentPane.setOpaque(true);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        myCellRenderer = new MyCellRenderer();

        online_panel = new JPanel();
        online_panel.setBounds(10, 10, 164, 376);
        contentPane.add(online_panel);
        online_panel.setLayout(new BorderLayout(0, 0));

        MyCellRenderer myCellRenderer = new MyCellRenderer();


        scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(new TitledBorder(null, "当前在线用户"
                + "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        online_panel.add(scrollPane, BorderLayout.CENTER);


        list = new JList();
        list.setCellRenderer(myCellRenderer);
        scrollPane.setViewportView(list);

        info_panel = new JPanel();
        info_panel.setBounds(10, 398, 763, 75);
        contentPane.add(info_panel);
        info_panel.setLayout(new BorderLayout(0, 0));

        textArea = new JTextArea();
        info_panel.add(textArea, BorderLayout.CENTER);

        send_panel = new JPanel();
        send_panel.setBounds(185, 10, 588, 376);
        send_panel.setBorder(BorderFactory.createTitledBorder("用户信息"));
        contentPane.add(send_panel);
        send_panel.setLayout(new BorderLayout(0, 0));

        table = new JTable();
        table.setEnabled(false);
        send_panel.add(table, BorderLayout.NORTH);

        panel = new JPanel();
        panel.setBounds(10, 475, 763, 18);
        contentPane.add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        buttonsendall = new JButton("发送给所有在线用户");
        buttonsendall.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                sendAll();
                textArea.setText("");
                textArea.requestFocus();
            }
        });
        panel.add("East", buttonsendall);

        dbs = new databasesess();
        commentForm = new editCommentForm(dbs);
        buttoncomment = new JButton("管理评论");
        buttoncomment.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                commentForm.show(true);
//                catServer.myListmodel.addElement("1111");
//                list.setModel(catServer.myListmodel);
            }
        });
        panel.add("West",buttoncomment);
        buttonqiangzhixiaxian = new JButton("强制下线");
        buttonqiangzhixiaxian.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                Object ob=list.getSelectedValue();
                if (ob==null)
                {
                    return;
                }
				String selectuserid=(String) ob;
				CatClientBean thisuser=CatServer.onlines.get(selectuserid);
				thisuser.getThreadname().interrupt();
//                for (int i = 0; i < CatServer.defaultTableModel.getRowCount(); i++)
//                {
//                    if (CatServer.defaultTableModel.getValueAt(i, 0).equals("bean.getUserid()"))
//                    {
//                        CatServer.defaultTableModel.removeRow(i);
//                    }
//                }
//                catServer.myListmodel.removeElement("bean.getUserid()");
//                list.setModel(catServer.myListmodel);
            }
        });
        panel.add("Center", buttonqiangzhixiaxian);
    }

    private void sendAll()
    {
        String text = textArea.getText();
        CatBean serverBean = new CatBean();
        serverBean.setInfo(text);
        serverBean.setType(1);
        serverBean.setTimer(CatUtil.getTimer());
        serverBean.setUserid("服务器");

        if (text.equals(""))
        {
            JOptionPane.showMessageDialog(ServerView.this, "不能发送空消息");
            return;
        }

        Collection<CatClientBean> clients = catServer.onlines.values();
        Iterator<CatClientBean> it = clients.iterator();
        //ObjectOutputStream oos;
        DataOutputStream oos;
        //while (it.hasNext()) {
        //Socket c = it.next().getSocket();
        Socket c = catServer.asock;
        try
        {
//				oos = new ObjectOutputStream(c.getOutputStream());
            oos = new DataOutputStream(c.getOutputStream());
//				oos.writeObject(serverBean);
            oos.write(text.getBytes("gbk"));
            //oos.writeBytes(text);
            //oos.writeUTF(text);
            //oos.writeChar(MSGENDCHAR);
            oos.flush();
        } catch (IOException e)
        {

            e.printStackTrace();
        }
        //}


//		try
//		{
//			Runtime runtime = Runtime.getRuntime();
//			String[] command = {"D:\\����\\������\\tcp\\send.exe","C:\\Users\\zang\\Music\\���ұ���.mp3","8888"};
//			Process process = runtime.exec(command);
//
//			int exitcode = process.waitFor();
//			System.out.println("returnfff "+exitcode);
//		} catch (Exception e)
//		{
//			System.out.println(e);
//		}
    }
}
	