package com.window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Passwd {
    static JTextField id;
    static JTextField passwd;
    static Vector titleVector = new Vector();
    static Vector departVector = new Vector();
    static JTable departTable;
    static JScrollPane departJScrollPane;

    public static JComponent change() {
        BackgroundPanel centre = new BackgroundPanel((new ImageIcon("src/img/2.png")).getImage());
        centre.setBorder(new EmptyBorder(5, 5, 5, 5));
        centre.setLayout(null);
        centre.setSize(800, 800);

        JPanel panel_1 = new JPanel();
        JPanel panel_2 = new JPanel();
        JPanel panel_3 = new JPanel();
        JPanel panel_4 = new JPanel();

        panel_3.setLayout(null);
        panel_3.add(new Label("修改密码"));
        centre.add(panel_3);
        panel_3.add(panel_1);
        panel_3.add(panel_2);


        panel_3.setBounds(0, 0, 800, 800);
        panel_1.setBounds(410, 175, 200, 50);
        panel_2.setBounds(410, 232, 200, 50);
        panel_4.setBounds(390, 290, 200, 50);
        JLabel label1 = new JLabel("工号");
        label1.setForeground(Color.RED); // 设置label1的文本颜色为红色

        label1.setBounds(350, 175, 50, 50); // 设置label1的位置和大小
        JLabel label2 = new JLabel("新密码");
        label2.setForeground(Color.RED); // 设置label1的文本颜色为红色

        label2.setBounds(350, 232, 50, 50); // 设置label2的位置和大小
        panel_3.add(label1);
        panel_3.add(label2);
        JButton button = new JButton("修改");
        button.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "确认修改用户的密码？",
                    "提示",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            System.out.println("选择结果: " + result);
            if (result == 0) upDadaProcess();
        });
        panel_4.add(button);
        panel_3.add(panel_4);


        id = new JTextField(15);
        passwd = new JTextField(15);

        panel_1.add(id);
        panel_2.add(passwd);

        panel_1.setOpaque(false);
        panel_2.setOpaque(false);
        panel_3.setOpaque(false);
        panel_4.setOpaque(false);


        return centre;
    }

    private static void upDadaProcess() {
        try {
            String sId = id.getText().trim();
            String sPasswd = MD5.encrypt(passwd.getText().trim());
            String sql = "update person set password='" + sPasswd + "' where id=" + sId + ";";
            if (DbProcess.executeUpdate(sql) < 1) {
                System.out.println("updateProcess(). update database failed.");
            } else {
                JOptionPane.showMessageDialog(null,
                        "密码修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("updateProcess(). update database successful.");
                id.setText("");
                passwd.setText("");
            }
        } catch (Exception even) {
            System.out.println("e = " + even);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
