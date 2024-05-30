package com.window;

import com.mysql.cj.xdevapi.Table;
import com.stytle.MyTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Chang {

    public static Label label = new Label("<html><font color='red'>选择查询字段</font></html>");

    public static String SelectQueryFieldStr = "职位";
    //public static
    public static Vector<String> titleVector;
    public static Vector staffVector;
    public static JTable staffJTable;
    public static JTextField id;
    public static JComboBox<String> jCBSelectQueryField2;
    public static JTextField jSelectFild2;
    public static int JTnum = 0;

    public static JComponent toFind() {
        //JPanel panel=new JPanel();

        BackgroundPanel centre = new BackgroundPanel((new ImageIcon("src/img/3.png")).getImage());
        centre.setBorder(new EmptyBorder(5, 5, 5, 5));
        //centre.setLayout(null);
        centre.setSize(800, 800);
        //centre.add(panel);

        //建立表格
        staffVector = new Vector();
        titleVector = new Vector<>();
        titleVector.add("记录编号");
        titleVector.add("员工号");
        titleVector.add("变更");
        titleVector.add("详细记录");
        //studentTableModel = new DefaultTableModel(tableTitle, 15);
        staffJTable = new MyTable(staffVector, titleVector);
        staffJTable.getTableHeader().setReorderingAllowed(false);

        staffJTable.setPreferredScrollableViewportSize(new Dimension(740, 340));
        JScrollPane staffJScrollPane = new JScrollPane(staffJTable);
        //分别设置水平和垂直滚动条自动出现
        staffJScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        staffJScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //staffJTable.setDefaultEditor();
        //为表格添加监听器
        staffJTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); // 获得行位置
                System.out.println("mouseClicked(). row = " + row);
                Vector v = new Vector();
                v = (Vector) staffVector.get(row);
                JTnum = (int) v.get(0);
            }
        });

        //设置按键
        JButton jSelect = new JButton("查询");
        JButton jSelectAll = new JButton("查询所有");
        JButton jDelete = new JButton("删除该记录");

        //设置下拉框
        String[] list = {"记录编号", "员工号", "变更"};
        JComboBox<String> jCBSelectQueryField = new JComboBox<String>(list);//查询字段
        JTextField jSelectFild = new JTextField(10);

        centre.setLayout(null);
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p1.setBounds(10, 20, 750, 400);
        p1.add(staffJScrollPane);
        JPanel p3 = new JPanel(null);
        p3.setBounds(10, 430, 750, 360);
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 40));
        JPanel p4 = new JPanel(null);
        p2.setBounds(0, 0, 750, 100);
        p4.setBounds(0, 100, 750, 200);
        label.setText("选择查询字段");

        p2.add(jCBSelectQueryField);
        p2.add(new Label("="));

        p2.add(jSelectFild);
        p2.add(jSelect);
        p2.add(jSelectAll);
        p2.add(jDelete);

        //设置按键监听
        jSelect.addActionListener(e -> {
            String query1 = (String) jCBSelectQueryField.getSelectedItem();
            String query2 = jSelectFild.getText().trim();
            String sql = "select personnel.num,id,intro,remark from personnel,personnel_change where personnel.code=personnel_change.num and ";
            switch (query1) {
                case "记录编号":
                    sql += "personnel.num=" + query2 + " order by num asc;";
                    break;
                case "员工号":
                    sql += "id=" + query2 + " order by num asc;";
                    break;
                case "变更":
                    sql += "intro='" + query2 + "' order by num asc;";
                    break;
            }
            selectProcess(sql);
        });
        jSelectAll.addActionListener(e -> {
            String sql = "select personnel.num,id,intro,remark from personnel,personnel_change where personnel.code=personnel_change.num order by num asc;";
            selectProcess(sql);
        });
        jDelete.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "确认删除该记录？",
                    "提示",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            System.out.println("选择结果: " + result);
            if (result == 0) {
                System.out.println("actionPerformed(). 删除当前记录");
                deleteCurrentRecordProcess();
            }
        });


        p4.setBorder(BorderFactory.createTitledBorder("<html><font color='red'>增添人事变动选项表</font></html>"));
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 40));
        p5.setBounds(5, 5, 700, 50);
        p6.setBounds(5, 58, 700, 100);
        p5.add(new JLabel("<html><font color='red'>员工号:</font></html>"));
        id = new JTextField(10);
        p5.add(id);
        p6.add(new JLabel("<html><font color='red'>变动项目:</font></html>"));
        String[] list2 = {"职位", "部门", "辞退"};
        jCBSelectQueryField2 = new JComboBox<String>(list2);//查询字段
        jCBSelectQueryField2.setSelectedIndex(0);
        jSelectFild2 = new JTextField(10);


        JButton upData = new JButton("确认");
        p6.add(jCBSelectQueryField2);
        p6.add(new Label("="));
        p6.add(jSelectFild2);
        p6.add(upData);
        p4.add(p5);
        p4.add(p6);

        upData.addActionListener(e -> {
            String sql = "insert into personnel value(";
            String sql2 = "update person set ";
            String NAME = selectOneProcess("select name from person where id=" + id.getText().trim() + ";");
            String NUM = selectOneProcess("select department from person where id=" + id.getText().trim() + ";");
            String sql3 = "";
            sql += (toCount() + 1) + "," + id.getText().trim() + ",";
            switch ((String) jCBSelectQueryField2.getSelectedItem()) {
                case "职位":
                    sql += 1 + ",'" + jSelectFild2.getText().trim() + "');";
                    sql2 += " job='" + jSelectFild2.getText().trim() + "'";
                    if (jSelectFild2.getText().trim().equals("经理")) {
                        sql3 = "update depart set manager='" + NAME + "' where id=" + NUM + ";";
                    } else {
                        sql3 = "update depart set manager=null where id=" + NUM + ";";
                    }
                    upDataDepertProcess(sql3);
                    break;
                case "部门":
                    sql += 2 + ",'部门" + jSelectFild2.getText().trim() + "');";
                    sql2 += " department=" + jSelectFild2.getText().trim() + "";
                    break;
                case "辞退":
                    sql += 3 + ",'辞退" + jSelectFild2.getText().trim() + "');";
                    sql2 += " state='F' ";
                    break;
            }
            sql2 += " where id=" + id.getText().trim() + ";";
            upDataProcess(sql);
            upDataPersonProcess(sql2);
        });


        p3.add(p2);
        p3.add(p4);

        p1.setOpaque(false);
        p2.setOpaque(false);
        p3.setOpaque(false);
        p5.setOpaque(false);
        p6.setOpaque(false);
        p4.setOpaque(false);

        centre.setOpaque(false);

        centre.add(p1);
        centre.add(p3);

        return centre;
    }

    public static void deleteCurrentRecordProcess() {
        // 建立删除条件
        String sql = "delete from personnel where num = " + JTnum + ";";
        System.out.println("deleteCurrentRecordProcess(). sql = " + sql);
        try {
            if (DbProcess.executeUpdate(sql) < 1) {
                System.out.println("deleteCurrentRecordProcess(). delete database failed.");
            }
        } catch (Exception e) {
            System.out.println("e = " + e);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误", "错误", JOptionPane.ERROR_MESSAGE);
        }
        String sql2 = "select personnel.num,id,intro,remark from personnel,personnel_change where personnel.code=personnel_change.num order by num asc;";
        selectProcess(sql2);
    }

    private static void upDataDepertProcess(String sql2) {
        System.out.println("updataDepartProcess(). sql = " + sql2);
        try {
            if (DbProcess.executeUpdate(sql2) < 1) {
                System.out.println("updataDepartProcess() database failed.");
            } else {
                System.out.println("updatDepartProcess() successful.");

            }
        } catch (Exception even) {
            System.out.println("e = " + even);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void upDataPersonProcess(String sql2) {
        System.out.println("updataPersonProcess(). sql = " + sql2);
        try {
            if (DbProcess.executeUpdate(sql2) < 1) {
                System.out.println("updataPersonProcess() database failed.");
            } else {
                System.out.println("updataPersonProcess() successful.");

            }
        } catch (Exception even) {
            System.out.println("e = " + even);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void upDataProcess(String sql) {
        System.out.println("insertProcess(). sql = " + sql);
        try {
            if (DbProcess.executeUpdate(sql) < 1) {
                System.out.println("insertProcess(). insert database failed.");
            } else {
                System.out.println("insertProcess(). insert successful.");
                JOptionPane.showMessageDialog(null,
                        "新增人事变动", "成功", JOptionPane.INFORMATION_MESSAGE);
                id.setText("");
                jSelectFild2.setText("");
            }
        } catch (Exception even) {
            System.out.println("e = " + even);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误", "错误", JOptionPane.ERROR_MESSAGE);
        }

    }


    private static String selectOneProcess(String sql) {
        String ans = "";
        try {
            DbProcess.connect();

            ResultSet rs = DbProcess.executeQuery(sql);
            while (rs.next()) {
                ans = rs.getString(1);
            }

            DbProcess.disconnect();
        } catch (Exception e) {
            System.out.println("e = " + e);
            JOptionPane.showMessageDialog(null,
                    "shuju操作错误", "错误", JOptionPane.ERROR_MESSAGE);
        }
        return ans;
    }

    private static void selectProcess(String sql) {
        try {
            DbProcess.connect();
            ResultSet rs = DbProcess.executeQuery(sql);

            // 将查询获得的记录数据，转换成适合生成JTable的数据形式
            staffVector.clear();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("num"));
                v.add(rs.getInt("id"));
                v.add(rs.getString("intro"));
                v.add(rs.getString("remark"));
                staffVector.add(v);
            }

            staffJTable.updateUI();

            DbProcess.disconnect();
        } catch (SQLException sqle) {
            System.out.println("sqle = " + sqle);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.out.println("e = " + e);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static int toCount() {
        int count = 0;
        try {

            // 建立查询条件
            String sql = "select * from personnel;";
            System.out.println("queryAllProcess(). sql = " + sql);

            DbProcess.connect();
            ResultSet rs = DbProcess.executeQuery(sql);

            // 将查询获得的记录数据，转换成适合生成JTable的数据形式

            while (rs.next()) {
                count++;
            }
            System.out.println("已有人事变动记录： " + (count) + "条");

        } catch (SQLException sqle) {
            System.out.println("sqle = " + sqle);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误", "错误", JOptionPane.ERROR_MESSAGE);
        }
        return count;
    }
}
