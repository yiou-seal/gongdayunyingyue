package cat.forms;


import database.databasesess;
import database.entity.Comments;
import database.entity.UsersEntity;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class editUserForm extends MouseAdapter
{
    JFrame frame;
    JPanel paneltwobutton;
    JList list1 = null;
    JList list2 = null;
    JButton btnfengjin = new JButton("封禁");
    JButton btnjiefeng = new JButton("解封");
    DataModel mode1 = null;
    DataModel mode2 = null;
    String[] s = {"用户129：这个不好。(2021-02-21 08:21:28)", "用户137：这个好。(2021-02-21 09:23:48)", "用户97：这个一般。(2021-02-21 09:25:43)", "用户177：别吵了。(2021-02-22 09:13:18)"};
    ArrayList<UsersEntity> commentsArrayList ;
    databasesess dbsession;

    public editUserForm(databasesess dbsession)
    {
        this.dbsession=dbsession;
        commentsArrayList=dbsession.getalluseinfo();
        frame = new JFrame("JList");
        frame.setSize(600, 800);
        JPanel panel = new JPanel();
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridLayout(1, 2));

        mode1 = new DataModel(1);
        list1 = new JList(mode1);
        list1.setBorder(BorderFactory.createTitledBorder("用户账户"));
        list1.addMouseListener(this);//一遇到鼠标事件立即执行.

        mode2 = new DataModel(2);
        list2 = new JList(mode2);
        //list2.setBorder(BorderFactory.createTitledBorder("删除 "));
        list2.addMouseListener(this);//一遇到鼠标事件立即执行.

        contentPane.add(new JScrollPane(list1));
        JScrollPane scrollPane = new JScrollPane(list2);
        contentPane.add(scrollPane);

        paneltwobutton=new JPanel(new GridLayout(1,2));
        paneltwobutton.add(btnfengjin);
        paneltwobutton.add(btnjiefeng);
        scrollPane.setColumnHeaderView(paneltwobutton);
        mode1.refresh();
        mode2.removeAllElements();
        frame.pack();
        //frame.show();
        frame.addWindowListener(new WindowAdapter()
        {
//            public void windowClosing(WindowEvent e)
//            {
//                System.exit(0);
//            }
        });
        btnfengjin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DataModel listModel = (DataModel) list2.getModel();
                for (int i=0;i<listModel.size();i++)
                {
                    int res=dbsession.blockuser(listModel.get(i));
                    if (res!=0)
                    {
                        JOptionPane.showMessageDialog(null,"封禁"+listModel.get(i).getUserId()+"失败，该用户可能已不存在","失败",JOptionPane.ERROR_MESSAGE);
                    }
                }
                chushihua();
            }
        });
        btnjiefeng.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DataModel listModel = (DataModel) list2.getModel();
                for (int i=0;i<listModel.size();i++)
                {
                    int res=dbsession.unblockuser(listModel.get(i));
                    if (res!=0)
                    {
                        JOptionPane.showMessageDialog(null,"封禁"+listModel.get(i).getUserId()+"失败，该用户可能已不存在","失败",JOptionPane.ERROR_MESSAGE);
                    }
                }
                chushihua();
            }
        });
    }

    public static void main(String[] args)
    {
        editUserForm ecf=new editUserForm(new databasesess());
        ecf.show(true);
    }

    //处理鼠标键击事件.
    public void mouseClicked(MouseEvent e)
    {
        int index;

        if (e.getSource() == list1)
        {
            if (e.getClickCount() == 2)
            {
                index = list1.locationToIndex(e.getPoint());
                UsersEntity tmp = (UsersEntity) mode1.getElementAt(index);
                mode2.addElement(tmp);
                list2.setModel(mode2);
                mode1.removeElementAt(index);
                list1.setModel(mode1);
            }
        }
        if (e.getSource() == list2)
        {
            if (e.getClickCount() == 2)
            {
                index = list2.locationToIndex(e.getPoint());
                UsersEntity tmp =  (UsersEntity)mode2.getElementAt(index);
                mode1.addElement(tmp);
                list1.setModel(mode1);
                mode2.removeElementAt(index);
                list2.setModel(mode2);
            }
        }
        if (e.getSource() == btnfengjin)
        {


        }
    }

    public void show(boolean b)
    {
        chushihua();
        frame.setVisible(b);
    }

    public void chushihua()
    {
        commentsArrayList=dbsession.getalluseinfo();
        mode1.refresh();
        mode2.removeAllElements();
    }

    class DataModel extends DefaultListModel<UsersEntity>
    {
        DataModel(int flag)
        {
            if (flag == 1)
            {
                for (int i = 0; i < commentsArrayList.size(); i++) addElement(commentsArrayList.get(i));
            }
        }
        public void refresh()
        {

            removeAllElements();
            for (int i = 0; i < commentsArrayList.size(); i++) addElement(commentsArrayList.get(i));

        }
    }
}
