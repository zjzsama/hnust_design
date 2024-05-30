package com.window;

import com.stytle.Fonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AddStaff {
    static  int count=0;
    static String Count;
    static JLabel Id=new JLabel("<html><font color='red'>员工号</font></html>",JLabel.CENTER);
    static JLabel Name=new JLabel("<html><font color='red'>姓       名* </font></html>",JLabel.CENTER);
    static JLabel Sex=new JLabel("<html><font color='red'>性       别*</font></html>",JLabel.CENTER);
    static JLabel Passwd=new JLabel("<html><font color='red'>密      码*</font></html>",JLabel.CENTER);
    static JLabel Authority=new JLabel("<html><font color='red'>用户权限*</font></html>",JLabel.CENTER);

    static JLabel Department=new JLabel("<html><font color='red'>所在部门*</font></html>",JLabel.CENTER);
    static JLabel Job=new JLabel("<html><font color='red'>职      务* </font></html>",JLabel.CENTER);
    static JLabel Education=new JLabel("<html><font color='red'>受教育程度*</font></html>",JLabel.CENTER);
    static JLabel Spcialty=new JLabel("<html><font color='red'>专业技能</font></html>",JLabel.CENTER);
    static JLabel Age=new JLabel("<html><font color='red'>年      龄*</font></html>",JLabel.CENTER);

    static JLabel Address=new JLabel("家庭住址",JLabel.CENTER);
    static JLabel Tel=new JLabel("<html><font color='red'>联系电话*</font></html>",JLabel.CENTER);
    static JLabel Email=new JLabel("<html><font color='red'>电子邮件</font></html>",JLabel.CENTER);
    static JLabel State=new JLabel("<html><font color='red'>当前状态*</font></html>",JLabel.CENTER);
    static JLabel Remark=new JLabel("<html><font color='red'>备        注</font></html>",JLabel.CENTER);
    static	JTextField id=new JTextField(10);
    static	JTextField passwd=new JTextField(10);
    static	JTextField authority=new JTextField(10);
    static	JTextField name=new JTextField(10);
    static	JTextField sex=new JTextField(10);
    static	JTextField age=new JTextField(10);
    static	JTextField department=new JTextField(10);
    static	JTextField job=new JTextField(10);
    static	JTextField education=new JTextField(10);
    static	JTextField specialty=new JTextField(10);
    static	JTextField address=new JTextField(10);
    static	JTextField tel=new JTextField(10);
    static	JTextField email=new JTextField(10);
    static JTextField state=new JTextField(10);
    static	JTextField remark=new JTextField(40);


    static BackgroundPanel centre;
    public static JComponent toAdd()
    {
        centre = new BackgroundPanel((new ImageIcon("src/img/5.png")).getImage());
        centre.setBorder(new EmptyBorder(5, 5, 5, 5));
        //centre.setLayout(null);
        centre.setSize(800, 800);

        id.setEditable(false);
        state.setEditable(false);
        Count=String.valueOf(toCount()+1);
        id.setText(Count);
        state.setText("T");
        count=0;

        //JPanel center=new  JPanel();
        //center.add(centre);
        //用一个网状布局表示各个部分
        centre.setLayout(new GridLayout(9,1,0,0));
        FlowLayout flow=new FlowLayout(FlowLayout.CENTER,150,40);

        //网状布局内部用一个固定布局的面板填充，每个面板又由两个小面板构成
        JPanel centre1=new  JPanel(flow);
        //第一行
        JPanel cen1=new  JPanel();
        //Id.setFont(Fonts.accounttext);
        cen1.add(Id);
        cen1.add(id);
        centre1.add(cen1);
        JPanel cen2=new  JPanel();
        cen2.add(Name);
        cen2.add(name);
        centre1.add(cen2);
        //第二行
        JPanel centre2=new  JPanel(flow);
        JPanel cen3=new  JPanel();
        cen3.add(Sex);
        cen3.add(sex);
        JPanel cen4=new  JPanel();
        cen4.add(Age);
        cen4.add(age);
        centre2.add(cen3);
        centre2.add(cen4);
        //第三行
        JPanel centre3=new  JPanel(flow);
        JPanel cen5=new  JPanel();
        cen5.add(Department);
        cen5.add(department);
        JPanel cen6=new  JPanel();
        cen6.add(Authority);
        cen6.add(authority);
        centre3.add(cen5);
        centre3.add(cen6);
        //第四行
        JPanel centre4=new  JPanel(flow);
        JPanel cen7=new  JPanel();
        cen7.add(Passwd);
        cen7.add(passwd);
        passwd.setText("123456");
        JPanel cen8=new  JPanel();
        cen8.add(Job);
        cen8.add(job);
        centre4.add(cen7);
        centre4.add(cen8);
        //第五行
        JPanel centre5=new  JPanel(flow);
        JPanel cen9=new  JPanel();
        cen9.add(Education);
        cen9.add(education);
        JPanel cen10=new  JPanel();
        cen10.add(Spcialty);
        cen10.add(specialty);
        centre5.add(cen9);
        centre5.add(cen10);
        //第六行
        JPanel centre6=new  JPanel(flow);
        JPanel cen11=new  JPanel();
        cen11.add(Address);
        cen11.add(address);
        JPanel cen12=new  JPanel();
        cen12.add(Tel);
        cen12.add(tel);
        centre6.add(cen11);
        centre6.add(cen12);
        //第七行
        JPanel centre7=new  JPanel(flow);
        JPanel cen13=new  JPanel();
        cen13.add(Email);
        cen13.add(email);
        JPanel cen14=new  JPanel();
        cen14.add(State);
        cen14.add(state);
        centre7.add(cen13);
        centre7.add(cen14);
        //第八行
        FlowLayout flow2=new FlowLayout(FlowLayout.CENTER,0,40);
        JPanel centre8=new  JPanel(flow2);
        centre8.add(Remark);
        centre8.add(remark);
        //第九行
        JPanel centre9=new JPanel(new FlowLayout(FlowLayout.CENTER,0,25));
        JButton botton=new JButton("注册");
        botton.addActionListener(e->{
            if(!name.getText().isEmpty()&&!sex.getText().isEmpty()&&!department.getText().isEmpty()&&!authority.getText().isEmpty()&&!job.getText().isEmpty()&&!education.getText().isEmpty()&&!tel.getText().isEmpty()&&!passwd.getText().isEmpty()&&!state.getText().isEmpty()&&!age.getText().isEmpty())
            {
                String sId = id.getText().trim();
                String sName = name.getText().trim();
                String sPasswd = MD5.encrypt(passwd.getText().trim());
                String sSex = sex.getText().trim();

                String sAge = age.getText().trim();
                String sSpecialty = specialty.getText().trim();
                String sAddress = address.getText().trim();
                String sAuthority = authority.getText().trim();
                String sDepartment = department.getText().trim();
                String sJob = job.getText().trim();
                String sEducation = education.getText().trim();
                String sTel = tel.getText().trim();
                String sEmail = email.getText().trim();
                String sState = state.getText().trim();
                String sRemark = remark.getText().trim();

                // 建立插入条件
                String sql = "insert into person values(";
                sql = sql + sId ;
                sql = sql +",'" +  sPasswd+"'";
                sql = sql + ",'"  + sName+"'";
                sql = sql +",'" + sSex+"'" ;
                sql = sql +","  + sAge ;
                sql = sql +",'"  + sAuthority+"'" ;
                sql = sql +",'"  + sDepartment+"'" ;
                sql = sql +",'"  + sJob +"'";
                sql =sql +",'"  + sEducation+"'" ;
                if(sAddress.isEmpty())sql= sql+",null";
                else sql =sql +",'" + sAddress+"'" ;
                if(sSpecialty.isEmpty())sql= sql+",null";
                else sql =sql +",'" + sSpecialty +"'";
                sql = sql +",'" + sTel +"'";
                if(sEmail.isEmpty())sql= sql+",null";
                else sql = sql +",'" + sEmail +"'";
                sql =sql + ",'" + sState+"'";
                if(sRemark.isEmpty())sql= sql+",null"+");";
                else sql = sql +",'" +  sRemark+"');";

                System.out.println("insertProcess(). sql = " + sql);
                try{
                    if (DbProcess.executeUpdate(sql) < 1) {
                        System.out.println("insertProcess(). insert database failed.");
                    }
                    else{
                        System.out.println("insertProcess(). insert successful.");
                        JOptionPane.showMessageDialog(null,
                                "新员工注册成功","成功",JOptionPane.INFORMATION_MESSAGE);
                        Chang.upDataProcess("insert into personnel value("+(Chang.toCount()+1)+",'"+id.getText().trim()+"',0,'部门"+department.getText().trim()+job.getText().trim()+"');");
                        Count=String.valueOf(toCount()+1);
                        id.setText(Count);
                        count=0;
                        name.setText("");
                        sex.setText("");
                        age.setText("");
                        department.setText("");
                        authority.setText("");
                        passwd.setText("123456");
                        job.setText("");
                        education.setText("");
                        specialty.setText("");
                        tel.setText("");
                        state.setText("");
                        email.setText("");
                        remark.setText("");
                        address.setText("");
                    }
                }catch(Exception even){
                    System.out.println("e = " + even);
                    JOptionPane.showMessageDialog(null,
                            "数据操作错误","错误",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                //System.out.println(name.getText()+sex.getText()+department.getText()+authority.getText()+job.getText()+education.getText().isEmpty()&&+tel.getText());
                System.out.println("注册失败:未填写所有带*的信息");
                JOptionPane.showMessageDialog(null,
                        "请填写所有带*的信息","错误",JOptionPane.ERROR_MESSAGE);
            }
        });
        botton.setOpaque(false);
        //botton.setSize(60,40);
        centre9.add(botton);

        //center.setOpaque(false);
        centre.setOpaque(false);
        centre1.setOpaque(false);
        centre2.setOpaque(false);
        centre3.setOpaque(false);
        centre4.setOpaque(false);
        centre5.setOpaque(false);
        centre6.setOpaque(false);
        centre7.setOpaque(false);
        centre8.setOpaque(false);
        centre9.setOpaque(false);
        cen1.setOpaque(false);
        cen2.setOpaque(false);
        cen3.setOpaque(false);
        cen4.setOpaque(false);
        cen5.setOpaque(false);
        cen6.setOpaque(false);
        cen7.setOpaque(false);
        cen8.setOpaque(false);
        cen9.setOpaque(false);
        cen10.setOpaque(false);
        cen11.setOpaque(false);
        cen12.setOpaque(false);
        cen13.setOpaque(false);
        cen14.setOpaque(false);
        id.setOpaque(false);
        passwd.setOpaque(false);
        authority.setOpaque(false);
        name.setOpaque(false);
        sex.setOpaque(false);
        age.setOpaque(false);
        department.setOpaque(false);
        job.setOpaque(false);
        education.setOpaque(false);
        specialty.setOpaque(false);
        address.setOpaque(false);
        tel.setOpaque(false);
        email.setOpaque(false);
        state.setOpaque(false);
        remark.setOpaque(false);


        //center.add(centre9);
        centre.add(centre1);
        centre.add(centre2);
        centre.add(centre3);
        centre.add(centre4);
        centre.add(centre5);
        centre.add(centre6);
        centre.add(centre7);
        centre.add(centre8);
        centre.add(centre9);

        return centre;
    }

    public static  int toCount(){
        try{
            // 建立查询条件
            String sql = "select * from person;";
            System.out.println("queryAllProcess(). sql = " + sql);

            DbProcess.connect();
            ResultSet rs = DbProcess.executeQuery(sql);

            // 将查询获得的记录数据，转换成适合生成JTable的数据形式

            while(rs.next()){
                count++;
            }
            System.out.println("已有员工： "+(count));

        }catch(SQLException sqle){
            System.out.println("sqle = " + sqle);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误","错误",JOptionPane.ERROR_MESSAGE);
        }
        return count;
    }
}
