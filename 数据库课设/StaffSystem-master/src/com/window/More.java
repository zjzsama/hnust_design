package com.window;

import com.stytle.Fonts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class More {

    static BackgroundPanel centre;
    public static JComponent to() {
        centre = new BackgroundPanel((new ImageIcon("src/img/6.png")).getImage());
        centre.setBorder(new EmptyBorder(5, 5, 5, 5));
        centre.setLayout(null);
        centre.setSize(800, 800);

        // 创建开关按钮
        JToggleButton toggleBtn = new JToggleButton();

        // 首先设置不绘制按钮边框
        toggleBtn.setBorderPainted(false);
        toggleBtn.setBackground(new Color(255,255,255));
        toggleBtn.setOpaque(false);
        toggleBtn.setAutoscrolls(false);
        toggleBtn.setVisible(true);
        toggleBtn.setFocusPainted(false);
        toggleBtn.setFocusable(false);
        toggleBtn.setForeground(new Color(255,255,255));
        Label label=new Label("Thank you!");
        label.setFont(Fonts.title);
        label.setBackground(new Color(255,255,255));
        label.setVisible(false);

        // 设置 选中(开) 和 未选中(关) 时显示的图片
        toggleBtn.setSelectedIcon(new ImageIcon("src/img/10.png"));
        toggleBtn.setIcon(new ImageIcon("src/img/9.png"));

        // 添加 toggleBtn 的状态被改变的监听
        toggleBtn.addActionListener(e -> {
            // 获取事件源（即开关按钮本身）
            JToggleButton toggleBtn1 = (JToggleButton) e.getSource();
            if(toggleBtn1.isSelected()) System.out.println("点赞");
            else System.out.println("取消点赞");
            if(toggleBtn1.isSelected())
            {
                label.setVisible(true);
            }else{
                label.setVisible(false);
            }
        });
        JButton bnt =new JButton("部门信息查询");
        bnt.setFocusPainted(false);
        bnt.addActionListener(e->{
            Depart.to();
        });


        JPanel panel1=new JPanel();
        JPanel panel2=new JPanel();
        JPanel panel3=new JPanel();
        panel2.setBounds(30,360,740,200);
        panel3.setBounds(30,440,740,200);

        panel2.add(label);
        panel1.setBounds(30,320,740,200);
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER,0,100));
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER,0,100));
        panel3.add(bnt);
        //panel1.add(new Label("cowiineiwn"));
        centre.add(panel3);
        centre.add(panel2);
        centre.add(panel1);
        //panel1.add(new Label("谢谢"));
        panel1.add(toggleBtn);

        panel1.setOpaque(false);
        panel2.setOpaque(false);
        panel3.setOpaque(false);
        return centre;
    }
}
