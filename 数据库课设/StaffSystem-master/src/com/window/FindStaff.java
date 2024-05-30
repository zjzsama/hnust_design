package com.window;

import com.mysql.cj.xdevapi.Table;
import com.stytle.MyTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class FindStaff implements ActionListener{
     static String SelectQueryFieldStr ="工号";
     static JTextField jTFQueryField=new JTextField(10);
     public static JLabel label=new JLabel("员工信息查询表",JLabel.CENTER);
     static Vector staffVector = new Vector();
     static Vector<String> titleVector = new Vector<>();
     static JTable staffJTable;
     static JButton jBQuery;

     static int JTid;
    JComponent toFind()
    {
        //背景
        BackgroundPanel centre = new BackgroundPanel((new ImageIcon("src/img/3.png")).getImage());
        centre.setBorder(new EmptyBorder(5, 5, 5, 5));
        //centre.setLayout(null);
        centre.setSize(800, 800);
        //JPanel centre=new JPanel();

        //设置按键
        jBQuery = new JButton("查询");
        JButton jBQueryAll = new JButton("查询所有记录");
        JButton jBDeleteCurrentRecord = new JButton("删除当前记录");
        JButton jBDeleteAllRecords = new JButton("删除所有记录");

        // 设置监听
        jBQuery.addActionListener(this);
        jBQueryAll.addActionListener(this);
        jBDeleteCurrentRecord.addActionListener(this);
        jBDeleteAllRecords.addActionListener(this);

        //下拉框
        JComboBox<String> jCBSelectQueryField = new JComboBox<>();//查询字段
        jCBSelectQueryField.addItem("工号");
        jCBSelectQueryField.addItem("姓名");
        jCBSelectQueryField.addItem("性别");
        jCBSelectQueryField.addItem("权限");
        jCBSelectQueryField.addItem("部门");
        jCBSelectQueryField.addItem("职务");
        jCBSelectQueryField.addItem("受教育程度");
        jCBSelectQueryField.addItem("当前状态");
        //文本框
        //JTextField Jid =new JTextField(10);
        //下拉框事件监听
        jCBSelectQueryField.addItemListener(event -> {
            switch (event.getStateChange()) {
                case ItemEvent.SELECTED:
                    SelectQueryFieldStr = (String) event.getItem();
                    System.out.println("选中：" + SelectQueryFieldStr);
                    break;
                case ItemEvent.DESELECTED:
                    System.out.println("取消选中：" + event.getItem());
                    break;
            }
        });

        //建立表格
        titleVector.add("工号");
        titleVector.add("姓名");
        titleVector.add("性别");
        titleVector.add("权限");
        titleVector.add("部门");
        titleVector.add("职务");
        titleVector.add("受教育程度");
        titleVector.add("当前状态");
        //studentTableModel = new DefaultTableModel(tableTitle, 15);
        staffJTable = new MyTable(staffVector, titleVector);
        staffJTable.setPreferredScrollableViewportSize(new Dimension(750,360));
        JScrollPane staffJScrollPane = new JScrollPane(staffJTable);
        //分别设置水平和垂直滚动条自动出现
        staffJScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        staffJScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //为表格添加监听器
        staffJTable.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); // 获得行位置
                System.out.println("mouseClicked(). row = " + row);
                Vector v = new Vector();
                v = (Vector) staffVector.get(row);
                JTid=(int)v.get(0);

            }
        });

        //放入元素
        centre.setLayout(new GridLayout(2,1));
        JPanel p1 =new JPanel();
        JPanel p11 =new JPanel();
        label.setText("选择查询字段");
        p11.add(label);
        JPanel p12 =new JPanel();
        p12.add(staffJScrollPane);
        p1.add(p11,BorderLayout.NORTH);
        p1.add(p12,BorderLayout.CENTER);
        JPanel p2 =new JPanel(new GridLayout(3,1));
        JPanel pp1 =new JPanel(new FlowLayout(FlowLayout.CENTER,20,90));

        pp1.add(label);
        pp1.add(jCBSelectQueryField);
        pp1.add(new Label("="));
        pp1.add(jTFQueryField);

        JPanel pp2 =new JPanel(new FlowLayout(FlowLayout.CENTER,30,50));
        pp2.add(jBQuery);
        pp2.add(jBQueryAll);

        JPanel pp3=new JPanel();
        pp3.add(jBDeleteCurrentRecord);
        pp3.add(jBDeleteAllRecords);

        p2.add(pp1);
        p2.add(pp2);
        p2.add(pp3);

        jCBSelectQueryField.setOpaque(false);
        p11.setOpaque(false);
        p12.setOpaque(false);
        pp1.setOpaque(false);
        pp2.setOpaque(false);
        pp3.setOpaque(false);
        p1.setOpaque(false);
        p2.setOpaque(false);
        centre.add(p1);
        centre.add(p2);
        return centre;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("查询")
                && !jTFQueryField.getText().isEmpty()){
            System.out.println("actionPerformed(). 查询");
            String sQueryField = jTFQueryField.getText().trim();
            queryProcess(sQueryField);
            //jTFQueryField.setText("");
        }else if(e.getActionCommand().equals("查询所有记录")) {
            System.out.println("actionPerformed(). 查询所有记录");
            queryAllProcess();
        }else if(e.getActionCommand().equals("详细信息")){
            System.out.println("actionPerformed(). 详细信息");
            //accountProcess();
            Account.start(JTid);
        }else if(e.getActionCommand().equals("删除当前记录")){
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "确认删除该记录？",
                    "提示",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            System.out.println("选择结果: " + result);
            if(result==0) {
                System.out.println("actionPerformed(). 删除当前记录");
                deleteCurrentRecordProcess();
            }
        }else if(e.getActionCommand().equals("删除所有记录")){
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "确认删除所有记录？",
                    "提示",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            System.out.println("选择结果: " + result);
            if(result==0) {
                System.out.println("actionPerformed(). 删除所有记录");
                deleteAllRecordsProcess();
            }

        }
    }

    public void deleteAllRecordsProcess()
    {
        // 建立删除条件
        String sql = "delete from person;";
        System.out.println("deleteAllRecordsProcess(). sql = " + sql);
        try{
            if (DbProcess.executeUpdate(sql) < 1) {
                System.out.println("deleteAllRecordsProcess(). delete database failed.");
            }
        }catch(Exception e){
            System.out.println("e = " + e);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误","错误",JOptionPane.ERROR_MESSAGE);
        }
        queryAllProcess();
    }

    public void deleteCurrentRecordProcess()
    {
        // 建立删除条件
        String sql = "delete from person where id = " + JTid + ";";
        System.out.println("deleteCurrentRecordProcess(). sql = " + sql);
        try{
            if (DbProcess.executeUpdate(sql) < 1) {
                System.out.println("deleteCurrentRecordProcess(). delete database failed.");
            }
        }catch(Exception e){
            System.out.println("e = " + e);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误","错误",JOptionPane.ERROR_MESSAGE);
        }
    }


    public void queryProcess(String sQueryField)
    {
        try{
            // 建立查询条件
            String sql = "select * from person where ";
            String queryFieldStr = jCBSelectQueryFieldTransfer(SelectQueryFieldStr);

            if(queryFieldStr.equals("age")||queryFieldStr.equals("id")){//int sAge.
                sql = sql + queryFieldStr;
                sql = sql + " = " + sQueryField;
            }else{
                sql = sql + queryFieldStr;
                sql = sql + " = ";
                sql = sql + "'" + sQueryField + "'";
            }
            sql+=" order by id asc;";

            System.out.println("queryProcess(). sql = " + sql);

            DbProcess.connect();
            ResultSet rs = DbProcess.executeQuery(sql);

            // 将查询获得的记录数据，转换成适合生成JTable的数据形式
            staffVector.clear();
            while(rs.next()){
                Vector v = new Vector();
                v.add(Integer.valueOf(rs.getInt("id")));
                v.add(rs.getString("name"));
                v.add(rs.getString("sex"));
                v.add(rs.getString("authority"));
                v.add(rs.getString("department"));
                v.add(rs.getString("job"));
                v.add(rs.getString("education"));
                v.add(rs.getString("state"));
                staffVector.add(v);
            }

            staffJTable.updateUI();

            DbProcess.disconnect();
        }catch(SQLException sqle){
            System.out.println("sqle = " + sqle);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误","错误",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            System.out.println("e = " + e);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误","错误",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void queryAllProcess()
    {
        try{
            // 建立查询条件
            String sql = "select * from person ";
            sql+=" order by id asc;";
            System.out.println("queryAllProcess(). sql = " + sql);

            DbProcess.connect();
            ResultSet rs = DbProcess.executeQuery(sql);

            // 将查询获得的记录数据，转换成适合生成JTable的数据形式
            staffVector.clear();
            while(rs.next()){
                Vector v = new Vector();
                v.add(Integer.valueOf(rs.getInt("id")));
                v.add(rs.getString("name"));
                v.add(rs.getString("sex"));
                v.add(rs.getString("authority"));
                v.add(rs.getString("department"));
                v.add(rs.getString("job"));
                v.add(rs.getString("education"));
                v.add(rs.getString("state"));
                staffVector.add(v);
            }
            staffJTable.updateUI();
            DbProcess.disconnect();
        }catch(SQLException sqle){
            System.out.println("sqle = " + sqle);
            JOptionPane.showMessageDialog(null,
                    "数据操作错误","错误",JOptionPane.ERROR_MESSAGE);
        }
    }

    public String jCBSelectQueryFieldTransfer(String InputStr)
    {
        String outputStr = "";
        System.out.println("jCBSelectQueryFieldTransfer(). InputStr = " + InputStr);

        if(InputStr.equals("工号")){
            outputStr = "id";
        }else if(InputStr.equals("姓名")){
            outputStr = "name";
        }else if(InputStr.equals("性别")){
            outputStr = "sex";
        }else if(InputStr.equals("权限")){
            outputStr = "authority";
        }else if(InputStr.equals("部门")){
            outputStr = "department";
        }else if(InputStr.equals("职务")){
            outputStr = "job";
        }else if(InputStr.equals("受教育程度")){
            outputStr = "education";
        }else if(InputStr.equals("当前状态")){
            outputStr = "state";
        }
        System.out.println("jCBSelectQueryFieldTransfer(). outputStr = " + outputStr);
        return outputStr;
    }



}
