import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
public class mylist extends MouseAdapter{
  JList list1=null;
  JList list2=null;
  JButton btnNewButton = new JButton("确认删除");
  DefaultListModel mode1=null;
  DefaultListModel mode2=null;
  String[] s = {"用户129：这个不好。(2021-02-21 08:21:28)","用户137：这个好。(2021-02-21 09:23:48)","用户97：这个一般。(2021-02-21 09:25:43)","用户177：别吵了。(2021-02-22 09:13:18)"};
 
  public mylist(){
  JFrame f=new JFrame("JList");
  f.setSize(600, 500);
  JPanel panel = new JPanel();
  Container contentPane=f.getContentPane();
  contentPane.setLayout(new GridLayout(1,2));
 
  mode1=new DataModel(1);
  list1=new JList(mode1);
  list1.setBorder(BorderFactory.createTitledBorder("用户评论"));
  list1.addMouseListener(this);//一遇到鼠标事件立即执行.
 
  mode2=new DataModel(2);
  list2=new JList(mode2);
  //list2.setBorder(BorderFactory.createTitledBorder("删除 "));
  list2.addMouseListener(this);//一遇到鼠标事件立即执行.
 
  contentPane.add(new JScrollPane(list1));
  JScrollPane scrollPane = new JScrollPane(list2);
  contentPane.add(scrollPane);
  

  scrollPane.setColumnHeaderView(btnNewButton);
   f.pack();
   f.show();
   f.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
         System.exit(0);
      }
   });   
  }
  public static void main(String[] args){
  new mylist();
  }
//处理鼠标键击事件.
  public void mouseClicked(MouseEvent e){
  int index;

  if (e.getSource()==list1){
     if (e.getClickCount()==2){
        index=list1.locationToIndex(e.getPoint());
        String tmp=(String)mode1.getElementAt(index);
        mode2 .addElement(tmp);
        list2.setModel(mode2);
        mode1.removeElementAt(index);
        list1.setModel(mode1);
     }
  }
  if (e.getSource()==list2){
     if (e.getClickCount()==2){
        index=list2.locationToIndex(e.getPoint());
        String tmp=(String)mode2.getElementAt(index);
        mode1.addElement(tmp);
        list1.setModel(mode1);
        mode2.removeElementAt(index);
        list2.setModel(mode2);
     }
  }
  if (e.getSource()==btnNewButton){
	  DefaultListModel listModel = (DefaultListModel) list2.getModel(); 
	   listModel.removeAllElements(); 
  }
  }
  class DataModel extends DefaultListModel{
    DataModel(int flag){
      if (flag==1){
      for (int i=0;i<s.length;i++)  addElement(s[i]);      
      }
    }
  }
}