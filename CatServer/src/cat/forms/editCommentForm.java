package cat.forms;

import database.databasesess;
import database.entity.Comments;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class editCommentForm extends MouseAdapter
{
    JFrame frame;
    JList list1 = null;
    JList list2 = null;
    JButton btnNewButton = new JButton("确认删除");
    DataModel mode1 = null;
    DataModel mode2 = null;
    String[] s = {"用户129：这个不好。(2021-02-21 08:21:28)", "用户137：这个好。(2021-02-21 09:23:48)", "用户97：这个一般。(2021-02-21 09:25:43)", "用户177：别吵了。(2021-02-22 09:13:18)"};
    ArrayList<Comments> commentsArrayList ;
    databasesess dbsession;

    public editCommentForm(databasesess dbsession)
    {
        this.dbsession=dbsession;
        commentsArrayList=dbsession.getallcommend();
        frame = new JFrame("JList");
        frame.setSize(600, 500);
        JPanel panel = new JPanel();
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridLayout(1, 2));

        mode1 = new DataModel(1);
        list1 = new JList(mode1);
        list1.setBorder(BorderFactory.createTitledBorder("用户评论"));
        list1.addMouseListener(this);//一遇到鼠标事件立即执行.

        mode2 = new DataModel(2);
        list2 = new JList(mode2);
        //list2.setBorder(BorderFactory.createTitledBorder("删除 "));
        list2.addMouseListener(this);//一遇到鼠标事件立即执行.

        contentPane.add(new JScrollPane(list1));
        JScrollPane scrollPane = new JScrollPane(list2);
        contentPane.add(scrollPane);


        scrollPane.setColumnHeaderView(btnNewButton);
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
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DataModel listModel = (DataModel) list2.getModel();
                for (int i=0;i<listModel.size();i++)
                {
                    int res=dbsession.delcommendbyid(listModel.get(i).getCommentID()+"");
                    if (res!=0)
                    {
                        JOptionPane.showMessageDialog(null,"删除消息"+listModel.get(i).getComment()+"失败，该消息可能不存在","失败",JOptionPane.ERROR_MESSAGE);
                    }
                }
                chushihua();
            }
        });
    }

    public static void main(String[] args)
    {
        editCommentForm ecf=new editCommentForm(new databasesess());
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
                Comments tmp = (Comments) mode1.getElementAt(index);
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
                Comments tmp =  (Comments)mode2.getElementAt(index);
                mode1.addElement(tmp);
                list1.setModel(mode1);
                mode2.removeElementAt(index);
                list2.setModel(mode2);
            }
        }
        if (e.getSource() == btnNewButton)
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
        commentsArrayList=dbsession.getallcommend();
        mode1.refresh();
        mode2.removeAllElements();
    }

    class DataModel extends DefaultListModel<Comments>
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