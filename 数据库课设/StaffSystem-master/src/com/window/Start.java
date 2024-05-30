package com.window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Start {


    public static void admin() {
        final JFrame start = new JFrame("人事管理系统");
        start.setBounds(600, 200, 800, 800);
        start.setLocationRelativeTo(start.getOwner());
        start.setVisible(true);
        start.setLayout(null);
        start.setResizable(false);

        final JTabbedPane tabbedPane = new JTabbedPane();

        //设计首页板块
        JPanel panel5 = new JPanel(new GridLayout(1, 1));

        BackgroundPanel background = new BackgroundPanel((new ImageIcon("src/img/2.png")).getImage());
        background.setBorder(new EmptyBorder(5, 5, 5, 5));
        background.setLayout(null);
        background.setSize(800, 800);
        panel5.add(background);
        tabbedPane.addTab("首页", panel5);

        //设计新员工注册板块
        JPanel panel1 = new JPanel(new GridLayout(1, 1));
        panel1.add(AddStaff.toAdd());
        //panel1.add(AddStaff.centre9);
        tabbedPane.addTab("注册新员工", panel1);

        //设计人事变动板块
        JPanel panel2 = new JPanel(new GridLayout(1, 1));
        panel2.add(Chang.toFind());
        tabbedPane.addTab("人事变动查询", panel2);

        //设计员工信息查询板块
        JPanel panel3 = new JPanel(new GridLayout(1, 1));
        FindStaff find = new FindStaff();
        panel3.add(find.toFind());
        tabbedPane.addTab("员工信息查询", panel3);

        //设计部门

        //设计修改密码板块
        JPanel panel4 = new JPanel(new GridLayout(1, 1));
        panel4.add(Passwd.change());
        tabbedPane.addTab("修改密码", panel4);


        // 设置默认选中的选项卡
        tabbedPane.setSelectedIndex(0);

        start.setContentPane(tabbedPane);
        start.setVisible(true);
    }


}
