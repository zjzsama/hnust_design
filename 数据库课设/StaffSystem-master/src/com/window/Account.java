package com.window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Account {
    static JLabel Id = new JLabel("员工号", JLabel.CENTER);
    static JLabel Name = new JLabel("姓       名 ", JLabel.CENTER);
    static JLabel Sex = new JLabel("性       别", JLabel.CENTER);
    static JLabel Passwd = new JLabel("部门经理", JLabel.CENTER);
    static JLabel Authority = new JLabel("用户权限", JLabel.CENTER);

    static JLabel Department = new JLabel("所在部门", JLabel.CENTER);
    static JLabel Job = new JLabel("职      务", JLabel.CENTER);
    static JLabel Education = new JLabel("受教育程度", JLabel.CENTER);
    static JLabel Specialty = new JLabel("专业技能", JLabel.CENTER);
    static JLabel Age = new JLabel("年    龄", JLabel.CENTER);

    static JLabel Address = new JLabel("家庭住址", JLabel.CENTER);
    static JLabel Tel = new JLabel("联系电话", JLabel.CENTER);
    static JLabel Email = new JLabel("电子邮件", JLabel.CENTER);
    static JLabel State = new JLabel("当前状态", JLabel.CENTER);
    static JLabel Remark = new JLabel("备     注", JLabel.CENTER);

    static JTextField id = new JTextField(10);
    static JTextField passwd = new JTextField(10);
    static JTextField authority = new JTextField(10);
    static JTextField name = new JTextField(10);
    static JTextField sex = new JTextField(10);
    static JTextField age = new JTextField(10);
    static JTextField department = new JTextField(10);
    static JTextField job = new JTextField(10);
    static JTextField education = new JTextField(10);
    static JTextField specialty = new JTextField(10);
    static JTextField address = new JTextField(10);
    static JTextField tel = new JTextField(10);
    static JTextField email = new JTextField(10);
    static JTextField state = new JTextField(10);
    static JTextField remark = new JTextField(40);
    static JFrame start;


    static BackgroundPanel centre;

    public static void start(int num) {
        start = new JFrame("员工信息");
        start.setBounds(600, 200, 800, 800);
        start.setLocationRelativeTo(start.getOwner());
        start.setVisible(true);
        start.setLayout(null);
        start.setResizable(false);

        centre = new BackgroundPanel((new ImageIcon("src/img/5.png")).getImage());
        centre.setBorder(new EmptyBorder(5, 5, 5, 5));
        //centre.setLayout(null);
        centre.setSize(800, 800);

        id.setEditable(false);
        department.setEditable(false);
        authority.setEditable(false);
        job.setEditable(false);
        passwd.setEditable(false);
        state.setEditable(false);


        queryProcess(num);

        //JPanel center=new  JPanel();
        //center.add(centre);
        //用一个网状布局表示各个部分
        centre.setLayout(new GridLayout(9, 1, 0, 0));
        FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 150, 40);

        //网状布局内部用一个固定布局的面板填充，每个面板又由两个小面板构成
        JPanel centre1 = new JPanel(flow);
        //第一行
        JPanel cen1 = new JPanel();
        //Id.setFont(Fonts.accounttext);
        cen1.add(Id);
        cen1.add(id);
        centre1.add(cen1);
        JPanel cen2 = new JPanel();
        cen2.add(Name);
        cen2.add(name);
        centre1.add(cen2);
        //第二行
        JPanel centre2 = new JPanel(flow);
        JPanel cen3 = new JPanel();
        cen3.add(Sex);
        cen3.add(sex);
        JPanel cen4 = new JPanel();
        cen4.add(Age);
        cen4.add(age);
        centre2.add(cen3);
        centre2.add(cen4);
        //第三行
        JPanel centre3 = new JPanel(flow);
        JPanel cen5 = new JPanel();
        cen5.add(Department);
        cen5.add(department);
        JPanel cen6 = new JPanel();
        cen6.add(Authority);
        cen6.add(authority);
        centre3.add(cen5);
        centre3.add(cen6);
        //第四行
        JPanel centre4 = new JPanel(flow);
        JPanel cen7 = new JPanel();
        cen7.add(Passwd);
        cen7.add(passwd);
        //passwd.setText("您不能查看密码");
        JPanel cen8 = new JPanel();
        cen8.add(Job);
        cen8.add(job);
        centre4.add(cen7);
        centre4.add(cen8);
        //第五行
        JPanel centre5 = new JPanel(flow);
        JPanel cen9 = new JPanel();
        cen9.add(Education);
        cen9.add(education);
        JPanel cen10 = new JPanel();
        cen10.add(Specialty);
        cen10.add(specialty);
        centre5.add(cen9);
        centre5.add(cen10);
        //第六行
        JPanel centre6 = new JPanel(flow);
        JPanel cen11 = new JPanel();
        cen11.add(Address);
        cen11.add(address);
        JPanel cen12 = new JPanel();
        cen12.add(Tel);
        cen12.add(tel);
        centre6.add(cen11);
        centre6.add(cen12);
        //第七行
        JPanel centre7 = new JPanel(flow);
        JPanel cen13 = new JPanel();
        cen13.add(Email);
        cen13.add(email);
        JPanel cen14 = new JPanel();
        cen14.add(State);
        cen14.add(state);
        centre7.add(cen13);
        centre7.add(cen14);
        //第八行
        FlowLayout flow2 = new FlowLayout(FlowLayout.CENTER, 0, 40);
        JPanel centre8 = new JPanel(flow2);
        centre8.add(Remark);
        centre8.add(remark);
        //第九行
        JPanel centre9 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 25));
        JButton botton = new JButton("修改");

        botton.addActionListener(e -> {
            updateProcess();
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

        start.add(centre);
    }

    public static void queryProcess(int sQueryField) {
        try {
            // 建立查询条件
            String sql = "select * from person,depart where person.id=" + sQueryField + " and person.department=depart.id;";
            System.out.println("queryProcess(). sql = " + sql);

            DbProcess.connect();
            ResultSet rs = DbProcess.executeQuery(sql);

            while (rs.next()) {
                id.setText(rs.getString("person.id"));
                passwd.setText(rs.getString("depart.manager"));
                authority.setText(rs.getString("authority"));
                name.setText(rs.getString("person.name"));
                sex.setText(rs.getString("sex"));
                age.setText(rs.getString("age"));
                department.setText(rs.getString("depart.name"));
                job.setText(rs.getString("job"));
                education.setText(rs.getString("education"));
                specialty.setText(rs.getString("specialty"));
                address.setText(rs.getString("address"));
                tel.setText(rs.getString("tel"));
                email.setText(rs.getString("email"));
                state.setText(rs.getString("state"));
                remark.setText(rs.getString("remark"));
            }
            DbProcess.disconnect();
        } catch (Exception e) {
            System.out.println("e = " + e);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误", "错误", JOptionPane.ERROR_MESSAGE);
            start.setVisible(false);
        }
    }

    public static void updateProcess() {

        if (!name.getText().isEmpty() && !sex.getText().isEmpty() && !department.getText().isEmpty() && !authority.getText().isEmpty() && !job.getText().isEmpty() && !education.getText().isEmpty() && !tel.getText().isEmpty() && !passwd.getText().isEmpty() && !state.getText().isEmpty() && !age.getText().isEmpty()) {
            String sId = id.getText().trim();
            String sName = name.getText().trim();
            //String sPasswd = passwd.getText().trim();
            String sSex = sex.getText().trim();

            String sAge = age.getText().trim();
            String sSpecialty = specialty.getText().trim();
            String sAddress = address.getText().trim();

            String sTel = tel.getText().trim();
            String sEmail = email.getText().trim();
            //String sState = state.getText().trim();
            String sRemark = remark.getText().trim();

            // 建立更新条件
            String sql = "update person set name = '";
            sql = sql + sName + "', sex = '";
            sql = sql + sSex + "', age = ";
            sql = sql + sAge + ", specialty = '";
            sql = sql + sSpecialty + "', address = '";
            sql = sql + sAddress + "',tel='";
            sql = sql + sTel + "',email='";
            sql = sql + sEmail + "',remark='";
            sql = sql + sRemark + "'";
            sql = sql + " WHERE id = " + sId + ";";
            System.out.println("updateProcess(). sql = " + sql);
            try {
                if (DbProcess.executeUpdate(sql) < 1) {
                    System.out.println("updateProcess(). update database failed.");
                } else {
                    JOptionPane.showMessageDialog(null,
                            "信息修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    //start.setVisible(false);
                    System.out.println("updateProcess(). update database successful.");
                }
            } catch (Exception e) {
                System.out.println("e = " + e);
                JOptionPane.showMessageDialog(null,
                        "数据操作错误", "错误", JOptionPane.ERROR_MESSAGE);
            }
            //queryAllProcess();
        } else {

            System.out.println("修改失败:未填写所有带 * 号的信息");
            JOptionPane.showMessageDialog(null,
                    "请填写所有带 * 号的信息", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
