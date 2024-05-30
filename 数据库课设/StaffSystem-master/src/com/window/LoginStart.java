package com.window;

import com.stytle.Fonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class LoginStart extends JFrame{

    static public int ver=0;
    FlowLayout flowLayout;
    //设定框架
    JLabel bgimg;
    JLabel password;
    JLabel account;
    JLabel title;
    JTextField accounttext;
    JPasswordField passwordtext;
    JButton ok;
    JButton register;

    final int WIDTH = 500;
    final int HEIGHT = 327;
    javax.swing.JPanel jpanel_1;//图片
    javax.swing.JPanel jpanel_2;//标题
    javax.swing.JPanel jpanel_3;//框框
    javax.swing.JPanel jpanel_4;//登录
    final static JFrame Login=new JFrame("登录界面");

    public LoginStart()
    {
        //Start.admin();
        //Start.admin();
        //设置窗口大小
        Login.setBounds(600, 200, WIDTH, HEIGHT);
        Login.setLocationRelativeTo(Login.getOwner());
        init();
        Login.setVisible(true);
        Login.setResizable(false);


        ok.addMouseListener(new MouseAdapter(){  //加入监听器
            public void mouseClicked(MouseEvent e){
                ver=verification(accounttext.getText(),passwordtext.getText());
                System.out.println(accounttext.getText());
                System.out.println(passwordtext.getText());

                //System.out.println(ver);
                if(ver==2){
                    System.out.println("登录成功");
                    Login.setVisible(false);
                    //Start.account();
                    Account.start(Integer.parseInt(accounttext.getText()));
                }
                else if(ver==1)
                {
                    System.out.println("登录成功");
                    Login.setVisible(false);
                    Start.admin();
                    //choice.choose();


                }
                else if(ver==3)
                {
                    System.out.println("登录失败");
                    JOptionPane.showMessageDialog(null, "          			账号已注销","错误",JOptionPane.ERROR_MESSAGE);
                    //Login.setVisible(false);
                    //Start.admin();
                }
                else {
                    JOptionPane.showMessageDialog(null, "          			用户账号或密码错误","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public static int  verification(String id,String password){
        try{
            // 建立查询条件
            String sql = "select id,password,authority,state from person;";
            System.out.println("queryAllProcess(). sql = " + sql);

            DbProcess.connect();
            ResultSet rs = DbProcess.executeQuery(sql);

            while(rs.next()){
//                System.out.println("输入密码为："+password);
//                String str1=MD5.encrypt(password);
//                System.out.println("加密后的密码为："+str1);
//                String str2=MD5.decrypt(str1);
//                System.out.println("解密后的密码为："+str2);
                if(id.equals(rs.getString("id"))&&MD5.decrypt(rs.getString("password")).equals(password))
                {
                    System.out.println("密码账号匹配成功");
                    if(rs.getString("Authority").equals("管理员")){
                        return 1;
                    }
                    else if(rs.getString("Authority").equals("用户")&&rs.getString("state").equals("T")){
                        return 2;
                    }
                    else {
                        return 3;
                    }

                }
            }
        }catch(SQLException sqle){
            System.out.println("sqle = " + sqle);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误","错误",JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }
    void init(){

        //初始化面板1（标题）
        jpanel_1 = new javax.swing.JPanel();
        jpanel_1.setBounds(0,0,WIDTH,HEIGHT);
        jpanel_1.setLayout(null);
        //初始化面板2（账号）
        jpanel_2 = new javax.swing.JPanel();
        jpanel_2.setBounds(0,50,WIDTH,50);
        flowLayout=new FlowLayout(FlowLayout.CENTER);//居中对齐
        jpanel_2.setLayout(flowLayout);
        jpanel_2.setOpaque(false);
        //初始化面板3（密码）
        jpanel_3 = new javax.swing.JPanel();
        jpanel_3.setBounds(80,100,310,200);
        flowLayout=new FlowLayout(FlowLayout.CENTER);//居中对齐
        jpanel_3.setLayout(flowLayout);
        jpanel_3.setOpaque(false);
        //初始化面板4（登录）
        jpanel_4 = new javax.swing.JPanel();
        jpanel_4.setBounds(100,170,300,70);
        flowLayout=new FlowLayout(FlowLayout.CENTER);//居中对齐
        jpanel_4.setLayout(flowLayout);
        jpanel_4.setOpaque(false);

        //初始化字体类
        Fonts fonts= new Fonts();


        //载入背景图片等信息
        ImageIcon img= new ImageIcon("src/img/wallhaven-md1yry.jpg");
        bgimg=new JLabel(img);
        bgimg.setBounds(0,0,img.getIconWidth(),img.getIconHeight());

        title=new JLabel("登录界面");
        title.setFont(Fonts.title);
        title.setForeground(new Color(255,228,181));

        account=new JLabel("账号");
        account.setFont(Fonts.account);
        account.setForeground(new Color(255,228,181));

        accounttext= new JTextField(15);
        accounttext.setFont(Fonts.account);
        accounttext.setForeground(Color.BLACK);

        password=new JLabel("密码");
        password.setFont(Fonts.account);
        password.setForeground(new Color(255,228,181));

        passwordtext= new JPasswordField(15);
        passwordtext.setFont(Fonts.account);
        passwordtext.setForeground(Color.BLACK);

        ok=new JButton("登录");
        ok.setPreferredSize(new Dimension(250,50));
        ok.setFont(Fonts.ok);
        ok.setForeground(Color.BLACK);
        ok.setBackground(new Color(255,255,255));

        register=new JButton(("注册账号"));
        register.setPreferredSize(new Dimension(120,50));
        register.setFont(Fonts.ok);
        register.setForeground(Color.BLACK);
        register.setBackground(new Color(8,189,252));



        jpanel_3.add(account);
        jpanel_3.add(accounttext);
        jpanel_3.add(password);
        jpanel_3.add(passwordtext);
        //jpanel_4.add(register);
        jpanel_4.add(ok);
        jpanel_2.add(title);
        jpanel_1.add(jpanel_2);
        jpanel_1.add(jpanel_3);
        jpanel_1.add(jpanel_4);


        Login.add(jpanel_2);
        Login.add(jpanel_1);

    }
}